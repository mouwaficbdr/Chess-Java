package com.mouwafic.main;


import com.mouwafic.piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanneauDeJeu extends JPanel implements Runnable{

    public static final int LARGEUR = 1100;
    public static final int HAUTEUR = 800;
    final int FPS = 60;  //Nombre d'images par seconde pour définir l'intervalle
    Thread threadDeJeu;
    Echiquier echiquier = new Echiquier();
    Souris souris = new Souris();
    //PIECES
    public static ArrayList<Piece> pieces = new ArrayList<>(); //L'etat réel des pièces dans le jeu
    public static ArrayList<Piece> simPieces = new ArrayList<>();
    ArrayList<Piece> piecesPromotion = new ArrayList<>();
    Piece pieceActive, pieceMetEnEchec;
    public static Piece pieceRoque;

    //COULEUR
    public static final int BLANC = 0;
    public static final int NOIR = 1;
    int couleurActuelle = BLANC;

    //BOOLEENS
    boolean peutBouger;
    boolean caseValide;
    boolean promotion;
    boolean gameOver;

    public PanneauDeJeu() {
        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        setBackground(Color.BLACK);
        addMouseMotionListener(souris);
        addMouseListener(souris);

        placerPieces();
        copiePieces(pieces, simPieces);
    }

    private void mettreAJour() {

        if (promotion) {
            promouvoir();
        } else if(gameOver == false){

            //Lorsqu'on appuie
        if (souris.appuie) {
            if (pieceActive == null) {

                for (Piece piece : simPieces) {
                    if (piece.couleur == couleurActuelle && piece.col == (int) (souris.x / Echiquier.TAILLE_CASE)
                            && piece.ligne == (int) (souris.y / Echiquier.TAILLE_CASE)) {
                        pieceActive = piece;
                    }
                }
            } else {
                simuler();
            }
        }

        //Lorsqu'on relache
        if (souris.appuie == false) {
            if (pieceActive != null) {
                if (caseValide) {

                    //Le mouvement est confirmé

                    //On met à jour la arraylist pieces dans le cas où une pièce a été capturée
                    copiePieces(simPieces, pieces);
                    pieceActive.mettreAJourPosition();
                    if (pieceRoque != null) {
                        pieceRoque.mettreAJourPosition();
                    }

                    if (roiEnEchec() && estEchecEtMat()) {
                        gameOver = true;
                    } 
                    else {

                        if (peutEtrePromu()) {
                            promotion = true;
                        } else {
                            alternerTour();  
                        }
                    }

                } else {

                    //Le mouvement n'est pas valide
                    copiePieces(pieces, simPieces);
                    pieceActive.retourPosition();
                    pieceActive = null;   
                }
            }
        }
    }

        

    }

    private void simuler() {

        peutBouger = false;
        caseValide = false;

        //A chaque itération on redéfinit à défaut l'état des pièces
        //On ramène toute pièce qui a été supprimée durant la simulation
        copiePieces(pieces, simPieces);

        //Remettre à défaut la position de la piece qui Roque
        if (pieceRoque != null) {
            pieceRoque.col = pieceRoque.preCol;
            pieceRoque.x = pieceRoque.getX(pieceRoque.col);
            pieceRoque = null;
        }

        //Si une pièce est maintenue, mettre à jour sa position
        pieceActive.x = souris.x - Echiquier.MOITIE_TAILLE_CASE; //On soustrait la taille de la moitié d'un carré pour que la piece active soit centrée sur la position de la souris
        pieceActive.y = souris.y - Echiquier.MOITIE_TAILLE_CASE;
        pieceActive.col = pieceActive.getCol(pieceActive.x);
        pieceActive.ligne = pieceActive.getLigne(pieceActive.y);

        //Vérifie si la pièce maintenue est au dessus d'un case valide
        if (pieceActive.peutBouger(pieceActive.col, pieceActive.ligne)) {
            peutBouger = true;

            if (pieceActive.pieceSurDestination != null) {
                simPieces.remove(pieceActive.pieceSurDestination.getIndex());
            }

            verifieRoque();

            if (estIllegal(pieceActive) == false && adversairePeutCapturerRoi() == false) {
                caseValide = true;
            }

        }

    }

    /**
        Boucle principale du jeu
     */
    @Override
    public void run() {
        // Calcule l'intervalle en nanosecondes entre chaque image en fonction des FPS désirés
        double intervalle = (double) 1000000000 / FPS;
        // Variation de temps pour accumuler les différences de temps entre les images
        double variationTemps = 0;
        // Enregistre le dernier moment où la boucle a été exécutée
        long dernierTemps = System.nanoTime();
        long tempsActuel;

        // Continue la boucle de jeu tant que le thread du jeu est actif
        while(threadDeJeu != null) {
            // Obtient le temps actuel en nanosecondes
            tempsActuel = System.nanoTime();
            // Accumule la fraction de temps écoulé par rapport à l'intervalle de frame que j'ai précisé
            variationTemps += (tempsActuel - dernierTemps) / intervalle;
            // Met à jour le dernier temps au temps actuel
            dernierTemps = tempsActuel;

            // Si suffisamment de temps s'est écoulé pour une image, mettre à jour l'état du jeu et rendre
            if(variationTemps >= 1) {
                mettreAJour(); // Met à jour la logique du jeu
                repaint(); // Rend le jeu
                // Décrémente la variation de temps de 1 pour tenir compte de l'image qui vient d'être traitée
                variationTemps--;
            }
        }
    }


    public void commencerJeu() {
        threadDeJeu = new Thread(this);
        threadDeJeu.start();
    }

    public void placerPieces() {
        // Pièces blanches
        pieces.add(new Pion(BLANC, 0, 6));
        pieces.add(new Pion(BLANC, 1, 6));
        pieces.add(new Pion(BLANC, 2, 6));
        pieces.add(new Pion(BLANC, 3, 6));
        pieces.add(new Pion(BLANC, 4, 6));
        pieces.add(new Pion(BLANC, 5, 6));
        pieces.add(new Pion(BLANC, 6, 6));
        pieces.add(new Pion(BLANC, 7, 6));

        // Pièces blanches non pions
        pieces.add(new Tour(BLANC, 0, 7));
        pieces.add(new Cavalier(BLANC, 1, 7));
        pieces.add(new Fou(BLANC, 2, 7));
        pieces.add(new Reine(BLANC, 3, 7));
        pieces.add(new Roi(BLANC, 4, 7));
        pieces.add(new Fou(BLANC, 5, 7));
        pieces.add(new Cavalier(BLANC, 6, 7));
        pieces.add(new Tour(BLANC, 7, 7));

        // Pièces noires
        pieces.add(new Pion(NOIR, 0, 1));
        pieces.add(new Pion(NOIR, 1, 1));
        pieces.add(new Pion(NOIR, 2, 1));
        pieces.add(new Pion(NOIR, 3, 1));
        pieces.add(new Pion(NOIR, 4, 1));
        pieces.add(new Pion(NOIR, 5, 1));
        pieces.add(new Pion(NOIR, 6, 1));
        pieces.add(new Pion(NOIR, 7, 1));

        // Pièces noires non pions
        pieces.add(new Tour(NOIR, 0, 0));
        pieces.add(new Cavalier(NOIR, 1, 0));
        pieces.add(new Fou(NOIR, 2, 0));
        pieces.add(new Reine(NOIR, 3, 0));
        pieces.add(new Roi(NOIR, 4, 0));
        pieces.add(new Fou(NOIR, 5, 0));
        pieces.add(new Cavalier(NOIR, 6, 0));
        pieces.add(new Tour(NOIR, 7, 0));
    }

    
    
    private void copiePieces(ArrayList<Piece> source, ArrayList<Piece> cible) {
        cible.clear();
        for (int i = 0; i < source.size(); i++) {
            cible.add(source.get(i));
        }
    }

    private boolean estIllegal(Piece roi) {
        if (roi.type == Type.ROI) {
            for (Piece piece : simPieces) {
                if (piece != roi && piece.couleur != roi.couleur && piece.peutBouger(roi.col, roi.ligne)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean adversairePeutCapturerRoi() {
    Piece roi = getRoi(false);

    // Vérification si roi est null
    if (roi == null) {
        return false;
    }

    for (Piece piece : simPieces) {
        if (piece.couleur != roi.couleur && piece.peutBouger(roi.col, roi.ligne)) {
            return true;
        }
    }

    return false;
}

    private boolean roiEnEchec() {
        Piece roi = getRoi(true);

        if (pieceActive.peutBouger(roi.col, roi.ligne)) {
            pieceMetEnEchec = pieceActive;
            return true;
        } else {
            pieceMetEnEchec = null;
        }

        return false;
    }
    
    private Piece getRoi(boolean adversaire) {

        Piece roi = null;
        
        for (Piece piece : simPieces) {
            if (adversaire) {
                if (piece.type == Type.ROI && piece.couleur != couleurActuelle) {
                    roi = piece;
                } else {
                    if (piece.type == Type.ROI && piece.couleur == couleurActuelle) {
                        roi = piece;
                    }
                }
            }
        }
        return roi;
    }

    private boolean estEchecEtMat() {

        Piece roi = getRoi(true);

        if (roiPeutBouger(roi)) {
            return false;
        } else {
            //Vérifie si on peut bloquer le chemin de l'echec

            //Vérifie la position de la picce pieceMetEnEchec et celle du roi
            int diffCol = Math.abs(pieceMetEnEchec.col - roi.col);
            int diffLigne = Math.abs(pieceMetEnEchec.ligne - roi.ligne);

            if (diffCol == 0) {

                // --la pièce attaque verticalement-- //

                if (pieceMetEnEchec.ligne < roi.ligne) {
                    //L'attque vient d'en haut
                    for(int ligne = pieceMetEnEchec.ligne; ligne < roi.ligne; ligne++) {
                        for(Piece piece : simPieces) {
                            if (piece != roi && piece.couleur != couleurActuelle
                                    && piece.peutBouger(pieceMetEnEchec.col, ligne)) {
                                return false;
                            }
                        }
                    }
                }
                if (pieceMetEnEchec.ligne > roi.ligne) {
                    //L'attque vient d'en bas
                    for(int ligne = pieceMetEnEchec.ligne; ligne > roi.ligne; ligne--) {
                        for(Piece piece : simPieces) {
                            if (piece != roi && piece.couleur != couleurActuelle
                                    && piece.peutBouger(pieceMetEnEchec.col, ligne)) {
                                return false;
                            }
                        }
                    }
                }
            }
            else if (diffLigne == 0) {

                //--La piece attaque horizontalement--//

                if (pieceMetEnEchec.col < roi.col) {
                    //L'attaque vient de la gauche
                    for (int col = pieceMetEnEchec.col; col < roi.ligne; col++) {
                        for (Piece piece : simPieces) {
                            if (piece != roi && piece.couleur != couleurActuelle
                                    && piece.peutBouger(col, pieceMetEnEchec.ligne)) {
                                return false;
                            }
                        }
                    }
                }
                if (pieceMetEnEchec.col > roi.col) {
                    //L'attaque vient de la droite
                    for(int col = pieceMetEnEchec.col; col > roi.ligne; col--) {
                            for(Piece piece : simPieces) {
                                if (piece != roi && piece.couleur != couleurActuelle
                                        && piece.peutBouger(col, pieceMetEnEchec.ligne)) {
                                    return false;
                                }
                            }
                    }
                }

            }
            else if (diffCol == diffLigne) {

                //--La pièce attaque diagonalemnent--//

                if (pieceMetEnEchec.ligne < roi.ligne) {

                    //L'attaque vient d'en haut
                    if (pieceMetEnEchec.col < roi.col) {
                        //L'attaque vient de haut à gauche
                        for (int col = pieceMetEnEchec.col, ligne = pieceMetEnEchec.ligne; col < roi.col; col++, ligne++) {
                            for (Piece piece : simPieces) {
                                if (piece != roi && piece.couleur != couleurActuelle && piece.peutBouger(col, ligne)) {
                                    return false;
                                }
                            }
                        }

                    }
                    if (pieceMetEnEchec.col > roi.col) {
                        //L'attaque vient d'en haut à droite
                        for (int col = pieceMetEnEchec.col, ligne = pieceMetEnEchec.ligne; col < roi.col; col--, ligne++) {
                            for (Piece piece : simPieces) {
                                if (piece != roi && piece.couleur != couleurActuelle && piece.peutBouger(col, ligne)) {
                                    return false;
                                }
                            }
                        }
                    }

                    //L'attaque vient d'en bas
                    if (pieceMetEnEchec.col < roi.col) {
                        //L'attaque vient d'en bas à gauche
                        for (int col = pieceMetEnEchec.col, ligne = pieceMetEnEchec.ligne; col < roi.col; col++, ligne--) {
                            for (Piece piece : simPieces) {
                                if (piece != roi && piece.couleur != couleurActuelle && piece.peutBouger(col, ligne)) {
                                    return false;
                                }
                            }
                        }

                    }
                    if (pieceMetEnEchec.col > roi.col) {
                        //L'attaque vient d'en bas à droite
                        for (int col = pieceMetEnEchec.col, ligne = pieceMetEnEchec.ligne; col > roi.col; col--, ligne--) {
                            for (Piece piece : simPieces) {
                                if (piece != roi && piece.couleur != couleurActuelle && piece.peutBouger(col, ligne)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            else {
                //--C'est le cavalier qui attaque--//
                

            }
        }

        return true;
    }

    private boolean roiPeutBouger(Piece roi) {

        //Vérifie s'il ya une case où le roi peut se retrancher
        if (mouvementEstValide(roi, -1, -1)) {
            return true;}
        if (mouvementEstValide(roi, 0, -1)) {
            return true;}
        if (mouvementEstValide(roi, 1, -1)) {
            return true;}
        if (mouvementEstValide(roi, -1, 0)) {
            return true;}
        if (mouvementEstValide(roi, 1, 0)) {
            return true;}
        if (mouvementEstValide(roi, -1, 1)) {
            return true;}
        if (mouvementEstValide(roi, 0, 1)) {
            return true;}
        if (mouvementEstValide(roi, 1, 1)) {
            return true;
        }
        return false;

    }

    
    private boolean mouvementEstValide(Piece roi, int colPlus, int lignePlus) {

    boolean leMouvementEstValide = false;

    // On met à jour la position du roi une seconde
    roi.col += colPlus;
    roi.ligne += lignePlus;

    if (roi.peutBouger(roi.col, roi.ligne)) {
        if (roi.pieceSurDestination != null) {
            simPieces.remove(roi.pieceSurDestination.getIndex());
        }
        if (!estIllegal(roi)) {
            leMouvementEstValide = true;
        }
    }
    
    // On reset la position du king et restore la pièce retirée
    roi.retourPosition();   
    copiePieces(pieces, simPieces);
    return leMouvementEstValide;
}

    private void verifieRoque() {
        if (pieceRoque != null) {
            if (pieceRoque.col == 0) {
                pieceRoque.col += 3;
            }
            else if (pieceRoque.col == 7) {
                pieceRoque.col -= 2;
            }
            pieceRoque.x = pieceRoque.getX(pieceRoque.col);
        }
    }

    public void alternerTour() {
        if (couleurActuelle == BLANC) {
            couleurActuelle = NOIR;

            //Remettre à défaut la valeur déplaceDE2Cases pour les noirs
            for (Piece piece : pieces) {
                if (piece.couleur == NOIR) {
                    piece.deplaceDe2Cases = false;
                }
            }

        } else {
            couleurActuelle = BLANC;

            //Remettre à défaut la valeur déplacéDE2Cases pour les blancs
            for (Piece piece : pieces) {
                if (piece.couleur == BLANC) {
                    piece.deplaceDe2Cases = false;
                }
            }
        }
        pieceActive = null;
    }

    private boolean peutEtrePromu() {

        if (pieceActive.type == Type.PION) {

            if (couleurActuelle == BLANC && pieceActive.ligne == 0
                    || couleurActuelle == NOIR && pieceActive.ligne == 7) {
                piecesPromotion.clear();
                piecesPromotion.add(new Tour(couleurActuelle, 9, 2));
                piecesPromotion.add(new Cavalier(couleurActuelle, 9, 3));
                piecesPromotion.add(new Fou(couleurActuelle, 9, 4));
                piecesPromotion.add(new Reine(couleurActuelle, 9, 5));
                return true;
            }

        }

        return false;
    }
    
    private void promouvoir() {
        if (souris.appuie) {
            for (Piece piece : piecesPromotion) {
                if (piece.col == souris.x / Echiquier.TAILLE_CASE && piece.ligne == souris.y / Echiquier.TAILLE_CASE) {
                    switch (piece.type) {
                        case TOUR:
                            simPieces.add(new Tour(couleurActuelle, pieceActive.col, pieceActive.ligne));
                            break;
                        case CAVALIER:
                            simPieces.add(new Cavalier(couleurActuelle, pieceActive.col, pieceActive.ligne));
                            break;
                        case FOU:
                            simPieces.add(new Fou(couleurActuelle, pieceActive.col, pieceActive.ligne));
                            break;
                        case REINE:
                            simPieces.add(new Reine(couleurActuelle, pieceActive.col, pieceActive.ligne));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(pieceActive.getIndex());
                    copiePieces(simPieces, pieces);
                    //On reset tout et on alterne le tour
                    pieceActive = null;
                    promotion = false;
                    alternerTour();
                }
                
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        echiquier.dessiner(g2);

        for (Piece p : simPieces) {
            p.dessiner(g2);
        }

        if (pieceActive != null) {
            if (peutBouger) {
                if (estIllegal(pieceActive) || adversairePeutCapturerRoi()) {
                    g2.setColor(Color.RED);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(pieceActive.col * Echiquier.TAILLE_CASE, pieceActive.ligne * Echiquier.TAILLE_CASE,
                            Echiquier.TAILLE_CASE, Echiquier.TAILLE_CASE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                } else {
                    g2.setColor(Color.YELLOW);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.fillRect(pieceActive.col * Echiquier.TAILLE_CASE, pieceActive.ligne * Echiquier.TAILLE_CASE,
                            Echiquier.TAILLE_CASE, Echiquier.TAILLE_CASE);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                
            }

            //Là je redessine la pièce active à la fin pour qu'elle ne soit pas caché par l'échiquier ou la case colorée
            pieceActive.dessiner(g2);

        }

        //INFORMATIONS SUR LE COTE
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 40));
        g2.setColor(Color.white);

        if (promotion) {
            g2.drawString("Promouvoir à: ", 840, 150);
            for (Piece piece : piecesPromotion) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.ligne), Echiquier.TAILLE_CASE,
                        Echiquier.TAILLE_CASE, null);
            }
        } else {
            if (couleurActuelle == BLANC) {
                g2.drawString("Tour Blancs", 840, 550);
                if (pieceMetEnEchec != null && pieceMetEnEchec.couleur == NOIR) {
                    g2.setColor(Color.red);
                    g2.drawString("Le Roi", 840, 650);
                    g2.drawString("est en échec", 840, 700);
                }
        } else {
            g2.drawString("Tour Noirs", 840, 250);
            if (pieceMetEnEchec != null && pieceMetEnEchec.couleur == BLANC) {
                    g2.setColor(Color.red);
                    g2.drawString("Le Roi", 840, 100);
                    g2.drawString("est en échec", 840, 150);
                }
            }
        }

        if (gameOver) {
            String texte = "";
            if(couleurActuelle == BLANC) {
                texte = "Victoire Blanc";
            } else {
                texte = "Victoire Noir";
            }
            g2.setFont(new Font("Arial", Font.PLAIN, 90));
            g2.setColor(Color.green);
            g2.drawString(texte, 200, 420);
        }
        
    }
    

}
