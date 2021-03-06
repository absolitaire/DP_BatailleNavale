package model;

import java.io.Serializable;
import java.util.Observable;


@SuppressWarnings("serial")
public class Game extends Observable implements Serializable{

	private Player[] players;
	private Board[] boards;
	private int joueurEnCours;
	private boolean gameIsRunning;
	private boolean boatsAreAllPlaced;
	private boolean multiplayer;

	public Game(int choixEpoque, boolean automaticArrangement, boolean multiplayer, String strategy){
		this.players = new Player[2];
		this.boards = new Board[2];
		this.multiplayer = multiplayer;

		if(multiplayer) {
			if(choixEpoque == 0){
				boards[1] = new Board(new Boat20thFactory());
				boards[0] = new Board(new Boat20thFactory());
			}else{
				boards[1] = new Board(new Boat30thFactory());
				boards[0] = new Board(new Boat30thFactory());
			}
			players[1] = new Player(1, "Host      ", new Human());
			//boards[1].setJoueur(players[1]);
			players[0] = new Player(0, "Client    ", new Human());
			//boards[0].setJoueur(players[0]);
		}else {
			if(choixEpoque == 0){
				boards[1] = new Board(new Boat20thFactory());
				boards[0] = new Board(new Boat20thFactory());
			}else{
				boards[1] = new Board(new Boat30thFactory());
				boards[0] = new Board(new Boat30thFactory());
			}
			players[1] = new Player(1, "Joueur    ", new Human());
			//boards[1].setJoueur(players[1]);
			//joueurs[0] = new Player(0, new IARandom(this, 1));
			if(strategy.equals("IACross")) {
				players[0] = new Player(0, "IA             ", new IACross(this, 1));
			}else if(strategy.equals("IARandom")){
				players[0] = new Player(0, "IA             ", new IARandom(this, 1));
			}
			//players[0] = new Player(0, "IA             ", new IACross(this, 1));
			//joueurs[0] = new Player(0, new Human());
			//boards[0].setJoueur(players[0]);
		}

		if(multiplayer){
			//gameIsRunning = false;
			//boatsAreAllPlaced = false;
			//Log.getInstance().addLog("Faites un clic droit pour alterner entre le \nplacement vertical et horizontal.", true);
			//boards[1].logPlaceNextBoat();

			if(automaticArrangement){
				boards[0].automaticArrangement();
			}
		}else{
			boards[0].automaticArrangement();
		}

		if(automaticArrangement){
			boards[1].automaticArrangement();
			boatsAreAllPlaced = true;
			gameIsRunning = true;
		}else{
			gameIsRunning = false;
			boatsAreAllPlaced = false;
			Log.getInstance().addLog("Faites un clic droit pour alterner entre le \nplacement vertical et horizontal.", true);
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
			//Log.getInstance().addLog("Joueur "+joueurEnCours+"> Tir en "+a+","+o);
			int i = ( joueur == 0 ? 1 : 0);
			Log.getInstance().addLog(players[i].getName()+"> Tir en "+a+","+o, true);
			if(boards[joueur].getSquares()[a][o].shootHere() == true) {
				boolean verif = false;

				for(Boat boat : boards[joueur].getBateaux()) {
					if(!boat.hasSunk()) {
						verif = true; 
						break;
					}
				}
				if(verif == false) {
					//System.out.println("Le joueur "+joueur+" a perdu");
					Log.getInstance().addLog(players[joueur].getName()+" a perdu !", true);
					gameIsRunning = false;
				}
			}

			//logTurn();

			joueurEnCours = (joueurEnCours == 0 ? 1 : 0);
			setChanged();
			notifyObservers();
			players[joueurEnCours].tirer();
		}

	}

	public void placeBoat(int a, int o, boolean horizontal){
		if(!gameIsRunning && !boatsAreAllPlaced){
			if(boards[1].placeBoat(a, o, horizontal)){
				if(boards[1].areBoatsAllPlaced() && multiplayer){
					Log.getInstance().addLog(players[1].getName()+" a place tous ses bateaux.", true);
				}else{
					boards[1].logPlaceNextBoat();
				}

				if(boards[1].areBoatsAllPlaced() && boards[0].areBoatsAllPlaced()){
					boatsAreAllPlaced = true;
					gameIsRunning = true;
					Log.getInstance().addLog("La partie peut commencer !", true);
				}
			}
			setChanged();
			notifyObservers();
		}

	}
	
	/*public void logTurn() {
		if(gameIsRunning) {
			System.out.println(joueurEnCours);
			if(joueurEnCours==1) {

				
				Log.getInstance().setTemporaryMessage("A votre tour de jouer"+ ++ppp);
			}else {
				Log.getInstance().setTemporaryMessage("En attente du coup de votre adversaire");
			}

		}else {
			System.out.println("zzz");
				Log.getInstance().setTemporaryMessage(" ");
		}
	}*/

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
