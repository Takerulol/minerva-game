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

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.ValueObject;
import de.hochschule.bremen.minerva.vo.World;

/**
 * Provides some helper methods for map processing.
 *
 * @since 1.0
 * @version $Id$
 * 
 */
public class MapTool {

	// The color on a map, that identifies the center point,
	// where to place some controls (e. g. "army count").
	private static final int COUNTRY_ANCHOR_IDENTIFIER = 0xFF000000;
	
	/**
	 * Returns a hash map, which contains the country anchor points.
	 * A country anchor means the center point where controls, the
	 * army count for example, can be placed.
	 * 
	 * Checking each map pixel if the color equals the COUNTRY_ANCHOR_IDENTIFIER
	 * color. If so, a new anchor was found and will be placed in the hash map
	 * identified by the country.
	 * 
	 * @return HashMap Contains the country as key and the anchor point as value.
	 * 
	 */
	public static HashMap<Country, Point> getCountryAnchors(BufferedImage map, BufferedImage mapUnderlay, World world) {
		HashMap<Country, Point> countryAnchors = new HashMap<Country, Point>();

		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getRGB(x, y) == COUNTRY_ANCHOR_IDENTIFIER) {
					Point point = new Point(x,y);
					Country country = world.getCountry(new Color(mapUnderlay.getRGB(x, y)));

					if (country.getId() > ValueObject.getDefaultId()) {
						countryAnchors.put(country, point);
					}
				}
			}
		}
		
		return countryAnchors;
	}
}