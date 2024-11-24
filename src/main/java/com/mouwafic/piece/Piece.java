package com.mouwafic.piece;


import com.mouwafic.main.Echiquier;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {
    public BufferedImage image;
    public int x, y;
    public int col, ligne , preCol, preLigne;
    public int couleur;

    public Piece(int couleur, int col, int ligne) {
        this.couleur = couleur;
        this.col = col;
        this.ligne = ligne;
        x = getX(col);
        y = getY(ligne);
        preCol = col;
        preLigne = ligne;
    }

    public BufferedImage getImage(String cheminImage) {
        BufferedImage image = null;
        try {
            System.out.println("Chemin de l'image : " + cheminImage);
            image = ImageIO.read(getClass().getResourceAsStream(cheminImage + ".png"));
        } catch (IOException e) {
            System.err.println("Erreur de chargement de l'image : " + e.getMessage());
        }
        if (image == null) {
            throw new IllegalArgumentException("L'image à l'emplacement " + cheminImage + " est introuvable.");
        }
        return image;
    }

    public int getX(int col) {
        return col * Echiquier.TAILLE_CARRE;
    }

    public int getY(int ligne) {
        return ligne * Echiquier.TAILLE_CARRE;
    }

    public void dessiner(Graphics2D g2) {

        // Obtenir la largeur et la hauteur de l'image
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();
    
    // Calculer la position centrée
    int xCentre = x + (Echiquier.TAILLE_CARRE - imageWidth) / 2;
    int yCentre = y + (Echiquier.TAILLE_CARRE - imageHeight) / 2;

        g2.drawImage(image, xCentre, yCentre,null);
    }
}

