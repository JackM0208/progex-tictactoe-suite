# TicTacToe — Servlet Web Application

A browser-based TicTacToe game built with Jakarta Servlets, deployed on Apache Tomcat.
The player (X) plays against a bot (O) through a REST-like API handled by `GameHandler`.

---

## Prerequisites

Before running this project, make sure you have:

- **Java 17+** — [Download here](https://adoptium.net/)
- **Apache Maven 3.6+** — [Download here](https://maven.apache.org/download.cgi)
- **Apache Tomcat 10.1.x** — [Download here](https://tomcat.apache.org/download-10.cgi)

To verify your installations:
```bash
java -version
mvn -version
```

---

## Project Structure

```
ttt-servlet/
├── src/
│   └── main/
│       ├── java/webapp_servlet/Server_side/
│       │   ├── GameHandler.java   ← Servlet (entry point for HTTP requests)
│       │   ├── Game.java          ← Game logic
│       │   ├── Board.java         ← Board state and operations
│       │   ├── BotPlayer.java     ← Bot move logic
│       │   └── Player.java        ← Abstract player class
│       └── webapp/
│           ├── BrowserClient.html ← Game UI
│           ├── script.js          ← Frontend logic (fetch API)
│           └── WEB-INF/
└── pom.xml
```

---

## Step 1 — Build the Project

Navigate to the project root (where `pom.xml` is) and run:

```bash
mvn clean install
```

This compiles the code and packages everything into a `.war` file located at:

```
target/ttt-servlet.war
```

---

## Step 2 — Set Up Tomcat

If you haven't already, extract your Tomcat zip. For example:

```
C:\Tomcat\apache-tomcat-10.1.56\
```

Set the `CATALINA_HOME` environment variable (required each terminal session unless set permanently):

```bat
set CATALINA_HOME=C:\Tomcat\apache-tomcat-10.1.56
```

> **Permanent setup (optional):** Add `CATALINA_HOME` to your Windows System Environment Variables so you don't need to set it every time.

---

## Step 3 — Deploy the WAR File

Copy the built `.war` file into Tomcat's `webapps/` folder:

```bat
copy target\ttt-servlet.war %CATALINA_HOME%\webapps\
```

---

## Step 4 — Start Tomcat

```bat
%CATALINA_HOME%\bin\startup.bat
```

A new terminal window will open showing Tomcat logs. Wait until you see:

```
INFO: Server startup in [X] milliseconds
```

That means Tomcat is ready.

---

## Step 5 — Play the Game

Open your browser and navigate to:

```
http://localhost:8080/ttt-servlet/BrowserClient.html
```

Click any cell to make your move. The bot responds automatically.

---

## Step 6 — Stop Tomcat

When you're done:

```bat
%CATALINA_HOME%\bin\shutdown.bat
```

---

## How It Works

```
Browser click
     ↓
fetch() POST → http://localhost:8080/ttt-servlet/play
     ↓
Tomcat receives request
     ↓
@WebServlet("/play") routes it to GameHandler.doPost()
     ↓
Game logic runs (Game.java, Board.java, BotPlayer.java)
     ↓
JSON response returned to browser
     ↓
script.js updates the board UI
```

### API Reference

**Endpoint:** `POST /ttt-servlet/play`

Request body:
```json
{
  "ID": 1,
  "STATE": "000 000 000",
  "MOVE": 5
}
```

Response body:
```json
{
  "MESSAGE": "Bot has moved.",
  "GAME_OVER": false,
  "STATE": "200 010 000"
}
```

Board state is a string of 9 digits (3 rows separated by spaces):
- `0` = empty
- `1` = Player 1 (human)
- `2` = Player 2 (bot)

---

## Redeploying After Code Changes

Every time you change Java or frontend files:

1. Stop Tomcat: `%CATALINA_HOME%\bin\shutdown.bat`
2. Rebuild: `mvn clean install`
3. Copy new WAR: `copy target\ttt-servlet.war %CATALINA_HOME%\webapps\`
4. Start Tomcat: `%CATALINA_HOME%\bin\startup.bat`

> Always stop Tomcat before copying a new WAR on Windows — the file will be locked otherwise.

---

## Troubleshooting

| Problem | Likely Cause | Fix |
|---|---|---|
| `CATALINA_HOME not defined` | Environment variable not set | Run `set CATALINA_HOME=...` in the same terminal |
| `405 Method Not Allowed` on `/play` | You opened `/play` in browser (GET request) | Normal — use the HTML page, not the URL directly |
| `Hello World!` on homepage | Default `index.jsp` showing | Navigate to `/BrowserClient.html` directly |
| Empty JSON response | Missing `resp.setStatus(200)` in doPost | Make sure `doPost()` sets status 200 explicitly |
| Old WAR still running | Tomcat cached previous deployment | Stop Tomcat, delete old folder in `webapps/`, redeploy |
