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
		Player currentPlayer = null;
		boolean found = false;
		for (Player player : this.player) {
			if (!found) {
				if (player.isCurrentPlayer()) {
					found = true;
					player.setCurrentPlayer(false);
				}
			} else {
				player.setCurrentPlayer(true);
				currentPlayer = player;
				break;
			}
		}

		this.turns.add(new Turn(currentPlayer, this.world, this.player));
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
	 * @return the player
	 */
	public Vector<Player> getPlayer() {
		return player;
	}

	/**
	 * @return the turns
	 */
	public Vector<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * 
	 * @return Game finished?
	 */
	public boolean isFinished() {
		return finished;
	}
}
