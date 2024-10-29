package com.github.Frenadol.view;

import java.io.IOException;

import com.github.Frenadol.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {
    @FXML
    private Button registrarButton;
    @FXML
    private Button iniciarButton;

    /**
     * Switches the view to the registration screen when the register button is clicked.
     *
     * @throws IOException if there is an issue loading the registration view.
     */
    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("RegisterUser");
    }

    /**
     * Switches the view to the login screen when the login button is clicked.
     *
     * @throws IOException if there is an issue loading the login view.
     */
    @FXML
    private void switchToInicie() throws IOException {
        App.setRoot("InicieSession");
    }
}
