/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: World.java 6 2010-03-30 08:13:52Z andre.koenig@gmail.com $
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

public class Country {

	private int id;
	private String token;
	private String name;
	private Color color = null;
	private Continent continent = null;
	private List<Country> neighbours = null;

	//private int world = 0;

	/**
	 * DOCME
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * DOCME
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * DOCME
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * DOCME
	 * @return
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * DOCME
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * DOCME
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * DOCME
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * DOCME
	 * @return
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * DOCME
	 * @param continent
	 */
	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	/**
	 * DOCME
	 * @return
	 */
	public Continent getContinent() {
		return this.continent;
	}

	/**
	 * DOCME
	 * @param neighbours
	 */
	public void setNeighbours(List<Country> neighbours) {
		this.neighbours = neighbours;
	}

	/**
	 * DOCME
	 * @return
	 */
	public List<Country> getNeighbours() {
		return this.neighbours;
	}

	/**
	 * DOCME
	 * @return
	 */
	public String toString() {
		return "";
	}
}
