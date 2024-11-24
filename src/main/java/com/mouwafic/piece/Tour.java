package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Tour extends Piece {
    public Tour(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_rook");
        } else {
            image = getImage("/images/b_rook");
        }
    }
}
