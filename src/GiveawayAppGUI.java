import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiveawayAppGUI {

    public static void main(String[] args) {
        // Création de la fenêtre principale de l'application
        JFrame frame = new JFrame("Giveaway Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);  // Taille de la fenêtre 500x500
        frame.setResizable(false);  // Empêcher le redimensionnement de la fenêtre
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)); // Utilisation de BoxLayout pour un agencement simple

        // Instance de la classe Giveaway
        Giveaway giveaway = new Giveaway();

        // Panneau pour ajouter des participants
        JPanel addParticipantsPanel = new JPanel();
        addParticipantsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner à gauche
        JLabel addLabel = new JLabel("Entrez les participants (séparés par des virgules) : ");
        JTextField addTextField = new JTextField(20);  // Largeur uniforme
        JButton addButton = new JButton("Ajouter");
        addParticipantsPanel.add(addLabel);
        addParticipantsPanel.add(addTextField);
        addParticipantsPanel.add(addButton);

        // Panneau pour importer des participants à partir d'un fichier texte
        JPanel importFilePanel = new JPanel();
        importFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner à gauche
        JButton importButton = new JButton("Importer depuis un fichier");
        importFilePanel.add(importButton);

        // Panneau pour exporter la liste de participants vers un fichier texte
        JPanel exportFilePanel = new JPanel();
        exportFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner à gauche
        JButton exportButton = new JButton("Exporter vers un fichier");
        exportFilePanel.add(exportButton);

        // Panneau pour retirer un participant
        JPanel removePanel = new JPanel();
        removePanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner à gauche
        JLabel removeLabel = new JLabel("Retirer un participant : ");
        JTextField removeTextField = new JTextField(20);  // Largeur uniforme
        JButton removeButton = new JButton("Retirer");
        removePanel.add(removeLabel);
        removePanel.add(removeTextField);
        removePanel.add(removeButton);

        // Zone pour afficher la liste des participants
        JTextArea participantsArea = new JTextArea(10, 30);
        participantsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(participantsArea);

        // Panneau pour spécifier le nombre de gagnants
        JPanel drawPanel = new JPanel();
        drawPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Aligner à gauche
        JLabel winnersLabel = new JLabel("Nombre de gagnants : ");
        JTextField winnersField = new JTextField(20);  // Largeur uniforme
        JButton drawButton = new JButton("Effectuer un tirage");
        drawPanel.add(winnersLabel);
        drawPanel.add(winnersField);
        drawPanel.add(drawButton);

        // Panneau pour quitter l'application
        JPanel quitPanel = new JPanel();
        JButton quitButton = new JButton("Quitter");
        quitPanel.add(quitButton);

        // Ajouter les panneaux dans la fenêtre principale
        frame.add(addParticipantsPanel);
        frame.add(importFilePanel);
        frame.add(exportFilePanel);
        frame.add(removePanel);
        frame.add(scrollPane);
        frame.add(drawPanel);
        frame.add(quitPanel);

        // Action pour ajouter des participants
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String names = addTextField.getText();
                giveaway.addParticipants(names);
                addTextField.setText(""); // Réinitialiser le champ de texte
                giveaway.displayParticipants(participantsArea); // Mise à jour dynamique de la liste
            }
        });

        // Action pour importer des participants depuis un fichier
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers texte", "txt"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    giveaway.importParticipantsFromFile(file);
                    giveaway.displayParticipants(participantsArea); // Mise à jour dynamique de la liste
                }
            }
        });

        // Action pour exporter la liste de participants dans un fichier texte
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers texte", "txt"));
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // Ajouter .txt si l'extension n'est pas déjà présente
                    if (!file.getName().endsWith(".txt")) {
                        file = new File(file.getAbsolutePath() + ".txt");
                    }
                    giveaway.exportParticipantsToFile(file);
                }
            }
        });

        // Action pour retirer un participant
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameToRemove = removeTextField.getText();
                giveaway.removeParticipant(nameToRemove);
                removeTextField.setText(""); // Réinitialiser le champ de texte
                giveaway.displayParticipants(participantsArea); // Mise à jour dynamique de la liste
            }
        });

        // Action pour effectuer un tirage au sort avec un nombre variable de gagnants
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String winnersInput = winnersField.getText();
                try {
                    int numberOfWinners = Integer.parseInt(winnersInput);
                    giveaway.drawWinners(participantsArea, numberOfWinners, frame); // Afficher les résultats dans une nouvelle fenêtre
                } catch (NumberFormatException ex) {
                    participantsArea.setText("Veuillez entrer un nombre valide pour les gagnants.");
                }
            }
        });

        // Action pour quitter l'application
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Quitter l'application
            }
        });

        // Afficher la fenêtre principale
        frame.setVisible(true);
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
            }
        }
    }

    // Importer des participants depuis un fichier texte
    public void importParticipantsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = line.trim();
                if (!name.isEmpty() && !participants.contains(name)) {
                    participants.add(name);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la lecture du fichier.");
        }
    }

    // Exporter la liste des participants vers un fichier texte
    public void exportParticipantsToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String participant : participants) {
                writer.write(participant);
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "La liste des participants a été exportée avec succès.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'exportation du fichier.");
        }
    }

    // Retirer un participant
    public void removeParticipant(String name) {
        participants.remove(name.trim()); // Retirer le participant en enlevant les espaces inutiles
    }

    // Afficher la liste des participants dans la zone de texte
    public void displayParticipants(JTextArea textArea) {
        if (participants.isEmpty()) {
            textArea.setText("Aucun participant pour l'instant.");
        } else {
            StringBuilder sb = new StringBuilder("Liste des participants :\n");
            for (String participant : participants) {
                sb.append("- ").append(participant).append("\n");
            }
            textArea.setText(sb.toString());
        }
    }

    // Effectuer un tirage au sort et afficher les résultats dans une nouvelle fenêtre
    public void drawWinners(JTextArea textArea, int numberOfWinners, JFrame parentFrame) {
        if (participants.isEmpty()) {
            textArea.setText("Aucun participant pour effectuer un tirage.");
            return;
        }
        if (numberOfWinners <= 0 || numberOfWinners > participants.size()) {
            textArea.setText("Veuillez entrer un nombre de gagnants valide.");
            return;
        }

        Random random = new Random();
        List<String> winners = new ArrayList<>();
        List<String> tempParticipants = new ArrayList<>(participants);

        for (int i = 0; i < numberOfWinners; i++) {
            int winnerIndex = random.nextInt(tempParticipants.size());
            String winner = tempParticipants.remove(winnerIndex);
            winners.add(winner);
        }

        // Affichage des résultats dans une nouvelle fenêtre
        new WinnersFrame(winners, parentFrame);
    }
}

class WinnersFrame extends JFrame {
    public WinnersFrame(List<String> winners, JFrame parentFrame) {
        super("Résultats du tirage");

        setSize(300, 200);
        setLocationRelativeTo(parentFrame);
        setLayout(new BorderLayout());

        JTextArea winnersArea = new JTextArea();
        for (String winner : winners) {
            winnersArea.append(winner + "\n");
        }
        winnersArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(winnersArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> {
            parentFrame.setEnabled(true);  // Réactiver la fenêtre principale
            dispose();  // Fermer la fenêtre des résultats
        });

        add(closeButton, BorderLayout.SOUTH);
        setVisible(true);
        parentFrame.setEnabled(false);  // Désactiver la fenêtre principale jusqu'à la fermeture des résultats
    }
}
