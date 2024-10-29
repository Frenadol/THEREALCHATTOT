package com.github.Frenadol.view;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MessageListCell extends ListCell<String> {
    private HBox content;
    private Label messageLabel;

    /**
     * Constructs a MessageListCell, initializing the label for the message and the layout container.
     */
    public MessageListCell() {
        super();
        messageLabel = new Label();
        content = new HBox(messageLabel);
    }

    /**
     * Updates the display of the list cell based on the item's content and its empty state.
     *
     * @param item  the item to be displayed in the cell, represented as a String.
     * @param empty indicates whether the cell is empty or not.
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            if (item.startsWith("Para ")) {
                content.setAlignment(Pos.CENTER_RIGHT);
                messageLabel.setStyle("-fx-background-color: lightblue; -fx-padding: 5px;");
            } else if (item.startsWith("De ")) {
                content.setAlignment(Pos.CENTER_LEFT);
                messageLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 5px;");
            }
            messageLabel.setText(item);
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
