# Collaborative Document Editor
### Real-time synchronization engine built on Gang of Four design patterns and WebSockets — pure Java, zero frameworks.

---

## What This Project Is

Modern collaborative tools like Google Docs solve three hard engineering problems at once — every keystroke must be reversible, changes must broadcast to all connected users instantly, and the full edit history must be navigable without corrupting document state.

This project solves all three, from scratch, in pure Java. No Spring. No Tomcat. No shortcuts.

Each problem maps directly to a classical design pattern. The architecture is not pattern-for-pattern's sake — every pattern earns its place by solving a real constraint.

---

## The Architecture

```
┌─────────────────────────────────────────────────────────┐
│                        Client A                         │
│  Console Input → CommandInvoker → DocumentModel        │
│                       ↓               ↓                │
│               CareTaker (Memento)   EventBus (Observer) │
│                                        ↓               │
│                              ChangeBroadcaster          │
└────────────────────────┬────────────────────────────────┘
                         │  WebSocket (java-websocket)
                    ┌────┴────┐
                    │  Server │  ← Relay node, broadcasts
                    └────┬────┘     to all other clients
                         │
┌────────────────────────┴────────────────────────────────┐
│                        Client B                         │
│  [SYNC] receives serialized command → applies locally  │
└─────────────────────────────────────────────────────────┘
```

---

## The Big Four Patterns

| Pattern | Where It Lives | What It Solves |
|---|---|---|
| **Command** | `commands/` | Wraps every user action as an object with `execute()` and `undo()` — makes unlimited undo/redo possible by design |
| **Memento** | `model/` | Snapshots full document state before each command — guarantees 100% safe restoration with no side effects |
| **Observer** | `observer/` | Decouples document core from UI, cursor tracking, and network — document never knows who is listening |
| **Iterator** | `history/` | Walks the command log forward and backward without exposing the internal undo/redo stacks |

---

## Feature Set

- **Unlimited undo / redo** — every insert, delete, and format operation is fully reversible via a double-stack Command pattern implementation
- **Real-time collaboration** — changes broadcast to all connected clients via WebSocket with sub-100ms latency on localhost
- **Full state snapshots** — Memento captures document state before every mutation; history jumps restore to any checkpoint cleanly
- **Collaborative cursor tracking** — each client's cursor position is tracked and updated on every incoming change event
- **Searchable audit log** — every edit is recorded as a `HistoryEntry` with user ID, timestamp, and human-readable label
- **History browser** — Iterator-powered navigation lets you walk the full edit timeline and jump the document to any past state
- **Standalone execution** — runs as a plain JAR; no application server, no container, no configuration files

---

## Project Structure

```
src/main/java/com/editor/
│
├── model/
│   ├── DocumentModel.java          ← Memento Originator; holds content + formatting
│   ├── Memento.java                ← Immutable state snapshot (id, timestamp, content)
│   └── CareTaker.java              ← Ordered list of snapshots indexed by command
│
├── commands/
│   ├── Command.java                ← Interface: execute(), undo(), getId(), getDescription()
│   ├── InsertTextCommand.java      ← Stores position + text; undo deletes same range
│   ├── DeleteTextCommand.java      ← Captures deleted text at construction for safe undo
│   └── FormatCommand.java          ← Stores previous style map for reversible formatting
│
├── invoker/
│   └── CommandInvoker.java         ← Snapshot → execute → push undo; undo → pop → push redo
│
├── observer/
│   ├── DocumentObserver.java       ← Interface: onDocumentChanged(command, snapshot)
│   ├── UIRenderer.java             ← Repaints affected character range only
│   ├── CollaboratorCursorTracker.java ← Maintains userId → cursorPosition map
│   └── ChangeBroadcaster.java      ← Serializes and queues events for WebSocket send
│
├── history/
│   ├── HistoryEntry.java           ← Pairs a Command with its pre-execution Memento
│   ├── HistoryIterator.java        ← Bidirectional cursor over the entry list
│   └── HistoryManager.java         ← Records entries; exposes iterator; handles jump-to
│
├── server/
│   └── EditorServer.java           ← WebSocket server; relays commands to all other clients
│
├── client/
│   └── EditorClient.java           ← WebSocket client + console input loop + command dispatch
│
└── Main.java                       ← Entry point; routes to server or client mode via args
```

