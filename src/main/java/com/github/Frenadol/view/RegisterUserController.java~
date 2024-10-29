package com.github.Frenadol.view;

import com.github.Frenadol.App;
import com.github.Frenadol.utils.Security;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RegisterUserController {
    @FXML
    private TextField textUsername;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Button usersRegister;
    @FXML
    private Button BackButton;
    @FXML
    private ImageView imageView;

    private File imageFile;
    private static final String USERS_FILE = "UsersData.xml";

    /**
     * Registers a new user and saves the user data to an XML file.
     * It performs several checks, including whether all fields are filled,
     * if the password is secure, and if the username is already taken.
     */
    @FXML
    public void registerUser() {
        String username = textUsername.getText();
        String pass = textPassword.getText();

        if (username.isEmpty() || pass.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (imageFile == null) {
            showAlert("Please choose a profile picture.");
            return;
        }

        if (!isPasswordSecure(pass)) {
            showAlert("Password must be at least 8 characters long, with an uppercase letter and a digit.");
            return;
        }

        try {
            File xmlFile = new File(USERS_FILE);

            if (isUserExists(username, xmlFile)) {
                showAlert("Username already in use. Please choose another.");
                return;
            }

            String hashedPassword;
            try {
                hashedPassword = Security.hashPassword(pass);
            } catch (NoSuchAlgorithmException e) {
                showAlert("Error hashing the password.");
                return;
            }

            byte[] imageData = new byte[(int) imageFile.length()];
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                fis.read(imageData);
            }

            addUserToXML(username, hashedPassword, imageData, xmlFile);
            showAlert("User registered successfully!");
            App.setRoot("primary");
        } catch (Exception e) {
            showAlert("Error registering user: " + e.getMessage());
        }
    }

    /**
     * Checks if a password is secure.
     * A secure password contains at least one uppercase letter, one digit, and is at least 8 characters long.
     *
     * @param password the password to check
     * @return true if the password is secure, false otherwise
     */
    private boolean isPasswordSecure(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUppercase && hasDigit;
    }

    /**
     * Checks if a user already exists in the XML file.
     *
     * @param username the username to check
     * @param xmlFile  the XML file containing user data
     * @return true if the user exists, false otherwise
     * @throws Exception if an error occurs while reading the XML file
     */
    private boolean isUserExists(String username, File xmlFile) throws Exception {
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

                if (existingUsername.equals(username)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds a new user to the XML file.
     * If the file does not exist, it creates a new one.
     *
     * @param username   the username of the new user
     * @param password   the hashed password of the new user
     * @param imageData  the profile image data in byte array format
     * @param xmlFile    the XML file where user data is saved
     * @throws ParserConfigurationException if there is an error in the XML parser configuration
     * @throws TransformerException if there is an error transforming the XML data
     * @throws IOException if there is an issue with file input/output
     * @throws org.xml.sax.SAXException if there is an error parsing the XML
     */
    private void addUserToXML(String username, String password, byte[] imageData, File xmlFile)
            throws ParserConfigurationException, TransformerException, IOException, org.xml.sax.SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc;

        if (xmlFile.exists()) {
            doc = docBuilder.parse(xmlFile);
        } else {
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);
        }

        Element rootElement = doc.getDocumentElement();

        Element userElement = doc.createElement("user");
        rootElement.appendChild(userElement);

        Element usernameElement = doc.createElement("username");
        usernameElement.appendChild(doc.createTextNode(username));
        userElement.appendChild(usernameElement);

        Element passwordElement = doc.createElement("password");
        passwordElement.appendChild(doc.createTextNode(password));
        userElement.appendChild(passwordElement);

        Element imageElement = doc.createElement("image");
        imageElement.appendChild(doc.createTextNode(Base64.getEncoder().encodeToString(imageData)));
        userElement.appendChild(imageElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    /**
     * Displays an alert dialog with a specified message.
     * The alert dialog is created and displayed to inform the user of various situations.
     *
     * @param message the message to display in the alert dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);

        alert.getDialogPane().setPrefSize(400, 200);
        alert.getDialogPane().setStyle("-fx-font-size: 14px;");

        alert.show();
    }



    /**
     * Allows the user to select an image file for their profile picture.
     * It opens a file chooser dialog and sets the selected image to the image view.
     */
    @FXML
    private void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) imageView.getScene().getWindow();
        imageFile = fileChooser.showOpenDialog(stage);
        if (imageFile != null) {
            try {
                InputStream is = new FileInputStream(imageFile);
                Image image = new Image(is);
                applyCircularClip(imageView);
                imageView.setImage(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Navigates back to the primary view.
     *
     * @throws IOException if there is an issue loading the primary view
     */
    @FXML
    public void goBack() throws IOException {
        App.setRoot("Primary");
    }

    /**
     * Applies a circular clip to the ImageView to create a circular appearance for the profile picture.
     *
     * @param imageView the ImageView to apply the circular clip to
     */
    private void applyCircularClip(ImageView imageView) {
        Circle clip = new Circle(imageView.getFitWidth() / 2, imageView.getFitHeight() / 2,
                Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
    }
}
