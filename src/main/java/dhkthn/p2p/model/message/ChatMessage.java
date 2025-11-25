package dhkthn.p2p.model.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private String content;
    private String sender;
    private LocalDateTime timestamp;
    private boolean isSelf;
    
    // 🆕 Constructor mặc định cho JSON
    public ChatMessage() {
        // Constructor rỗng cho JSON deserialization
    }
    
    public ChatMessage(String content, String sender, boolean isSelf) {
        this.content = content;
        this.sender = sender;
        this.isSelf = isSelf;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters (QUAN TRỌNG cho JSON)
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public boolean isSelf() { return isSelf; }
    public void setSelf(boolean self) { isSelf = self; }
    
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timestamp != null ? timestamp.format(formatter) : "";
    }
    
    @Override
    public String toString() {
        String direction = isSelf ? "→" : "←";
        return String.format("[%s] %s %s: %s", getFormattedTime(), direction, sender, content);
    }
}