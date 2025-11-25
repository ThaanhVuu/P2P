package dhkthn.p2p.service.message;

import dhkthn.p2p.model.message.ChatMessage;
import javafx.application.Platform; // 🆕

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class MessageServer {
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private Consumer<ChatMessage> onMessageReceived;
    private int port;
    
    public MessageServer(int port, Consumer<ChatMessage> onMessageReceived) {
        this.port = port;
        this.onMessageReceived = onMessageReceived;
        this.executor = Executors.newFixedThreadPool(10);
    }
    
    public void startServer() {
        executor.execute(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("🚀 Message Server started on port " + port);
                
                while (!Thread.currentThread().isInterrupted()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("✅ Có kết nối mới từ: " + clientSocket.getInetAddress());
                    
                    handleClientConnection(clientSocket);
                }
                
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.out.println("❌ Server error: " + e.getMessage());
                }
            }
        });
    }
    
    private void handleClientConnection(Socket clientSocket) {
        executor.execute(() -> {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
                );
                
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("📥 Nhận tin nhắn: " + message);
                    
                    // 🆕 SỬA: Dùng Platform.runLater()
                    ChatMessage chatMessage = new ChatMessage(message, "Người gửi", false);
                    
                    Platform.runLater(() -> {
                        onMessageReceived.accept(chatMessage);
                    });
                }
                
            } catch (IOException e) {
                System.out.println("🔌 Client disconnected: " + e.getMessage());
            }
        });
    }
    
    public void stopServer() {
        try {
            if (serverSocket != null) serverSocket.close();
            if (executor != null) executor.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}