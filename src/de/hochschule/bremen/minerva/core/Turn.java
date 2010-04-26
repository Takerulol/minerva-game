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
	 * DOCME
	 * 
	 * @param currentPlayer
	 * @param world
	 * @param players
	 */
	public Turn(Player currentPlayer, World world, Vector<Player> players) {
		this.world = world;
		this.currentPlayer = currentPlayer;
		this.players = players;
		allocatableArmies = createArmies(this.currentPlayer);
	}
	
	/**
	 * TODO:
	 * 	- Actually create armies.
	 * 
	 * DOCME
	 * @param currentPlayer
	 * @return
	 */
	private Vector<Army> createArmies(Player currentPlayer) {
		return null;
	}
	
	/**
	 * DOCME
	 * 
	 * @param country
	 */
	public void allocateArmy(Country country) {
		
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
	
}
