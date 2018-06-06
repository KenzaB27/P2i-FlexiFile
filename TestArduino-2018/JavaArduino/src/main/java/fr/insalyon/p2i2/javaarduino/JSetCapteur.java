/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.*;

/**
 *
 * @author bouzi
 */
public class JSetCapteur extends JPanel{
    public JPanel panel; 

    public JLabel position; 
    public JButton numCapteur; 
    public JTextField pos; 
    
    public JSetCapteur (int i){
        numCapteur = new JButton ("Capteur n°"+i); 
        position = new JLabel ("Position en m :"); 
        pos = new JTextField (null); 
        panel = new JPanel (new GridLayout(1,3,10,10)); 
        panel.add (numCapteur); 
        panel.add(position); 
        panel.add(pos);   
    }
    
    public int getPosition (){
        int c =0; 
        try {
            c=Integer.parseInt(pos.getText());
        }
        catch ( NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Ceci n'est pas une position de capteur");
            System.out.println (e.getMessage());
            System.exit(0);
        }
        return c;
    } 
    
    
    
}
