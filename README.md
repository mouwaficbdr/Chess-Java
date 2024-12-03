# README du Projet Échecs

## Description

Ce projet est un jeu d'échecs développé en Java, utilisant la bibliothèque Swing pour l'interface graphique. Le jeu permet aux utilisateurs de jouer aux échecs contre un adversaire, avec des fonctionnalités telles que le mouvement des pièces, la détection des échecs et des échecs et mat, ainsi que la promotion des pions.

## Fonctionnalités

- **Interface graphique** : Une interface utilisateur intuitive pour jouer aux échecs.
- **Mouvement des pièces** : Les pièces peuvent être déplacées selon les règles traditionnelles des échecs.
- **Détection des échecs** : Le jeu détecte si un roi est en échec.
- **Échec et mat** : Détection de la condition d'échec et mat.
- **Promotion des pions** : Les pions peuvent être promus en d'autres pièces lorsqu'ils atteignent la dernière rangée.
- **Multijoueur** : Permet à deux joueurs de jouer l'un contre l'autre.

## Technologies Utilisées

- **Java** : Langage de programmation principal.
- **Swing** : Bibliothèque pour créer l'interface graphique.
- **Maven** : Outil de gestion de projet et d'automatisation de la construction.

## Installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/votre-utilisateur/echecs.git
   ```

2. Accédez au répertoire du projet :
   ```bash
   cd echecs
   ```

3. Compilez le projet avec Maven :
   ```bash
   mvn clean install
   ```

4. Exécutez le projet :
   ```bash
   mvn exec:java -Dexec.mainClass="com.mouwafic.main.Main"
   ```

## Utilisation

- Lancez le programme pour ouvrir la fenêtre du jeu.
- Utilisez la souris pour sélectionner et déplacer les pièces.
- Suivez les instructions à l'écran pour jouer.


## Contribuer

Les contributions sont les bienvenues ! Si vous souhaitez contribuer, veuillez suivre ces étapes :

1. Forkez le projet.
2. Créez une nouvelle branche (`git checkout -b feature/YourFeature`).
3. Apportez vos modifications et validez (`git commit -m 'Ajout d'une nouvelle fonctionnalité'`).
4. Poussez vos modifications (`git push origin feature/YourFeature`).
5. Ouvrez une Pull Request.

## Auteurs

- **Mouwafic**

## License

Ce projet est sous licence MIT.

---

N'hésitez pas à poser des questions ou à signaler des problèmes via les issues du dépôt. Amusez-vous bien à jouer aux échecs !

