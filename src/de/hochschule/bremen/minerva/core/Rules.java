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

public class Rules {
	private Vector<Player> players = null;
	private Vector<Country> countries = null;
	
	private int playerCount;
	private int countryCount;
	private int minimumUnitGain;
	private int maxAttackUnits;
	private int maxDefendUnits;
	
	/*
	 * TODO:
	 * - mehr regeln
	 */
	
	public Rules(Vector<Player> pl, Vector<Country> cnt) {
		setPlayers(pl);
		setCountries(cnt);
		
		setPlayerCount(pl.size());
		setCountryCount(cnt.size());
		setMinimumUnitGain(3);
		setMaxAttackUnits(3);
		setMaxDefendUnits(2);
	}
	
	/*
	 * TODO:
	 * - fehlende methoden und werte, die sich z.B. auch aus den laendern ergeben
	 * 
	 */
	
	
	
	
	

	
	// Getter/Setter
	
	private void setMinimumUnitGain(int minimumUnitGain) {
		this.minimumUnitGain = minimumUnitGain;
	}

	public int getMinimumUnitGain() {
		return minimumUnitGain;
	}

	private void setMaxAttackUnits(int maxAttackUnits) {
		this.maxAttackUnits = maxAttackUnits;
	}

	public int getMaxAttackUnits() {
		return maxAttackUnits;
	}

	private void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	private void setMaxDefendUnits(int maxDefendUnits) {
		this.maxDefendUnits = maxDefendUnits;
	}

	public int getMaxDefendUnits() {
		return maxDefendUnits;
	}

	private void setPlayers(Vector<Player> players) {
		this.players = players;
	}

	public Vector<Player> getPlayers() {
		return players;
	}

	private void setCountries(Vector<Country> countries) {
		this.countries = countries;
	}

	public Vector<Country> getCountries() {
		return countries;
	}

	private void setCountryCount(int countryCount) {
		this.countryCount = countryCount;
	}

	public int getCountryCount() {
		return countryCount;
	}
	
}
