package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Cavalier extends Piece{
    public Cavalier(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_knight");
        } else {
            image = getImage("/images/b_knight");
        }
    }
    
    public boolean peutBouger(int colCible, int ligneCible) {

        if (estDansEchiquier(colCible, ligneCible)) {
            if (Math.abs(colCible - preCol) * Math.abs(ligneCible - preLigne) == 2) {
                if (verifieCaseValide(colCible, ligneCible)) {
                    return true;
                }
            }
        }
        return false;
    }

}
