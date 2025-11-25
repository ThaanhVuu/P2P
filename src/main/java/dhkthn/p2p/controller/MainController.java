package dhkthn.p2p.controller;

import dhkthn.p2p.model.message.ChatMessage;
import dhkthn.p2p.service.message.MessageServer;
import dhkthn.p2p.service.message.MessageService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class MainController {

    // CÁC COMPONENT UI
    @FXML private ListView<String> peerListView;
    @FXML private TextArea chatArea;
    @FXML private TextField messageInput;
    @FXML private Button sendButton;
    @FXML private VBox messageContainer;

    // SERVICES
    private MessageService messageService;
    private MessageServer messageServer;
    private int myPort = 12345; // Port của UserA

    @FXML
    public void initialize() {
        setupMessageServices();
        setupEventHandlers();
        startMessageServer();
        
        // Thêm vài peer mẫu để test
        peerListView.getItems().addAll(
            "UserB - 127.0.0.1:12346",
            "UserC - 127.0.0.1:12347"
        );
        
        addSystemMessage("💬 Chat P2P đã sẵn sàng!");
        addSystemMessage("Chọn một peer để bắt đầu chat");
    }

    private void setupMessageServices() {
        // Khởi tạo service với callback khi có tin nhắn mới
        messageService = new MessageService(this::displayMessage);
        messageServer = new MessageServer(myPort, this::displayMessage);
    }

    private void startMessageServer() {
        messageServer.startServer();
    }

    private void setupEventHandlers() {
        // Bấm nút Send
        sendButton.setOnAction(e -> sendMessage());
        
        // Enter để gửi
        messageInput.setOnAction(e -> sendMessage());
        
        // Chọn peer từ list
        peerListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> connectToPeer(newVal)
        );
    }

    // KẾT NỐI ĐẾN PEER ĐƯỢC CHỌN
    private void connectToPeer(String peerInfo) {
        if (peerInfo == null) return;
        
        try {
            String[] parts = peerInfo.split(" - ");
            String peerName = parts[0];
            String address = parts[1];
            String ip = address.split(":")[0];
            int port = Integer.parseInt(address.split(":")[1]);
            
            boolean success = messageService.connectToPeer(ip, port);
            if (success) {
                addSystemMessage("✅ Đã kết nối với " + peerName);
            } else {
                addSystemMessage("❌ Không thể kết nối với " + peerName);
            }
            
        } catch (Exception e) {
            addSystemMessage("❌ Lỗi kết nối: " + e.getMessage());
        }
    }

    // GỬI TIN NHẮN
    @FXML
    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (message.isEmpty()) return;
        
        messageService.sendMessage(message);
        messageInput.clear();
    }

    // HIỂN THỊ TIN NHẮN LÊN UI
    private void displayMessage(ChatMessage chatMessage) {
        // Hiển thị trong TextArea (đơn giản)
        chatArea.appendText(chatMessage + "\n");
        
        // Hoặc hiển thị bubble chat (đẹp hơn)
        addMessageBubble(chatMessage);
    }

    // HIỂN THỊ BUBBLE CHAT (Như app chat thật)
    private void addMessageBubble(ChatMessage chatMessage) {
        HBox messageBox = new HBox();
        messageBox.setMaxWidth(Double.MAX_VALUE);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        
        Label label = new Label(chatMessage.getContent());
        label.setWrapText(true);
        label.setMaxWidth(300);
        label.setPadding(new Insets(8, 12, 8, 12));
        
        // Style cho bubble
        if (chatMessage.isSelf()) {
            // Tin nhắn của mình - màu xanh, bên phải
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            label.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 15;");
        } else {
            // Tin nhắn người khác - màu xám, bên trái  
            messageBox.setAlignment(Pos.CENTER_LEFT);
            label.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: black; -fx-background-radius: 15;");
        }

        messageBox.getChildren().add(label);
        messageContainer.getChildren().add(messageBox);
    }

    // TIN NHẮN HỆ THỐNG
    private void addSystemMessage(String content) {
        chatArea.appendText("⚡ " + content + "\n");
    }

    // DỌN DẸP KHI ĐÓNG APP
    public void shutdown() {
        if (messageService != null) messageService.disconnect();
        if (messageServer != null) messageServer.stopServer();
    }
}