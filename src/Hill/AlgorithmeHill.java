package Hill;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class AlgorithmeHill {
    //Chiffrement
    public Entry<String, Boolean> encrypt(String texteClair) {
        System.out.println("\nChiffrement avec Hill-ADN :\n");
        System.out.println("Texte clair original : " + texteClair);

        texteClair = texteClair.replaceAll("[^a-zA-Z]", "").toUpperCase();
        System.out.println("Texte clair traité (suppression des non-lettres, en majuscules) : " + texteClair);

        boolean ajoutX = false;
        if (texteClair.length() % 2 != 0) {
            texteClair += "X"; // Remplissage avec 'X'
            ajoutX = true;
            System.out.println("Texte clair rembourré : " + texteClair);
        }

        ArrayList<Integer> phraseToNum = new ArrayList<>();
        for (int i = 0; i < texteClair.length(); i++) {
            int valeurNumerique = texteClair.charAt(i) - 'A';
            phraseToNum.add(valeurNumerique);
        }

        ArrayList<Integer> phraseEncodee = new ArrayList<>();
        for (int i = 0; i < phraseToNum.size(); i += 2) {
            int x = (matriceCle[0][0] * phraseToNum.get(i) + matriceCle[0][1] * phraseToNum.get(i + 1)) % 26;
            int y = (matriceCle[1][0] * phraseToNum.get(i) + matriceCle[1][1] * phraseToNum.get(i + 1)) % 26;
            phraseEncodee.add(x);
            phraseEncodee.add(y);
        }

        StringBuilder texteChiffre = new StringBuilder();
        for (Integer num : phraseEncodee) {
            texteChiffre.append((char) (num + 'A'));
        }

        System.out.println("Texte chiffré final : " + texteChiffre);
        return new SimpleEntry<>(texteChiffre.toString(), ajoutX);
    }
    
    //Dechiffrement
    public String decrypt(String texteChiffre, boolean supprimerXAjouté) {
        System.out.println("\nDéchiffrement de Hill-ADN :\n");
        System.out.println("Texte chiffré : " + texteChiffre);

        texteChiffre = texteChiffre.replaceAll("[^a-zA-Z]", "").toUpperCase();
        System.out.println("Texte chiffré traité (suppression des non-lettres, en majuscules) : " + texteChiffre);

        ArrayList<Integer> phraseToNum = new ArrayList<>();
        for (int i = 0; i < texteChiffre.length(); i++) {
            int valeurNumerique = texteChiffre.charAt(i) - 'A';
            phraseToNum.add(valeurNumerique);
        }

        ArrayList<Integer> phraseDécodee = new ArrayList<>();
        for (int i = 0; i < phraseToNum.size(); i += 2) {
            int x = (matriceCleInverse[0][0] * phraseToNum.get(i) + matriceCleInverse[0][1] * phraseToNum.get(i + 1)) % 26;
            int y = (matriceCleInverse[1][0] * phraseToNum.get(i) + matriceCleInverse[1][1] * phraseToNum.get(i + 1)) % 26;
            phraseDécodee.add(x);
            phraseDécodee.add(y);
        }

        StringBuilder texteDéchiffré = new StringBuilder();
        for (Integer num : phraseDécodee) {
            texteDéchiffré.append((char) (num + 'A'));
        }

        if (supprimerXAjouté && texteDéchiffré.length() > 0 && texteDéchiffré.charAt(texteDéchiffré.length() - 1) == 'X') {
            texteDéchiffré.deleteCharAt(texteDéchiffré.length() - 1); // Supprimer le 'X' ajouté
        }

        System.out.println("Texte déchiffré final : " + texteDéchiffré);
        return texteDéchiffré.toString();
    }
    private int[][] matriceCle;
    private int[][] matriceCleInverse;

    public AlgorithmeHill(int[][] matriceCle) {
        if (matriceCle.length != 2 || matriceCle[0].length != 2 || matriceCle[1].length != 2) {
            throw new IllegalArgumentException("La matrice de clé doit être de taille 2x2");
        }
        this.matriceCle = matriceCle;
        System.out.println("Chiffre Hill initialisé avec la matrice de clé : " + matriceToString(matriceCle));
        this.matriceCleInverse = inverserMatrice(matriceCle);
    }

    private static String matriceToString(int[][] matrice) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : matrice) {
            sb.append("[");
            for (int value : row) {
                sb.append(value).append(" ");
            }
            sb.append("]\n");
        }
        return sb.toString().trim();
    }

    private static int[][] inverserMatrice(int[][] matriceCle) {
        int det = (matriceCle[0][0] * matriceCle[1][1] - matriceCle[0][1] * matriceCle[1][0]) % 26;
        if (det < 0) det += 26;
        if (gcd(det, 26) != 1) {
            throw new IllegalArgumentException("La matrice n'est pas inversible");
        }

        int detInverse = -1;
        for (int i = 0; i < 26; i++) {
            if ((det * i) % 26 == 1) {
                detInverse = i;
                break;
            }
        }

        int[][] matriceInverse = new int[2][2];
        matriceInverse[0][0] = matriceCle[1][1] * detInverse % 26;
        matriceInverse[0][1] = (26 - matriceCle[0][1]) * detInverse % 26;
        matriceInverse[1][0] = (26 - matriceCle[1][0]) * detInverse % 26;
        matriceInverse[1][1] = matriceCle[0][0] * detInverse % 26;

        System.out.println("Matrice inverse calculée : " + matriceToString(matriceInverse));
        return matriceInverse;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
}
