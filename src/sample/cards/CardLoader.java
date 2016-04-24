/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.cards;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author scanferla.giovanni
 */
public class CardLoader {
    
    private final String path;
    private final TypeCard type;
    private ArrayList<Card> buffer;

    public CardLoader(String path, TypeCard type) {
        this.path = path;
        this.type = type;
        buffer = new ArrayList<>();
        loader();
    }
    
    private void loader(){
        try {
            FileReader fr= new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null){
                buffer.add(new Card(line.substring(2), type));
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(CardLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Card> getBuffer() {
        return buffer;
    }
    
    
    
}
