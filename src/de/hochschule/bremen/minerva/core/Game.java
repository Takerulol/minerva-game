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

import java.util.Collections;
import java.util.Vector;

import de.hochschule.bremen.minerva.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.vo.CanonCard;
import de.hochschule.bremen.minerva.vo.CardSeriesCounter;
import de.hochschule.bremen.minerva.vo.CavalerieCard;
import de.hochschule.bremen.minerva.vo.Continent;
import de.hochschule.bremen.minerva.vo.ContinentConquerMission;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.CountryCard;
import de.hochschule.bremen.minerva.vo.CountryConquerMission;
import de.hochschule.bremen.minerva.vo.DefeatPlayerMission;
import de.hochschule.bremen.minerva.vo.Mission;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.SoldierCard;
import de.hochschule.bremen.minerva.vo.World;

/**
 * The core game class.
 * 
 * @since 1.0
 * @version $Id$
 *
 */
public class Game {

	private World world = null;
	private Vector<Player> players = new Vector<Player>();
	private Vector<Turn> turns = new Vector<Turn>();
	private Vector<Mission> missions = new Vector<Mission>();
	private Vector<CountryCard> countryCards = new Vector<CountryCard>();
	private Vector<CountryCard> usedCountryCards = new Vector<CountryCard>();
	private CardSeriesCounter seriesCounter = new CardSeriesCounter();
	private boolean finished = false;
	private Player winner = null;

