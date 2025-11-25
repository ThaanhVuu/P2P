package dhkthn.p2p.service.message;

import dhkthn.p2p.model.message.ChatMessage;
import javafx.application.Platform; // 🆕

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class MessageService {
    private Consumer<ChatMessage> onMessageReceived;
    private PrintWriter out;
    private Socket currentSocket;
    
    public MessageService(Consumer<ChatMessage> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }
    
    public boolean connectToPeer(String ip, int port) {
        try {
            currentSocket = new Socket(ip, port);
            out = new PrintWriter(currentSocket.getOutputStream(), true);
            
            startListening();
            return true;
            
        } catch (IOException e) {
            System.out.println("❌ Kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
    
    public void sendMessage(String message) {
        if (out == null || currentSocket.isClosed()) {
            System.out.println("⚠️ Chưa kết nối đến ai!");
            return;
        }

        try {
            out.println(message);
            System.out.println("📤 Đã gửi: " + message);
            
            // 🆕 Tin nhắn của mình cũng cần Platform.runLater() 
            // vì có thể được gọi từ network thread
            ChatMessage myMessage = new ChatMessage(message, "Bạn", true);
            
            Platform.runLater(() -> {
                onMessageReceived.accept(myMessage);
            });
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi gửi: " + e.getMessage());
        }
    }
    
    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(currentSocket.getInputStream())
                );
                
                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {
                    System.out.println("📥 Nhận được: " + receivedMessage);
                    
                    // 🆕 SỬA: Dùng Platform.runLater()
                    ChatMessage theirMessage = new ChatMessage(receivedMessage, "Đối phương", false);
                    
                    Platform.runLater(() -> {
                        onMessageReceived.accept(theirMessage);
                    });
                }
                
            } catch (IOException e) {
                System.out.println("🔌 Mất kết nối với peer!");
            }
        });
        
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
    
    public void disconnect() {
        try {
            if (out != null) out.close();
            if (currentSocket != null) currentSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}