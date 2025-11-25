package dhkthn.p2p.model.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    private String peerName;
    private String peerIP;
    private int peerPort;
    private List<ChatMessage> messages;
    private LocalDateTime lastUpdated;

    public ChatHistory(String peerName, String peerIP, int peerPort) {
        this.peerName = peerName;
        this.peerIP = peerIP;
        this.peerPort = peerPort;
        this.messages = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public String getPeerName() { return peerName; }
    public String getPeerIP() { return peerIP; }
    public int getPeerPort() { return peerPort; }
    public List<ChatMessage> getMessages() { return messages; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        this.lastUpdated = LocalDateTime.now();
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}