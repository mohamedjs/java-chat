 # Java Chat – File & Voice Messaging Application

A desktop chat application implemented in Java (Swing) that allows users to send and receive files and voice messages over a local network. The project includes both a graphical client and server, supporting file transfer, audio recording, and real-time broadcast to all connected clients.

---

## 🧩 Features

- **File Transfer:** Send and receive any file type between clients via the server.
- **Voice Messaging:** Record, send, and play back audio messages (WAV format).
- **Real-Time Broadcast:** Files and messages are broadcast to all connected clients instantly.
- **Graphical User Interface:** Modern, user-friendly Swing GUI for both client and server.
- **File Preview & Download:** Preview text and image files before downloading; play audio files directly.
- **Multi-Client Support:** Server can handle multiple clients simultaneously.

---

## 📁 Project Structure

- `src/main/java/Client/Client.java` – Client application with GUI, file/voice send, and receive logic.
- `src/main/java/Server/Server.java` – Server application with GUI, file receive, broadcast, and preview logic.
- `src/main/java/Server/MyFile.java` – Data model for file transfer (id, name, data, extension).

---

## 🚀 How to Run

1. **Compile the project:**
   - Use your IDE (e.g., NetBeans, IntelliJ) or command line:
     ```bash
     javac -d bin src/main/java/Client/Client.java src/main/java/Server/Server.java src/main/java/Server/MyFile.java
     ```
2. **Start the server:**
   - Run the `Server` class:
     ```bash
     java -cp bin Server.Server
     ```
3. **Start one or more clients:**
   - Run the `Client` class for each user:
     ```bash
     java -cp bin Client.Client
     ```
4. **Use the GUI:**
   - On the client, choose files or record audio to send.
   - On the server, view incoming files and broadcast to all clients.
   - Click file entries to preview, download, or play audio.

---

## 🛠️ Main Functionalities

- **Send File:** Select and send any file to the server, which broadcasts it to all clients.
- **Send Voice Message:** Record audio, send to server, and play on any client.
- **Receive & Preview:** All clients receive files in real time; preview or download as needed.
- **Multi-Client:** Multiple clients can connect and interact simultaneously.

---

## 👤 Author
- Educational project for learning Java networking, Swing GUI, and file/audio handling.

---

Enjoy sharing files and voice messages over your local network with Java Chat!