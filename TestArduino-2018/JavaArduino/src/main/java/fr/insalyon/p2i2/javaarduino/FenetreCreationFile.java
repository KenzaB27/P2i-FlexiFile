/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author bouzi
 */
public class FenetreCreationFile extends JFrame{
    private Connection connection;
    
    private Logo logo;
    
    public JTable tableau;
        
    public Object[][] data; 
    public String[] title ;
    
    public JButton setCapteur;
    public JButton exit; 

    public JPanel cadreCommande;
    public EcouteurSetCapteur ecouteurSetCapteur;
 
    private final int nbGrp;
    

    public FenetreCreationFile(Connection c , int nbGrp){
        // appel à super 
        super("Creez vos flexifiles d'attente"); 
        // définition de la taille de la fenetre, son emplacement et sa commande fermeture 
        this.setSize(400,100);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        this.nbGrp= nbGrp;
    // Instanciation des Widgets qui forment le contenu de la fenetre 
        
        title = new String [nbGrp+1]; 
        title[0]= " ";
        for (int i =1; i<nbGrp+1; i++)
           title[i] = "Groupe "+ i; 
        
        data = new Object [2][nbGrp+1];
        data[0][0]="Nom du Groupe"; 
        data[1][0]="Nombre de capteur";
        for (int j=1; j<nbGrp+1; j++){
            data[0][j]= new JTextField (null);
            data[1][j]= new JTextField (null);
        }  
        
        ZModel model = new ZModel(data, title);
        System.out.println("Nombre de colonne : " + model.getColumnCount());
        System.out.println("Nombre de ligne : " + model.getRowCount());
        this.tableau = new JTable(model);
        this.tableau.setDefaultRenderer(JTextField.class, new TableComponent());
        

        setCapteur = new JButton ("Set sensors");
        exit = new JButton ("Exit"); 

        // creer le cadre qui contient les elements de controle (bouttons, zone de saisies
        cadreCommande = new JPanel(new FlowLayout());

        
                // Ajouter des wigdets de saisie
        cadreCommande.add(setCapteur);
        cadreCommande.add(exit);
        
	 //Nous ajoutons notre tableau à notre contentPane dans un scroll
        //Sinon les titres des colonnes ne s'afficheront pas !
        this.getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);
        this.getContentPane().add(cadreCommande, BorderLayout.SOUTH);
        
        //Ajouter l'ecouteur du boutton pour les carres
        ecouteurSetCapteur=new EcouteurSetCapteur(this);
        setCapteur.addActionListener(ecouteurSetCapteur);       
        //rendre la fenetre visible
        this.setVisible(true);
    }

    
   public String getNom(JTextField text){
        String c =" "; 
        try {
            c=text.getText();
        }
        catch ( NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Ceci n'est pas une position de capteur");
            System.out.println (e.getMessage());
            System.exit(0);
        }
        return c;
    } 


       public int getNbCapteur(JTextField cap){
        int c =0; 
        try {
            c=Integer.parseInt(cap.getText());
        }
        catch ( NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Ceci n'est pas un nombre");
            System.out.println (e.getMessage());
            System.exit(0);
        }
        return c;
    } 
    public int getNbGrp (){
        return nbGrp;
    }   
    public Connection getConnection (){
        return connection; 
    }
    
    
    public class TableComponent extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    //Si la valeur de la cellule est un JButton, on transtype cette valeur
    if (value instanceof JTextField)
      return (JTextField) value;
    else
      return this;
  }
}
}

    
    
    
        
        
    

  
    
    
    

