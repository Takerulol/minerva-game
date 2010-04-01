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
package de.hochschule.bremen.minerva.vo;

import java.util.Vector;

/**
 * Eine kurze Beschreibung für die JavaDoc-Generierung
 * 
 * @author akoenig
 */

public class World {
	
	private Vector<Continent> continents = null;

	/**
	 * DOCME hallo
	 */
	public World() {
		
	}
	
	/**
	 * DOCME
	 * @return
	 */
	public Vector<Continent> getContinents() {
			return this.continents;
	}

	/**
	 * 
	 * @param continents
	 */
	public void setContinents(Vector<Continent> continents) {
		this.continents = continents;
		System.out.print("Hallo Welt");
	}
}
