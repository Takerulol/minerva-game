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

package de.hochschule.bremen.minerva.util;

import de.hochschule.bremen.minerva.vo.Country;
import java.util.HashMap;
import java.util.Vector;

public class CountryGraph {
	
	/**
	 * CountryGraph with neighbour-relations.
	 * 
	 */
	private HashMap<Integer, Vector<Integer>> neighbours = new HashMap<Integer, Vector<Integer>>();


	/**
	 * Connects country one with country in the map. If the country doesn't exit in the map, it will be created.
	 * 
	 * @param source
	 * @param with
	 */
	public void connect(Country source, Country with) {
		Vector<Integer> connections;

		if (neighbours.containsKey(source.getId())) {
			 connections = neighbours.get(source.getId());
		} else {
			connections = new Vector<Integer>();
		}

		connections.add(with.getId());
		neighbours.put(source.getId(), connections);
	}
	
	/**
	 * Compares two countries for a neighbour-connection.
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean neighbours(Country one, Country two) {
		if (neighbours.get(one.getId()).contains(two.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a integer vector with the neighbour ids
	 * by a given country.
	 * 
	 * @param byCountryId
	 * @return
	 */
	public Vector<Integer> getNeighbours(int byCountryId) {
		return this.neighbours.get(byCountryId);
	}

	/**
	 * Has a specific country neighbours?
	 * 
	 * @param countryId
	 * @return
	 */
	public boolean hasNeighbours(int countryId) {
		if (this.neighbours.get(countryId) == null) {
			return false;
		} else {
			return !this.neighbours.get(countryId).isEmpty();
		}
	}
}