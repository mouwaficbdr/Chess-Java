package com.mouwafic.main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame fenetre = new JFrame("Echecs");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setResizable(false);

        PanneauDeJeu panneau = new PanneauDeJeu();
        fenetre.add(panneau);
        fenetre.pack();

        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);

        panneau.commencerJeu();

    }
}
