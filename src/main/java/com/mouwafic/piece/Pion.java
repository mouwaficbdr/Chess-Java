package com.mouwafic.piece;

import com.mouwafic.main.PanneauDeJeu;

public class Pion extends Piece{
    public Pion(int couleur, int col, int ligne) {
        super(couleur, col, ligne);

        if (couleur == PanneauDeJeu.BLANC) {
            image = getImage("/images/w_pawn");
        } else {
            image = getImage("/images/b_pawn");
        }
    }
    
    public boolean peutBouger(int colCible, int ligneCible) {

        if (estDansEchiquier(colCible, ligneCible) && memeCase(colCible, ligneCible) == false) {

            //Définis la valeur du déplacement selon sa couleur
            int valeurDeplacement;
            if (couleur == PanneauDeJeu.BLANC) {
                valeurDeplacement = -1;
            } else {
                valeurDeplacement = 1;
            }

            //Vérifie la piece sur la destination
            pieceSurDestination = tombeSurPiece(colCible, ligneCible);

            //Déplacement d'une case
            if (colCible == preCol && ligneCible == preLigne + valeurDeplacement && pieceSurDestination == null) {
                return true;
            }

            //Déplacement de 2 cases
            if (colCible == preCol && ligneCible == preLigne + 2 * valeurDeplacement && pieceSurDestination == null
                    && aEteDeplace == false && pieceSurLigneDroite(colCible, ligneCible) == false) {
                return true;
            }

            //Déplacement en diagonal et capture
            if (Math.abs(colCible - preCol) == 1 && ligneCible == preLigne + valeurDeplacement
                    && pieceSurDestination != null && pieceSurDestination.couleur != couleur) {
                return true;
            }

        }
        
        return false;

    }
}
