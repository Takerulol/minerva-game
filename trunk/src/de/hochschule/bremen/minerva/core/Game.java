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

public class Game {
	
	private Vector<Player> players = null;
	private Vector<Country> countries = null;
	private Player currentPlayer = null;
	private Rules rules = null;
	
	public Game (Vector<Player> pl, Vector<Country> cnt) {
		players = pl;
		countries = cnt;
		initialize();
	}
	
	
	//initialisierung von anfangswerten
	public static void initialize() {
		/*
		 * TODO:
		 * 	- Anfangsspieler bestimmen
		 *  - Methodenaufrufe zur Verteilung (carina)
		 *  - ggf. missionsverteilung (später)
		 *  - regeln initilisieren
		 */
		
	}
	
	public void run() {
		
	}
	
	public void close() {
		players = null;
		countries = null;
		currentPlayer = null;
		rules = null;
	}
	
	
	
	
	// Getter/Setter
	public Vector<Player> getPlayers() {
		return players;
	}
	public Vector<Country> getCountries() {
		return countries;
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	// Testkonstrukt fuer kleine Kartenumgebung (vll auch in cui auslagern)
	public void testGame() {
		
	}
}