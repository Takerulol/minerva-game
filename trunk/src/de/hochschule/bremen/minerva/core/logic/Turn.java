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

package de.hochschule.bremen.minerva.core.logic;

import java.util.Collections;
import java.util.Vector;

import de.hochschule.bremen.minerva.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.util.Die;
import de.hochschule.bremen.minerva.vo.Army;
import de.hochschule.bremen.minerva.vo.CanonCard;
import de.hochschule.bremen.minerva.vo.CardSeriesCounter;
import de.hochschule.bremen.minerva.vo.CavalerieCard;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.SoldierCard;
import de.hochschule.bremen.minerva.vo.World;

/**
 * This represents a single turn by the current player inside a game/match.
 * 
 * @since 1.0
 * @version $Id$
 * 
 */
public class Turn {

	public static final int CONTINENT_ARMY_GET = 5;
	
	private World world = null;
	private Vector<Player> players = null;
	private Player currentPlayer = null;
	private Vector<Army> allocatableArmies = null;
	private Vector<AttackResult> attackResults = null;
	private Vector<CountryCard> countryCards = null;
	private Vector<CountryCard> usedCountryCards = null;
	private CardSeriesCounter seriesCounter = null;
	private Boolean cardGet = true;
	
	/**
	 * Constructs a new turn.
	 * 
	 * @param currentPlayer This is the new current player.
	 * @param world The world object played on.
	 * @param players The player vector inheriting all players to this match.
	 * @param Vector<CountryCard> countryCards Vector with country card obtainable after a succesful attack.
	 * @param Vector<CountryCard> usedCountryCards Vector with turned in country cards.
	 * @param CardSeriesCounter seriesCounter Counter of turned in series in the game.
	 */
	public Turn(Player currentPlayer, World world, Vector<Player> players,
			Vector<CountryCard> countryCards, Vector<CountryCard> usedCountryCards,
			CardSeriesCounter seriesCounter) {
		
		this.setWorld(world);
		this.setCurrentPlayer(currentPlayer);
		this.setPlayers(players);
		this.allocatableArmies = new Vector<Army>();
		this.createArmies();
		this.attackResults = new Vector<AttackResult>();
		this.countryCards = countryCards;
		this.usedCountryCards = usedCountryCards;
		this.seriesCounter = seriesCounter;
	}
	
	/**
	 * Creates armies for the current player by taking his countryCount / 3.
	 * If its less than 3, the current player gets 3 armies.
	 * 
	 * @param currentPlayer The current player of this turn.
	 * @return Vector of armies gained per turn.
	 */
	private void createArmies() {
		//army via country count
		int armyGet = this.currentPlayer.getCountryCount() / 3;

		if (armyGet < 3) {
			armyGet = 3;
		}
		
		//armies via fully conquered continents
		int conqueredContinents = 0;
		boolean check;
		for (Continent continent : this.world.getContinents()) {
			check = true;
			for (Country country : this.world.getCountries(continent)) {
				if (!this.currentPlayer.hasCountry(country)) {
					check = false;
				}
			}
			if (check) {
				conqueredContinents++;
			}
		}
		armyGet += conqueredContinents * Turn.CONTINENT_ARMY_GET;
		
		//creating armies
		for (int i = 0; i < armyGet; i++) {
			this.allocatableArmies.add(new Army());
		}
	}
	
	/**
	 * Creates armies by a certain amount.
	 * @param numberOfArmies Number of armies to create.
	 */
	private void createArmies(int numberOfArmies) {
		for (int i = 0; i < numberOfArmies; i++) {
			this.allocatableArmies.add(new Army());
		}
	}
	
	/**
	 * Creates armies out of a series or just creates beginning armies.
	 * @param seriesTurnIn True if a series is turned in.
	 */
	private void createArmies(Boolean seriesTurnIn) {
		if (seriesTurnIn) {
			switch(this.seriesCounter.getCounter()) {
				case 0: this.createArmies(3); break;
				case 1: this.createArmies(5); break;
				case 2: this.createArmies(7); break;
				case 3: this.createArmies(10); break;
				default: this.createArmies(10+5*(this.seriesCounter.getCounter()-3));
			}
			this.seriesCounter.increaseCounter();
		}
	}
	
