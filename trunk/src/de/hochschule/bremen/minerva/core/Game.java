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

import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME 
 * 
 *
 */
public class Game {

	//private static Logger LOGGER = Logger.getLogger(Game.class.getName());

	private World world = null;
	private Vector<Player> players = null;
	private Vector<Turn> turns = new Vector<Turn>();
	private boolean finished = false;

	/**
	 * DOCME
	 * 
	 * @param world
	 * @param player
	 */
	public Game(World world, Vector<Player> player) {
		this.setWorld(world);
		this.setPlayer(player);
		
		this.allocateCountries();
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
		this.turns.add(new Turn(this.nextPlayer(), this.world, this.players));
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
	 * DOCME
	 * @return Game finished?
	 */
	public boolean isFinished() {
		if (turns.lastElement().getCurrentPlayer().getCountryCount() == world.getCountryCount()) {
			finished = true;
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
//		Player currentPlayer = null;
//
//		boolean foundCurrentPlayer = false;
//		boolean wasCurrentPlayerLast = false;

		
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
		
		
		
//		for (Player player : this.players) {
//			if (!foundCurrentPlayer) {
//				if (player.isCurrentPlayer()) {
//					player.setCurrentPlayer(false);
//					foundCurrentPlayer = true;
//	
//					if (player == this.players.lastElement()) {
//						wasCurrentPlayerLast = true;
//					}
//				}
//			} else {
//				player.setCurrentPlayer(true);
//				currentPlayer = player;
//			}
//		}
//
//		// If there was no player the current player before or the last
//		// current player was the last entry in the vector use the first
//		// player as the new current player.
//		if (wasCurrentPlayerLast || !foundCurrentPlayer) {
//			currentPlayer = this.players.firstElement();
//			currentPlayer.setCurrentPlayer(true);
//		}
//
//		return currentPlayer;
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
				if (!(allocatableCountries.size() == 0)) {
					int index = (int) (Math.random() * allocatableCountries.size());
					player.addCountry(allocatableCountries.get(index));

					//LOGGER.log(Level.INFO, allocatableCountries.get(index).getName() + " gehört jetzt dem Spieler '" + player.getUsername() + "'");

					allocatableCountries.remove(index);
				}
			}
		}
	}
}
