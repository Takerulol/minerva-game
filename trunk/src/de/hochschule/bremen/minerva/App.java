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

import java.util.Vector;

import de.hochschule.bremen.minerva.persistence.exceptions.PersistenceIOException;
import de.hochschule.bremen.minerva.persistence.service.ContinentService;
import de.hochschule.bremen.minerva.persistence.service.CountryService;
import de.hochschule.bremen.minerva.persistence.service.NeighbourService;
import de.hochschule.bremen.minerva.persistence.service.WorldService;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Neighbour;
import de.hochschule.bremen.minerva.vo.World;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			World world = WorldService.getInstance().load(1);

			//for (World world : worlds) {			
				Vector<Country> countries = CountryService.getInstance().loadAll(world);

				for (Country country : countries) {
					country.setContinent(ContinentService.getInstance().load(country.getContinent().getId()));

					for (Neighbour neighbour : NeighbourService.getInstance().loadAll(country)) {
						world.getCountryGraph().connect(country, neighbour);
					}
				}

				world.setCountries(countries);

				System.out.println(world.toString());

				Country ukraine = CountryService.getInstance().load(3);
				Country scandinavia = CountryService.getInstance().load(1);

				if (world.getCountryGraph().neighbours(ukraine, scandinavia)) {
					System.out.println("Ukraine und Skandinavien sind benachbart.");
				} else {
					System.out.println("Ukraine und Skandinavien sind NICHT benachbart.");
				}
			//}

			/*
			 * - Eingabe der Spieler
			 * - Auswahl der Welt
			 * 
			 * Game game = new Game(world, player);
			 *
			 */
				
		} catch (PersistenceIOException e) {
			e.printStackTrace();
		}
	}
}
