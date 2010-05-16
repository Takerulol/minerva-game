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

package de.hochschule.bremen.minerva.persistence;

/**
 * Abstract datatype for Crudible interface for having the possibility
 * to enter different datatypes into the same read() method.
 * 
 * @author Christian Bollmann
 */
public class FilterParameter {
	

	
	Object param;
	
	/**
	 * Constructor for integer values converted to Object.
	 * 
	 * @param param
	 */
	public FilterParameter(int param) {
		this((Object)param);
	}
	
	/**
	 * Constructor for String values converted to Object.
	 * 
	 * @param param
	 */
	public FilterParameter(String param) {
		this((Object)param);
	}
	
	/**
	 * Constructor for Object-type.
	 * 
	 * @param param
	 */
	private FilterParameter(Object param) {
		this.param = param;
	}
	

	/**
	 * Getting String out of param via typecast.
	 * 
	 * @return
	 */
	public String getString() {
		return (String)this.param;
	}
	
	/**
	 * Getting Integer out of param via typecast.
	 * 
	 * @return
	 */
	public int getInt() {
		return (Integer)this.param;
	}

	/**
	 * Is this filter parameter a string?
	 * 
	 * @return 
	 */
	public boolean isString() {
		return (this.param instanceof java.lang.String);
	}
}
