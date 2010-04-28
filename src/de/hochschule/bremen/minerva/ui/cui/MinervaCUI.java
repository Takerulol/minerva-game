/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: UserInterface.java 117 2010-04-22 18:19:46Z cbollmann@stud.hs-bremen.de $
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.hochschule.bremen.minerva.core.Game;
import de.hochschule.bremen.minerva.core.Turn;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;
import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.service.ContinentService;
import de.hochschule.bremen.minerva.persistence.service.CountryService;
import de.hochschule.bremen.minerva.persistence.service.NeighbourService;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
import de.hochschule.bremen.minerva.ui.UserInterface;

public class MinervaCUI implements UserInterface {
	
	private static Logger LOGGER = Logger.getLogger(MinervaCUI.class.getName());
	
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
		this.createGame();

		do {
			this.outln(true, "## Neue Runde ##");

			Turn turn = game.nextTurn();			
			this.outln(true, "Spieler '"+turn.getCurrentPlayer().getUsername() + "' ist am Zug und hat " + turn.getAllocatableArmyCount() + " Einheiten bekommen, die verteilt werden müssen.");
			
			// Step 1: Allocate new armies.
			//LOGGER.log(Level.INFO, ""+turn.getAllocatableArmyCount());
			int armyCount = turn.getAllocatableArmyCount();
			for (int x = 0; x < armyCount; x++) {				
				this.printCountryList();

				this.outln("["+ turn.getCurrentPlayer().getUsername() +"]: "+ (x+1)+ ". Einheit setzen. Eingabe der [Id] des Landes: ");
				Country country = this.game.getWorld().getCountry(this.readInt());
				//LOGGER.log(Level.INFO, ""+x);
				turn.allocateArmy(country);
				// TODO: Handle "CountryOwnershipException".
			}

		} while (!game.isFinished());
	}
	
	/**
	 * DOCME
	 * 
	 * @return
	 */
	private void createGame() {
		this.outln("## Initialisierung des Spiels ##");
		this.outln(true, "- Mit wie vielen Spieler möchten Sie spielen? ");

		Vector<Player> player = this.createPlayers(this.readInt());
		World world = null;

		try {
			world = this.createWorld();
		} catch (PersistenceIOException e) {
			this.error("[FEHLER] Auswahl der Welt nicht möglich. Grund: "+e.getMessage());
			throw new RuntimeException(e);
		}

		this.game = new Game(world, player);
	}

	/**
	 * DOCME
	 * 
	 * @param playerCount
	 * @return
	 */
	private Vector<Player> createPlayers(int playerCount) {
		Vector<Player> player = new Vector<Player>();
		for (int i = 0; i < playerCount; i++) {
			this.outln(true, "- Bitte geben Sie den Namen des "+(i+1)+". Spielers ein: ");
			
			Player newPlayer = new Player();
			newPlayer.setUsername(this.readString());
			player.add(newPlayer);
		}
		return player;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 * @throws PersistenceIOException
	 */
	private World createWorld() throws PersistenceIOException {
		World world = null;

		this.outln(true, "- Auf welcher Welt möchten Sie spielen. Es stehen folgende zur Auswahl (kleinen Moment, bitte ...): \n");
		Vector<World> worlds = WorldService.getInstance().loadAll();
		
		int i = 1;
		for (World oneWorld : worlds) {
			this.out("["+i+"]" + ": " + oneWorld.getName()+"\n");
			i++;
		}
		
		this.outln("");
		world = worlds.get(this.readInt() - 1);

		Vector<Country> countries = CountryService.getInstance().loadAll(world);

		for (Country country : countries) {
			country.setContinent(ContinentService.getInstance().load(country.getContinent().getId()));

			for (Neighbour neighbour : NeighbourService.getInstance().loadAll(country)) {
				world.getCountryGraph().connect(country, neighbour);
			}
		}
		world.setCountries(countries);
		
		return world;
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
		System.err.println(message);
	}
	
	/**
	 * DOCME
	 */
	private void printCountryList() {
		this.outln("\n-- Länderinformationen:");
		int i = 0;

		for (Country country : this.game.getWorld().getCountries()) {
			this.outln("["+i+"]: Name: "+country.getName() + " - Einheiten: "+country.getArmyCount());
			i++;
		}
		this.outln("-- \n");
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
