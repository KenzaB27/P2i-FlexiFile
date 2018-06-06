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

/**
 *
 * @author bouzi
 */
public class GestionnaireStats { 
    private Connection connection ;
    private int idGroupe;
    
    public GestionnaireStats (int idGroupe, Connection c){
        connection = c; 
        this.idGroupe= idGroupe; 
    }
    
    public void calcul(){
        //set X distance after 60 seconds to do it after some measurements
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        insertIntoStats(); 
                    }
                }, 
                300000,
                300000
        );
    }
    
    public int getTempsStats(){
	int temps=0; 
        try{
	    String query = "SELECT ROUND(AVG(f.tmpAttente),2) as temps FROM File f  "
                         + "WHERE f.idGroupe= ? "
                         + "AND f.idFile IN (SELECT idFile from File "
                         + "WHERE TIME_TO_SEC(TIMEDIFF(now(),dateMesure)) between 0 and 300 " 
                         + "AND f.idGroupe = ?)"; 
			
            PreparedStatement ps = connection.prepareStatement(query);

            // transformation en requête statique
            ps.setInt(1,idGroupe); 
            ps.setInt(2,idGroupe);
            System.out.println(ps);
            //execution de la requete
            ResultSet rs = ps.executeQuery();
            System.out.println("requete executee ....");
            
            while ( rs.next () ) {
                temps = rs.getInt("temps");
            }
            }catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);   
            
            }
        return temps;
    }
	
    public int getLongueurStats(){
        int longueur = 0;	
	try {
            String query = "SELECT ROUND(AVG(f.longueur),2) as longueur FROM File f "
                         + "WHERE f.idGroupe= ? "
                         + "AND f.idFile IN (SELECT idFile from File "
                         + "WHERE TIME_TO_SEC(TIMEDIFF(now(),dateMesure)) between 0 and 300 "
                         + "AND f.idGroupe = ?)"; 
            PreparedStatement ps = connection.prepareStatement(query);

            // transformation en requÃªte statique
            ps.setInt(1,idGroupe); 
            ps.setInt(2,idGroupe);
            System.out.println(ps);
            //execution de la requete
            ResultSet rs = ps.executeQuery();
            System.out.println("requete executee ....");
                
            while ( rs.next () ) {
                longueur = rs.getInt("longueur");
                }
            }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);                      
        }            
        return longueur;
	}
	
    public void insertIntoStats (){
        try {
            
            //Creation de la requete
            String sqlStr = "INSERT INTO Stats(idGroupe,longueur, temps, dateStat) VALUES (?,?,?,?);";

            PreparedStatement ps = connection.prepareStatement(sqlStr);
            
            java.sql.Timestamp times = new java.sql.Timestamp(System.currentTimeMillis());
            
            ps.setInt(1,idGroupe);
            ps.setInt(2,getLongueurStats());
            ps.setInt(3,getTempsStats());
            ps.setTimestamp(4,times);

            //execution de la requete
            ps.executeUpdate();
            }
        catch(SQLException e){
            //si une erreur se produit, affichage du message correspondant
            e.printStackTrace();
        }
    }
    
    public int getIdGroupe(){
        return idGroupe;
    }
}


