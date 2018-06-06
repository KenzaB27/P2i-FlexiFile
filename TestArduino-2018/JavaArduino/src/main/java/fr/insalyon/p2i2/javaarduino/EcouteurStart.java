/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;

/**
 *
 * @author bouzi
 */
public class EcouteurStart implements ActionListener {
    private FenetreInitCapteur fen ;
    
    public EcouteurStart (FenetreInitCapteur f){
	fen=f;
    }   
     //actionPerformed indique les instructions a executer au clic du bouton
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource (); 
        int nbCap;
        int tmp=0;
        if (source instanceof JButton){
            if (source == fen.start){
                
            }
        }
    }
        public void InsertIntoLocalisation (int idCapteur, int position){
        try {
            String sqlStr = "INSERT INTO Localisation (idCapteur, position,dateDebut) VALUES (?,?,?)";
            java.sql.Timestamp times = new java.sql.Timestamp(System.currentTimeMillis());
            PreparedStatement ps= fen.getConnection().prepareStatement(sqlStr);
            ps.setInt(1,idCapteur);
            ps.setInt(2, position);
            ps.setTimestamp(3,times);
            
            //execution de la requete
            ps.executeUpdate();
            System.out.println (ps); 
                
            }
        catch(NumberFormatException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
        } 
        catch (SQLException e) {
            //si une erreur se produit, affichage du message correspondant
           e.printStackTrace();
        }
    }  
            
}
