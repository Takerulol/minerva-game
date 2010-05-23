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

import java.io.File;

import de.hochschule.bremen.minerva.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.vo.World;

public class WorldFile extends World {
	
	private File worldFile = null;
	
	/**
	 * DOCME
	 * 
	 * @param worldFile
	 */
	public WorldFile(File worldFile) {
		this.worldFile = worldFile;
	}

	/**
	 * DOCME
	 * @throws WorldFileNotFoundException 
	 * @throws UnknownWorldFileExtensionException 
	 * 
	 */
	public void exec() throws WorldFileExtensionException, WorldFileNotFoundException {
		if (!this.worldFile.exists()) {
			throw new WorldFileNotFoundException();
		} else if (!this.worldFile.getName().endsWith(".world")) {
			throw new WorldFileExtensionException("The file '"+this.worldFile.getName() +
					  "' does not have the correct extension. Please verify to import a valid "
					+ "world import file (*.world).");
		}

		
	}
	
	/**
	 * DOCME
	 * 
	 */
	public void close() {
		
	}

	/**
	 * DOCME
	 * 
	 * @return
	 */
	public File getWorldFile() {
		return this.worldFile;
	}
}