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
    public static ArrayList<Piece> simPieces = new ArrayList<>(); //Permet de simuler le déplacement sans affecter l'état réel des pièces dans le jeu
    Piece pieceActive;

    //COULEUR
    public static final int BLANC = 0;
    public static final int NOIR = 1;
    int couleurActuelle = BLANC;

    //BOOLEENS
    boolean peutBouger;
    boolean caseValide;


    public PanneauDeJeu() {
        setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        setBackground(Color.BLACK);
        addMouseMotionListener(souris);
        addMouseListener(souris);

        placerPieces();
        copiePieces(pieces, simPieces);
    }

    private void mettreAJour() {

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

                    //Une fois le mouvement confirmé

                    //On met à jour la arraylist pieces dans le cas où une pièce a été capturée
                    copiePieces(simPieces, pieces);
                    pieceActive.mettreAJourPosition();
                    
                } else {

                    //Le mouvement n'est pas valide
                    copiePieces(pieces, simPieces);
                    pieceActive.retour();
                    pieceActive = null;   
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

            caseValide = true;
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        echiquier.dessiner(g2);

        for (Piece p : simPieces) {
            p.dessiner(g2);
        }
        
        if (pieceActive != null) {
            if (peutBouger) {
                g2.setColor(Color.YELLOW);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(pieceActive.col * Echiquier.TAILLE_CASE, pieceActive.ligne * Echiquier.TAILLE_CASE,
                        Echiquier.TAILLE_CASE, Echiquier.TAILLE_CASE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }

            //Là je redessinela pièce active à la fin pour qu'elle ne soit pas caché par l'échiquier ou la case colorée
            pieceActive.dessiner(g2);

        }
    }

}
