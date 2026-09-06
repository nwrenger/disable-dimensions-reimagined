# Translations for Disable Dimensions Reimagined

Official translation reference for the **Disable Dimensions Reimagined** mod. Contributions for improving existing translations or adding new languages are very welcome!

## Supported translations

- `Deutsch (Deutschland)` by @nwrenger (Author)

The default fallback strings are provided directly in the mod's command components, so `en_us.json` is intentionally not included.

As you can see, not many translations are available yet. Therefore, I encourage you to contribute your own translation if you are a native speaker (or at the quality of one). I won't accept **AI generated** translations!

## Adding a Translation

1. Go to the [repository](https://github.com/nwrenger/disable-dimensions-reimagined)
2. Duplicate `common/src/main/resources/assets/disable_dimensions_reimagined/lang/de_de.json`
3. Rename it to your Minecraft language code (e.g. `fr_fr.json`, `es_mx.json`)
4. Translate the values appropriately
5. Open a PR on the repository

## Updating a Translation

- Keep every key from the source file, even if a value has not been translated yet.
- Preserve placeholders such as `%1$s`, `%2$s`, and `%3$s` exactly.
- Preserve the formatting expectations: short labels should stay short, and tooltip text should be clear in chat UI.
- Use UTF-8 JSON without comments or trailing commas.
- If a phrase sounds awkward because of placeholder order, reorder the placeholders instead of changing or removing them.

## Key Reference

### General

| Key                                                    | Description                                              |
| ------------------------------------------------------ | -------------------------------------------------------- |
| `disable_dimensions_reimagined.command.state.now`      | Word for "now", used inline in status messages           |
| `disable_dimensions_reimagined.command.state.enabled`  | Word for "enabled"                                       |
| `disable_dimensions_reimagined.command.state.disabled` | Word for "disabled"                                      |
| `disable_dimensions_reimagined.command.header`         | Header pattern — `%1$s` = mod name, `%2$s` = author name |

### About Screen

| Key                                                          | Description                                    |
| ------------------------------------------------------------ | ---------------------------------------------- |
| `disable_dimensions_reimagined.command.about.other_projects` | Intro text before the author's website link    |
| `disable_dimensions_reimagined.command.about.issue_prefix`   | Intro text before the issue tracker link       |
| `disable_dimensions_reimagined.command.about.issue_link`     | Clickable link label for the issue tracker     |
| `disable_dimensions_reimagined.command.about.config`         | Intro text before the config command           |
| `disable_dimensions_reimagined.command.about.reload`         | Intro text before the reload command           |
| `disable_dimensions_reimagined.command.about.toggle`         | Intro text before the dimension toggle command |

### Config — General

| Key                                                  | Description                                    |
| ---------------------------------------------------- | ---------------------------------------------- |
| `disable_dimensions_reimagined.command.config.title` | Title of the config screen                     |
| `disable_dimensions_reimagined.command.config.empty` | Shown when the config has no dimension entries |

### Config — Dimension Entry

| Key                                                                       | Description                                                                                                                             |
| ------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| `disable_dimensions_reimagined.command.config.dimension.status.label`     | Label for the dimension status field                                                                                                    |
| `disable_dimensions_reimagined.command.config.dimension.status.hover`     | Status tooltip pattern — `%1$s` = dimension ID, `%2$s` = `command.state.now`, `%3$s` = `command.state.enabled`/`command.state.disabled` |
| `disable_dimensions_reimagined.command.config.dimension.message.label`    | Label for the block message field                                                                                                       |
| `disable_dimensions_reimagined.command.config.dimension.conditions.label` | Label for the conditions list                                                                                                           |
| `disable_dimensions_reimagined.command.config.dimension.conditions.empty` | Text shown when the conditions list is empty                                                                                            |

### Config — Condition Entry

| Key                                                                     | Description                                                                                                                            |
| ----------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------- |
| `disable_dimensions_reimagined.command.config.condition.status.hover`   | Status tooltip pattern — `%1$s` = `command.config.condition.status.context`, `%2$s` = `command.state.enabled`/`command.state.disabled` |
| `disable_dimensions_reimagined.command.config.condition.status.context` | Phrase used as `%1$s` in the condition status tooltip                                                                                  |

### Reload Command

| Key                                                    | Description                      |
| ------------------------------------------------------ | -------------------------------- |
| `disable_dimensions_reimagined.command.reload.title`   | Title shown in the reload result |
| `disable_dimensions_reimagined.command.reload.success` | Successful reload status         |
| `disable_dimensions_reimagined.command.reload.failure` | Failed reload status             |

### Toggle Command

| Key                                                           | Description                                         |
| ------------------------------------------------------------- | --------------------------------------------------- |
| `disable_dimensions_reimagined.command.toggle.title`          | Title shown in the toggle result                    |
| `disable_dimensions_reimagined.command.toggle.save_failure`   | Error shown when the updated config cannot be saved |
| `disable_dimensions_reimagined.command.toggle.not_configured` | Error pattern — `%1$s` = requested dimension ID     |
