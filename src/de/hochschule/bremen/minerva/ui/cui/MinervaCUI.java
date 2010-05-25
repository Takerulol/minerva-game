/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id$
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Christian Bollmann: cbollmann@stud.hs-bremen.de
 *     Carina Strempel: cstrempel@stud.hs-bremen.de
 *     André König: akoenig@stud.hs-bremen.de
 * 
 * Web:
 *     http://minerva.idira.de
 * 
 */
package de.hochschule.bremen.minerva.ui.cui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import de.hochschule.bremen.minerva.core.Game;
import de.hochschule.bremen.minerva.core.Turn;
import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.exceptions.WorldNotFoundException;
import de.hochschule.bremen.minerva.ui.UserInterface;

public class MinervaCUI implements UserInterface {

	private Game game = null;
	private BufferedReader console = null;

	/**
	 * DOCME
	 * 
	 */
	public MinervaCUI() {
		this.console = new BufferedReader(new InputStreamReader(System.in));
		this.printWelcome();
	}

	/**
	 * DOCME
	 * 
	 */
	public void run() {
		int input = this.menue();
		
		switch (input) {

			// Import new world from world import file.
			case 1:
				this.importWorld();
				this.run();
			break;

			// Start a new game
			case 2:
				this.startGame();
			break;
			
			default:
				this.error("Ungültige Eingabe ...");
				this.run();
			break;
		}
	}

	/**
	 * Prints the top menue
	 * 
	 * @return int - The selected menue item
	 */
	private int menue() {
		this.outln(true, "# Menü #");
		this.outln(true, "[1] Eine neue Welt aus einer *.world-Datei importieren.");
		this.outln("[2] Neues Spiel starten.");
		this.outln("");
		return this.readInt();
	}

	/**
	 * World import user interaction.
	 * 
	 */
	private void importWorld() {
		this.importWorld(true);
	}

	/**
	 * World import user interaction.
	 * 
	 * @param showHeadline
	 */
	private void importWorld(boolean showHeadline) {
		if (showHeadline) {
			this.outln(true, "## Importieren einer neuen Welt aus einer *.world-Datei. ##");
		}
		
		this.outln(true, "Bitte geben Sie die 'WorldImport-Datei' (inkl. des kompletten Pfades) ein (Abbruch durch Eingabe von  'x'): ");
		
		String input = this.readString();
		
		if (!input.equals("x")) {
			try {
				WorldManager.getInstance().store(new File(input));
			} catch (WorldFileNotFoundException e) {
				this.error("Die angegebene WorldImport-Datei wurde nicht gefunden. Bitte überprüfen Sie den Pfad.");
				this.importWorld(false);
			} catch (WorldFileExtensionException e) {
				this.error("Die angegebene WorldImport-Datei besitzt nicht die richtige Dateierweiterung (*.world).");
				this.importWorld(false);
			} catch (WorldFileParseException e) {
				this.error("Die angegebene WorldImport-Datei ist nicht 'wohlgeformt': "+e.getMessage());
				this.importWorld(false);
			} catch (PersistenceIOException e) {
				this.error("Es ist ein allgemeiner Persistierungsfehler aufgetreten: "+e.getMessage());
				Runtime.getRuntime().exit(0);
			}
		}
	}

	/**
	 * Game initialization.
	 * 
	 */
	private void startGame() {
		this.outln(true, "## Initialisierung des Spiels ##");

		// Player initialization
		Vector<Player> players = new Vector<Player>();
		this.initPlayers(players);

		// World initialization
		World selectedWorld = null;
		
		try {
			selectedWorld = this.initWorld();
		} catch (WorldNotFoundException e) {
			this.error("Die ausgewählte Welt wurde nicht gefunden. Grund: "+e.getMessage());
		} catch (PersistenceIOException e) {
			this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten. Beende die Anwendung. Grund: "+e.getMessage());
		}

		this.game = new Game(selectedWorld, players);

		do {
			this.outln(true, "## Neue Runde ##");

			Turn turn = game.nextTurn();			
			this.outln(true, "Spieler '"+turn.getCurrentPlayer().getUsername() + "' ist am Zug und hat " + turn.getAllocatableArmyCount() + " Einheiten bekommen, die verteilt werden müssen.");
			
			this.outln(true, "### Verteilung der Einheiten ###");
			
			// Step 1: Allocate new armies
			this.allocateNewArmies(turn);
			
			// Step 2: Attack
			this.attack(turn);

			// Step 3: Relocate your armies
			this.moveArmies(turn);
			
			
		} while (!game.isFinished());
	}

