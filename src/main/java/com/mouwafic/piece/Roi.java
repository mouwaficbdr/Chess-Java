package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Roi extends Piece{
    public Roi(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_king");
        } else {
            image = getImage("/images/b_king");
        }
    }

    public boolean peutBouger(int colCible, int ligneCible) {
        
        if (estDansEchiquier(colCible, ligneCible)) {
            

            //Déplacement
            if (Math.abs(colCible - preCol) + Math.abs(ligneCible - preLigne) == 1
                    || Math.abs(colCible - preCol) * Math.abs(ligneCible - preLigne) == 1) {

                if (verifieCaseValide(colCible, ligneCible)) {
                    return true;
                }
            }
            
            //Roque

            if (aEteDeplace == false) {

                //Petit roque (droite)
                if (colCible == preCol + 2 && ligneCible == preLigne
                        && pieceSurLigneDroite(colCible, ligneCible) == false) {
                    for (Piece piece : PanneauDeJeu.simPieces) {
                        if (piece.col == preCol + 3 && piece.ligne == preLigne && piece.aEteDeplace == false) {
                            PanneauDeJeu.pieceRoque = piece;
                            return true;
                        }
                    }
                }

                //Grand roque(gauche)
                if (colCible == preCol - 2 && ligneCible == preLigne && pieceSurLigneDroite(colCible, ligneCible) == false) {
                    Piece p[] = new Piece[2];
                    for (Piece piece : PanneauDeJeu.simPieces) {
                        if (piece.col == preCol - 3 && piece.ligne == colCible) {
                            p[0] = piece;
                        }
                        if (piece.col == preCol - 4 && piece.ligne == ligneCible) {
                            p[1] = piece;
                        }
                        if (p[0] == null && p[1] != null && p[1].aEteDeplace == false) {
                            PanneauDeJeu.pieceRoque = p[1];
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
}
