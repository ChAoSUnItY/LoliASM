# LoliASM

## In development

### Potential downsides

- StringPool optimization requires java.base/java.security be opened in EE module system, need to find a way to open it in player's environment.
- - `--add-opens java.base/java.security=ALL-UNNAMED`