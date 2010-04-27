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

package de.hochschule.bremen.minerva.core;

import java.util.Vector;


import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.util.Die;
import de.hochschule.bremen.minerva.vo.Army;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;


public class Turn {
	private World world = null;
	private Vector<Player> players = null;
	private Player currentPlayer = null;
	private Vector<Army> allocatableArmies = null;
	
	/**
	 * Constructs a new turn.
	 * 
	 * @param currentPlayer
	 * @param world
	 * @param players
	 */
	public Turn(Player currentPlayer, World world, Vector<Player> players) {
		setWorld(world);
		setCurrentPlayer(currentPlayer);
		setPlayers(players);
		setAllocatableArmies(createArmies(this.currentPlayer));
	}
	
	
	/**
	 * Creates armies for the current player by taking his countryCount / 3.
	 * If its less than 3, the current player gets 3 armies.
	 * 
	 * @param currentPlayer
	 * @return
	 */
	private Vector<Army> createArmies(Player currentPlayer) {
		int armyGet = currentPlayer.getCountryCount() / 3;
		Vector<Army> newArmies = new Vector<Army>();
		if ( armyGet > 3 ) {
			for (int i = 0; i < armyGet; i++ ) {
				newArmies.add(new Army());
			}
		} else {
			for (int i = 0; i < 3; i++ ) {
				newArmies.add(new Army());
			}
		}
		return newArmies;
	}
	
	/**
	 * Allocates a single allocatable army into a country.
	 * 
	 * @param country
	 */
	public void allocateArmy(Country country) {
		if ((currentPlayer.hasCountry(country)) && (getAllocatableArmies().size() > 0)) {
			country.addArmy();
			getAllocatableArmies().remove(getAllocatableArmies().size() - 1);
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @param attacker
	 * @param defender
	 * @param armyCount
	 * @throws CountriesNotInRelationException
	 */
	public void attack(Country attacker, Country defender, int armyCount) throws CountriesNotInRelationException {
		/*
		 * TODO:
		 * 	-angreifen
		 * 	-würfeln
		 * 	-defender berechnen
		 */
		if (this.world.getCountryGraph().neighbours(attacker, defender) && (armyCount <= 3) && (armyCount>0)) {
			Vector<Die> attackerDice = new Vector<Die>();
			Vector<Die> defenderDice = new Vector<Die>();
			int defenderCount = this.calcMaxDefenderCount(defender);
			
			// Attacker and defender roll as much dice as they are allowed to.
			for (int i = 1; i < armyCount; i++) {
				Die die = new Die();
				die.dice();
				attackerDice.add(die);
			}
			for (int i = 1; i < defenderCount; i++) {
				Die die = new Die();
				die.dice();
				defenderDice.add(die);
			}
			
			
			
			
		}
	}
	
	/**
	 * DOCME
	 * 
	 * @param from
	 * @param destination
	 * @param armyCount
	 * @throws CountriesNotInRelationException
	 */
	public void moveArmies(Country from, Country destination, int armyCount) throws CountriesNotInRelationException {
		
	}
	
	
	/**
	 * 
	 * @param currentPlayer
	 */
	private void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	/**
	 * 
	 * @return
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * 
	 * @param world
	 */
	private void setWorld(World world) {
		this.world = world;
	}
	/**
	 * 
	 * @return
	 */
	public World getWorld() {
		return world;
	}
	/**
	 * 
	 * @param players
	 */
	private void setPlayers(Vector<Player> players) {
		this.players = players;
	}
	/**
	 * 
	 * @return
	 */
	public Vector<Player> getPlayers() {
		return players;
	}
	/**
	 * 
	 * @param allocatableArmies
	 */
	private void setAllocatableArmies(Vector<Army> allocatableArmies) {
		this.allocatableArmies = allocatableArmies;
	}
	/**
	 * 
	 * @return
	 */
	private Vector<Army> getAllocatableArmies() {
		return allocatableArmies;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAllocatableArmyCount() {
		return getAllocatableArmies().size();
	}
	
	/**
	 * Calculates maximum number of possible defending armies.
	 * 
	 * @param defender
	 * @return
	 */
	private int calcMaxDefenderCount(Country defender) {
		int count = 0;
		for (Army army : defender.getArmies()) {
			count++;
		}
		if (count > 1) {
			return 2;
		} else {
			return 1;
		}
	}
	
	/**
	 * Finds biggest die in dice-vector.
	 * 
	 * @param dice
	 * @return
	 */
	private Die findLargestDie(Vector<Die> dice) {
		Die output = new Die();
		int largest = 0;
		for (Die die : dice) {
			if (die.getNumber() > largest) {
				largest = die.getNumber();
				output = die;
			}
		}
		return output;
	}
}