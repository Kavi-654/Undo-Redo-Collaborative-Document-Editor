# 📝 Collaborative Design Patterns Editor  
> **A high-fidelity synchronization engine demonstrating the synergy of GoF Design Patterns and WebSockets.**

---

## 🌟 Project Overview
Modern collaborative tools like Google Docs solve complex architectural challenges regarding state synchronization and history management.  

This project is a **deep-dive into backend engineering**, built in **Pure Java** to highlight raw architectural skills.

Every feature—from real-time typing to undo/redo—is powered by a dedicated **Gang of Four (GoF)** design pattern working together.

---

## 🏗️ The Architectural "Big Four"

| Pattern   | Role in Project                                      | Engineering Benefit |
|----------|------------------------------------------------------|--------------------|
| Command  | Encapsulates user actions (Insert/Delete/Format)     | Enables clean Undo/Redo and action replay |
| Memento  | Captures immutable snapshots of document state       | Ensures 100% state integrity |
| Observer | Broadcasts changes to UI and WebSocket clients       | Decouples business logic from communication |
| Iterator | Navigates edit history chronologically               | Traverses history safely |

---

## ⚙️ Core Features

- ⚡ **Real-time Synchronization**  
  Sub-100ms latency broadcasting across multiple clients using WebSockets  

- 🔁 **Granular Undo/Redo**  
  Unlimited stack depth powered by Command Pattern  

- 👥 **Collaborative Cursor Tracking**  
  Real-time visibility of all users  

- 📜 **Audit Log & History Browser**  
  Searchable timeline of document changes  

- 🧩 **Standalone Architecture**  
  Runs without heavy servers like Tomcat  

---

## 🛠️ Technical Implementation

### 🔌 Real-Time Networking (WebSockets)
- Implemented full-duplex communication using `Java-WebSocket`
- Server acts as a **Relay Node**
- Broadcasts serialized commands to all clients

---

### 🧠 State Management (Memento)
- Uses **Incremental Mementos**
- Stores only modified parts (memory efficient)
- Ensures consistent undo/redo behavior

---

### ⚙️ Concurrency Handling
- Supports multiple users simultaneously  
- Simulates distributed environment using multiple JVM instances  

---

## 🚀 Execution Guide

### ✅ Prerequisites
- JDK 17+
- Maven 3+

---

### 🔨 Build the Project
```bash
mvn clean install

▶️ Start Server
java -cp target/UndoRedo-1.0-SNAPSHOT.jar com.editor.Main server
👥 Start Clients
# Instance 1
java -cp target/UndoRedo-1.0-SNAPSHOT.jar com.editor.Main client User1

# Instance 2
java -cp target/UndoRedo-1.0-SNAPSHOT.jar com.editor.Main client User2
👨‍💻 Skills Demonstrated
🏗️ Software Architecture
GoF Design Patterns
SOLID Principles
🔙 Backend Development
Socket Programming
Real-time systems
⚡ Concurrency
Multi-user real-time handling
🧩 Problem Solving
Translating theory into production-ready systems
📌 Highlights
Pure Java implementation (no heavy frameworks)
Real-time multi-user collaboration
Advanced design pattern usage
Scalable and modular architecture
📧 Contact
🔗 LinkedIn
💻 GitHub

🚀 Developed as a capstone project for advanced Software Architecture and Backend Engineering
