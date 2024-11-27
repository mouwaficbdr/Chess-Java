package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;
import com.mouwafic.main.Type;

public class Reine extends Piece{
    public Reine(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

                type = Type.REINE;


        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_queen");
        } else {
            image = getImage("/images/b_queen");
        }
    }
    
    public boolean peutBouger(int colCible, int ligneCible) {
        if (estDansEchiquier(colCible, ligneCible) && memeCase(colCible, ligneCible) == false) {

            //Vertical et horizontal comme la tour
            if (colCible == preCol || ligneCible == preLigne) {
                if (verifieCaseValide(colCible, ligneCible) && pieceSurLigneDroite(colCible, ligneCible) == false) {
                    return true;
                }
            }

            //Diagonal comme le fou
            if(Math.abs(colCible - preCol) == Math.abs(ligneCible - preLigne)) {
                if (verifieCaseValide(colCible, ligneCible) && pieceSurDiagonale(colCible, ligneCible) == false) {
                    return true;
                }
            }

        }
        return false;
    }
}
