package Hill;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HiillADN {
	// convertisseur Binnair
	public static String texteEnBinaire(String texte) {
        StringBuilder binaire = new StringBuilder();
        for (char caractere : texte.toCharArray()) {
            binaire.append(String.format("%8s", Integer.toBinaryString(caractere)).replace(' ', '0'));
        }
        return binaire.toString();
    }

    public static String binaireEnTexte(String binaire) {
        StringBuilder texte = new StringBuilder();
        for (int i = 0; i < binaire.length(); i += 8) {
            int endIndex = Math.min(i + 8, binaire.length());
            String octetStr = binaire.substring(i, endIndex);
            if (octetStr.length() < 8) {
                octetStr = String.format("%-8s", octetStr).replace(' ', '0');
            }
            int codeCaractere = Integer.parseInt(octetStr, 2);
            texte.append((char) codeCaractere);
        }
        return texte.toString();
    }
    
    // convertisseur ADN
    public static String binaireEnADN(String binaire) {
        StringBuilder adn = new StringBuilder();
        for (int i = 0; i < binaire.length(); i += 2) {
            String paireBits = binaire.substring(i, i + 2);
            switch (paireBits) {
                case "00":
                    adn.append("A");
                    break;
                case "01":
                    adn.append("C");
                    break;
                case "10":
                    adn.append("G");
                    break;
                case "11":
                    adn.append("T");
                    break;
            }
        }
        return adn.toString();
    }

    public static String adnEnBinaire(String adn) {
        StringBuilder binaire = new StringBuilder();
        for (int i = 0; i < adn.length(); i++) {
            char nucleotide = adn.charAt(i);
            switch (nucleotide) {
                case 'A':
                    binaire.append("00");
                    break;
                case 'C':
                    binaire.append("01");
                    break;
                case 'G':
                    binaire.append("10");
                    break;
                case 'T':
                    binaire.append("11");
                    break;
                default:
                    throw new IllegalArgumentException("Cette nucléotide n'existe pas : " + nucleotide);
            }
        }
        return binaire.toString();
    }
    
    // convertisseur AminoAcide
    private Map<String, List<String>> carteCodons = new HashMap<>();
    private StringBuilder sequenceIndex = new StringBuilder();

    public HiillADN() {
        initialiserCarteCodons();
    }

    private void initialiserCarteCodons() {
        // Vos mappages de codons
        carteCodons.put("C", Arrays.asList("UGU", "UGC"));
        carteCodons.put("Q", Arrays.asList("CAA", "CAG"));
        carteCodons.put("D", Arrays.asList("GAU", "GAC"));
        carteCodons.put("Z", Arrays.asList("UAC"));
        carteCodons.put("S", Arrays.asList("ucu", "ucc", "UCA", "UCG"));
        carteCodons.put("F", Arrays.asList("uuu", "uuc"));
        carteCodons.put("N", Arrays.asList("AAU", "AAC"));
        carteCodons.put("I", Arrays.asList("AUU", "AUC", "AUA"));
        carteCodons.put("P", Arrays.asList("CCU", "CCC", "CCA", "CCG"));
        carteCodons.put("U", Arrays.asList("AGA", "AGG"));
        carteCodons.put("G", Arrays.asList("GGU", "GGC", "GGA", "GGG"));
        carteCodons.put("K", Arrays.asList("AAA", "AAG"));
        carteCodons.put("E", Arrays.asList("GAA", "GAG"));
        carteCodons.put("L", Arrays.asList("cuu", "CUC", "CUA", "CUG"));
        carteCodons.put("X", Arrays.asList("AGU", "AGC"));
        carteCodons.put("M", Arrays.asList("AUG")); 
        carteCodons.put("O", Arrays.asList("UUA", "UUG"));
        carteCodons.put("A", Arrays.asList("GCU", "GCC", "GCA", "GCG"));
        carteCodons.put("R", Arrays.asList("CGU", "CGC", "CGA", "CGG"));
        carteCodons.put("B", Arrays.asList("UAA", "UAG", "UGA"));
        carteCodons.put("T", Arrays.asList("ACU", "ACC", "ACA", "ACG"));
        carteCodons.put("V", Arrays.asList("GUU", "GUC", "GUA", "GUG"));
        carteCodons.put("W", Arrays.asList("UGG"));
        carteCodons.put("X", Arrays.asList("AGU", "AGC"));
        carteCodons.put("H", Arrays.asList("CAU", "CAC"));
        carteCodons.put("Y", List.of("UAU"));
    }

    public String dnaVersAminoAcide(String dna) {
        sequenceIndex.setLength(0); // Réinitialiser sequenceIndex pour une nouvelle conversion
        StringBuilder sequenceAminoAcide = new StringBuilder();

        for (int i = 0; i < dna.length(); i += 3) {
            if (i + 3 > dna.length()) {
                // Ajouter directement les codons incomplets à la séquence d'index
                sequenceIndex.append(dna.substring(i));
                break; // Fin du traitement
            }

            String codon = dna.substring(i, i + 3);
            boolean trouve = false;
            for (Map.Entry<String, List<String>> entry : carteCodons.entrySet()) {
                if (entry.getValue().contains(codon)) {
                    sequenceAminoAcide.append(entry.getKey());
                    sequenceIndex.append(entry.getValue().indexOf(codon)); // Index numérique pour le codon reconnu
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                sequenceAminoAcide.append("-"); // Placeholder pour le codon sauté
                sequenceIndex.append(codon); // Conserver le codon d'origine dans la séquence
            }
        }

        return sequenceAminoAcide.toString();
    }

    public String getSequenceIndex() {
        return sequenceIndex.toString();
    }

    public String aminoAcideVersDNA(String sequenceAminoAcide, String sequenceIndex) {
        StringBuilder sequenceDNA = new StringBuilder();
        int pointeurAminoAcide = 0;  // Pointeur pour la séquence d'acides aminés

        for (int i = 0; i < sequenceIndex.length(); i++) {
            char caractereIndex = sequenceIndex.charAt(i);

            if (Character.isDigit(caractereIndex)) {
                // C'est un codon reconnu
                if (pointeurAminoAcide < sequenceAminoAcide.length()) {
                    String aminoAcide = String.valueOf(sequenceAminoAcide.charAt(pointeurAminoAcide));
                    int indexCodon = Character.getNumericValue(caractereIndex);
                    List<String> codons = carteCodons.get(aminoAcide);
                    if (codons != null && indexCodon >= 0 && indexCodon < codons.size()) {
                        sequenceDNA.append(codons.get(indexCodon));
                    } else {
                        throw new IllegalArgumentException("Acide aminé ou index de codon invalide : " + aminoAcide + ", " + indexCodon);
                    }
                    pointeurAminoAcide++;  // Passer à l'acide aminé suivant
                }
            } else {
                // C'est un codon non reconnu ou incomplet
                if (i + 2 < sequenceIndex.length() && !Character.isDigit(sequenceIndex.charAt(i + 1))) {
                    String codon = sequenceIndex.substring(i, i + 3);
                    sequenceDNA.append(codon);
                    i += 2;  // Ignorer les deux caractères suivants car ils font partie du codon
                } else {
                    // Codon non reconnu à un ou deux caractères
                    sequenceDNA.append(caractereIndex);
                }
            }
        }

        return sequenceDNA.toString();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez le texte clair :");
        String texteClair = scanner.nextLine();
        // Replace spaces in the entered text
        String texteCL = texteClair.replaceAll("[^a-zA-Z]", "");
        System.out.println("Texte clair après remplacement d'espaces : " + texteCL);

        // Convertir le texte clair en texte binaire
        String texteBinaire = texteEnBinaire(texteCL);

        // Convertir le texte en binaire en  ADN
        String texteADN = binaireEnADN(texteBinaire);

        // Convertir le texte ADN en acides aminés
        HiillADN convertisseurAcidesAmines = new HiillADN();
        String texteAminoAcide = convertisseurAcidesAmines.dnaVersAminoAcide(texteADN);
        String sequenceIndex = convertisseurAcidesAmines.getSequenceIndex();

        // La matrice du chiffrement de Hill 
        System.out.println("Entrez la matrice (2x2) du chiffrement de Hill :");
        int[][] matriceCle = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                matriceCle[i][j] = scanner.nextInt();
            }
        }
        try {
        	AlgorithmeHill chiffreHill = new AlgorithmeHill(matriceCle);

            // Chiffrement
        	 Map.Entry<String, Boolean> donneesChiffrees = chiffreHill.encrypt(texteAminoAcide);
             String texteChiffreHill = donneesChiffrees.getKey();
             boolean ajoutX = donneesChiffrees.getValue();
             System.out.println("Texte chiffré : " + texteChiffreHill);


            // Déchiffrement
            String texteDechiffreHill = chiffreHill.decrypt(texteChiffreHill, ajoutX);

            // Convertir le texte acides aminés en ADN 
            String texteADNDechiffre = convertisseurAcidesAmines.aminoAcideVersDNA(texteDechiffreHill, sequenceIndex);
            System.out.println("Texte converti de acides aminés à ADN : " + texteADNDechiffre);

            // Convertir le texte ADN en binaire
            String texteBinaireDechiffre = adnEnBinaire(texteADNDechiffre);
            System.out.println("Le texte en binaire : " + texteBinaireDechiffre);

            // Conversion le texte binaire en texte
            String texteEnclairDechiffre = binaireEnTexte(texteBinaireDechiffre);
            System.out.println("Texte déchiffré: " + texteEnclairDechiffre);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            scanner.close();
            return;
        }
        scanner.close();
    }
}