package cardsagainsthumanity.interfaccie;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by Giovanni on 24/04/2016.
 */
public class WhiteCardPane extends StackPane {

    private StackPane defaultPane;
    private Label defaultLabel;
    private Label label;
    private String phrase;

    public WhiteCardPane(StackPane defaultPane, Label defaultLabel, String phrase) {
        this.phrase = phrase;
        this.defaultLabel = defaultLabel;
        this.defaultPane = defaultPane;
        this.label = new Label(phrase);

        this.setWidth(defaultPane.getWidth() - 20);
        this.setHeight(defaultPane.getHeight() - 20);
        this.setPrefSize(defaultPane.getWidth() - 20, defaultPane.getHeight() - 20);
        this.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);\n"
                        + "-fx-background-radius: 10 10 10 10;\n"
                        + "-fx-border-color: #000000;\n"
                        + "-fx-border-radius: 10 10 10 10;\n"
                        + "-fx-background-color: #FFFFFF;");

        this.label.setLayoutX(defaultLabel.getLayoutX());
        this.label.setLayoutY(defaultLabel.getLayoutY());
        this.label.setPrefSize(defaultLabel.getPrefWidth() - 20, defaultLabel.getPrefHeight() - 20);
        this.label.setFont(defaultLabel.getFont());
        this.label.setTextFill(Color.BLACK);
        this.label.setWrapText(true);
        this.label.setAlignment(Pos.TOP_LEFT);


        this.getChildren().add(label);
    }

    public String getPhrase() {
        return phrase;
    }
}
