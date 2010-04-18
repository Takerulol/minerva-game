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

public class Player extends ValueObject {

	private int id = 0;
	private String username = "";
	private String last_name = "";
	private String first_name = "";
	private String email = "";
	private String last_login = "";
	
	/*
	 *  TODO:
	 *  - add getter/setter documentation.
	 * 
	 */
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setLast_login(String last_login) {
		this.last_login = last_login;
	}
	public String getLast_login() {
		return last_login;
	}

	/**
	 * Made out of all attributes one string
	 * 
	 * @return
	 */
	public String toString() {
		return getClass().getName() + "[id=" + id + ",username=" + username + ",last_name=" + last_name + ",first_name=" + first_name + ",email=" + email + ",last_login=" + last_login + "]";
	}
	
}
