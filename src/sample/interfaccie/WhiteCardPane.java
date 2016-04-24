package sample.interfaccie;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import javax.swing.*;

/**
 * Created by Giovanni on 24/04/2016.
 */
public class WhiteCardPane extends Pane {

    private Pane defaultPane;
    private Label defaultLabel;
    private Label label;
    private String phrase;

    public WhiteCardPane(Pane defaultPane, Label defaultLabel, String phrase) {
        this.phrase = phrase;
        this.defaultLabel = defaultLabel;
        this.defaultPane = defaultPane;
        this.label = new Label(phrase);

        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setWidth(defaultPane.getWidth() - 20);
        this.setHeight(defaultPane.getHeight() - 20);
        this.setPrefSize(defaultPane.getWidth() - 20, defaultPane.getHeight() - 20);

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
