# chatbotmc

Fabric mod for Minecraft 1.21.7 (Java 21, Gradle).

## Build & run

```sh
./gradlew build          # full build
./gradlew runClient      # launch Minecraft with mod
```

CI (`.github/workflows/build.yml`) builds on push/PR with `./gradlew build`.

## Project structure

- `src/main/java/` — common mod code, entrypoint `io.github.vtf6259.Chatbot` (implements `ModInitializer`)
- `src/client/java/` — client-side code, entrypoint `io.github.vtf6259.client.ChatbotClient` (implements `ClientModInitializer`)
- `src/test/java/` — test sources (currently empty)
- `src/main/resources/fabric.mod.json` defines entrypoints and mixins
- `src/client/resources/chatbot.client.mixins.json` — client mixins (currently empty)

Loom uses `splitEnvironmentSourceSets()` — client sources are separate from main.

## Key config

| Property | Value |
|---|---|
| Minecraft | 1.21.7 |
| Fabric Loader | >=0.19.2 |
| Fabric API | 0.129.0+1.21.7 |
| Java target | 21 |
| Mod ID | `chatbot` |

## Commands

Registered in `ChatbotClient.onInitializeClient()` via Fabric API client commands:
- `/helloworld` — sends "Hello, World!"
- `/afk` — toggles AFK flag

All chat-command responses are prefixed with `FAI!` and handled in `handleMessage()` with a 2-second scheduled delay via `ScheduledExecutorService`.
