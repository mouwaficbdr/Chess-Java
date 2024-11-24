package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Pion extends Piece{
    public Pion(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_pawn");
        } else {
            image = getImage("/images/b_pawn");
        }
    }
}
