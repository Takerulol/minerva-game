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

	private Game game = null;
	private BufferedReader console = null;
	
	/**
	 * DOCME
	 * 
	 */
	public MinervaCUI() {
		this.console = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * DOCME
	 * 
	 */
	public void run() {
int i = 0;
		this.createGame();

		do {
			this.outln("Neue Runde beginnt ...");

			Turn turn = game.nextTurn();			
			this.outln(turn.getCurrentPlayer().getUsername() + " ist dran ...");
			this.outln(turn.getCurrentPlayer().getUsername() + " hat " + turn.getAllocatableArmyCount() + " Einheiten bekommen, die er jetzt verteilen muss.");

			// Step 1: Allocate new armies.
			for (int x = 0; x < turn.getAllocatableArmyCount(); x++) {				
				this.printCountryList();

				this.out((x+1)+ ". Einheit setzen. Eingabe der Land-Id: ");
				Country country = this.game.getWorld().getCountry(this.readInt());
				
				turn.allocateArmy(country);
				// TODO: Handle "CountryOwnershipException".
			}

i++;
		} while (i < 3); //!game.isFinished());
	}
	
	/**
	 * DOCME
	 * 
	 * @return
	 */
	private void createGame() {		
		this.out("Spieleranzahl? ");

		Vector<Player> player = this.createPlayers(this.readInt());
		World world = null;

		try {
			world = this.createWorld();
		} catch (PersistenceIOException e) {
			this.out("[FEHLER] Auswahl der Welt nicht möglich. Grund: "+e.getMessage());
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
			this.out("Bitte geben Sie den Namen für Spieler "+(i+1)+" ein: ");
			
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

		this.out("Auf welcher Welt möchten Sie spielen. Es stehen folgende zur Auswahl: \n");
		int i = 1;
		Vector<World> worlds = WorldService.getInstance().loadAll();
		
		for (World oneWorld : worlds) {
			this.out("["+i+"]" + ": " + oneWorld.getName()+"\n");
			i++;
		}
		
		this.out("Auswahl: ");
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
	 */
	private void printCountryList() {
		this.outln("Länder =======================");
		int i = 0;

		for (Country country : this.game.getWorld().getCountries()) {
			this.outln("[Id: "+i+ " - Name: "+country.getName() + " - Anzahl der Einheiten: "+country.getArmyCount());
			i++;
		}
		this.outln("==============================");
	}
	
}
