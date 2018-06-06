/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
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
public class FenetreInitCapteur extends JFrame{
    private Connection connection;
    
    private Logo logo;
    
    public JTable tableau;
        
    public Object[][] data; 
    public String[] title ;
    
    public JButton start;
    public JButton exit; 

    public JPanel cadreCommande;
 
    private final int nbGrp;
    private final int nbCapteur; 
    
       public FenetreInitCapteur(Connection c , int nbGrp, int nbCapteur){
        // appel à super 
        super("Configuration des positions des capteurs"); 
        // définition de la taille de la fenetre, son emplacement et sa commande fermeture 
        this.setSize(400,100);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        this.nbGrp= nbGrp;
        this.nbCapteur= nbCapteur; 
    // Instanciation des Widgets qui forment le contenu de la fenetre 
        
        title = new String [2*nbGrp+1]; 
        for (int i =1; i<2*nbGrp+1; i++){
            if (i%2==1)
                title[i-1] = "Groupe "+(i%2); 
            if (i%2==0)
                title[i+1]="position";
        }
        
        data = new Object [nbCapteur][2*nbGrp+1];
        
        for (int j=1; j<nbCapteur+1; j++){
            data[0][j]= new JTextField ("Capteur"+j);
            for (int i=1; i<2*nbGrp;i++)
            data[i][j]= new JTextField (null);
        }  
        
        ZModel model = new ZModel(data, title);
        System.out.println("Nombre de colonne : " + model.getColumnCount());
        System.out.println("Nombre de ligne : " + model.getRowCount());
        this.tableau = new JTable(model);
        this.tableau.setDefaultRenderer(JTextField.class, new TableComponent());
        
        start = new JButton ("Start");
        exit = new JButton ("Exit"); 

        // creer le cadre qui contient les elements de controle (bouttons, zone de saisies
        cadreCommande = new JPanel(new FlowLayout());

        
                // Ajouter des wigdets de saisie
        
        cadreCommande.add(start);
        cadreCommande.add(exit);
        
	 //Nous ajoutons notre tableau à notre contentPane dans un scroll
        //Sinon les titres des colonnes ne s'afficheront pas !
        this.getContentPane().add(new JScrollPane(tableau), BorderLayout.CENTER);
        this.getContentPane().add(cadreCommande, BorderLayout.SOUTH);
        
        //Ajouter l'ecouteur du boutton pour les carres
        
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
