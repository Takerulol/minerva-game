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

public class Player extends ValueObject {

	private int id = 0;
	private String username = null;
	private String password = null;
	private String lastName = null;
	private String firstName = null;
	private String email = null;
	private boolean loggedIn = false;
	private boolean currentPlayer = false;
	
	// The countries, that the player won.
	private Vector<Country> countries = new Vector<Country>();
	
	/**
	 * Sets the player id.
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
		return id;
	}

	/**
	 * Sets the player username.
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the username.
	 * 
	 * @return 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the player password.
	 * 
	 * @param passwort 
	 */
	public void setPassword(String md5Hash) {
		this.password = md5Hash;
	}

	/**
	 * Returns the password.
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the player last name.
	 * 
	 * @param last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the last name.
	 * 
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the player first name.
	 * 
	 * @param first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the first name.
	 * 
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the player email.
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the email.
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the player as an logged in player.
	 * 
	 * @param isLoggedIn
	 * 
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * Is this player logged in?
	 * 
	 * @return isloggedIn
	 * 
	 */
	public boolean isLoggedIn(){
		return this.loggedIn;
	}

	/**
	 * Sets the player as the current player.
	 * 
	 * @param currentPlayer the currentPlayer to set
	 * 
	 */
	public void setCurrentPlayer(boolean currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Is this player the current player?
	 * 
	 * @return the currentPlayer
	 * 
	 */
	public boolean isCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * The player has captured a new country.
	 * Add this country to the players country
	 * vector.
	 * 
	 * @param countries
	 */
	public void addCountry(Country country) {
		this.countries.add(country);
	}
	
	/**
	 * Oh no. The player has lost one country.
	 * Remove it from the players country vector.
	 * 
	 */
	public void removeCountry(Country country) {
		this.countries.remove(country);
	}

	/**
	 * Returns the current players country count.
	 *  
	 * @return
	 */
	public int getCountryCount() {
		return this.countries.size();
	}

	/**
	 * Check if the user owns the given country.
	 * 
	 * @param country
	 * @return
	 */
	public boolean hasCountry(Country country) {
		return (this.countries.contains(country));
	}

	/**
	 * Has the user lost all his countries?
	 *  
	 * @return true / false
	 * 
	 */
	public boolean hasCountries() {
		return (this.countries.size() > 0);
	}
	
	/**
	 * Made out of all attributes one string
	 * 
	 * @return
	 */
	public String toString() {
		return getClass().getName() + "[id=" + this.id + ",username=" + this.username + ",last_name=" + this.lastName + ",first_name=" + this.firstName + ",email=" + this.email + ", loggedIn=" + this.loggedIn + "]";
	}
}