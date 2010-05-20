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

import de.hochschule.bremen.minerva.ui.UserInterface;
import de.hochschule.bremen.minerva.ui.cui.MinervaCUI;

public class App {

	public static void main(String[] args) {
		UserInterface ui = new MinervaCUI();
		ui.run();
	}
}


/*

TESTCODE für das Erstellen einer Welt. (Bitte, nicht entfernen :))
==================================================================

		try {
			World world = new World();
			world.setAuthor("André König");
			world.setDescription("Eine Testwelt, um den WorldManager zu testen.");
			world.setName("ManagerTest");
			world.setToken("wmgr");
			world.setVersion("1.0 gamma");

			Vector<Country> countries = new Vector<Country>();

			Continent continent1 = new Continent();
			continent1.setName("Continent1");
			
			Country one = new Country();
			one.setName("Land 1");
			one.setColor(Color.BLACK);
			one.setContinent(continent1);
			one.setToken("ln1");
			countries.add(one);

			Country two = new Country();
			two.setName("Land 2");
			two.setColor(Color.WHITE);
			two.setContinent(continent1);
			two.setToken("ln2");
			countries.add(two);
			
			Country three = new Country();
			three.setName("Land 3");
			three.setColor(Color.BLUE);
			three.setContinent(continent1);
			three.setToken("ln3");
			countries.add(three);
			
			world.setCountries(countries);

			// Persistence
			WorldManager.getInstance().add(world);

			for (Country country : world.getCountries()) {
				System.out.println("Country id is now: "+country.getId());
			}

			world.connectCountries(one, two);

			try {
				WorldManager.getInstance().store(world);
			} catch (WorldDoesNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PersistenceIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




*/