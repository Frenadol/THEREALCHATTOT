package com.github.Frenadol.view;

import com.github.Frenadol.model.User;
import com.github.Frenadol.model.Message;
import com.github.Frenadol.utils.XmlReader;
import com.github.Frenadol.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController implements Initializable {
    @FXML
    private VBox VboxChat;
    @FXML
    private ListView<String> messageList;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;

    private User currentUser;
    private User selectedUser;

    private static final String filePathChat = "ChatData.xml";
    private static final String filePathChatTxt = "ChatData.txt";

    private static final Logger logger = Logger.getLogger(ChatController.class.getName());

    SessionManager sessionManager = SessionManager.getInstance();

    /**
     * Initializes the controller.
     * This method is called when the view is loaded.
     * It sets up the current user, selected user, and displays messages.
     *
     * @param url the URL of the FXML resource.
     * @param resourceBundle the resource bundle associated with the view.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = sessionManager.getCurrentUser();
        selectedUser = sessionManager.getSelectedUser();

        logger.log(Level.INFO, "Current User: {0}", currentUser);
        logger.log(Level.INFO, "Selected User: {0}", selectedUser);
        messageList.setCellFactory(listView -> new MessageListCell());

        if (selectedUser != null) {
            displayMessages();
        } else {
            showAlert("Error", "No se ha seleccionado un usuario", "Por favor, selecciona un usuario con el que chatear.");
        }

        sendButton.setOnAction(event -> {
            if (selectedUser != null) {
                sendMessage();
            } else {
                showAlert("Error", "No se ha seleccionado un usuario", "Por favor, selecciona un usuario con el que chatear.");
            }
        });

        messageField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    sendMessage();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Displays messages in the message list.
     * It loads messages from the XML file and shows them in the UI.
     */
    @FXML
    private void displayMessages() {
        try {
            List<Message> messages = XmlReader.getMessagesFromXML(filePathChat);

            for (Message message : messages) {
                if (message.getSender().equals(currentUser) && message.getReceiver().equals(selectedUser)) {
                    messageList.getItems().add("Para " + selectedUser.getName() + ": " + message.getContent());
                } else if ((message.getSender().equals(selectedUser) && message.getReceiver().equals(currentUser))) {
                    messageList.getItems().add("De " + selectedUser.getName() + ": " + message.getContent());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading messages", e);
            showAlert("Error al cargar mensajes", "No se pudieron cargar los mensajes", e.getMessage());
        }
    }

    /**
     * Adds a new message to the XML file.
     *
     * @param newMessage the message to be added.
     * @param xmlFile the XML file where the message will be stored.
     * @throws ParserConfigurationException if there is an issue with the parser configuration.
     * @throws TransformerException if there is an issue transforming the document.
     * @throws IOException if there is an issue reading/writing the file.
     * @throws org.xml.sax.SAXException if there is an issue with the XML structure.
     */
    private void addMessageToXML(Message newMessage, File xmlFile)
            throws ParserConfigurationException, TransformerException, IOException, org.xml.sax.SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc;

        if (xmlFile.exists()) {
            doc = docBuilder.parse(xmlFile);
        } else {
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("messages");
            doc.appendChild(rootElement);
        }

        Element rootElement = doc.getDocumentElement();

        Element messageElement = doc.createElement("message");
        rootElement.appendChild(messageElement);

        Element senderElement = doc.createElement("sender");
        senderElement.appendChild(doc.createTextNode(newMessage.getSender().getName()));
        messageElement.appendChild(senderElement);

        Element receiverElement = doc.createElement("receiver");
        receiverElement.appendChild(doc.createTextNode(newMessage.getReceiver().getName()));
        messageElement.appendChild(receiverElement);

        Element contentElement = doc.createElement("content");
        contentElement.appendChild(doc.createTextNode(newMessage.getContent()));
        messageElement.appendChild(contentElement);

        Element timestampElement = doc.createElement("timestamp");
        timestampElement.appendChild(doc.createTextNode(newMessage.getTimestamp().toString()));
        messageElement.appendChild(timestampElement);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    /**
     * Adds a new message to the text file.
     *
     * @param newMessage the message to be added.
     * @param txtFile the text file where the message will be stored.
     * @throws IOException if there is an issue reading/writing the file.
     */
    private void addMessageToTxt(Message newMessage, File txtFile) throws IOException {
        try (FileWriter writer = new FileWriter(txtFile, true)) {
            writer.write("[" + newMessage.getTimestamp() + "] " + newMessage.getSender().getName() +
                    " a " + newMessage.getReceiver().getName() + ": " + newMessage.getContent() + "\n");
        }
    }

    /**
     * Sends a message to the selected user.
     * Creates a new Message object and saves it to the XML and text files.
     */
    @FXML
    private void sendMessage() {
        String content = messageField.getText();

        if (content != null && !content.isEmpty() && selectedUser != null) {
            Message newMessage = new Message(currentUser, selectedUser, content, LocalDateTime.now());

            try {
                addMessageToXML(newMessage, new File(filePathChat));
                addMessageToTxt(newMessage, new File(filePathChatTxt));
                messageList.getItems().add("A " + selectedUser.getName() + ": " + content);
                messageField.clear();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error sending message", e);
                showAlert("Error al enviar el mensaje", "No se pudo enviar el mensaje", e.getMessage());
            }
        } else {
            showAlert("Información", "No se ha seleccionado un usuario", "Por favor, selecciona un usuario antes de enviar un mensaje.");
        }
    }

    /**
     * Exports the conversation with the selected user to a text file.
     */
    @FXML
    private void exportConversationToTXT() {
        if (selectedUser == null) {
            showAlert("Error", "No se ha seleccionado un usuario", "Por favor, selecciona un usuario para exportar la conversación.");
            return;
        }

        try {
            List<Message> allMessages = XmlReader.getMessagesFromXML(filePathChat);
            List<Message> conversation = new ArrayList<>();

            for (Message message : allMessages) {
                if ((message.getSender().equals(currentUser) && message.getReceiver().equals(selectedUser)) ||
                        (message.getSender().equals(selectedUser) && message.getReceiver().equals(currentUser))) {
                    conversation.add(message);
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Conversación");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (*.txt)", "*.txt"));
            File file = fileChooser.showSaveDialog(messageList.getScene().getWindow());

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file);
                     OutputStreamWriter osw = new OutputStreamWriter(fos);
                     PrintWriter writer = new PrintWriter(osw)) {

                    for (Message message : conversation) {
                        writer.println("[" + message.getTimestamp() + "] " +
                                message.getSender().getName() + " a " +
                                message.getReceiver().getName() + ": " +
                                message.getContent());
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error exporting conversation", e);
            showAlert("Error al exportar la conversación", "No se pudo exportar la conversación", e.getMessage());
        }
    }

    /**
     * Displays an alert dialog with a specified title, header, and content.
     *
     * @param title   the title of the alert.
     * @param header  the header text of the alert.
     * @param content the content text of the alert.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
