package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;
import com.mouwafic.main.Type;

public class Fou extends Piece{
    public Fou(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

                type = Type.FOU;


        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_bishop");
        } else {
            image = getImage("/images/b_bishop");
        }
    } 
    
    public boolean peutBouger(int colCible, int ligneCible) {

        if (estDansEchiquier(colCible, ligneCible) && memeCase(colCible, ligneCible) == false) {
            if(Math.abs(colCible - preCol) == Math.abs(ligneCible - preLigne)) {
                if (verifieCaseValide(colCible, ligneCible) && pieceSurDiagonale(colCible, ligneCible) == false) {
                    return true;
                }
            }
        }

        return false;
    }
}
