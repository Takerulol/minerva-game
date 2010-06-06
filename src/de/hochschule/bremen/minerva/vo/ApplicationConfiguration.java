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

/**
 * A value object that encapsulate the configuration from the
 * "application configuration file".<br /><br />
 * 
 * This object will be instantiated by the ApplicationConfigurationManager.
 *
 * @see ApplicationConfigurationManager
 * @version $Id$
 * @since 1.0
 * 
 */
public class ApplicationConfiguration extends ValueObject {

	// #########################################################
	// #
	// # DON'T FORGET TO MODIFY THE MANAGER
	// # IF YOU ADD NEW ATTRIBUTES AND GETTER-/SETTER-METHODS
	// # TO THIS CLASS. OTHERWISE THEY WILL NOT READ-/STORABLE!
	// #
	// # A nice but time-consuming solution is described in the
	// # AppConfigurationManager (see: NICE SOLUTION).
	// #
	// #########################################################

	private String worldsAssetsDirectory = null;

	private String userInterfaceAssetsDirectory = null;
	
	/**
	 * Sets the worlds assets directory path.
	 * This directory contains all the worlds related resources (e. g. map images, thumbnail, etc.)
	 *  
	 * @param path The string with the full path to the worlds assets.
	 * 
	 */
	public void setWorldsAssetsDirectory(String path) {
		this.worldsAssetsDirectory = path;
	}

	/**
	 * Returns the worlds assets directory path.
	 * 
	 * @return The path to the worlds assets.
	 * 
	 */
	public String getWorldsAssetsDirectory() {
		return worldsAssetsDirectory;
	}

	/**
	 * Sets the user interface assets directory path.
	 * This directory holds all user interface related resources.
	 * 
	 * @param path The string with the full path to the ui assets.
	 * 
	 */
	public void setUserInterfaceAssetsDirectory(String path) {
		this.userInterfaceAssetsDirectory = path;
	}

	/**
	 * Returns the ui assets directory path.
	 * 
	 * @return The path to the ui assets.
	 * 
	 */
	public String getUserInterfaceAssetsDirectory() {
		return userInterfaceAssetsDirectory;
	}

	/**
	 * Returns the application configuration in a single string.
	 * 
	 * @return The application configuration in a single string.
	 * 
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + ": [worldAssetsDir="+this.getWorldsAssetsDirectory()+ ", uiAssetsDir="+this.getUserInterfaceAssetsDirectory()+"]";
	}
}