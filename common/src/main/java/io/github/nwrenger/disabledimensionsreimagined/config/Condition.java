package io.github.nwrenger.disabledimensionsreimagined.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.nwrenger.disabledimensionsreimagined.Constants;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;

public class Condition {

    public ConditionType type;
    public String value;
    public boolean disabled;

    public Condition(ConditionType type, String value, boolean disabled) {
        this.type = type;
        this.value = value;
        this.disabled = disabled;
    }

    public static void validate(@Nullable Condition condition) {
        if (condition == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] Condition must not be null"
            );
        }

        if (condition.type == null) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `condition.type` must not be null"
            );
        }

        if (condition.value == null || condition.value.isBlank()) {
            throw new IllegalStateException(
                "[Disable Dimensions Reimagined] `condition.value` must not be blank"
            );
        }

        switch (condition.type) {
            case ADVANCEMENT -> validateIdentifier(condition);
            case DAY, GAMETIME, PLAYTIME -> validateNonNegativeInteger(
                condition
            );
            case GAMEMODE -> {
                if (GameType.byName(condition.value, null) == null) {
                    throw invalidValue(condition);
                }
            }
            case SCORE -> validateScore(condition);
            case ITEM, TAG, TEAM -> {
            }
        }
    }

    private static void validateIdentifier(Condition condition) {
        if (Identifier.tryParse(condition.value) == null) {
            throw invalidValue(condition);
        }
    }

    private static void validateNonNegativeInteger(Condition condition) {
        try {
            if (Integer.parseInt(condition.value) < 0) {
                throw invalidValue(condition);
            }
        } catch (NumberFormatException exception) {
            throw invalidValue(condition);
        }
    }

    private static void validateScore(Condition condition) {
        int separator = condition.value.lastIndexOf('=');
        if (separator <= 0 || separator == condition.value.length() - 1) {
            throw invalidValue(condition);
        }

        String objectiveName = condition.value.substring(0, separator).trim();
        if (objectiveName.isEmpty()) {
            throw invalidValue(condition);
        }

        try {
            Integer.parseInt(condition.value.substring(separator + 1).trim());
        } catch (NumberFormatException exception) {
            throw invalidValue(condition);
        }
    }

    private static IllegalStateException invalidValue(Condition condition) {
        return new IllegalStateException(
            "[Disable Dimensions Reimagined] Invalid value for condition `" +
                condition.type +
                "`: " +
                condition.value
        );
    }

    public boolean check(Entity entity) {
        switch (this.type) {
            case ADVANCEMENT -> {
                if (!(entity instanceof ServerPlayer player)) {
                    return false;
                }
                return Condition.hasAdvancement(player, this.value);
            }
            case DAY -> {
                return Condition.hasDay(entity, this.value);
            }
            case GAMEMODE -> {
                if (!(entity instanceof ServerPlayer player)) {
                    return false;
                }
                return Condition.hasGamemode(player, this.value);
            }
            case GAMETIME -> {
                return Condition.hasGametime(entity, this.value);
            }
            case ITEM -> {
                return Condition.hasItem(entity, this.value);
            }
            case PLAYTIME -> {
                if (!(entity instanceof ServerPlayer player)) {
                    return false;
                }
                return Condition.hasPlaytime(player, this.value);
            }
            case SCORE -> {
                return Condition.hasScore(entity, this.value);
            }
            case TAG -> {
                return Condition.hasTag(entity, this.value);
            }
            case TEAM -> {
                return Condition.hasTeam(entity, this.value);
            }
            default -> {
                // Unreachable
                return false;
            }
        }
    }

    private static boolean hasAdvancement(ServerPlayer player, String value) {
        Identifier id = Identifier.tryParse(value);
        if (id == null) {
            return false;
        }

        AdvancementHolder advancement = player
            .level()
            .getServer()
            .getAdvancements()
            .get(id);

        return (
            advancement != null &&
            player.getAdvancements().getOrStartProgress(advancement).isDone()
        );
    }

    private static boolean hasGamemode(ServerPlayer player, String gamemode) {
        return player.gameMode().getName().equals(gamemode);
    }

    private static boolean hasPlaytime(ServerPlayer player, String timeStr) {
        int neededPlayTime = Integer.parseInt(timeStr);

        int playTimeTicks = player
            .getStats()
            .getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));

        int playTimeSeconds = ticksToSeconds(playTimeTicks);

        return playTimeSeconds >= neededPlayTime;
    }

    private static boolean hasDay(Entity entity, String timeStr) {
        int neededDay = Integer.parseInt(timeStr);

        long dayTicks = entity.level().getDayTime();
        int completedDays = ticksToIngameDays(dayTicks);

        return completedDays >= neededDay;
    }

    private static boolean hasGametime(Entity entity, String timeStr) {
        int neededGameTime = Integer.parseInt(timeStr);

        long gameTime = entity.level().getGameTime();
        int gameTimeSeconds = ticksToSeconds(gameTime);

        return gameTimeSeconds >= neededGameTime;
    }

    private static boolean hasItem(Entity entity, String itemStr) {
        Container inventory = Condition.getInventory(entity);
        if (
            inventory == null || !(entity.level() instanceof ServerLevel level)
        ) {
            return false;
        }

        CommandBuildContext context = CommandBuildContext.simple(
            level.registryAccess(),
            level.enabledFeatures()
        );

        try {
            StringReader reader = new StringReader(itemStr);
            ItemPredicateArgument.Result predicate =
                ItemPredicateArgument.itemPredicate(context).parse(reader);

            return !reader.canRead() && inventory.hasAnyMatching(predicate);
        } catch (CommandSyntaxException exception) {
            Constants.LOG.warn(
                "[Disable Dimensions Reimagined] Invalid item condition: {}",
                itemStr,
                exception
            );

            return false;
        }
    }

    private static boolean hasScore(Entity entity, String value) {
        int separator = value.lastIndexOf('=');
        if (separator <= 0 || separator == value.length() - 1) {
            return false;
        }

        String objectiveName = value.substring(0, separator).trim();
        if (objectiveName.isEmpty()) {
            return false;
        }

        int expectedScore = Integer.parseInt(
            value.substring(separator + 1).trim()
        );

        Scoreboard scoreboard = entity.level().getScoreboard();
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            return false;
        }

        ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(
            entity,
            objective
        );

        return score != null && score.value() == expectedScore;
    }

    private static boolean hasTag(Entity entity, String tag) {
        return entity.getTags().contains(tag);
    }

    private static boolean hasTeam(Entity entity, String name) {
        return (
            entity.getTeam() != null && entity.getTeam().getName().equals(name)
        );
    }

    private static @Nullable Container getInventory(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            return player.getInventory();
        } else if (entity instanceof InventoryCarrier carrier) {
            return carrier.getInventory();
        } else if (entity instanceof Container container) {
            return container;
        }

        return null;
    }

    private static int ticksToSeconds(int ticks) {
        return (int) Math.floorDiv(ticks, Constants.TICKS_PER_SECOND);
    }

    private static int ticksToSeconds(long ticks) {
        return (int) Math.floorDiv(ticks, Constants.TICKS_PER_SECOND);
    }

    private static int ticksToIngameDays(long ticks) {
        return (int) Math.floorDiv(ticks, Constants.TICKS_PER_INGAME_DAY);
    }
}
