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
	private String lastLogin = null;
	
	// The countries, that the player won.
	private Vector<Country> countries = null;

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
	 * Sets the player passwort.
	 * 
	 * @param passwort 
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * Sets the player last login.
	 * 
	 * @param last login
	 */
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * Returns the last login.
	 * 
	 * @return
	 */
	public String getLastLogin() {
		return lastLogin;
	}

	/**
	 * Sets the countries (owned by the player).
	 * 
	 * @param countries
	 */
	public void setCountries(Vector<Country> countries) {
		this.countries = countries;
	}
	
	/**
	 * Returns the countries (owned by the player).
	 * 
	 * @return Vector with the country pointer.
	 */
	public Vector<Country> getCountries() {
		return this.countries;
	}
	
	/**
	 * Made out of all attributes one string
	 * 
	 * @return
	 */
	public String toString() {
		return getClass().getName() + "[id=" + id + ",username=" + username + ",last_name=" + lastName + ",first_name=" + firstName + ",email=" + email + ",last_login=" + lastLogin + "]";
	}
}