package model.gameStatus;

import java.util.*;
import java.util.stream.Collectors;

import model.characters.*;
import model.characters.Character;
import model.point.Point;

import view.*;
import controller.*;

public class Game 
{   
    private static final int TOTAL_LEVEL          = 5;   // Numero di livelli del gioco
    private static final int MAX_ALLIES_PER_ROUND = 3;   // Numero di personaggi giocabili per round
    
    private List<Level> gameLevels;             // Lista dei livelli del gioco  
    private List<Character> availableAllies ;   // Lista di tutti i personaggi giocabili
    private List<Character> selectedAllies;     // Lista dei personaggi con cui l'utente giocherà il livello
    
    private GameStateManager gameStateManager ; // Oggetto che gestiste il caricamento di una partita 
    
    private static final Random rand = new Random();  // ci inizializza i valori dei personaggi
    
    //Oggetti Grafici 
    private GraphicsMenu graphicsMenu;
    private TutorialMenu tutorialMenu;
	
    
    public Game() 
    {
        this.gameLevels       = new ArrayList<>();
        this.availableAllies  = new ArrayList<>();
        this.selectedAllies   = new ArrayList<>();
        this.gameStateManager = new GameStateManager();

        
        // Inizializzazione Oggetti Grafici
        this.graphicsMenu = new GraphicsMenu();
        this.tutorialMenu = new TutorialMenu();
        
        // Avvio il menù principale
        this.graphicsMenu.start(this);    
    }

    private void createAllies()
    {
        // Popolo la lista di personaggi giocabili
        this.availableAllies.add(new Barbarian(new Point(0,0),""));
        this.availableAllies.add(new Archer(new Point(0,0),""));
        //... Aggiungere altri personaggi
    }

    private void initializeTutorial()
    {
        // Il tutorial prevede un gameplay statico, sempre uguale percio' non permettiamo la scelta dei personaggi

        // Popolo la lista di nemici del tutorial
        List<Character> tutorialEnemies = new ArrayList<>();
        tutorialEnemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        tutorialEnemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        List<Character> tutorialAllies = new ArrayList<>();
        tutorialAllies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        tutorialAllies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        // Aggiungo il tutorial alla lista dei livelli
        this.gameLevels.add(new Level(new LevelMap(), tutorialEnemies, tutorialAllies));
    }

    private void initializeGameLevels() 
    {
        // Popolo le liste di nemici dei livelli principali 
        List<Character> level1Enemies = new ArrayList<>();
        level1Enemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        level1Enemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        List<Character> level2Enemies = new ArrayList<>();
        level2Enemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        level2Enemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        List<Character> level3Enemies = new ArrayList<>();
        level3Enemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        level3Enemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        List<Character> level4Enemies = new ArrayList<>();
        level4Enemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        level4Enemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        List<Character> level5Enemies = new ArrayList<>();
        level5Enemies.add(new Barbarian(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));
        level5Enemies.add(new Archer(new Point(rand.nextInt(0,6),rand.nextInt(0,3)),""));

        // Agggiungo tutti i livelli alla lista dei 
        // PROBLEM :  se qualche personaggio mi muore poi nei livelli la lista viene aggiornata o tengono questa originale
        // PROBLEM :  LevelMap non funziona e quando chiamo il metodo initializeGameLevels mi crea gia le istanze delle finestre
        this.gameLevels.add(new Level(new LevelMap(), level1Enemies, this.selectedAllies));
        this.gameLevels.add(new Level(new LevelMap(), level2Enemies, this.selectedAllies));
        this.gameLevels.add(new Level(new LevelMap(), level3Enemies, this.selectedAllies));
        this.gameLevels.add(new Level(new LevelMap(), level4Enemies, this.selectedAllies));
        this.gameLevels.add(new Level(new LevelMap(), level5Enemies, this.selectedAllies));
    }

    public void startNewGame() 
    {       

    	// Avvio il Tutorial Menu
    	this.tutorialMenu.start(e -> 
    	{
            // Il flusso del codice rimane in attesa finche l'utente non sceglie se giocare o saltare il tutorial
            if (this.tutorialMenu.isTutorialSelected()) 
            {
            	// Ha scelto di giocare il livello
                System.out.println("Tutorial selected, starting Tutorial...");
                this.initializeTutorial();
                
                // Gioca il tutorial
                if (this.gameLevels.get(0).playTutorial()) 
                {
                    System.out.println("Congratulations! You completed the tutorial, now starting the game...");
                }
            } 
            else
            {
                System.out.println("Tutorial skipped, starting game...");
            }
        });
                   	
        // InizializzA la lista con tutti i personaggi giocabili
		this.createAllies(); 
		
		// Appare il menu per la selezione dei personaggi
		// Trasforma la lista di char in selectedAllies
		
		// INIZIANO I LIVELLI
		
		
		// Inizializzo le liste dei nemici e dei livelli
	    //this.initializeGameLevels();   PROBLEMATICO CON LEVELMAP
		// Gioca i livelli
	    //for (int i = 1; i <= Game.TOTAL_LEVEL; i++) 
	    //{
	        // Gioca il livello, playLevel ritorna true se il livello è stato completato sennò interrompe dal ciclo
	        //if (!this.gameLevels.get(i).playLevel(this.gameStateManager , i)) 
	        //{
	        //    System.out.println("Il livello non è stato completato, uscita dal ciclo.");
	        //    break;
	        //}
	
	        //Sostituisci gli alleati morti con nuovi alleati
	        //this.checkAndReplaceDeadAllies();
	
	        //System.out.println("Passaggio al livello " + (i + 1));
	    //}
    }
    
    private void transformList(List<String> nameCharacters) 
    {
        this.selectedAllies = this.availableAllies.stream()
            .filter(ally -> nameCharacters.contains(ally.getClass().getSimpleName()))
            .collect(Collectors.toList());
    }

	// Metodo per sostituire gli alleati morti con nuovi personaggi scelti dall'utente
    private void checkAndReplaceDeadAllies() 
    {
        // Calcola quanti alleati sono morti
        int alliesToChange = MAX_ALLIES_PER_ROUND - this.selectedAllies.size();
    
        if (alliesToChange > 0) 
        {
            System.out.println("Sostituzione di " + alliesToChange + " personaggi morti.");
    
            // Seleziona nuovi alleati
            List<Character> newAllies = CharacterMenu.replaceDeadAlliesMenu(this.availableAllies , alliesToChange);
    
            // Aggiungi i nuovi alleati
            this.selectedAllies.addAll(newAllies);
        }
    }    

    // Metodo per generare il filePath delle immagini dei personaggi
    private void generateCharacterImageFilePath() 
    {
        
    }
}