---

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher

### Build

```bash
git clone https://github.com/yourusername/collab-editor.git
cd collab-editor
mvn clean install
```

### Run the server

```bash
java -cp target/collab-editor-1.0.jar com.editor.Main server
```

Output:
```
[SERVER] Editor server started on port 8080
```

### Connect clients (open separate terminals)

```bash
# Terminal 2
java -cp target/collab-editor-1.0.jar com.editor.Main client Priya

# Terminal 3
java -cp target/collab-editor-1.0.jar com.editor.Main client Ravi
```

### Available console commands

| Command | Usage | What It Does |
|---|---|---|
| `INSERT` | `INSERT <position> <text>` | Inserts text at the given position |
| `DELETE` | `DELETE <position> <length>` | Deletes characters from position |
| `FORMAT` | `FORMAT <start> <end> <style>` | Applies formatting to a range |
| `UNDO` | `UNDO` | Reverses the last command |
| `REDO` | `REDO` | Re-applies the last undone command |
| `HISTORY` | `HISTORY` | Prints the full edit timeline |
| `JUMP` | `JUMP <index>` | Restores document to a history checkpoint |
| `SHOW` | `SHOW` | Prints current document content and cursor |

### Demo session

```
Priya > INSERT 0 Hello
[DONE] INSERT "Hello" at position 0
--- Document ---
Hello
Cursor at: 5
----------------

Priya > INSERT 5  World
[DONE] INSERT " World" at position 5

Priya > UNDO
[UNDO] INSERT " World" at position 5

Priya > HISTORY
--- Edit History ---
  [0] [Priya] INSERT "Hello" at position 0  << current
  [1] [Priya] INSERT " World" at position 5  ↩ undone
--------------------

# Meanwhile in Ravi's terminal:
[SYNC] [Priya] INSERT "Hello" at position 0
[SYNC] [Priya] INSERT " World" at position 5
[SYNC] [Priya] UNDO — " World" removed
```

---

## Design Decisions Worth Noting

**Why snapshot before execute, not after**
The Memento is saved inside `CommandInvoker.execute()` before the command runs. This means the snapshot always represents the state *before* the mutation, so `undo()` can restore it perfectly — even if the command partially fails.

**Why `deletedText` is captured in the constructor**
`DeleteTextCommand` reads the target substring from the document at construction time, not at execute time. This ensures the text is captured at the moment the user issues the command, before any other command could mutate the same region.

**Why `redoStack.clear()` on every new execute**
Any new user action creates a new timeline. Preserving the redo stack after a new action would allow the document to branch into inconsistent states. Clearing it enforces a single linear history, which is the correct behavior for a text editor.

**Why Iterator instead of direct stack access**
Giving the History Panel direct access to the undo/redo stacks would let UI code accidentally pop commands, breaking the engine. The Iterator exposes read-only traversal with no mutation capability — the stacks stay protected.

---

## Skills Demonstrated

- **Software architecture** — four GoF patterns composing into a coherent system, each with a distinct responsibility
- **Backend engineering** — socket programming, real-time event routing, multi-client state synchronization
- **Java fundamentals** — interfaces, generics, collections, UUID, ANSI terminal output, clean package structure
- **Engineering reasoning** — every design decision has a traceable reason; nothing is pattern-for-pattern's sake
- **Documentation** — architecture diagrams, decision rationale, reproducible run instructions

---

## Possible Extensions

- **Operational Transformation** — resolve conflicts when two users edit the same position simultaneously
- **Persistent history** — serialize the command log to JSON and reload on reconnect
- **Named checkpoints** — let users label a Memento as a named version (v1.0, "before refactor")
- **Branching history** — VS Code-style undo tree where new edits after an undo create a branch

---

## Author

**Your Name**
[LinkedIn](https://linkedin.com/in/kavinayasri-m) · [GitHub](https://github.com/Kavi-654)

> Built as a deep-dive into software architecture — demonstrating that design patterns are not academic theory but practical tools that solve real engineering constraints.
