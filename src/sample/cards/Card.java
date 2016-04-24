/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.cards;

/**
 *
 * @author scanferla.giovanni
 */
public class Card {
    
    private final String content;
    private final TypeCard type;

    public Card(String content, TypeCard type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    
    
}
