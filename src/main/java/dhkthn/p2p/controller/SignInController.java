package dhkthn.p2p.controller;

import dhkthn.p2p.Main;
import dhkthn.p2p.config.AppConfig;
import dhkthn.p2p.model.User;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.InputEvent;

@SuppressWarnings("CallToPrintStackTrace")
public class SignInController {
    @FXML
    private MFXTextField usernameField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    public void handleSignUpClicked(InputEvent mouseEvent) throws Exception {
        Main.setRoot("register.fxml");
    }

    @FXML
    private void handleSignInButton(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = new User().authenticate(username, password);
            if(user != null){
                AppConfig.setUser(user);
                Helper.showAlert("Login successful!");
                Main.setRoot("chat.fxml");
            }else{
                Helper.showAlert("Invalid Incredential!");
            }
        }catch (Exception e){
            Helper.showAlert("Some thing went wrong when sign in");
            e.printStackTrace();
        }
    }
}
