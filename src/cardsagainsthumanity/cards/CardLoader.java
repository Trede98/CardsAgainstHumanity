/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardsagainsthumanity.cards;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author scanferla.giovanni
 */
public class CardLoader {
    
    private DatabaseConnector databaseConnector;
    private final TypeCard type;
    private ArrayList<Card> buffer;

    public CardLoader(DatabaseConnector databaseConnector, TypeCard type) {
        this.databaseConnector = databaseConnector;
        this.type = type;
        buffer = new ArrayList<>();
        loader();
    }
    
    private void loader(){
        try {
            ResultSet rs = databaseConnector.executeQuery("SELECT Cards FROM Cards WHERE Type='" + type + "'");
            while(rs.next()){
                String v = rs.getString("Cards");
                buffer.add(new Card(v, type));
            }
            databaseConnector.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Card> getBuffer() {
        return buffer;
    }
    
    
    
}
