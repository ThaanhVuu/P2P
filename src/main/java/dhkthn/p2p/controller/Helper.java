package dhkthn.p2p.controller;

import javafx.scene.control.Alert;

public class Helper {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);  // nếu không muốn header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
