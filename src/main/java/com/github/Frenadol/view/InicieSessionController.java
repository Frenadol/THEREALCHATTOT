package com.github.Frenadol.view;

import com.github.Frenadol.App;
import com.github.Frenadol.model.User;
import com.github.Frenadol.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InicieSessionController {
    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Button UsersSession;
    @FXML
    private Button Back;

    private static final String USERS_FILE = "UsersData.xml";

    /**
     * Initiates user login by verifying the username and password.
     * Displays an alert if the login is successful or unsuccessful.
     */
    @FXML
    public void inicieUser() {
        String username = textUsername.getText();
        String pass = textPassword.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            String message = "Por favor, complete todos los campos.";
            showAlert(message);
            return;
        }

        try {
            File xmlFile = new File(USERS_FILE);

            if (isUserExists(username, pass, xmlFile)) {
                String message = "Usuario iniciado sesión exitosamente.";
                showAlert(message);
                App.setRoot("MainMenu");
            } else {
                String message = "Nombre de usuario o contraseña incorrectos.";
                showAlert(message);
            }

        } catch (Exception e) {
            showAlert("Error al iniciar sesión: " + e.getMessage());
        }
    }

    /**
     * Checks if the user exists in the XML file with the provided username and password.
     *
     * @param username the username to check.
     * @param password the password to check.
     * @param xmlFile  the XML file containing user data.
     * @return true if the user exists and the password is correct; false otherwise.
     * @throws Exception if an error occurs while reading the XML file.
     */
    private boolean isUserExists(String username, String password, File xmlFile) throws Exception {
        if (!xmlFile.exists()) return false;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("user");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String existingUsername = element.getElementsByTagName("username").item(0).getTextContent();
                String existingPasswordHash = element.getElementsByTagName("password").item(0).getTextContent();

                if (existingUsername.equals(username) && checkPassword(password, existingPasswordHash)) {
                    SessionManager sessionManager = SessionManager.getInstance();
                    User userLogin = new User(username, existingPasswordHash.getBytes());
                    sessionManager.setCurrentUser(userLogin);
                    System.out.println(userLogin);

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the provided password matches the stored hashed password.
     *
     * @param password     the password to check.
     * @param storedHash   the stored hashed password.
     * @return true if the passwords match; false otherwise.
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available.
     */
    private boolean checkPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(storedHash);
    }

    /**
     * Hashes the provided password using SHA3-256 algorithm.
     *
     * @param password the password to hash.
     * @return the hashed password as a hex string.
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available.
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Navigates back to the primary view.
     *
     * @throws IOException if there is an issue changing the view.
     */
    @FXML
    public void goBack() throws IOException {
        App.setRoot("Primary");
    }

    /**
     * Displays an alert dialog with the specified message.
     *
     * @param message the message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
