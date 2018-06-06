/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author bouzi
 */
public class FenetreCreation extends JFrame {
    
    private Connection connection;
    final String DB_NAME = "flexifile"; //G223_B_BD2
    final String DB_LOGIN = "root";
    final String DB_PW = "";
    
    private Logo logo;
    
    public JLabel nom; 
    public JLabel ajoutGrp; 
    
    public JTextField nomClient; 
    public JTextField nbGrp; 
    
    public JButton createGrp;
    public JButton exit; 
    
    public JPanel cadrePrincipal; 
    public JPanel cadreMilieu; 
    public JPanel cadreCommande;
    public JPanel cadre1; 
    public JPanel cadre2;
    public JPanel cadre3;

    GestionnaireFile gestionnaireFile;

    
    public FenetreCreation(){
        // appel à super 
        super("Flexifile's creation");
        
        // définition de la taille de la fenetre, son emplacement et sa commande fermeture 
        this.setSize(400,100);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       // Instanciation des Widgets qui forment le contenu de la fenetre 
        nom = new JLabel ("Inserez votre nom client :");
        ajoutGrp = new JLabel ("Inserez le nombre de groupe :"); 
        
        nbGrp= new JTextField (null);
        nomClient = new JTextField (null); 
        
        createGrp = new JButton ("Creation des groupes");
        exit = new JButton ("Exit"); 
        
        // Organisation structurelle
	// creer le cadre principal qui contiendra tous les elements de la fenetre
	// le layout permet simplement d'obtnenir une organisation automatique des 
	// differents elements
        cadrePrincipal= new JPanel(new BorderLayout());
        
        // creer le cadre qui contient les elements de controle (bouttons, zone de saisies
        cadreMilieu = new JPanel (new GridLayout (2,2,20,20));
        cadreCommande = new JPanel(new FlowLayout());
        cadre1 =  new JPanel(new GridLayout(1,2,10,10));
        cadre2 = new JPanel(new GridLayout(1,2,10,10));
        
	
        // Ajouter des wigdets de saisie
	cadre1.add(nom);
        cadre1.add(nomClient);
        cadre2.add(ajoutGrp);
        cadre2.add(nbGrp);
        cadreCommande.add(createGrp);
        cadreCommande.add(exit);
        cadreMilieu.add(cadre1);
        cadreMilieu.add(cadre2);

	// ajouter le panneau de commandes
	//dans le cadre principal (en specifiant en bas)
	cadrePrincipal.add(cadreMilieu,BorderLayout.CENTER); 
        cadrePrincipal.add (cadreCommande,BorderLayout.SOUTH);

	// ajouter le cadre principal dnas la fenetre
	setContentPane(cadrePrincipal);

        //Etablir la connection avec la base de données 
        this.setConnection (); 
        
        //Ajouter l'ecouteur du boutton pour les carres
        EcouteurCreateGrp ecouteurCreateGrp=new EcouteurCreateGrp(this);
        createGrp.addActionListener(ecouteurCreateGrp);
        
        //rendre la fenetre visible
        this.setVisible(true);
    }
    
    public void setConnection(){
        try {
            // Chargement de la classe du driver par le DriverManager
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver trouvé...");

            // Création d'une connexion sur la base de donnée
            //connection = DriverManager.getConnection("jdbc:mysql://PC-TP-MYSQL.insa-lyon.fr:3306/" + DB_NAME, DB_LOGIN, DB_PW);
            connection = DriverManager.getConnection("jdbc:mysql://62.210.182.114:3306/" + DB_NAME, DB_LOGIN, DB_PW);
            System.out.println("Connexion établie...");

        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    } 
    
    public int getNbGrp(){
        int c =0; 
        try {
            c=Integer.parseInt(nbGrp.getText());
        }
        catch ( NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Ceci n'est pas un nombre");
            System.out.println (e.getMessage());
            System.exit(0);
        }
        return c;
    }
    
    public String getNomClient(){
        String c= " " ;
        try {
            c=nomClient.getText();
        }
        catch ( Exception e){
            JOptionPane.showMessageDialog(this,"Veuillez insérer votre nom d'utilisateur");
            System.out.println (e.getMessage());
            System.exit(0);
        }
        return c;
    }
      
    public GestionnaireFile getGestionnaire (){
        return gestionnaireFile; 
    }
    public Connection getConnection (){
        return connection; 
    }
    
   // public void 
    
    }
        
        
        
      
            
        
    

