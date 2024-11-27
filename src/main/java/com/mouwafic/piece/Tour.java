package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Tour extends Piece {
    public Tour(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_rook");
        } else {
            image = getImage("/images/b_rook");
        }
    }
    
    public boolean peutBouger(int colCible, int ligneCible) {

        if (estDansEchiquier(colCible, ligneCible) && memeCase(colCible, ligneCible) == false) {
            if (colCible == preCol || ligneCible == preLigne) {
                if (verifieCaseValide(colCible, ligneCible) && pieceSurLigneDroite(colCible, ligneCible) == false) {
                    return true;
                }
            }
        }
        return false;
    }
}
