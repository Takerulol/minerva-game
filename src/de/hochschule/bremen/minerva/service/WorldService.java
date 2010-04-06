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
package de.hochschule.bremen.minerva.service;

import de.hochschule.bremen.minerva.persistence.Serializable;
import de.hochschule.bremen.minerva.vo.World;

public class WorldService extends AbstractService {

	private static WorldService instance = null;
	private Serializable storageHandler = this.storage.createHandler(World.class);

	private WorldService() {}
	
	/**
	 * DOCME
	 *  
	 * @return
	 */
	public static WorldService getInstance() {
		if (WorldService.instance == null) {
			WorldService.instance = new WorldService();
		}
		return WorldService.instance;
	}

	/**
	 * DOCME
	 * @return
	 */
	public World load() {
		System.out.println("WorldService::load");
		return (World)storageHandler.read();
	}
}
