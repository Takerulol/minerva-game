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

import de.hochschule.bremen.minerva.core.AttackResult;
import de.hochschule.bremen.minerva.core.Game;
import de.hochschule.bremen.minerva.core.Turn;
import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.exceptions.WorldDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.vo.CavalerieCard;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Mission;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.SoldierCard;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.ui.UserInterface;
import de.hochschule.bremen.minerva.util.Die;

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
		
		try {
			ApplicationConfigurationManager.setup();

			int input = this.menue();

			switch (input) {
			
			// Import new world from world import file.
			case 1:
				this.importWorld();
				this.run();
				break;
				
				// Print the world list
			case 2:
				this.printWorldList();
				this.run();
				break;
				
				// Start a new game
			case 3:
				this.startGame();
				break;
				
			default:
				this.error("Ungültige Eingabe ...");
				this.run();
				break;
			}
		} catch (AppConfigurationNotFoundException e) {
			this.error(e.getMessage());
		} catch (AppConfigurationNotReadableException e) {
			this.error(e.getMessage());
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
		this.outln("[2] Alle Welten anzeigen.");
		this.outln("[3] Neues Spiel starten.");
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
				this.outln(true, "Die Welt wurde erfolgreich importiert ...");
			} catch (WorldFileNotFoundException e) {
				this.error(e.getMessage());
				this.importWorld(false);
			} catch (WorldFileExtensionException e) {
				this.error(e.getMessage());
				this.importWorld(false);
			} catch (WorldFileParseException e) {
				this.error(e.getMessage());
				this.importWorld(false);
			} catch (DataAccessException e) {
				this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten: "+e.getMessage());
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
		try {
			players = AccountManager.getInstance().getPlayerList(true);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		
		this.initPlayers(players);

		// World initialization
		World selectedWorld = null;
		
		try {
			selectedWorld = this.initWorld();
			this.game = new Game(selectedWorld, players);

			this.outln(true, "## Missionen ##");
			this.outln();

			for (Mission mission : game.getMissions()) {
				this.outln("- "+mission.getOwner().getUsername()+", du hast die Mission »"+mission.getTitle()+"« ("+mission.getDescription()+") erhalten. Viel Erfolg ;)");
			}

			do {
				this.outln(true, "## Neue Runde ##");
				
				Turn turn = game.nextTurn();			
				this.outln(true, "Spieler »"+turn.getCurrentPlayer().getUsername() + "« ist am Zug und hat " + turn.getAllocatableArmyCount() + " Einheiten bekommen, die verteilt werden müssen.");
				
				this.outln(true, "### Verteilung der Einheiten ###");
				
				// Step 0: Turn cards in
				this.turnCardsIn(turn);
				
				// Step 1: Allocate new armies
				this.allocateNewArmies(turn);
				
				// Step 2: Attack
				this.attack(turn);

				// Step 3: Relocate your armies
				this.moveArmies(turn);
			} while (!game.isFinished());

			Player champ = this.game.getWinner();
			this.outln("--");
			this.outln("Das Spiel wurde beendet.");
			
			this.outln("Der Gewinner ist: "+ champ.getFirstName() + " " + champ.getLastName() + ". Herzlichen Glückwunsch!");

		} catch (WorldDoesNotExistException e) {
			this.error(e.getMessage());
		} catch (NoPlayerLoggedInException e) {
			this.error(e.getMessage());
		} catch (NotEnoughPlayersLoggedInException e) {
			this.error(e.getMessage());
		} catch (DataAccessException e) {
			this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten. Beende die Anwendung. Grund: "+e.getMessage());
		}
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
		this.outln("[4] Eingeloggte Spieler anzeigen");
		this.outln("[5] Alle Spieler ausloggen");
		this.outln("[6] Initialisierung beenden");
		this.outln();
		this.outln("Derzeit sind "+players.size()+" Spieler eingeloggt.");

		int input = this.readInt();

		switch (input) {
			case 1:
				Player player = this.loginPlayer();
				if (player.isLoggedIn()) {
					players.add(player);
				}
				this.initPlayers(players);
			break;

			case 2:
				this.registerPlayer();
				this.initPlayers(players);
			break;
			
			case 3:
				this.printRegisteredUsers(false);
				this.initPlayers(players);
			break;

			case 5:
			try {
				AccountManager.getInstance().logout();
				this.outln(true, "Es wurden alle Spieler ausgeloggt.");
				this.initPlayers(new Vector<Player>());
			} catch (DataAccessException e) {
				this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten: "+e.getMessage());
				Runtime.getRuntime().exit(0);
			}
			break;
			
			case 4:
				this.printRegisteredUsers(true);
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
			this.outln("Hallo "+player.getFirstName()+", schön dich zu sehen :)");
		} catch (WrongPasswordException e) {
			this.error(e.getMessage());
			return this.loginPlayer();
		} catch (PlayerDoesNotExistException e) {
			this.error(e.getMessage());
			return this.loginPlayer();
		} catch (DataAccessException e) {
			this.error("Allgemeiner Persistenzfehler: "+e.getMessage());
			Runtime.getRuntime().exit(0);
		} catch (PlayerAlreadyLoggedInException e) {
			this.error(e.getMessage());
		}

		return player;
	}
	
	/**
	 * DOCME
	 * 
	 * @return
	 */
	private void registerPlayer() {
		this.outln(true, "#### Registrierung ####");
		this.outln("");

		Player player = new Player();

		this.outln("- Bitte geben Sie den Benutzernamen ein: ");
		player.setUsername(this.readString());
		this.outln();

		this.outln("- Bitte geben Sie das Passwort des Benutzers ein: ");
		String password = this.readString();
		player.setPassword(password);
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
			this.outln("Der Spieler '"+player.getUsername()+"' wurde registriert.");
		} catch (PlayerExistsException e) {
			this.error("Der eingegebene Spieler existiert bereits. Legen Sie bitte einen neuen an (anderer Benutzername/E-Mail).");
			this.registerPlayer();
		} catch (DataAccessException e) {
			this.error("Allgemeiner Persistenzfehler: "+e.getMessage());
			Runtime.getRuntime().exit(0);
		}
	}

	/**
	 * DOCME
	 * @throws DataAccessException 
	 * @throws WorldDoesNotExistException 
	 * 
	 */
	private World initWorld() throws WorldDoesNotExistException, DataAccessException {
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
		
		World world = turn.getWorld();
		
		// Step 3: Relocate your armies!
		if (answer) {
			boolean nextMove = false;
			do {
				//
				this.printCountryList();

				// We need to get the reference because there is no possibility
				// for getting a country by vector index (see depreciated method in world).
				// So this problem is not relevant in the GUI (see world.getCountry(byColor).
				Vector<Country> countries = world.getCountries();
				
				// Choose own country
				this.outln(true, "- Wählen Sie das Ausgangsland: ");
				Country source = countries.get(this.readInt());
				
				// Choose second own country
				this.outln(true, "- Wählen Sie das Ziel-Land: ");
				Country destination = countries.get(this.readInt());
				
				// Choose army-count to move (one must remain)
				this.outln(true, "- Bitte geben Sie die Anzahl der Einheiten an, die verschoben werden sollen (max: "+(source.getArmyCount()-1)+"): ");
				int movingArmies = this.readInt();
				
				// relocation
				try {
					turn.moveArmies(source, destination, movingArmies);
				} catch (CountriesNotInRelationException e) {
					this.outln();
					this.error(e.getMessage());
				} catch (NotEnoughArmiesException e) {
					this.outln();
					this.error(e.getMessage());
				} catch (CountryOwnerException e) {
					this.outln();
					this.error(e.getMessage());
				}

				this.printCountryList();

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
		AttackResult result = null;

		this.outln(true, "### Angriff ###");
		
		this.outln(true, "- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie angreifen [J/N]?");
		boolean answer = this.readBoolean();
		
		World world = turn.getWorld();
		
		// Step 2: Attack!
		if (answer) {
			boolean nextAttack = false;
			do {
				this.printCountryList();

				// We need to get the reference because there is no possibility
				// for getting a country by vector index (see depreciated method in world).
				// So this problem is not relevant in the GUI (see world.getCountry(byColor).
				Vector<Country> countries = world.getCountries();
				
				// Choose own country
				this.outln(true, "- Wählen Sie ihr Land, von dem Sie angreifen möchten: ");
				Country attacker = countries.get(this.readInt());
				
				// Choose enemy country
				this.outln(true, "- Wählen Sie ein Land, das Sie angreifen möchten: ");
				Country defender = countries.get(this.readInt());
				
				// Choose 1 to max 3 armies to attack
				this.outln(true, "- Bitte geben Sie die Anzahl der angreifenden Einheiten ein (max: "+turn.calcMaxAttackCount(attacker)+"): ");
				int attackingArmies = this.readInt();
				
				// Attack
				try {
					result = turn.attack(attacker, defender, attackingArmies);

					this.outln(true, "#### Resultat des Angriffs ####");
					this.outln(true, result.getAttacker().getUsername() + " hat " + result.getDefender().getUsername() + " angegriffen.");
					this.outln(result.getAttacker().getUsername() + " hat dabei " + result.getLostAttackerArmies() + " Einheiten verloren.");
					this.outln(result.getDefender().getUsername() + " hat dabei " + result.getLostDefenderArmies() + " Einheiten verloren.");

					this.out(result.getAttacker().getUsername() + " hat folgende Augenzahlen gewürfelt: ");
					for (Die die : result.getAttackerDice()) {
						this.out(String.valueOf(die.getRollResult())+ " ");
					}
					this.outln();

					this.out(result.getDefender().getUsername() + " hat folgende Augenzahlen gewürfelt: ");
					for (Die die : result.getDefenderDice()) {
						this.out(String.valueOf(die.getRollResult()) + " ");
					}

					if (result.isWin()) {
						this.outln();
						this.outln(true, "Glückwunsch " + result.getAttacker().getUsername() + ", du hast diesen Angriff gewonnen.");
					} else {
						this.outln();
						this.outln(true, "Cool, "+result.getDefender().getUsername() + ". Du hast dein Land erfolgreich verteidigen können.");
					}

				} catch (CountriesNotInRelationException e) {
					this.error(e.getMessage());
				} catch (NotEnoughArmiesException e) {
					this.error(e.getMessage());
				} catch (IsOwnCountryException e) {
					this.error(e.getMessage());
				}

				this.printCountryList();
				
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

		World world = turn.getWorld();
		Player currentPlayer = turn.getCurrentPlayer();
		
		// We need to get the reference because there is no possibility
		// for getting a country by vector index (see depreciated method in world).
		// So this problem is not relevant in the GUI (see world.getCountry(byColor).
		Vector<Country> countries = world.getCountries();
		
		for (int x = 0; x < armyCount; x++) {				
			this.printCountryList();

			this.outln("["+ currentPlayer.getUsername() +"]: "+ (x+1)+ ". Einheit setzen. Eingabe der [Id] des Landes: ");
			Country country = world.getCountry(countries.get(this.readInt()));

			try {
				turn.allocateArmy(country);
			} catch (NotEnoughArmiesException e) {
				this.error(e.getMessage());
				x--; // Not so nice. But there is no other solution. It's the CUI. No problem in the GUI.
			} catch (CountryOwnerException e) {
				this.error(e.getMessage());
				x--; // Not so nice. But there is no other solution. It's the CUI. No problem in the GUI.
			}
		}
	}
	
	private void turnCardsIn(Turn turn) {
		boolean turnIn = true;
		int oldArmyCount = turn.getAllocatableArmyCount();
		Vector<CountryCard> cards = turn.getCurrentPlayer().getCountryCards();
		
		this.outln(true, "#### Resultat des Angriffs ####");
		this.outln();
		
		if (cards.isEmpty()) {
			this.outln("Sie haben keine Karten.");
			turnIn = false;
		} else {
			this.outln("Sie besitzen folgende Karten:");
			this.printCardList(turn);
			
			this.outln(true, "- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie Karten eintauschen [J/N]?");
			turnIn = this.readBoolean();
		}
		
		if (turnIn) {
			boolean cardTurnIn = true;
			
			do {
				//show cards
				this.outln("Sie besitzen folgende Karten:");
				this.printCardList(turn);
				
				//actual card turn in
				if (cards.size() == 5) {
					this.outln("Sie haben 5 Länderkarten, es wird zwangsweise eine Serie abgegeben.");
					turn.releaseCardSeries();
				} else {
					
					this.outln(true, "Bitte wählen sie:");
					this.outln("[0] Serie eintauschen");
					this.outln("[1] Karte eintauschen");
					int choise = this.readInt();
					
					if (choise == 0) {
						if (cards.size() >= 3) {
							Vector<CountryCard> series = new Vector<CountryCard>();
							//TODO: check index
							
							this.outln(true, "Erste Karte: ");
							int first = this.readInt();
							this.outln("Zweite Karte: ");
							int second = this.readInt();
							this.outln("Dritte Karte: ");
							int third = this.readInt();
							
							series.add(cards.elementAt(first));
							series.add(cards.elementAt(second));
							series.add(cards.elementAt(third));
							
							turn.releaseCardSeries(series);
							
							if (!(oldArmyCount < turn.getAllocatableArmyCount())) {
								this.error("Falsche Eingabe.");
							}
						} else {
							this.error("Es sind nicht genug Karten für eine Serie vorhanden.");
						}
						
					} else if (choise == 1) {
						this.outln(true,"Bitte wählen sie eine Karte: ");
						int cardChoise = this.readInt();
						if (turn.getCurrentPlayer().hasCountry(cards.elementAt(cardChoise).getReference())) {
							turn.releaseCard(cards.elementAt(cardChoise));
						} else {
							this.error("Sie besitzen das dazugehörige Land nicht.");
						}
							
					} else {
						this.error("Falsche Eingabe.");
					}
				}
				
				if (oldArmyCount < turn.getAllocatableArmyCount()) {
					this.outln("Sie haben "+(turn.getAllocatableArmyCount()-oldArmyCount)+" neue Armeen erhalten.");
				}
				//again ? and card stack empty
				if (cards.isEmpty()) {
					this.outln("Sie haben nun keine Karten mehr.");
					cardTurnIn = false;
				} else {
					this.outln("- '"+turn.getCurrentPlayer().getUsername()+"' möchten Sie weiter Karten eintauschen [J/N]?");
					cardTurnIn = this.readBoolean();
					if (cardTurnIn) {
						this.outln(true,"Sie besitzen folgende Karten:");
						this.printCardList(turn);
					}
				}
			} while (cardTurnIn);
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
			this.error("Beim Lesen der Eingabe. Grund: "+e.getMessage());
			Runtime.getRuntime().exit(0);
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
			
			this.outln("["+i+"]: Name: "+country.getName() + " - Einheiten: "+country.getArmyCount() + ((hasCountry) ? " - Von dir besetzt!" : ""));
			i++;
		}
		this.outln("-- \n");
	}

	/**
	 * DOCME 
	 * 
	 */
	private void printRegisteredUsers(boolean loggedInUsers) {
		this.outln();
		try {
			Vector<Player> players = AccountManager.getInstance().getPlayerList(loggedInUsers);
			if (players.size() > 0) {
				for (Player player : players) {
					this.outln(player.getLastName() + ", "+player.getFirstName() + " - "+player.getUsername() + " - "+player.getEmail());
				}
			} else {
				this.outln("Keine Spieler gefunden ...");
			}
		} catch (DataAccessException e) {
			this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten. Grund: "+ e.getMessage());
			Runtime.getRuntime().exit(0);
		}
	}

	/**
	 * DOCME
	 * 
	 */
	private void printWorldList() {
		this.outln();
		try {
			for (World world : WorldManager.getInstance().getList(true)) {
				this.outln(world.getName() + " - " + world.getDescription());
			}
		} catch (DataAccessException e) {
			this.error("Es ist ein allgemeiner Persistenzfehler aufgetreten. Grund: "+ e.getMessage());
			Runtime.getRuntime().exit(0);
		}
	}
	
	/**
	 * DOCME
	 * @param turn
	 */
	private void printCardList(Turn turn) {
		this.outln();
		int i = 0;
		for (CountryCard card : turn.getCurrentPlayer().getCountryCards()) {
			String name;
			if (card instanceof SoldierCard) {
				name = "Soldier Card";
			} else if (card instanceof CavalerieCard) {
				name = "Cavalerie Card";
			} else {
				name = "Canon Card";
			}
			
			this.outln("["+i+"] Symbol: "+name+", Land der Karte: "
					+card.getReference().getName()+", Ihr Land: "
					+(turn.getCurrentPlayer().hasCountry(card.getReference()) ? "Ja" : "Nein"));
			i++;
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