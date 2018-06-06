/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.p2i2.javaarduino;

import java.awt.*;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author bouzi
 */

public class Logo extends JPanel implements Serializable {
    Image image = null;
    public Logo(Image image) {
        this.image = image;
    }
    public Logo() {
    }
    public void setImage(Image image){
        this.image = image;
    }
    public Image getImage(Image image){
        return image;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //paint background
        if (image != null) { //there is a picture: draw it
            int height = this.getSize().height;
            int width = this.getSize().width;
            //g.drawImage(image, 0, 0, this); //use image size          
            g.drawImage(image,0,0, width, height, this);
        }
    }
}

