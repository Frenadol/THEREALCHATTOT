package com.github.Frenadol.view;

import com.github.Frenadol.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashController implements Initializable {
    @FXML
    private AnchorPane splashPane;

    /**
     * Initializes the SplashController. This method is called after the FXML
     * file has been loaded. It runs an asynchronous task to simulate a splash
     * screen duration before transitioning to the Primary view.
     *
     * @param url the location used to resolve relative paths for the root object, or null
     * @param rb  the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(600);
                Platform.runLater(() -> {
                    try {
                        App.setRoot("Primary");
                    } catch (IOException ex) {
                        Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
