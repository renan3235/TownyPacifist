# ☮ TownyPacifist

> A lightweight [Towny](https://github.com/TownyAdvanced/Towny) addon that enforces peace for peaceful towns — everywhere, not just within their borders.

---

## 📖 About

Many servers struggle with a common problem: players from peaceful towns getting killed by aggressive towns, or peaceful town members attacking others without consequence.

**TownyPacifist** solves this by hooking directly into Towny's own peaceful flag (`/t toggle peaceful`). No custom data, no extra commands — if a town is marked as peaceful in Towny, this plugin enforces that everywhere on the map.

---

## ✨ Features

- **Zero custom storage** — reads directly from Towny's `isNeutral()` flag
- **Bidirectional protection** — peaceful towns can't attack OR be attacked
- **Arena support** — peaceful players can still PvP inside Towny arena plots (`/plot set arena`)
- **EventWar integration** — works naturally alongside EventWar's neutrality system
- **Multi-language** — comes with `en_US` and `pt_BR`, easily extendable
- **Lightweight** — a single event listener, no databases, no commands

---

## 🔗 Dependencies

| Plugin | Required |
|---|---|
| [Towny](https://github.com/TownyAdvanced/Towny) | ✅ Required |
| [EventWar](https://github.com/TownyAdvanced/Towny) | ➕ Optional (integrates automatically) |

---

## ⚙️ How It Works

TownyPacifist listens to `TownyPlayerDamagePlayerEvent` and checks both players' towns using Towny's API:

```
Player A hits Player B
  └─ Is either player from a neutral (peaceful) town?
       ├─ No  → do nothing, let Towny handle it normally
       ├─ Yes → are they inside an arena plot?
       │         ├─ Yes + arenas allowed → permit combat (no spam, notified on entry)
       │         └─ No  → cancel damage + send message
       └─ Both peaceful → cancel damage + specific message
```

The "peaceful" status is set by the town mayor using Towny's own command:
```
/t toggle peaceful
```

No TownyPacifist commands needed.

---

## 📁 Config

```yaml
# Enable or disable all protection globally
enabled: true

# Allow peaceful town members to PvP inside Towny arena plots
allow-pacifist-in-arenas: true
```

---

## 🌍 Adding a Language

TownyPacifist uses Towny's built-in translation system. Messages are delivered in each player's own language automatically.

1. Go to `plugins/TownyPacifist/lang/`
2. Copy `en-US.yml` and rename it (e.g. `es-ES.yml`)
3. Translate the messages
4. Towny picks it up automatically — no config change needed

---

## 🔨 Building from Source

**Requirements:**
- JDK 21 (for compilation)
- Gradle 8.12 (or use the included `gradlew`)
- Towny JAR → `libs/Towny-x.x.x.x.jar` (download from [Towny releases](https://github.com/TownyAdvanced/Towny/releases))
- EventWar JAR → `libs/EventWar-x.x.x.jar` *(optional, for IDE support only)*

```bash
./gradlew build
# Output: build/libs/TownyPacifist-1.0.0.jar
```

---

## 🗺️ Planned Integrations

- [ ] Capture sites
- [ ] SiegeWar

---

## 📜 License

Apache 2.0 — see [LICENSE](LICENSE)

---

## 👤 Author

Made by **RN_Axol**
