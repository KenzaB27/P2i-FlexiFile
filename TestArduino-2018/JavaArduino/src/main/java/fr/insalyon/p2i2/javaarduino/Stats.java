/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.sql.Date;

/**
 *
 * @author bouzi
 */
public class Stats {
        //attributs de la classe stats 
    private int idStats ;
    private int idFile; 
    private int longueur;
    private int temps;
    private Date date; 


    //constructeur de la classe 
    public Stats(int idStats, int idFile ){
        this.idStats= idStats; 
        this.idFile = idFile;     
    }
        
        // ------ getters -------     
    public int getIdStats(){
        return idStats; 
    }
    public int getIdFile(){
        return idFile; 
    }
    public int getLongeur (){
       return longueur; 
    }
    public int getTemps(){
        return temps;
    }

    public Date getDate(){
        return date; 
    }

        
        // ------ setters -------
    public void setIdFile (int f){
        idFile = f; 
    }
    public void setLongueur (int l){
        longueur= l;
    }
    public void setDate (Date d){
        date = d;
    }
    public void setTime (int t){
        temps = t; 
    }
}
