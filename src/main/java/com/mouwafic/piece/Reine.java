package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Reine extends Piece{
    public Reine(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_queen");
        } else {
            image = getImage("/images/b_queen");
        }
    }
}
