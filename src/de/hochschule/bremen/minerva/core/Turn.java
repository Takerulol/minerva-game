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
import java.util.logging.Logger;


import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.util.Die;
import de.hochschule.bremen.minerva.vo.Army;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;


public class Turn {
	
	private static Logger LOGGER = Logger.getLogger(Turn.class.getName());
	
	private World world = null;
	private Vector<Player> players = null;
	private Player currentPlayer = null;
	private Vector<Army> allocatableArmies = null;
	private Vector<AttackResult> attackResults = null;
	
	/**
	 * Constructs a new turn.
	 * 
	 * @param currentPlayer
	 * @param world
	 * @param players
	 */
	public Turn(Player currentPlayer, World world, Vector<Player> players) {
		this.setWorld(world);
		this.setCurrentPlayer(currentPlayer);
		this.setPlayers(players);
		this.setAllocatableArmies(this.createArmies(this.currentPlayer));
		this.attackResults = new Vector<AttackResult>();
	}
	
	//TODO: Regelabfrage, ob ganzer Kontinent besetzt ist.
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

		if (armyGet < 3) {
			armyGet = 3;
		}

		for (int i = 0; i < armyGet; i++) {
			newArmies.add(new Army());
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
	 * Attacker country attacks defender country and chooses how many armies he uses.
	 * Returns int[3] Array:
	 * 	- first value: lost attacker armies
	 *  - second value: lost defender armies
	 *  - third value: 0 = attacker lost, 1 = attacker won the country
	 * 
	 * @param attacker
	 * @param defender
	 * @param armyCount
	 * @return
	 * @throws CountriesNotInRelationException
	 */
	public void attack(Country attacker, Country defender, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException {

		if (this.world.getCountryGraph().neighbours(attacker, defender)) {
			if ((armyCount <= 3) && (armyCount>0) && (currentPlayer.hasCountry(attacker)) && (!currentPlayer.hasCountry(defender))) {
				
				//Exception for not enough armies on the attacker country
				if (!(armyCount <= attacker.getArmyCount())) {
					throw new NotEnoughArmiesException("There are not enough armies to attack.");
				}
				
				Vector<Die> attackerDice = new Vector<Die>();
				Vector<Die> defenderDice = new Vector<Die>();
				int defenderCount = this.calcMaxDefenderCount(defender);
				int[] lostArmies = {0,0};
				boolean won = false;
				
				
				// Attacker and defender roll as much dice as they are allowed to.
				for (int i = 0; i < armyCount; i++) {
					Die die = new Die();
					die.dice();
					attackerDice.add(die);
				}
				for (int i = 0; i < defenderCount; i++) {
					Die die = new Die();
					die.dice();
					defenderDice.add(die);
				}
				
				// Dice are compared and armies removed.
				int def = defenderDice.size();
				for (int i = 0; i < def; i++) {
					Die highestAttacker = Die.getLargest(attackerDice);
					Die highestDefender = Die.getLargest(defenderDice);
					if (highestAttacker.getNumber() > highestDefender.getNumber()) {
						defender.removeArmy();
						lostArmies[1]++;
					} else {
						attacker.removeArmy();
						lostArmies[0]++;
					}
					attackerDice.remove(highestAttacker);
					defenderDice.remove(highestDefender);
				}
				
				// If attacker won, he gets the country and moves his attacking armies there.
				if (defender.getArmies().isEmpty()) {
					Player loser = findPlayerToCountry(defender);
					loser.removeCountry(defender);
					currentPlayer.addCountry(defender);
					for (int i = 0; i < armyCount-lostArmies[0]; i++) {
						defender.addArmy();
						attacker.removeArmy();
					}
					won = true;
				}
				
				
				//Creating new AttackResult
				this.attackResults.add(new AttackResult(currentPlayer,findPlayerToCountry(defender), lostArmies[0], lostArmies[1], won));
				
			}
			
		} else {
			throw new CountriesNotInRelationException("Countries are not connected.");
		}
	}
	
	/**
	 * Moves specific amount of armies from one to another, related country.
	 * 
	 * @param from
	 * @param destination
	 * @param armyCount
	 * @throws CountriesNotInRelationException
	 */
	public void moveArmies(Country from, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException {
		if ((currentPlayer.hasCountry(from)) && (currentPlayer.hasCountry(destination))) {
			
			//Exception for not enough armies on the country to be moved from
			if (!(world.getCountryGraph().neighbours(from, destination))) {
				throw new CountriesNotInRelationException("Countries are not connected.");
			}
			if (from.getArmyCount() <= armyCount) {
				throw new NotEnoughArmiesException("There are not enough armies to move.");
			}
			
			//actually moving armies
			for (int i = 0; i < armyCount; i++) {
				from.removeArmy();
				Army newArmy = new Army();
				newArmy.moved(true);
				destination.addArmy(newArmy);
			}
		}
	}
	
	
	/**
	 * Sets current player.
	 * 
	 * @param currentPlayer
	 */
	private void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	/**
	 * Returns current player.
	 * 
	 * @return
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * Sets world.
	 * 
	 * @param world
	 */
	private void setWorld(World world) {
		this.world = world;
	}
	/**
	 * Returns world.
	 * 
	 * @return
	 */
	public World getWorld() {
		return world;
	}
	/**
	 * Sets players. (Vector)
	 * 
	 * @param players
	 */
	private void setPlayers(Vector<Player> players) {
		this.players = players;
	}
	/**
	 * Returns players. (Vector)
	 * 
	 * @return
	 */
	public Vector<Player> getPlayers() {
		return players;
	}
	/**
	 * Sets allocatable armies.
	 * 
	 * @param allocatableArmies
	 */
	private void setAllocatableArmies(Vector<Army> allocatableArmies) {
		this.allocatableArmies = allocatableArmies;
	}
	/**
	 * Returns allocatable armies.
	 * 
	 * @return
	 */
	private Vector<Army> getAllocatableArmies() {
		return allocatableArmies;
	}
	
	/**
	 * Gets allocatable army-count.
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
		// TODO: verhaeltnis angreifer/verteitiger
		if (defender.getArmyCount() > 1) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * DOCME
	 * 
	 * @param country
	 * @return
	 */
	public int calcMaxAttackCount(Country country) {
		int armyCount = country.getArmyCount();

		if (armyCount > 3) {
			return 3;
		} else {
			return armyCount-1;
		}
	}
	
	/**
	 * Finds the owner of a country.
	 * 
	 * @param country
	 * @return
	 */
	public Player findPlayerToCountry(Country country) { // private
		for (Player player : players) {
			if (player.hasCountry(country)) {
				return player;
			}
		}
		return null;
	}

	public Vector<AttackResult> getAttackResults() {
		return attackResults;
	}
}
