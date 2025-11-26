package dhkthn.p2p.controller;

import dhkthn.p2p.Main;
import dhkthn.p2p.config.AppConfig;
import dhkthn.p2p.model.User;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.InputEvent;
import lombok.NoArgsConstructor;
import java.io.IOException;

@SuppressWarnings("ALL")
@NoArgsConstructor
public class SignUpController {
    private final Helper helper = new Helper();
    private static final PseudoClass INVALID = PseudoClass.getPseudoClass("invalid");

    @FXML
    private MFXTextField usernameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private MFXPasswordField passwordField2;

    @FXML
    private void handleSignUpBtn(ActionEvent e) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || username.length() < 6) {
            usernameField.pseudoClassStateChanged(INVALID, true);
            usernameField.setFloatingText("Username must be at least 6 character");
            return;
        } else {
            usernameField.pseudoClassStateChanged(INVALID, false);
            usernameField.setFloatingText("Username");
        }

        if (password.isBlank() || password.length() < 6) {
            passwordField.pseudoClassStateChanged(INVALID, true);
            passwordField.setFloatingText("Password must be at least 6 character");
            return;
        } else {
            passwordField.pseudoClassStateChanged(INVALID, false);
            passwordField.setFloatingText("Password");
        }

        if(!password.equals(passwordField2.getText())){
            passwordField2.pseudoClassStateChanged(INVALID, true);
            passwordField2.setFloatingText("Password not matched");
            return;
        } else {
            passwordField2.pseudoClassStateChanged(INVALID, false);
            passwordField2.setFloatingText("Password");
        }

        User user = User.builder()
                .password(AppConfig.hashPassword(password))
                .username(username)
                .build();

        try {
            user.saveUser();
            helper.showAlert("Sign up successful!");
            Main.setRoot("sign-in.fxml");
        } catch (Exception ex) {
            helper.showAlert(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleSignInClicked(InputEvent e) throws IOException {
        Main.setRoot("sign-in.fxml");
    }
}