	//TODO: möglicherweise Exception wenn keine einheiten mehr verfügbar sind? oder abwaelzen 
	// auf gui, dass naechster schritt automatisch kommt.
	/**
	 * Allocates a single allocatable army into a country.
	 * 
	 * @param country The country where to put an army.
	 * @throws NotEnoughArmiesException 
	 * @throws CountryOwnerException 
	 */
	public void allocateArmy(Country country) throws NotEnoughArmiesException, CountryOwnerException {
		if (currentPlayer.hasCountry(country)) {
			if (this.getAllocatableArmyCount() > 0) {
				country.addArmy();
				getAllocatableArmies().remove(getAllocatableArmies().size() - 1);				
			} else {
				throw new NotEnoughArmiesException(country, this.getAllocatableArmyCount(), true);
			}
		} else {
			throw new CountryOwnerException(country, currentPlayer);
		}
	}
	
	/**
	 * Attacker country attacks defender country and chooses how many armies he uses.
	 * Attack result with be formed.
	 * If the players wins a country first time in this turn he'll get a country card.
	 * 
	 * @param attackerCountry Country where to attack from.
	 * @param defenderCountry Country which will be attacked.
	 * @param armyCount Number of armies used. max: 1 <= armies available <= 3 ; min: 1. Minimum one army must remain on the country.
	 * @throws CountriesNotInRelationException The countries are not connected.
	 * @throws IsOwnCountryException Trying to attack an own country.
	 * @throws NotEnoughArmiesException Too little or too many armies used.
	 * 
	 * TODO: Implement feedback (won / lost).
	 * 		- put dice into attack result (is it changed yet?)
	 * 
	 */
	public AttackResult attack(Country attackerCountry, Country defenderCountry, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {
		AttackResult result = null;

		if (this.world.areNeighbours(attackerCountry, defenderCountry)) {
			
			Player defenderPlayer = this.findPlayerToCountry(defenderCountry);
			
			//Exception for attacking an own country
			if (this.currentPlayer.hasCountry(defenderCountry)) {
				throw new IsOwnCountryException(this.currentPlayer, defenderCountry);
			}
			
			if ((armyCount <= 3) && (armyCount>0) && (this.currentPlayer.hasCountry(attackerCountry))) {
				
				//Exception for not enough armies on the attacker country
				if (!(armyCount <= attackerCountry.getArmyCount())) {
					throw new NotEnoughArmiesException(attackerCountry, defenderCountry);
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

				
				Vector<Die> rawAttackerDice = new Vector<Die>(attackerDice);				
				Vector<Die> rawDefenderDice = new Vector<Die>(defenderDice);

				// Dice are compared and armies removed.
				int def = defenderDice.size();
				for (int i = 0; i < def; i++) {
					Die highestAttacker = Die.getLargest(attackerDice);
					Die highestDefender = Die.getLargest(defenderDice);
					if (highestAttacker.getRollResult() > highestDefender.getRollResult()) {
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
					Player loser = this.findPlayerToCountry(defenderCountry);
					loser.removeCountry(defenderCountry);

					this.currentPlayer.addCountry(defenderCountry);

					for (int i = 0; i < armyCount-lostArmies[0]; i++) {
						defenderCountry.addArmy();
						attackerCountry.removeArmy();
					}
					
					// current player gets a card at the first win
					if (cardGet) {
						cardGet = false;
						if (this.countryCards.size() > 0) {
							this.currentPlayer.addCountryCard(this.countryCards.firstElement());
							this.countryCards.removeElementAt(0);
						} else {
							//turning used card stack into the new card stack and shuffle it
							this.countryCards.addAll(this.usedCountryCards);
							Collections.shuffle(this.countryCards);
							this.usedCountryCards.clear();
							this.currentPlayer.addCountryCard(countryCards.firstElement());
							this.countryCards.removeElementAt(0);
						}
					}
					won = true;
				}
				
				result = new AttackResult(this.currentPlayer, defenderPlayer, lostArmies[0], lostArmies[1], rawAttackerDice, rawDefenderDice, won);
				//Creating new AttackResult
				this.attackResults.add(result);
				
			}
			
		} else {
			throw new CountriesNotInRelationException(attackerCountry, defenderCountry);
		}
		
		return result;
	}
	
	/**
	 * Moves specific amount of armies from one to another, related country.
	 * 
	 * @param from Country from where to move.
	 * @param destination Country to move to.
	 * @param armyCount Number of armies you want to move.
	 * @throws CountriesNotInRelationException Countries are not connected.
	 * @throws NotEnoughArmiesException Not enough armies on the country to move.
	 * @throws CountryOwnerException 
	 */
	public void moveArmies(Country from, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException {
		if (currentPlayer.hasCountry(from)) {
			
			if (currentPlayer.hasCountry(destination)) {
			
				//Exception for not enough armies on the country to be moved from
				if (!(world.areNeighbours(from, destination))) {
					throw new CountriesNotInRelationException(from, destination);
				}
	
				if (from.getArmyCount() <= armyCount) {
					throw new NotEnoughArmiesException(from, armyCount, true);
				}
				
				//TODO: abfrage ob armee schon bewegt wurde
				//actually moving armies
				for (int i = 0; i < armyCount; i++) {
					from.removeArmy();
					Army newArmy = new Army();
					newArmy.moved(true);
					destination.addArmy(newArmy);
				}
			} else {
				throw new CountryOwnerException(destination, currentPlayer);
			}
		} else {
			throw new CountryOwnerException(destination, currentPlayer);
		}
	}
	
	/**
	 * Releases the desired card if the country is owned by the current player.
	 *
	 * @param countryCard Card to release.
	 */
	public void releaseCard(CountryCard countryCard) {
		if (currentPlayer.hasCountry(countryCard.getReference())) {
			this.addAllocatableArmy();
			this.usedCountryCards.add(countryCard);
			this.currentPlayer.getCountryCards().remove(countryCard);
		}
	}
	
	/**
	 * Releases one possible series of the currentPlayers card stack.
	 */
	public void releaseCardSeries() {
		cardStackReleaser(this.currentPlayer.getCountryCards());
	}
	
	/**
	 * Releases the the desired card series.
	 * 
	 * @param cardSeries
	 */
	public void releaseCardSeries(Vector<CountryCard> cardSeries) {
		cardStackReleaser(cardSeries);
	}
	
	/*
	 * TODO:
	 * 	- probably Exceptions?
	 *  - maybe return value if no card series could be released.
	 */
	/**
	 * If there is a series available in the given card stack, it will be released.
	 * If there are two different series in the card stack only one will be released.
	 * Card stack must not be greater than 5 cards because a player should hold more.
	 * @param releasableCards Card stack to be released.
	 */
	private void cardStackReleaser(Vector<CountryCard> releasableCards) {
		//need at least 3 cards to ever release a series
		if (releasableCards.size() > 2) {
			
			Vector<CountryCard> soldierCards = new Vector<CountryCard>();
			Vector<CountryCard> canonCards = new Vector<CountryCard>();
			Vector<CountryCard> cavalerieCards = new Vector<CountryCard>();
			
			//count card types
			for (CountryCard card : releasableCards) {
				if (card instanceof SoldierCard) {
					soldierCards.add(card);
				} else if (card instanceof CanonCard) {
					canonCards.add(card);
				} else if (card instanceof CavalerieCard) {
					cavalerieCards.add(card);
				}
			}
			
			//one card of each type
			if ((soldierCards.size() >= 1) && (canonCards.size() >= 1 ) && (cavalerieCards.size() >= 1)) {
				this.usedCountryCards.add(soldierCards.firstElement());
				this.usedCountryCards.add(canonCards.firstElement());
				this.usedCountryCards.add(cavalerieCards.firstElement());
				this.currentPlayer.getCountryCards().remove(soldierCards.firstElement());
				this.currentPlayer.getCountryCards().remove(canonCards.firstElement());
				this.currentPlayer.getCountryCards().remove(cavalerieCards.firstElement());
				this.createArmies(true);
			} else if (soldierCards.size() >= 3) { //3 soldier cards
				for (int i = 0; i < 3; i++) {
					this.usedCountryCards.add(soldierCards.firstElement());
					this.currentPlayer.getCountryCards().remove(soldierCards.firstElement());
					this.createArmies(true);
				}
			} else if (canonCards.size() >= 3) { //3 canon cards
				for (int i = 0; i < 3; i++) {
					this.usedCountryCards.add(canonCards.firstElement());
					this.currentPlayer.getCountryCards().remove(canonCards.firstElement());
					this.createArmies(true);
				}
			} else if (cavalerieCards.size() >= 3) { //3 cavalerie cards
				for (int i = 0; i < 3; i++) {
					this.usedCountryCards.add(cavalerieCards.firstElement());
					this.currentPlayer.getCountryCards().remove(cavalerieCards.firstElement());
					this.createArmies(true);
				}
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
	 * @deprecated
	 */
	@SuppressWarnings("unused") //old
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
	 * Adds one allocatable army to the vector.
	 */
	private void addAllocatableArmy() {
		allocatableArmies.add(new Army());
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
	private Player findPlayerToCountry(Country country) { 
		for (Player player : players) {
			if (player.hasCountry(country)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Gets vector with all attack results of this match.
	 * 
	 * @return attack results
	 */
	public Vector<AttackResult> getAttackResults() {
		return attackResults;
	}
}