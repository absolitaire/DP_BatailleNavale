package model;

import java.io.Serializable;
import java.util.Observable;

public class Game extends Observable implements Serializable{

	private Player[] players;
	private Board[] boards;
	private int joueurEnCours;
	private boolean gameIsRunning;
	private boolean boatsAreAllPlaced;

	public Game(int choixEpoque, boolean automaticArrangement, boolean multiplayer){
		players = new Player[2];
		boards = new Board[2];

		boards[1] = new Board(new Boat20thFactory());
		players[1] = new Player(1, new Human());
		boards[1].setJoueur(players[1]);

		if(multiplayer) {
			boards[0] = new Board(new Boat20thFactory());
			players[0] = new Player(0, new Human());
			boards[0].setJoueur(players[0]);
		}else {
			boards[0] = new Board(new Boat20thFactory());
			//joueurs[0] = new Player(0, new IARandom(this, 1));
			players[0] = new Player(0, new IACross(this, 1));
			//joueurs[0] = new Player(0, new Human());
			boards[0].setJoueur(players[0]);
		}



		boards[0].automaticArrangement();
		if(automaticArrangement){
			boards[1].automaticArrangement();
			boatsAreAllPlaced = true;
			gameIsRunning = true;
		}else{
			gameIsRunning = false;
			boatsAreAllPlaced = false;
			Log.getInstance().addLog("Appuyez sur n'importe quelle touche pour alterner entre le \nplacement vertical et horizontal.");
			boards[1].logPlaceNextBoat();
		}
		joueurEnCours = 1;

	}

	public Player getJoueur(int id) {
		return players[id];
	}

	public Board getBoard(int id) {
		return boards[id];
	}

	public int getJoueurEnCours() {
		return joueurEnCours;
	}

	public void setJoueurEnCours(int joueurEnCours) {
		this.joueurEnCours = joueurEnCours;
	}

	public boolean getGameIsRunning() {
		return gameIsRunning;
	}

	public void setGameIsRunning(boolean gameIsRunning) {
		this.gameIsRunning = gameIsRunning;
	}

	public void shoot(int joueur, int a, int o){
		if(gameIsRunning){
			//System.out.println("Tir en "+a+","+o);
			Log.getInstance().addLog("Joueur "+joueurEnCours+"> Tir en "+a+","+o);
			if(boards[joueur].getSquares()[a][o].tirer() == true) {
				boolean verif = false;

				for(Boat boat : boards[joueur].getBateaux()) {
					if(!boat.isCoule()) {
						verif = true; 
						break;
					}
				}
				if(verif == false) {
					//System.out.println("Le joueur "+joueur+" a perdu");
					Log.getInstance().addLog("Le joueur "+joueur+" a perdu !");
					gameIsRunning = false;
				}
			}

			joueurEnCours = (joueurEnCours == 0 ? 1 : 0);
			setChanged();
			notifyObservers();
			players[joueurEnCours].tirer();
		}

	}

	public void placeBoat(int a, int o, boolean horizontal){
		if(!gameIsRunning && !boatsAreAllPlaced){
			if(boards[1].placeBoat(a, o, horizontal)){


				if(boards[1].areBoatsAllPlaced()){
					boatsAreAllPlaced = true;
					gameIsRunning = true;
					Log.getInstance().addLog("La partie peut commencer !");
				}else{
					boards[1].logPlaceNextBoat();
				}
			}
			setChanged();
			notifyObservers();
		}

	}

	public boolean getBoatsAreAllPlaced() {
		return boatsAreAllPlaced;
	}

	public void swapPlayers() {
		Player p = players[0];
		Board b = boards[0];
		
		players[0] = players[1];
		boards[0] = boards[1];
		
		players[1] = p;
		boards[1] = b;
		
		joueurEnCours = (joueurEnCours == 0 ? 1 : 0);
	}


	/*public void boucle() {
		while(gameIsRunning) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			joueurs[joueurEnCours].tirer();
		}
	}*/
}
