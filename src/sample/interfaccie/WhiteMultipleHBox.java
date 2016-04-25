package sample.interfaccie;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WhiteMultipleHBox extends HBox {

    public WhiteMultipleHBox(Font f, String all, double width, double heigh) {
        String[] cards = all.split("@@");

        this.setWidth(width*cards.length);
        this.setHeight(heigh);
        this.setSpacing(5);


        for (String card : cards) {
            Label l = new Label(card);
            l.setPrefSize(width - 20, heigh - 20);
            l.setStyle("-fx-padding: 10;\n"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);\n"
                    + "-fx-background-radius: 10 10 10 10;\n"
                    + "-fx-border-color: #000000;\n"
                    + "-fx-border-radius: 10 10 10 10;\n"
                    + "-fx-background-color: #FFFFFF;");
            l.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            l.setFont(f);
            l.setTextFill(Color.BLACK);
            l.setWrapText(true);
            l.setAlignment(Pos.TOP_LEFT);
            this.getChildren().add(l);
        }
    }


    public String getAllPhrase(){
        String s = "";
        for (int i = 0; i < this.getChildren().size(); i++){
            s = s + ((Label)this.getChildren().get(i)).getText() + "@@";
        }
        return s.substring(0, s.length()-2);
    }
}
