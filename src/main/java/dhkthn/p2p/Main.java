package dhkthn.p2p;

import dhkthn.p2p.controller.ChatController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        Parent root = loader.load();
        
        // Load CSS
        root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        
        // Lấy controller để shutdown khi đóng app
        ChatController controller = loader.getController();
        
        primaryStage.setTitle("P2P Chat - UserA (Port: 12345)");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setX(100);
        primaryStage.setY(100);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(e -> {
            if (controller != null) {
                controller.shutdown();
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}