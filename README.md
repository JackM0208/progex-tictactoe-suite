# TicTacToe — A Multi-Version Java Project

This repository documents a progressive series of TicTacToe implementations built across a programming exercises course. Each version builds on the previous one, introducing new architectural concepts and Java skills while keeping the same core game logic.

---

## Project Structure

```
ttt/
├── ttt-basic/          # Version 1 — Single-process, terminal-based game
├── ttt-network/        # Version 2 — Client-server architecture (Sockets + HTTP)
└── ttt-servlet/        # Version 3 — Servlet-based web application
```

---

## Version Overview

### `ttt-basic` — OOP Fundamentals on the Terminal

The starting point. Everything runs in a single process inside one terminal window. This version is about getting the fundamentals right:

- Object-Oriented Design with abstract classes (`Player`, subclassed by `HumanPlayer` and `BotPlayer`)
- A `Board` class that encapsulates all game state and logic (win detection, cell validation, serialization)
- A `Game` class that orchestrates the game loop
- Input/output via `Scanner` and `System.out`
- A full JUnit 5 test suite covering board logic, player behavior, and end-to-end game scenarios

The human picks a player number (1 or 2) as a command-line argument. The bot always picks the first available empty cell. The game ends when someone wins, the board fills up (draw), or the player types `q`.

---

### `ttt-network` — Client-Server Architecture

The same game logic, now split across a network. This version comes in **three sub-architectures** within the same Maven module:

#### `net_threads` — TCP Sockets with Threading
A persistent TCP connection is held for the entire game session. The server spawns a new `Game` instance per connected client and drives the game loop over the socket using `BufferedReader` / `PrintWriter`. The client reads server prompts and sends moves back interactively.

#### `multiClient_singleThread` — Stateless Socket Protocol with Security
Moves to a **stateless, ticket-based protocol**: each move is a fresh TCP connection. The server issues a cryptographic ticket (HMAC-SHA256 signature + one-time token + TTL) after each move, and the client must present the valid ticket with the next move. This prevents:
- **Board tampering** — the server rejects any state whose HMAC signature doesn't match
- **Replay attacks** — used tokens are stored in a PostgreSQL database and rejected on reuse
- **Timeout violations** — tokens carry a 10-second TTL; expired tokens are rejected

#### `webapp` — HTTP/JSON API with a Browser Client
Replaces raw TCP with an HTTP server (`com.sun.net.httpserver`). The server exposes a single `POST /play` endpoint that accepts and returns JSON. This makes the game playable from a browser via a minimal HTML + JavaScript client (`BrowserClient.html` + `script.js`).

See [`ttt-network/README.md`](ttt-network/README.md) for full setup and run instructions.

---

### `ttt-servlet` — Servlet-Based Web Application

Extends the HTTP version into a proper Java EE servlet deployed on a servlet container (e.g. Tomcat). See [`ttt-servlet/README.md`](ttt-servlet/README.md) for setup and run instructions.

---

## What This Project Covers

| Concept | Where |
|---|---|
| OOP — inheritance, abstract classes, polymorphism | All versions |
| Unit testing with JUnit 5 | `ttt-basic` |
| TCP socket programming | `ttt-network/net_threads` |
| Stateless protocol design | `ttt-network/multiClient_singleThread` |
| HMAC-SHA256 message authentication | `ttt-network/multiClient_singleThread` |
| One-time tokens & replay attack prevention | `ttt-network/multiClient_singleThread` |
| PostgreSQL integration (JDBC) | `ttt-network/multiClient_singleThread` |
| HTTP server & REST-style JSON API | `ttt-network/webapp` |
| Browser client (HTML + JavaScript) | `ttt-network/webapp` |
| Java Servlets | `ttt-servlet` |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL (required only for `ttt-network/multiClient_singleThread`)
