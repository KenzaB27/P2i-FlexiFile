/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author bouzi
 */
public class ZModel extends AbstractTableModel{
    private Object[][] data;
    private String[] title;

    //Constructeur
    public ZModel(Object[][] data, String[] title){
      this.data = data;
      this.title = title;
    }

    //Retourne le nombre de colonnes
    public int getColumnCount() {
      return this.title.length;
    }

    //Retourne le nombre de lignes
    public int getRowCount() {
      return this.data.length;
    }

    //Retourne la valeur à l'emplacement spécifié
    public Object getValueAt(int row, int col) {
      return this.data[row][col];
    }  
    /**
    * Retourne le titre de la colonne à l'indice spécifié
    */
    @Override
    public String getColumnName(int col) {
        return this.title[col];
    }
    //Retourne la classe de la donnée de la colonne
    @Override
    public Class getColumnClass(int col){
    //On retourne le type de la cellule à la colonne demandée
    //On se moque de la ligne puisque les types de données sont les mêmes quelle que soit la ligne
    //On choisit donc la première ligne
        return this.data[0][col].getClass();
    }
    //Retourne vrai si la cellule est éditable : celle-ci sera donc éditable
    @Override
    public boolean isCellEditable(int row, int col){
        return true; 
    }
    //Retourne vrai si la cellule est éditable : celle-ci sera donc éditable
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
    //CTRL + SHIFT + O pour générer les imports
