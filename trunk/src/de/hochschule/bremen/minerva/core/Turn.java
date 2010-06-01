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
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.util.Die;
import de.hochschule.bremen.minerva.vo.Army;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * This represents a single turn by the current player inside a game/match.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public class Turn {

	private World world = null;
	private Vector<Player> players = null;
	private Player currentPlayer = null;
	private Vector<Army> allocatableArmies = null;
	private Vector<AttackResult> attackResults = null;
	
	/**
	 * Constructs a new turn.
	 * 
	 * @param currentPlayer This is the new current player.
	 * @param world The world object played on.
	 * @param players The player vector inheriting all players to this match.
	 */
	public Turn(Player currentPlayer, World world, Vector<Player> players) {
		this.setWorld(world);
		this.setCurrentPlayer(currentPlayer);
		this.setPlayers(players);
		this.setAllocatableArmies(this.createArmies(this.currentPlayer));
		this.attackResults = new Vector<AttackResult>();
	}
	
	//TODO: Regelabfrage, ob ganzer Kontinent besetzt ist. änderung dass methode nur einmal
	// ausgefuehrt werden darf.
	/**
	 * Creates armies for the current player by taking his countryCount / 3.
	 * If its less than 3, the current player gets 3 armies.
	 * 
	 * @param currentPlayer The current player of this turn.
	 * @return Vector of armies gained per turn.
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
	
	//TODO: möglicherweise Exception wenn keine einheiten mehr verfügbar sind? oder abwaelzen 
	// auf gui, dass naechster schritt automatisch kommt.
	/**
	 * Allocates a single allocatable army into a country.
	 * 
	 * @param country The country where to put an army.
	 */
	public void allocateArmy(Country country) {
		if ((currentPlayer.hasCountry(country)) && (getAllocatableArmies().size() > 0)) {
			country.addArmy();
			getAllocatableArmies().remove(getAllocatableArmies().size() - 1);
		}
	}
	
	/**
	 * Attacker country attacks defender country and chooses how many armies he uses.
	 * 
	 * @param attackerCountry Country where to attack from.
	 * @param defenderCountry Country which will be attacked.
	 * @param armyCount Number of armies used. max: 1 <= armies available <= 3 ; min: 1. Minimum one army must remain on the country.
	 * @throws CountriesNotInRelationException The countries are not connected.
	 * @throws IsOwnCountryException Trying to attack an own country.
	 * @throws NotEnoughArmiesException Too little or too many armies used.
	 */
	public void attack(Country attackerCountry, Country defenderCountry, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {

		if (this.world.areNeighbours(attackerCountry, defenderCountry)) {
			
			//Exception for attacking an own country
			if (currentPlayer.hasCountry(defenderCountry)) {
				throw new IsOwnCountryException("The owner of the denfending country is " +
													"the attacker himself.");
			}
			
			if ((armyCount <= 3) && (armyCount>0) && (currentPlayer.hasCountry(attackerCountry))) {
				
				//Exception for not enough armies on the attacker country
				if (!(armyCount <= attackerCountry.getArmyCount())) {
					throw new NotEnoughArmiesException("There are not enough armies to attack.");
				}
				
				
				
				
				Vector<Die> attackerDice = new Vector<Die>();
				Vector<Die> defenderDice = new Vector<Die>();
				int defenderCount = this.calcMaxDefenderCount(defenderCountry, armyCount);
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
						defenderCountry.removeArmy();
						lostArmies[1]++;
					} else {
						attackerCountry.removeArmy();
						lostArmies[0]++;
					}
					attackerDice.remove(highestAttacker);
					defenderDice.remove(highestDefender);
				}
				
				// If attacker won, he gets the country and moves his attacking armies there.
				if (defenderCountry.getArmies().isEmpty()) {
					Player loser = findPlayerToCountry(defenderCountry);
					loser.removeCountry(defenderCountry);
					currentPlayer.addCountry(defenderCountry);
					for (int i = 0; i < armyCount-lostArmies[0]; i++) {
						defenderCountry.addArmy();
						attackerCountry.removeArmy();
					}
					won = true;
				}
				
				
				//Creating new AttackResult
				this.attackResults.add(new AttackResult(currentPlayer,findPlayerToCountry(defenderCountry), lostArmies[0], lostArmies[1], won));
				
			}
			
		} else {
			throw new CountriesNotInRelationException("Countries are not connected.");
		}
	}
	
	/**
	 * Moves specific amount of armies from one to another, related country.
	 * 
	 * @param from Country from where to move.
	 * @param destination Country to move to.
	 * @param armyCount Number of armies you want to move.
	 * @throws CountriesNotInRelationException Countries are not connected.
	 * @throws NotEnoughArmiesException Not enough armies on the country to move.
	 */
	public void moveArmies(Country from, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException {
		if ((currentPlayer.hasCountry(from)) && (currentPlayer.hasCountry(destination))) {
			
			//Exception for not enough armies on the country to be moved from
			if (!(world.areNeighbours(from, destination))) {
				throw new CountriesNotInRelationException("Countries are not connected.");
			}
			if (from.getArmyCount() <= armyCount) {
				throw new NotEnoughArmiesException("There are not enough armies to move.");
			}
			
			//TODO: abfrage ob armee schon bewegt wurde
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
	 * @param currentPlayer The current player.
	 */
	private void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	/**
	 * Returns current player.
	 * 
	 * @return The current player.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * Sets world.
	 * 
	 * @param world The world to play in.
	 */
	private void setWorld(World world) {
		this.world = world;
	}
	/**
	 * Returns world.
	 * 
	 * @return The world where you are playing in.
	 */
	public World getWorld() {
		return world;
	}
	/**
	 * Sets players. (Vector)
	 * 
	 * @param players Vector of all players in this game/match.
	 */
	private void setPlayers(Vector<Player> players) {
		this.players = players;
	}
	/**
	 * Returns players. (Vector)
	 * 
	 * @return players Vector of all players in this game/match.
	 */
	public Vector<Player> getPlayers() {
		return players;
	}
	/**
	 * Sets allocatable armies, usually the one created with createArmies.
	 * 
	 * @param allocatableArmies The armies which can be allocated by the player.
	 */
	private void setAllocatableArmies(Vector<Army> allocatableArmies) {
		this.allocatableArmies = allocatableArmies;
	}
	/**
	 * Returns allocatable armies.
	 * 
	 * @return Vector with armies.
	 */
	private Vector<Army> getAllocatableArmies() {
		return allocatableArmies;
	}
	
	/**
	 * Gets allocatable army-count.
	 * 
	 * @return Integer of the number of armies to allocate.
	 */
	public int getAllocatableArmyCount() {
		return getAllocatableArmies().size();
	}
	
	/**
	 * Calculates maximum number of possible defending armies.
	 * 
	 * @param defender Country which will defend.
	 * @param armyCount Number of attacking armies.
	 * @return Number of armies defending (1 or 2).
	 */
	private int calcMaxDefenderCount(Country defender, int armyCount) {
			
		if(armyCount == 1){
			return 1;
		}
		
		if (defender.getArmyCount() > 1) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * Calculates maximum number of armies which can attack from the selected country.
	 * 
	 * @param country Country where to calculate the maximum number to attack with.
	 * @return Maximum number of armies to attack with.
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
	 * @param country Country where you want to find the owner.
	 * @return Owner of the country.
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
