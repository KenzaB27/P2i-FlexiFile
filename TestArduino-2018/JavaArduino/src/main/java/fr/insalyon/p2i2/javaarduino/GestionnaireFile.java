/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author bouzi
 */
public class GestionnaireFile {
    //Attributs de la classe GestionairFile
    private ArrayList <Groupe> listeGroupe = new ArrayList <Groupe>(); 
    private Client client ; 
    private Connection connection;
    private boolean _initialized;
    
    
    //Constructeur
    public GestionnaireFile (Client c ,Connection con){
        client = c;
        connection = con ;
        _initialized = false;
    }
        
    public void setup()
    {
        this.initListGroupe();
        this.initCapteur();
        
    }
    
    public boolean isSetup()
    {
        return _initialized;
    }
    
    public void start(){
        //set X distance after 60 seconds to do it after some measurements
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        setDistanceX();
                        updateDistanceX();
                        _initialized = true;
                    }
                }, 
                120000
        );
    }
    
    public void initListGroupe (){
        int idGroupe ; 
        String nomGroupe; 
        try{            
            String query = "select * from Groupe";
       
            // Construction de l'objet « requête statique »
            Statement statement= connection.createStatement();
            
            //execution de la requete
            ResultSet rs = statement.executeQuery(query);
            System.out.println("requete executee ....");
            
            while ( rs.next () ) {
                idGroupe =rs.getInt("idGroupe"); 
                nomGroupe = rs.getString("nomGroupe");
                listeGroupe.add(new Groupe (idGroupe, nomGroupe)); 
            }
        }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
           e.printStackTrace();
            System.exit(0);
        } 
    }
    
    /** Méthode permettant d'associer à chaque groupe de la liste des groupes du gestionnaire 
    *   les différents capteurs installés dans le groupe */
     public void initCapteur(){ 
        Capteur cap; 
        int idGroupe; 
        for (Groupe grp : listeGroupe){ 
            idGroupe= grp.getIdGroupe();         
            try{            
                String query = "select * from Capteur where idGroupe= ? ";

                // Construction de l'objet « requête parametrée »
                PreparedStatement ps = connection.prepareStatement(query);

                // transformation en requête statique
                ps.setInt(1,idGroupe) ; 

                //execution de la requete
                ResultSet rs = ps.executeQuery();
                System.out.println("requete executee ....");

                while ( rs.next () ) {
                    cap = new Capteur (rs.getInt("idCapteur"),idGroupe,rs.getInt("numSerie"));
                    grp.getListeCapteur().add(cap);    
                }
            }
            catch(SQLException e){
                //si une erreur se produit, affichage du message correspondant
               e.printStackTrace();
                System.exit(0);
                }
            }
     }
     
    /** Méthode permettant d'associer à chaque capteur (d'un même groupe)
    *  la distance X à laquelle il est positionné par rapport au  mur (obstacle à vide) 
    */
    public void setDistanceX(){ 
        String query = "select MAX(m.valeur) as distanceX from Capteur c, Mesure m "+
                       "where c.idCapteur=? and m.idCapteur=c.idCapteur "
                     + " AND TIME_TO_SEC(TIMEDIFF(now(), m.dateMesure)) between 0 and 120 ";
        for (Groupe grp : listeGroupe ){                
            for (Capteur c : grp.getListeCapteur()){
                try{
                    // Construction de l'objet « requête parametrée »
                    PreparedStatement ps = connection.prepareStatement(query);

                     // transformation en requête statique
                    ps.setInt(1,c.getIdCapteur()) ; 

                    //execution de la requete
                    ResultSet rs = ps.executeQuery();
                    System.out.println(ps);
                    System.out.println("requete executee ....");
                    rs.next();
                    c.localisation.setDistanceX(rs.getInt("distanceX"));
                }
                catch(Exception e){
                    //si une erreur se produit, affichage du message correspondant
                   e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
        
    /** Méthode permettant d'insérer la valeur de la distanceX pour chaque entité capteur d'un même groupe 
    */
    public void updateDistanceX (){ 
        String query = "UPDATE Localisation set distanceX =? where idCapteur =? ";
        for (Groupe grp : listeGroupe){
            for (Capteur c : grp.getListeCapteur() ){                    
                try{
                    // Construction de l'objet « requête parametrée »
                    PreparedStatement ps = connection.prepareStatement(query);

                    // transformation en requête statique
                    ps.setInt(1,c.localisation.getDistanceX()) ; 
                    ps.setInt(2,c.getIdCapteur());
                    
                    //execution de la requete
                    ps.executeUpdate();
                    System.out.print(ps);
                    System.out.println("update réussie");
                }

                catch(SQLException e){
                    //si une erreur se produit, affichage du message correspondant
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
    // ------ getters -------  
    public Client getClient (){
        return client; 
    }
    
    public ArrayList<Groupe> getListeGroupe(){
        return listeGroupe;
    }
}
