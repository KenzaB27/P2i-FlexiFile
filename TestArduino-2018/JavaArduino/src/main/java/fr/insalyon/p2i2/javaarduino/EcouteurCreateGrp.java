/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author bouzi
 */
public class EcouteurCreateGrp implements ActionListener{
    private final FenetreCreation fen;
    
    public EcouteurCreateGrp(FenetreCreation f){
	fen=f;
    }
    //actionPerformed indique les instructions a executer au clic du bouton
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource (); 
        if (source instanceof JButton){
            if (source == fen.createGrp){
                String nomClient = fen.getNomClient();
                fen.gestionnaireFile = new GestionnaireFile ( new Client (nomClient), fen.getConnection());
                int nbGrp = fen.getNbGrp();
                System.exit(0);
                FenetreCreationFile f = new FenetreCreationFile (fen.getConnection(), nbGrp);
            }
            else if (source == fen.exit){
                System.exit(0);
            }
        }
    }

}

    


