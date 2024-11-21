import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GiveawayApp {

    public static void main(String[] args) {
        Giveaway giveaway = new Giveaway();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Menu Giveaway ===");
            System.out.println("1. Ajouter un ou plusieurs participants");
            System.out.println("2. Retirer un participant");
            System.out.println("3. Afficher les participants");
            System.out.println("4. Effectuer un tirage au sort");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne après l'entrée

            switch (choice) {
                case 1:
                    System.out.print("Entrez les noms des participants à ajouter (séparés par des virgules): ");
                    String addNames = scanner.nextLine();
                    giveaway.addParticipants(addNames);
                    break;

                case 2:
                    System.out.print("Entrez le nom du participant à retirer: ");
                    String removeName = scanner.nextLine();
                    giveaway.removeParticipant(removeName);
                    break;

                case 3:
                    giveaway.displayParticipants();
                    break;

                case 4:
                    giveaway.drawWinner();
                    break;

                case 5:
                    running = false;
                    System.out.println("Au revoir!");
                    break;

                default:
                    System.out.println("Option invalide. Essayez à nouveau.");
            }
        }

        scanner.close();
    }
}

class Giveaway {
    private List<String> participants;

    // Constructeur
    public Giveaway() {
        participants = new ArrayList<>();
    }

    // Ajouter un ou plusieurs participants
    public void addParticipants(String names) {
        String[] nameList = names.split(",");  // Diviser la chaîne de texte par les virgules
        for (String name : nameList) {
            name = name.trim();  // Enlever les espaces inutiles
            if (!participants.contains(name)) {
                participants.add(name);
                System.out.println(name + " a été ajouté à la liste des participants.");
            } else {
                System.out.println(name + " est déjà dans la liste.");
            }
        }
    }

    // Retirer un participant
    public void removeParticipant(String name) {
        if (participants.contains(name)) {
            participants.remove(name);
            System.out.println(name + " a été retiré de la liste des participants.");
        } else {
            System.out.println("Ce participant n'est pas dans la liste.");
        }
    }

    // Afficher la liste des participants
    public void displayParticipants() {
        if (participants.isEmpty()) {
            System.out.println("Aucun participant pour l'instant.");
        } else {
            System.out.println("Liste des participants :");
            for (String participant : participants) {
                System.out.println("- " + participant);
            }
        }
    }

    // Effectuer un tirage au sort
    public void drawWinner() {
        if (participants.isEmpty()) {
            System.out.println("Il n'y a pas de participants.");
        } else {
            Random random = new Random();
            String winner = participants.get(random.nextInt(participants.size()));
            System.out.println("Le gagnant du giveaway est : " + winner);
        }
    }
}
