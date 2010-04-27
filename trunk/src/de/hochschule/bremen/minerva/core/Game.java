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

import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME 
 * 
 *
 */
public class Game {
	
	private World world = null;
	private Vector<Player> player = null;
	private Vector<Turn> turns = null;
	private boolean finished = false;

	/**
	 * Sets the next player and creates a new
	 * Turn. With this turn it is possible to
	 * implement the application logic.
     *
	 * @return Turn
	 * 
	 */
	public Turn nextTurn() {
		this.turns.add(new Turn(this.nextPlayer(), this.world, this.player));
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
	 * @return the player
	 */
	public Vector<Player> getPlayer() {
		return player;
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
		Player currentPlayer = null;

		boolean foundCurrentPlayer = false;
		boolean wasCurrentPlayerLast = false;
		
		for (Player player : this.player) {
			if (!foundCurrentPlayer) {
				if (player.isCurrentPlayer()) {
					player.setCurrentPlayer(false);
					foundCurrentPlayer = true;
	
					if (player == this.player.lastElement()) {
						wasCurrentPlayerLast = true;
					}
				}
			} else {
				player.setCurrentPlayer(true);
				currentPlayer = player;
			}
		}

		// If there was no player the current player before or the last
		// current player was the last entry in the vector use the first
		// player as the new current player.
		if (wasCurrentPlayerLast || !foundCurrentPlayer) {
			currentPlayer = this.player.firstElement();
			currentPlayer.setCurrentPlayer(true);
		}

		return currentPlayer;
	}
}