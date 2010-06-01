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
import java.util.Vector;

import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.util.CountryGraph;

/**
 * Represents a world with countries and a country graph, which
 * stores the country/neighbour-mapping.
 * 
 * @see Country
 * @see CountryGraph
 * 
 * @version $Id$
 * @since 1.0
 *
 */
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
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the id.
	 * 
	 * @return The world id.
	 * 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the world token.
	 * 
	 * @param token
	 * 
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Returns the token.
	 * 
	 * @return The world token.
	 * 
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Sets the world name.
	 * 
	 * @param name
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The world name.
	 * 
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the world description.
	 * 
	 * @param description
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the description.
	 * 
	 * @return The world description.
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the world author.
	 * 
	 * @param author
	 * 
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns the author.
	 * 
	 * @return The world author.
	 * 
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Sets the world version.
	 * 
	 * @param version
	 * 
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the version.
	 * 
	 * @return The world version.
	 * 
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Add a country to this world.
	 * 
	 * @param country
	 * @see Country
	 * 
	 */
	public void addCountry(Country country) {
		this.countries.add(country);
	}
	
	/**
	 * Sets the world countries.
	 * 
	 * @param countries
	 * @see Vector
	 * 
	 */
	public void setCountries(Vector<Country> countries) {
		this.countries = countries;
	}

	/**
	 * Returns the countries.
	 * 
	 * @return A vector with country objects.
	 * @see Country
	 * 
	 */
	public Vector<Country> getCountries() {
		return this.countries;
	}

	/**
	 * Returns a country vector by an given continent
	 * 
	 * @param byContinent The @see continent that represents the filter parameter.
	 * @return A country vector with countries. Each country belongs to the given continent.
	 * 
	 */
	public Vector<Country> getCountries(Continent byContinent) {
		Vector<Country> countries = new Vector<Country>();

		for (Country country : this.getCountries()) {
			Continent continent = country.getContinent();
			
			if (continent.getName().equals(byContinent.getName())) {
				countries.add(country);
			}
		}

		return countries;
	}

	/**
	 * Wrapper method, which returns the country count.
	 * 
	 * @return The country count.
	 * 
	 */
	public int getCountryCount() {
		return this.countries.size();
	}

	/**
	 * Get a country by an given vector index.
	 * 
	 * @param index
	 * @return The country object, which was searched for.
	 * @deprecated Use {@link World#getCountry(Country)} instead.
	 * 
	 */
	public Country getCountry(int index) {
		return this.countries.get(index);
	}

	/**
	 * Returns a country object by an given country object :)
	 * Note that, the given country object must have
	 * the country id.
	 * 
	 * @param byCountry
	 * @return The country object, which was searched for.
	 * @see Country
	 * 
	 */
	public Country getCountry(Country byCountry) {
		Country searchedCountry = new Country();

		search: for (Country country : this.getCountries()) {
			if (country.getId() == byCountry.getId()) {
				searchedCountry = country;
				break search;
			}
		}
		return searchedCountry;
	}

	/**
	 * Returns a country by an given color.
	 * 
	 * @param byColor @see java.awt.Color
	 * @return The country reference. If no country was found with the given color, then the country is empty.
	 * 
	 */
	public Country getCountry(Color byColor) {
		Country foundCountry = new Country();
		
		search: for (Country country : this.getCountries()) {
			if (ColorTool.toHexCode(byColor).equals(ColorTool.toHexCode(country.getColor()))) {
				foundCountry = country;
				break search;
			}
		}
		
		return foundCountry;
	}

	/**
	 * Returns the country graph, which contains the country-neighbour-relation.
	 * 
	 * @return
	 * @deprecated Use methods within the world instead (for example {@link World#connectCountries(Country, Country)})
	 * 
	 */
	public CountryGraph getCountryGraph() {
		return countryGraph;
	}

	/**
	 * Wrapper class for connecting countries (CountryGraph).
	 * 
	 * @param source The source country. 
	 * @param with The country which will be connected with the source
	 * @see Country
	 * @see CountryGraph
	 * 
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
	 * @return true/false
	 * 
	 */
	public boolean areNeighbours(Country one, Country two) {
		return this.countryGraph.neighbours(one, two);
	}

	/**
	 * Wrapper method for hiding the country graph.
	 * Checks if a given country has neighboring countries.
	 * 
	 * @param country - Has this county neighbours?
	 * @return true/false
	 * @see Country
	 * 
	 */
	public boolean hasNeighbours(Country country) {
		return this.countryGraph.hasNeighbours(country.getId());
	}

	/**
	 * Wrapper method for hiding the country graph. Returns
	 * a country by an given country object.
	 * 
	 * @param country
	 * @return A vector with integers, which represents referenced country id's
	 * @deprecated Use {@link World#getNeighbours(Country)} instead.
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
		Vector<Integer> neighbours = this.countryGraph.getNeighbours(country.getId());
		Vector<Country> selectedCountries = new Vector<Country>();

		for (Integer neighbourId : neighbours) {
			search: for (Country originCountry : this.getCountries()) {
				if (originCountry.getId() == neighbourId) {
					selectedCountries.add(originCountry);
					break search;
				}
			}
		}
		
		return selectedCountries;
	}

	/**
	 * Returns a string with all object attributes.
	 * 
	 * @return All object attributes.
	 * 
	 */
	public String toString() {
		return getClass().getName() + ": [id=" + id + ", token=" +token + ", name=" + name + ", description=" + description + ", author=" + author + ", version=" + version + ", countries=" + countries +"]";
	}
}
