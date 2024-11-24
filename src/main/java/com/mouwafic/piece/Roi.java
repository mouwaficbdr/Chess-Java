package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Roi extends Piece{
    public Roi(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_king");
        } else {
            image = getImage("/images/b_king");
        }
    }
}
