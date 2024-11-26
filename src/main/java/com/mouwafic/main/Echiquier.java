package com.mouwafic.main;

import java.awt.*;

public class Echiquier {
    final int MAX_COL = 8;
    final int MAX_LIGNE = 8;
    public static final int TAILLE_CASE = 100;
    public static final int MOITIE_TAILLE_CASE = TAILLE_CASE/2;

    public void dessiner(Graphics2D g2) {
        int coloriage = 0;

        for (int lignes = 0; lignes < MAX_LIGNE; lignes++) {
            for(int colonnes = 0; colonnes < MAX_COL; colonnes++) {
                if(coloriage == 0){
                    g2.setColor(new Color(118, 150, 86));
                    coloriage = 1;
                }
                else{
                    g2.setColor(new Color(238, 238, 210));
                    coloriage = 0;
                }
                g2.fillRect(colonnes*TAILLE_CASE, lignes*TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
            }
            if(coloriage == 0){
                coloriage = 1;
            } else {
                coloriage = 0;
            }
        }
    }
}
