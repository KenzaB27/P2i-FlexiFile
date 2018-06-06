/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.*;

/**
 *
 * @author bouzi
 */
public class EcouteurSetCapteur implements ActionListener {
    private FenetreCreationFile fen ;
    
    public EcouteurSetCapteur(FenetreCreationFile f){
	fen=f;
    }   
     //actionPerformed indique les instructions a executer au clic du bouton
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource (); 
        String nomGrp; 
        int nbCap;
        int tmp=0;
        if (source instanceof JButton){
            if (source == fen.setCapteur){
                for (int i =1; i<fen.getNbGrp()+1; i++){
                    nomGrp = fen.getNom((JTextField)fen.data[1][i]);
                    nbCap = fen.getNbCapteur((JTextField)fen.data[2][i]);
                    tmp = Math.max(tmp,nbCap);
                    insertIntoGroupe(nomGrp);
                    insertIntoCapteur(getIdGroupe(nomGrp)); 
                } 
                System.exit(0);
                FenetreInitCapteur fe= new FenetreInitCapteur(fen.getConnection(),fen.getNbGrp(),tmp); 
            }
            else if (source == fen.exit)
                System.exit(0);
           
    } 
        }
    
    public LinkedList getIdCapteur(){
        LinkedList cap = new LinkedList() ; 
        return cap;    
    }
    
    public int getIdGroupe (String nomGrp){
        int idGrp=0;
        try {
            
            String sqlStr = "SELECT idGroupe FROM Groupe WHERE nomGroupe=?";
            PreparedStatement ps= fen.getConnection().prepareStatement(sqlStr);
            ps.setString(1,nomGrp);
           
            //execution de la requete
            ps.executeUpdate();
            System.out.println (ps);
            
            ResultSet rs = ps.executeQuery  ();
            while (rs.next())
                idGrp = rs.getInt ("idGroupe"); 
            }
        catch(NumberFormatException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
        } 
        catch (SQLException e) {
            //si une erreur se produit, affichage du message correspondant
           e.printStackTrace();
        }
        return idGrp; 
    }
    public void insertIntoGroupe(String nomGrp){
        try {
 
            String sqlStr = "INSERT INTO Groupe(nomGroupe) VALUES (?)";
            PreparedStatement ps= fen.getConnection().prepareStatement(sqlStr);
            ps.setString(1,nomGrp);
           
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
    public void insertIntoCapteur (int idGrp){
        try {
 
            String sqlStr = "INSERT INTO Capteur(idGroupe, numSerie,fabricant) VALUES (?,?,?)";
            PreparedStatement ps= fen.getConnection().prepareStatement(sqlStr);
            ps.setInt(1,idGrp);
            ps.setString (2, Integer.toString((int) (Math.random() * 1000000)));
            ps.setString(3, "Grove");
            
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
