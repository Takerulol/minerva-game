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

import de.hochschule.bremen.minerva.util.CountryGraph;

public class World extends ValueObject {
	
	private int id = DEFAULT_ID;
	private String token = null;
	private String name = null;
	private String description = null;
	private String author = null;
	private String version = null;

	private Vector<Country> countries = new Vector<Country>();
	private CountryGraph countryGraph = new CountryGraph();
	
	/**
	 * Sets the world id.
	 * 
	 * @param id - Integer value (see persistence).
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
	 * Sets the world token.
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
	 * Sets the world name.
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
	 * Sets the world description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the world author.
	 * 
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns the author.
	 * 
	 * @return
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Sets the world version.
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the version.
	 * 
	 * @return
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * DOCME
	 * 
	 * @param country
	 */
	public void addCountry(Country country) {
		this.countries.add(country);
	}
	
	/**
	 * Sets the world countries.
	 * 
	 * @param countries
	 */
	public void setCountries(Vector<Country> countries) {
		this.countries = countries;
	}

	/**
	 * Returns the countries.
	 * 
	 * @return
	 */
	public Vector<Country> getCountries() {
		return this.countries;
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public int getCountryCount() {
		return this.countries.size();
	}

	/**
	 * DOCME
	 * 
	 * @param index
	 * @return
	 */
	public Country getCountry(int index) {
		return this.countries.get(index);
	}
	
	/**
	 * Returns the country graph, which contains
	 * the country-neighbour-relation.
	 * 
	 * @return
	 * @deprecated - Use methods within the world instead.
	 */
	public CountryGraph getCountryGraph() {
		return countryGraph;
	}

	/**
	 * Wrapper class for connecting countries (CountryGraph).
	 * 
	 * @param source
	 * @param with
	 */
	public void connectCountries(Country source, Country with) {
		this.countryGraph.connect(source, with);
	}

	/**
	 * Wrapper method for hiding the country graph.
	 * Checks if two given countries are connected or not.
	 * 
	 * @param one - The first country
	 * @param two - The second country
	 * 
	 * @return
	 */
	public boolean areNeighbours(Country one, Country two) {
		return this.countryGraph.neighbours(one, two);
	}

	/**
	 * Wrapper method for hiding the country graph.
	 * Checks if a given country has neighbouring countries.
	 * 
	 * @param country - Has this county neighbours?
	 * @return
	 */
	public boolean hasNeighbours(Country country) {
		return this.countryGraph.hasNeighbours(country.getId());
	}

	/**
	 * Wrapper method for hiding the country graph.
	 * 
	 * @param country
	 * @return
	 * 
	 */
	public Vector<Integer> getNeighbours(int countryId) {
		return this.countryGraph.getNeighbours(countryId);
	}

	/**
	 * Wrapper method for hiding the country graph.
	 * Returns a country vector which represents the neighbouring
	 * countries by an given country.
	 * 
	 * @param country
	 * @return
	 */
	public Vector<Country> getNeighbours(Country country) {
		Vector<Integer> countries = this.countryGraph.getNeighbours(country.getId());
		Vector<Country> selectedCountries = new Vector<Country>();

		for (Integer countryId : countries) {
			for (Country originCountry : this.getCountries()) {
				if (country.getId() == countryId) {
					selectedCountries.add(originCountry);
					break;
				}
			}
		}
		
		return selectedCountries;
	}

	/**
	 * Made out of all attributes one string.
	 * 
	 * @return
	 */
	public String toString() {
		return getClass().getName() + ": [id=" + id + ", token=" +token + ", name=" + name + ", description=" + description + ", author=" + author + ", version=" + version + ", countries=" + countries +"]";
	}
}
