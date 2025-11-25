package dhkthn.p2p.service.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import dhkthn.p2p.model.message.ChatHistory;
import dhkthn.p2p.model.message.ChatMessage;
import dhkthn.p2p.util.LocalDateTimeAdapter;

public class ChatHistoryService {
    private static final String HISTORY_DIR = "chat_history";
    private static final Gson gson;
    
    static {
        // 🆕 Cấu hình Gson để hỗ trợ LocalDateTime
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        
        // Tạo thư mục lưu trữ nếu chưa tồn tại
        File dir = new File(HISTORY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private Map<String, ChatHistory> chatHistories = new ConcurrentHashMap<>();
    
    // 🆕 Thêm tin nhắn vào lịch sử
    public void addMessage(String peerKey, ChatMessage message) {
        ChatHistory history = chatHistories.computeIfAbsent(peerKey, 
            key -> new ChatHistory(extractPeerName(key), extractPeerIP(key), extractPeerPort(key)));
        
        history.addMessage(message);
        
        // 🆕 Tự động lưu vào file sau mỗi tin nhắn
        saveChatHistory(peerKey, history);
    }
    
    // 🆕 Lấy lịch sử chat với một peer
    public List<ChatMessage> getChatHistory(String peerKey) {
        ChatHistory history = chatHistories.get(peerKey);
        if (history == null) {
            // 🆕 Thử load từ file nếu chưa có trong memory
            history = loadChatHistory(peerKey);
            if (history != null) {
                chatHistories.put(peerKey, history);
            }
        }
        return history != null ? history.getMessages() : new ArrayList<>();
    }
    
    // 🆕 Lưu lịch sử vào file JSON
    private void saveChatHistory(String peerKey, ChatHistory history) {
        String filename = getHistoryFilename(peerKey);
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(history, writer);
            System.out.println("💾 Đã lưu lịch sử chat: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi lưu lịch sử: " + e.getMessage());
        }
    }
    
    // 🆕 Load lịch sử từ file JSON
    private ChatHistory loadChatHistory(String peerKey) {
        String filename = getHistoryFilename(peerKey);
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        
        try (FileReader reader = new FileReader(filename)) {
            ChatHistory history = gson.fromJson(reader, ChatHistory.class);
            System.out.println("📂 Đã load lịch sử chat: " + filename);
            return history;
        } catch (IOException e) {
            System.err.println("❌ Lỗi khi load lịch sử: " + e.getMessage());
            return null;
        }
    }
    
    // 🆕 Lấy tất cả các file lịch sử có sẵn
    public List<String> getAvailableHistories() {
        List<String> histories = new ArrayList<>();
        File dir = new File(HISTORY_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        
        if (files != null) {
            for (File file : files) {
                String filename = file.getName();
                String peerKey = filename.substring(0, filename.length() - 5); // bỏ .json
                histories.add(peerKey);
            }
        }
        return histories;
    }
    
    // 🆕 Xóa lịch sử chat
    public boolean deleteChatHistory(String peerKey) {
        chatHistories.remove(peerKey);
        String filename = getHistoryFilename(peerKey);
        File file = new File(filename);
        return file.delete();
    }
    
    // 🆕 Tiện ích: Tạo key từ thông tin peer
    public static String createPeerKey(String peerName, String peerIP, int peerPort) {
        return peerName + "_" + peerIP + "_" + peerPort;
    }
    
    // 🆕 Tiện ích: Tạo filename từ peerKey
    private String getHistoryFilename(String peerKey) {
        return HISTORY_DIR + File.separator + peerKey + ".json";
    }
    
    private String extractPeerName(String peerKey) {
        return peerKey.split("_")[0];
    }
    
    private String extractPeerIP(String peerKey) {
        return peerKey.split("_")[1];
    }
    
    private int extractPeerPort(String peerKey) {
        return Integer.parseInt(peerKey.split("_")[2]);
    }
}