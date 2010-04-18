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

import java.awt.Color;
import java.util.List;

public class Country extends ValueObject {

	private int id;
	private String token;
	private String name;
	private Color color = null;
	private Continent continent = null;
	private List<Country> neighbours = null;
	private int worldId = 0;

	/**
	 * Sets the country id.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the id.
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the country token.
	 * 
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Returns the token.
	 * 
	 * @return
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Sets the country name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the country color.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the color.
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the country continent.
	 * 
	 * @param continent
	 */
	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	/**
	 * Returns the continent.
	 * 
	 * @return
	 */
	public Continent getContinent() {
		return this.continent;
	}

	/**
	 * Sets the country neighbours.
	 * 
	 * @param neighbours
	 */
	public void setNeighbours(List<Country> neighbours) {
		this.neighbours = neighbours;
	}

	/**
	 * Returns the neighbours.
	 * @return
	 */
	public List<Country> getNeighbours() {
		return this.neighbours;
	}

	/**
	 * Sets the world id.
	 * 
	 * @param worldId
	 */
	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	/**
	 * Returns the world id
	 * 
	 * @return
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * Made out of all attributes one string.
	 * 
	 * @return
	 */
	public String toString() {
		return getClass().getName() + "[id=" + id + ",token=" +token + ",name=" + name + ",color=" + color + ",continent=" + continent + ",neighbours=" + neighbours + "]";
	}
}
