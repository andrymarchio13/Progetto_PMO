package view;

import javax.swing.*;

import model.gameStatus.Game;

import java.awt.*;

public class GraphicsMenu {

	public static boolean newGame = false; // Variabile per tracciare la scelta
	public static boolean loadGame = false; // Variabile per tracciare la scelta
	
    public static void startMenu(Game g) {
        JFrame frame = new JFrame("Graphics Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        JLabel backgroundLabel = new JLabel(new ImageIcon("images/Background/background4.jpg"));
        backgroundLabel.setLayout(new GridBagLayout());

        JButton startButton = createButton("New Game", e -> {
        	newGame = true;
            g.startNewGame();   // Avvia una nuova partita
            frame.dispose(); // Chiude il menu attuale
        });

        JButton loadButton = createButton("Load Game", e -> {
        	loadGame = true;
            LoadGameMenu.startLoadGame();
            frame.dispose(); // Chiude il menu attuale

            //g.loadGame();    // Carica una partita salvata
        });

        JButton exitButton = createButton("Exit", e -> System.exit(0));

        // Disabilita il pulsante "Load Game" se non ci sono salvataggi (logica da implementare)
        boolean hasSaves = checkSavedGames();
        loadButton.setEnabled(hasSaves);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;
        backgroundLabel.add(startButton, gbc);
        gbc.gridy = 1;
        backgroundLabel.add(loadButton, gbc);
        gbc.gridy = 2;
        backgroundLabel.add(exitButton, gbc);

        frame.add(backgroundLabel);
        frame.setVisible(true);
    }

    private static JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(139, 69, 19));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(action);
        return button;
    }

    private static boolean checkSavedGames() {
        // Qui puoi aggiungere la logica per verificare la presenza di salvataggi
        return true; // Modifica questo valore in base ai tuoi dati
    }

}