	/**
	 * DOCME
	 * 
	 * @param world
	 * @param players
	 * @throws NoPlayerLoggedInException 
	 * @throws NotEnoughPlayersLoggedInException
	 *  
	 */
	public Game(World world, Vector<Player> players) throws NoPlayerLoggedInException, NotEnoughPlayersLoggedInException {
		if (players.size() == 0) {
			throw new NoPlayerLoggedInException();
		} else if (players.size() == 1) {
			throw new NotEnoughPlayersLoggedInException(players);
		}
		this.setWorld(world);
		this.setPlayer(players);
		
		this.allocateCountries();
		this.allocateMissions();
		this.generateCountryCards();
	}

	
	/**
	 * Sets the next player and creates a new
	 * Turn. With this turn it is possible to
	 * implement the application logic.
     *
	 * @return Turn
	 * 
	 */
	public Turn nextTurn() {
		this.turns.add(new Turn(this.nextPlayer(), this.world, this.players, this.countryCards, this.usedCountryCards, this.seriesCounter));
		return this.turns.lastElement();
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * DOCME
	 * 
	 * @param world
	 */
	private void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * DOCME
	 * @return the player
	 */
	public Vector<Player> getPlayer() {
		return players;
	}

	/**
	 * DOCME
	 * 
	 * @param player
	 */
	private void setPlayer(Vector<Player> player) {
		this.players = player;
	}

	/**
	 * DOCME
	 * @return the turns
	 */
	public Vector<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * Returns the turn count.
	 * 
	 * @return int The turn count.
	 *  
	 */
	public int getTurnCount() {
		return this.turns.size();
	}
	
	/**
	 * DOCME
	 * @return Game finished?
	 * 
	 * TODO: Mission aus dem Vector entfernen.
	 * 
	 */
	public boolean isFinished() {
		if (turns.lastElement().getCurrentPlayer().getCountryCount() == world.getCountryCount()) {
			this.setWinner(turns.lastElement().getCurrentPlayer());
			finished = true;
		} else {
			Vector<Player> winners = new Vector<Player>();
			for(Mission mission : this.missions) {
				if (mission.isFulfilled()) {
					winners.add(mission.getOwner());
				}
			}
			if (!winners.isEmpty()) {
				if (winners.contains(turns.lastElement().getCurrentPlayer())) {
					//TODO: write wrapper method for turns.lastElement()
					this.setWinner(turns.lastElement().getCurrentPlayer());
				} else {
					this.setWinner(winners.firstElement());
				}
				finished = true;
			}
		}

		return finished;
	}

	/**
	 * Iterates over the player vector and determines
	 * the current player. This player is not the current player
	 * anymore. Sets the next player object in the list as the
	 * current player and returns this instance.
	 * 
	 * If there is no player the "current player", we flag the first
	 * player in the vector as the new current player.
	 * 
	 * @return The next player.
	 * 
	 */
	private Player nextPlayer() {
		if (!turns.isEmpty()) {
			int currentIndex = players.indexOf(turns.lastElement().getCurrentPlayer());
			turns.lastElement().getCurrentPlayer().setCurrentPlayer(false);
			int nextIndex ;
			do {
				nextIndex = (++currentIndex)%players.size();
			} while (!players.get(nextIndex).hasCountries());
			Player nextPlayer = players.get(nextIndex);
			nextPlayer.setCurrentPlayer(true);
			return nextPlayer;
		} else {
			Player nextPlayer = players.firstElement();
			nextPlayer.setCurrentPlayer(true);
			return nextPlayer;
		}
	}

	/**
	 * DOCME
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void allocateCountries() {
		Vector<Country> allocatableCountries = new Vector<Country>();
		allocatableCountries = (Vector<Country>) this.world.getCountries().clone();
		
		for (int i = 0; i < ((this.world.getCountryCount() / this.players.size()) + 1); i++) {
			for (Player player : this.players) {
				if (allocatableCountries.size() > 0) {
					int index = (int) (Math.random() * allocatableCountries.size());
					player.addCountry(allocatableCountries.get(index));
					allocatableCountries.remove(index);
				}
			}
		}
	}
	
	/**
	 * Generates missions randomly for all players in the game.
	 * 
	 */
	private void allocateMissions() {
		Vector<Player> defeatablePlayers = new Vector<Player>(this.getPlayer());
		Vector<Continent> conquerableContinents = new Vector<Continent>(this.world.getContinents());
		
		int missionType = 0;

		for (Player player : this.players) {
			missionType = (int)(Math.random()*3);
			
			switch (missionType) {
				
				// Conquer country mission
				case 0:
					// DOCME: The calculation!
					short conquerableCountryCount = (short)((this.world.getCountryCount()*4)/7);
					this.missions.add(new CountryConquerMission(conquerableCountryCount, player));
				break;

				// Conquer continent mission
				case 1:
					Collections.shuffle(conquerableContinents);
					Vector<Country> firstContinent = this.world.getCountries(conquerableContinents.get(0));
					Vector<Country> secondContinent = this.world.getCountries(conquerableContinents.get(1));
					this.missions.add(new ContinentConquerMission(firstContinent, secondContinent, player));
				break;

				// Defeat player mission
				default:
					// Creates a copy of the defeatable players vector and removes the mission owner.
					// So that the mission owner has no possibility to be the enemy.
					Vector<Player> shuffableDefeatablePlayer = new Vector<Player>(defeatablePlayers);
					shuffableDefeatablePlayer.remove(player);

					Collections.shuffle(shuffableDefeatablePlayer);
					Player enemy = shuffableDefeatablePlayer.firstElement();
					this.missions.add(new DefeatPlayerMission(enemy, player));
					
					// Remove the enemy from the vector with defeatable player. So that the enemy can
					// no be the enemy in another defeat player mission.
					defeatablePlayers.remove(enemy);
				break;
			}
		}
	}
	
	//TODO:	maybe shuffle country vector without creating temp
	/**
	 * Generates full stack of country cards according to the number country list with
	 * a random symbol.
	 * 
	 */
	private void generateCountryCards() {
		Vector<Country> temp = new Vector<Country>(this.world.getCountries());
		Collections.shuffle(temp);
		
		for (int countryNumber = 0; countryNumber < this.world.getCountryCount(); countryNumber++) {
			CountryCard card;
			if ((countryNumber % 3) == 0) {
				card = new SoldierCard(temp.elementAt(countryNumber));
			} else if ((countryNumber % 3) == 1) {
				card = new CanonCard(temp.elementAt(countryNumber));
			} else {
				card = new CavalerieCard(temp.elementAt(countryNumber));
			}
			this.countryCards.add(card);
		}
	}

	/**
	 * Returns the generated mission vector.
	 * 
	 * @return Vector<Mission> The missions.
	 * 
	 */
	public Vector<Mission> getMissions() {
		return this.missions;
	}
	
	/**
	 * 
	 * @return
	 */
	public Player getWinner() {
		return this.winner;
	}

	/**
	 * Sets the champ.
	 * 
	 * @param champ Player The winner
	 * 
	 */
	private void setWinner(Player champ) {
		this.winner = champ;
	}
}
