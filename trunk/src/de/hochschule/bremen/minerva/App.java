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
package de.hochschule.bremen.minerva;

//import de.hochschule.bremen.minerva.exceptions.WorldImportException;
import java.util.List;

import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
//import de.hochschule.bremen.minerva.util.*;
import de.hochschule.bremen.minerva.vo.World;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			List<World> worlds = WorldService.getInstance().loadAll();
			for (World world : worlds) {
				System.out.println(world.toString());
			}

			World myWorld = WorldService.getInstance().load(2);
			
			//WorldService.getInstance().delete(myWorld);

			/*World w = new World();
			w.setAuthor("Andre");
			w.setDescription("Ein kleiner Text.");
			w.setName("Eine Welt");
			w.setToken("w1");
			w.setVersion("1.0.1");

			WorldService.getInstance().save(w);
			myWorld.setToken("earth");
			WorldService.getInstance().save(myWorld);*/
			myWorld.setToken("usa");

			WorldService.getInstance().save(myWorld);
			
		} catch (PersistenceIOException e) {
			e.printStackTrace();
		}
	}
}
