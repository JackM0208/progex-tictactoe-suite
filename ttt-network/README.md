# ttt-network — Client-Server TicTacToe

This module contains three progressively more sophisticated client-server implementations of TicTacToe, all sharing the same core game logic (`Board`, `Player`, `BotPlayer`, `HumanPlayer`). Each sub-architecture lives in its own package.

---

## Package Overview

```
ttt-network/src/main/java/vgu/pe2026/ttt/
├── net_threads/                    # TCP sockets, persistent connection
│   ├── Client.java
│   └── Server_side/
│       ├── Server.java
│       ├── Game.java
│       ├── Board.java
│       ├── Player.java
│       ├── HumanPlayer.java
│       └── BotPlayer.java
│
├── multiClient_singleThread/       # TCP sockets, stateless + security
│   ├── Client.java
│   ├── ClientCheater.java          # Test client that attempts to cheat
│   └── Server_side/
│       ├── Server.java
│       ├── Game.java
│       ├── Board.java
│       ├── Player.java
│       ├── HumanPlayer.java
│       ├── BotPlayer.java
│       ├── CryptographicUtils.java
│       └── DatabaseUtils.java
│
└── webapp/                         # HTTP/JSON API + browser client
    ├── HTTPClient.java
    ├── BrowserClient.html
    ├── script.js
    └── Server_side/
        ├── Server.java
        ├── GameHandler.java
        ├── Game.java
        ├── Board.java
        ├── Player.java
        ├── HumanPlayer.java
        ├── BotPlayer.java
        ├── CryptographicUtils.java
        └── DatabaseUtils.java
```

All three versions run on port **5678**.

---

## Version 1 — `net_threads`: Persistent TCP Connection

### How it works

The client opens a single TCP socket that stays open for the entire game. The server reads the client's player ID, assigns roles (human vs. bot), and drives the full game loop over the socket, sending prompts and reading moves line by line.

**Server → Client messages:**
- `TURN:<id>` — it's your turn, send your move
- Board display lines (e.g. `| 1 | 0 | 2 |`)
- `Player#<id> won!` / `It is a draw!`
- `GAME_OVER` — disconnect

**Client → Server messages:**
- Player ID (sent once at connection start)
- Move as a number `1–9`, or `q` to quit

### Running

**Terminal 1 — Start the server:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.net_threads.Server_side.Server
```

**Terminal 2 — Start the client:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.net_threads.Client
```

The client is hardcoded to player ID `1` (goes second, bot goes first). To play as player 2 (bot goes first), edit `myId` in `Client.java`.

---

## Version 2 — `multiClient_singleThread`: Stateless Protocol with Security

### How it works

Each move is a **fresh TCP connection**. After the server processes a move, it closes the connection and issues a cryptographic **ticket** the client must present with the next move. There is no persistent session — the board state travels with every request.

### The Ticket System

After each move the server sends:

| Field | Description |
|---|---|
| `STATE:<value>` | Serialized board state (e.g. `100 020 000`) |
| `SIG:<value>` | HMAC-SHA256 signature of the board state |
| `TOKEN:<value>` | One-time HMAC-SHA256 token (board state + TTL) |
| `TTL:<value>` | Unix timestamp (ms) when the token expires (10 seconds) |

On the next move the client sends all four values back. The server runs four checks before accepting the move:

1. **Replay attack check** — has this token been used before? (checked against PostgreSQL)
2. **Board integrity check** — does the HMAC signature match the submitted board state?
3. **Token validity check** — does the token match HMAC(board state + TTL)?
4. **Timeout check** — is the current time before the TTL?

If any check fails, the server sends a warning and `GAME_OVER`. Used tokens are stored in the database until their TTL expires, then cleaned up automatically.

### Database Setup (PostgreSQL)

This version requires a PostgreSQL database. The connection is configured in `DatabaseUtils.java`:

```
URL:      jdbc:postgresql://localhost:5432/[ProgEx]-ttt_network_db
User:     postgres
Password: admin
```

Create the database and table:

```sql
CREATE DATABASE "[ProgEx]-ttt_network_db";

\c "[ProgEx]-ttt_network_db"

CREATE TABLE used_tokens (
    token    TEXT PRIMARY KEY,
    deadline BIGINT NOT NULL
);
```

Update the credentials in `DatabaseUtils.java` if your PostgreSQL setup differs.

### Running

**Terminal 1 — Start the server:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.multiClient_singleThread.Server_side.Server
```

**Terminal 2 — Start the client:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.multiClient_singleThread.Client
```

**To test the security (cheating client):**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.multiClient_singleThread.ClientCheater
```
`ClientCheater` attempts to substitute a fabricated board state and a hardcoded signature. The server will detect the mismatch and terminate the session.

### First move / session start

On the very first connection, the client sends `START` as the board state (no move yet). The server responds with the initial board display plus a ticket for the client to use on its first real move.

---

## Version 3 — `webapp`: HTTP/JSON API

### How it works

The server is an HTTP server (`com.sun.net.httpserver`) that exposes one endpoint:

```
POST http://localhost:5678/play
Content-Type: application/json
```

**Request body:**
```json
{
  "ID":    1,
  "STATE": "000 000 000",
  "MOVE":  5
}
```

**Response body:**
```json
{
  "STATUS":    "success",
  "MESSAGE":   "Bot has moved.",
  "STATE":     "100 020 000",
  "GAME_OVER": false
}
```

`MESSAGE` contains the human-readable outcome of the turn (e.g. `Player#1 won!`, `It is a draw!`, or `Bot has moved.`). `GAME_OVER` is `true` when the game has ended.

The bot always moves immediately after the human's move within the same request, so each HTTP call advances the board by two moves (human + bot) unless the game ends after the human's move.

### Board state format

The board is serialized as a string of three space-separated rows, each row being three digits:

```
"100 020 000"
 ^^^          row 0: cells 1-2-3
     ^^^      row 1: cells 4-5-6
         ^^^  row 2: cells 7-8-9
```

`0` = empty, `1` = Player 1, `2` = Player 2.

### Running — Java HTTP client

**Terminal 1 — Start the server:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.webapp.Server_side.Server
```

**Terminal 2 — Start the Java client:**
```bash
mvn exec:java -Dexec.mainClass=vgu.pe2026.ttt.webapp.HTTPClient
```

### Running — Browser client

1. Start the server as above.
2. Open `BrowserClient.html` directly in a browser (no web server needed — it talks to `localhost:5678` via `fetch`).
3. Click a cell to make your move. The board updates after each round.

### Testing with curl

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"ID":1, "STATE":"000 000 000", "MOVE":5}' \
  http://localhost:5678/play
```

---

## Common Issues

**Port already in use** — Another process is bound to 5678. Kill it or change `PORT` in `Server.java`.

**PostgreSQL connection refused** (`multiClient_singleThread` only) — Make sure PostgreSQL is running and the database/table exist. Check the credentials in `DatabaseUtils.java`.

**CORS error in browser** (`webapp` only) — The server sets `Access-Control-Allow-Origin: *`, so this should not occur when opening the HTML file locally. If it does, try serving the HTML through a local HTTP server instead of opening it as a `file://` URL.

**Move integer parsing error** — The `HTTPClient` sends `userInput` (a raw string from `Scanner`) as the `MOVE` field. Make sure to type a number when prompted.

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ with the `[ProgEx]-ttt_network_db` database (required for `multiClient_singleThread` only)
- The `org.json` library (declared in `pom.xml`, fetched automatically by Maven)