	/**
	 * DOCME
	 * 
	 * @param players
	 */
	private void initPlayers(Vector<Player> players) {
		this.outln(true, "### Initialisierung der Spieler  ###");

		this.outln(true, "[1] Login");
		this.outln("[2] Registrierung");
		this.outln("[3] Registrierte Spieler anzeigen");
		this.outln("[4] Initialisierung beenden");
		this.outln();

		int input = this.readInt();

		switch (input) {
			case 1:
				players.add(this.loginPlayer());
				this.initPlayers(players);
			break;

			case 2:
				players.add(this.registerPlayer());
				this.initPlayers(players);
			break;
			
			case 3:
				this.printRegisteredUsers();
				this.initPlayers(players);
			break;
		}
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	private Player loginPlayer() {
		this.outln(true, "#### Login ####");

		Player player = new Player();

		this.outln(true, "- Bitte geben Sie den Benutzernamen des Players an: ");
		player.setUsername(this.readString());
		this.outln();
		
		this.outln(true, "- Bitte geben Sie das Passwort ein: ");
		player.setPassword(this.readString());
		this.outln();
		
		try {
			AccountManager.getInstance().login(player);
		} catch (WrongPasswordException e) {
			this.error("Login fehlgeschlagen.");
			return this.loginPlayer();

		} catch (PlayerDoesNotExistException e) {
			this.error("Der eingegebene Spieler existiert nicht.");
			return this.loginPlayer();

		} catch (PersistenceIOException e) {
			this.error("Allgemeiner Persistierungsfehler: "+e.getMessage());
			Runtime.getRuntime().exit(0);
		}

		return player;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	private Player registerPlayer() {
		this.outln(true, "#### Registrierung ####");
		this.outln("");

		Player player = new Player();

		this.outln("- Bitte geben Sie den Benutzernamen ein: ");
		player.setUsername(this.readString());
		this.outln();

		this.outln("- Bitte geben Sie das Passwort des Benutzers ein: ");
		player.setPassword(this.readString());
		this.outln();

		this.outln("- Bitte geben Sie den Namen des Benutzers ein: ");
		player.setLastName(this.readString());
		this.outln();

		this.outln("- Bitte geben Sie den Vornamen des Benutzers ein: ");
		player.setFirstName(this.readString());
		this.outln();

		this.outln("- Bitte geben Sie die E-Mail Adresse des Benutzers ein: ");
		player.setEmail(this.readString());
		this.outln();

		try {
			AccountManager.getInstance().createPlayer(player);
		} catch (PlayerExistsException e) {
			this.error("Der eingegebene Spieler existiert bereits. Legen Sie bitte einen neuen an (anderer Benutzername/E-Mail).");
			return this.loginPlayer();
		} catch (PersistenceIOException e) {
			this.error("Allgemeiner Persistierungsfehler: "+e.getMessage());
			Runtime.getRuntime().exit(0);
		}
		
		
		return player;
	}

	/**
	 * DOCME
	 * @throws PersistenceIOException 
	 * @throws WorldNotFoundException 
	 * 
	 */
	private World initWorld() throws WorldNotFoundException, PersistenceIOException {
		this.outln(true, "### Initialisierung der Welt ###");

		Vector<World> worlds = WorldManager.getInstance().getList(true);

		this.outln(true, "- Auf welcher Welt möchten Sie spielen. Es stehen folgende zur Auswahl (kleinen Moment, bitte ...): \n");
		
		// This is code is ugly. We know. But this loop is only for the CUI.
		int i = 1;
		for (World oneWorld : worlds) {
			this.out("["+i+"]" + ": " + oneWorld.getName()+" - "+oneWorld.getDescription()+"\n");
			i++;
		}
		this.outln("");
		// END-UGLY-CODE

		World world = worlds.get(this.readInt() - 1);
		return WorldManager.getInstance().get(world);
	}

	/**
	 * DOCME
	 * 
	 * @param turn
	 */
	private void moveArmies(Turn turn) {
		boolean answer;
		this.outln(true, "### Einheiten verschieben ###");
		this.outln(true, "- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie Einheiten verschieben [J/N]?");
		answer = this.readBoolean();
		
		// Step 3: Relocate your armies!
		if (answer) {
			boolean nextMove = false;
			do {
				//
				this.printCountryList();
				
				// Choose own country
				this.outln(true, "- Wählen Sie das Ausgangsland: ");
				Country source = turn.getWorld().getCountry(this.readInt());
				
				// Choose second own country
				this.outln(true, "- Wählen Sie das Ziel-Land: ");
				Country destination = turn.getWorld().getCountry(this.readInt());
				
				// Choose army-count to move (one must remain)
				this.outln(true, "- Bitte geben Sie die Anzahl der Einheiten an, die verschoben werden sollen (max: "+(source.getArmyCount()-1)+"): ");
				int movingArmies = this.readInt();
				
				// relocation
				try {
					turn.moveArmies(source, destination, movingArmies);
				} catch (CountriesNotInRelationException e) {
					this.error("Länder sind nicht benachbart. Grund: "+e.getMessage());
				} catch (NotEnoughArmiesException e) {
					this.error("Sie haben nicht genug Einheiten.");
				}
				
				this.outln(true, "- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie weiter Einheiten verschieben [J/N]?");
				nextMove = this.readBoolean();
			} while (nextMove);
		}
	}

	/**
	 * DOCME
	 * 
	 * @param turn
	 */
	private void attack(Turn turn) {
		this.outln(true, "### Angriff ###");
		
		this.outln(true, "- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie angreifen [J/N]?");
		boolean answer = this.readBoolean();
		
		// Step 2: Attack!
		if (answer) {
			boolean nextAttack = false;
			do {
				//
				this.printCountryList();
				
				// Choose own country
				this.outln(true, "- Wählen Sie ihr Land, von dem Sie angreifen möchten: ");
				Country attacker = turn.getWorld().getCountry(this.readInt());
				
				// Choose enemy country
				this.outln(true, "- Wählen Sie ein Land, das Sie angreifen möchten: ");
				Country defender = turn.getWorld().getCountry(this.readInt());
				
				// Choose 1 to max 3 armies to attack
				this.outln(true, "- Bitte geben Sie die Anzahl der angreifenden Einheiten ein (max: "+turn.calcMaxAttackCount(attacker)+"): ");
				int attackingArmies = this.readInt();
				
				// Attack
				try {
					turn.attack(attacker, defender, attackingArmies);
				} catch (CountriesNotInRelationException e) {
					this.error("Länder sind nicht benachbart. Grund: "+e.getMessage());
				} catch (NotEnoughArmiesException e) {
					this.error("Sie haben nicht genug Einheiten, um diesen Zug auszuführen");
				}
				
				this.outln("- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie weiter angreifen [J/N]?");
				nextAttack = this.readBoolean();
			} while (nextAttack);
		}
	}

	/**
	 * DOCME
	 * 
	 * @param turn
	 */
	private void allocateNewArmies(Turn turn) {
		int armyCount = turn.getAllocatableArmyCount();

		for (int x = 0; x < armyCount; x++) {				
			this.printCountryList();

			this.outln("["+ turn.getCurrentPlayer().getUsername() +"]: "+ (x+1)+ ". Einheit setzen. Eingabe der [Id] des Landes: ");
			Country country = this.game.getWorld().getCountry(this.readInt());

			turn.allocateArmy(country);
		}
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	private int readInt() {
		return Integer.parseInt(this.read());
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	private String readString() {
		return this.read();
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	private boolean readBoolean() {
		String answer = this.read();
		if (answer.equals("J") || answer.equals("j")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads a line from the console.
	 * 
	 * @return
	 */
	private String read() {
		String line = "";

		try {
			this.out("EINGABE> ");
			line = this.console.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return line;
	}
	
	/**
	 * DOCME
	 * 
	 * @param message
	 */
	private void out(String message) {
		System.out.print(message);
	}

	/**
	 * DOCME
	 * 
	 * @param message
	 */
	private void outln(String message) {
		this.out(message + "\n");
	}

	/**
	 * DOCME
	 * 
	 */
	private void outln() {
		this.outln("");
	}
	
	/**
	 * DOCME
	 * 
	 * @param gap
	 * @param message
	 */
	private void outln(boolean gap, String message) {
		if (gap) {
			this.outln("\n" + message);
		} else {
			this.outln(message);
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @param message
	 */
	private void error(String message) {
		this.outln("[ERROR]: "+message);
		this.outln("");
	}
	
	/**
	 * DOCME
	 */
	private void printCountryList() {
		this.outln("\n-- Länderinformationen:");
		int i = 0;

		for (Country country : this.game.getWorld().getCountries()) {
			
			// WARNING: ugly hack
			Turn turn = this.game.getTurns().lastElement();
			Player currentPlayer = turn.getCurrentPlayer();
			boolean hasCountry = currentPlayer.hasCountry(country);
			
			this.outln("["+i+"]: Name: "+country.getName() + " - Einheiten: "+country.getArmyCount() + ((hasCountry) ? " - Dein Land!" : ""));
			i++;
		}
		this.outln("-- \n");
	}

	/**
	 * DOCME 
	 * 
	 */
	private void printRegisteredUsers() {
		this.outln();
		try {
			for (Player player : AccountManager.getInstance().getPlayerList()) {
				this.outln(player.getLastName() + ", "+player.getFirstName() + " - "+player.getUsername() + " - "+player.getEmail());
			}
		} catch (PersistenceIOException e) {
			this.error(e.getMessage());
			throw new RuntimeException();
		}
	}
	
	/**
	 * DOCME
	 * 
	 */
	private void printWelcome() {
		this.outln("Minerva - The Game (http://minerva.idira.de)\n");
		this.outln("Herzlich Willkommen zu einer Runde Minerva. Lass uns anfangen ...\n");
		this.outln("Autoren:");
		this.outln(true, "  - Carina Strempel <cstrempel@stud.hs-bremen.de>");
		this.outln("  - Christian Bollmann <cbollmann@stud.hs-bremen.de>");
		this.outln("  - André König <akoenig@stud.hs-bremen.de>\n");		
		this.outln("=\n");
	}
}