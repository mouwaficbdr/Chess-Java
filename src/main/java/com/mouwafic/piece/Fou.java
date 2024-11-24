package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Fou extends Piece{
    public Fou(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if(couleur == PanneauDeJeu.BLANC){
            image = getImage("/images/w_bishop");
        } else {
            image = getImage("/images/b_bishop");
        }
    }
}
