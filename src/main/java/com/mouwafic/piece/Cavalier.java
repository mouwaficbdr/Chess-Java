package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Cavalier extends Piece{
    public Cavalier(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_knight");
        } else {
            image = getImage("/images/b_knight");
        }
    }
}
