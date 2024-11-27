package com.mouwafic.piece;

import com.mouwafic.main.Echiquier;
import com.mouwafic.main.PanneauDeJeu;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

// import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
// import java.io.IOException;
import java.io.InputStream;


public class Piece {
    public BufferedImage image;
    public int x, y;
    public int col, ligne, preCol, preLigne;
    public int couleur;
    public Piece pieceSurDestination;
    public boolean aEteDeplace, deplaceDe2Cases;

    public Piece(int couleur, int col, int ligne) {
        this.couleur = couleur;
        this.col = col;
        this.ligne = ligne;
        x = getX(col);
        y = getY(ligne);
        preCol = col;
        preLigne = ligne;
    }

    // ! Chargement de l'image à partir de fichier PNG
    // public BufferedImage getImage(String cheminImage) {
    //     BufferedImage image = null;
    //     try {
    //         image = ImageIO.read(getClass().getResourceAsStream(cheminImage + ".png"));
    //     } catch (IOException e) {
    //         System.err.println("Erreur de chargement de l'image : " + e.getMessage());
    //     }
    //     if (image == null) {
    //         throw new IllegalArgumentException("L'image à l'emplacement " + cheminImage + " est introuvable.");
    //     }
    //     return image;
    // }

    // ! Chargement de l'image à partir d'un fichier svg
    public BufferedImage getImage(String cheminImage) {
        BufferedImage image = null;

        try (InputStream svgStream = getClass().getResourceAsStream(cheminImage + ".svg")) {
            // Vérifier si le fichier SVG existe
            if (svgStream == null) {
                throw new IllegalArgumentException(
                        "Le fichier SVG à l'emplacement " + cheminImage + " est introuvable.");
            }

            // Créer un Transcoder pour convertir le SVG en BufferedImage
            final BufferedImage[] tempImage = new BufferedImage[1];

            Transcoder transcoder = new ImageTranscoder() {
                @Override
                public BufferedImage createImage(int width, int height) {
                    // Créer et stocker l'image localement
                    return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                }

                @Override
                public void writeImage(BufferedImage img, TranscoderOutput out) {
                    // Stocker l'image dans le tableau temporaire
                    tempImage[0] = img;
                }
            };

            // Définir les dimensions de l'image rendue
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) Echiquier.TAILLE_CASE);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) Echiquier.TAILLE_CASE);

            // Effectuer la conversion
            TranscoderInput input = new TranscoderInput(svgStream);
            transcoder.transcode(input, null);

            // Récupérer l'image générée
            image = tempImage[0];

        } catch (TranscoderException e) {
            System.err.println("Erreur lors de la conversion du fichier SVG : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Argument invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Une erreur inattendue s'est produite : " + e.getMessage());
            e.printStackTrace();
        }

        if (image == null) {
            throw new IllegalStateException(
                    "Impossible de convertir le fichier SVG en BufferedImage pour " + cheminImage);
        }

        return image;
    }

    public int getX(int col) {
        return col * Echiquier.TAILLE_CASE;
    }

    public int getY(int ligne) {
        return ligne * Echiquier.TAILLE_CASE;
    }

    public int getCol(int x) {
        return (x + Echiquier.MOITIE_TAILLE_CASE) / Echiquier.TAILLE_CASE; //On rajoute la valeur de la moitié d'un carré pour prendre comme référence le centre de la case
    }

    public int getLigne(int y) {
        return (y + Echiquier.MOITIE_TAILLE_CASE) / Echiquier.TAILLE_CASE;
    }

    public int getIndex() {
        for (int index = 0; index < PanneauDeJeu.simPieces.size(); index++) {
            if (PanneauDeJeu.simPieces.get(index) == this) {
                return index;
            }
        }
        return 0;
    }

    public void mettreAJourPosition() {
        x = getX(col);
        y = getY(ligne);
        preCol = getCol(x);
        preLigne = getLigne(y);
        aEteDeplace = true;
    }

    public void retour() {
        col = preCol;
        ligne = preLigne;
        x = getX(col);
        y = getY(ligne);
    }

    public boolean peutBouger(int colCible, int ligneCible) {
        return false;
    }

    public boolean estDansEchiquier(int colCible, int ligneCible) {
        if (colCible >= 0 && colCible <= 7 && ligneCible >= 0 && ligneCible <= 7) {
            return true;
        }
        return false;
    }

    public boolean memeCase(int colCible, int ligneCible) {
        if (colCible == preCol && ligneCible == preLigne) {
            return true;
        }
        return false;
    }

    public Piece tombeSurPiece(int colCible, int ligneCible) {
        for (Piece piece : PanneauDeJeu.simPieces) {
            if (piece.col == colCible && piece.ligne == ligneCible && piece != this) {
                return piece;
            }
        }
        return null;
    }

    public boolean verifieCaseValide(int colCible, int ligneCible) {

        pieceSurDestination = tombeSurPiece(colCible, ligneCible);

        if (pieceSurDestination == null) {
            return true;
        } else {
            if (pieceSurDestination.couleur != this.couleur) {
                return true;
            } else {
                pieceSurDestination = null;
            }
        }

        return false;
    }

    public boolean pieceSurLigneDroite(int colCible, int ligneCible) {

        //Quand la piece bouge vers la gauche
        for (int c = preCol - 1; c > colCible; c--) {
            for (Piece piece : PanneauDeJeu.simPieces) {
                if (piece.col == c && piece.ligne == ligneCible) {
                    pieceSurDestination = piece;
                    return true;
                }
            }
        }

        //Quand la piece bouge vers la droite
        for (int c = preCol + 1; c < colCible; c++) {
            for (Piece piece : PanneauDeJeu.simPieces) {
                if (piece.col == c && piece.ligne == ligneCible) {
                    pieceSurDestination = piece;
                    return true;
                }
            }
        }

        //Quand la piece bouge vers le haut
        for (int l = preLigne - 1; l > ligneCible; l--) {
            for (Piece piece : PanneauDeJeu.simPieces) {
                if (piece.ligne == l && piece.col == colCible) {
                    pieceSurDestination = piece;
                    return true;
                }
            }
        }

        //Quand la piece bouge vers le bas
        for (int l = preLigne + 1; l < ligneCible; l++) {
            for (Piece piece : PanneauDeJeu.simPieces) {
                if (piece.ligne == l && piece.col == colCible) {
                    pieceSurDestination = piece;
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean pieceSurDiagonale(int colCible, int ligneCible) {

        //Haut
        if (ligneCible < preLigne) {
            //Gauche
            for (int c = preCol - 1; c > colCible; c--) {

                int diff = Math.abs(c - preCol);
                for (Piece piece : PanneauDeJeu.simPieces) {
                    if (piece.col == c && piece.ligne == preLigne - diff) {
                        pieceSurDestination = piece;
                        return true;
                    }
                }
            }

            //Droite
            for (int c = preCol + 1; c < colCible; c++) {

                int diff = Math.abs(c - preCol);
                for (Piece piece : PanneauDeJeu.simPieces) {
                    if (piece.col == c && piece.ligne == preLigne - diff) {
                        pieceSurDestination = piece;
                        return true;
                    }
                }
            }
        }
        
        //Bas
        if (ligneCible > preLigne) {
            //Gauche
            for (int c = preCol - 1; c > colCible; c--) {

                int diff = Math.abs(c - preCol);
                for (Piece piece : PanneauDeJeu.simPieces) {
                    if (piece.col == c && piece.ligne == preLigne + diff) {
                        pieceSurDestination = piece;
                        return true;
                    }
                }
            }

            //Droite
            for (int c = preCol + 1; c < colCible; c++) {

                int diff = Math.abs(c - preCol);
                for (Piece piece : PanneauDeJeu.simPieces) {
                    if (piece.col == c && piece.ligne == preLigne + diff) {
                        pieceSurDestination = piece;
                        return true;
                    }
                }
            }
        }
        
        return false;

    }

    public void dessiner(Graphics2D g2) {

        // Obtenir la largeur et la hauteur de l'image
        int largeurImage = image.getWidth();
        int hauteurImage = image.getHeight();

        // Calculer la position centrée
        int xCentre = x + (Echiquier.TAILLE_CASE - largeurImage) / 2;
        int yCentre = y + (Echiquier.TAILLE_CASE - hauteurImage) / 2;

        g2.drawImage(image, xCentre, yCentre, null);
    }
}

