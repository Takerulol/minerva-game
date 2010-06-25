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

package de.hochschule.bremen.minerva.ui.gui.panels.prototype;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.jws.soap.SOAPBinding.Use;

import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;

/**
 * 
 * DOCME
 *
 * @deprecated {@link Use MapTool instead.}
 */
public class CountryAnchorMap {

	private HashMap<Country, Point> countryAnchors = new HashMap<Country, Point>();
	
	/**
	 * 
	 * @param upperMap
	 * @param lowerMap
	 * @param world
	 */
	public CountryAnchorMap(MapPanel upperMap, MapPanel lowerMap, World world) {
		BufferedImage pointGetter = upperMap.getMapImage();
		BufferedImage countryGetter = lowerMap.getMapImage();
		this.createCountryAnchors(pointGetter, countryGetter, world);
	}
	
	/**
	 * 
	 * @param upperMap
	 * @param lowerMap
	 * @param world
	 */
	public CountryAnchorMap(BufferedImage upperMap, BufferedImage lowerMap, World world) {
		this.createCountryAnchors(upperMap, lowerMap, world);
	}
	
	/**
	 * Creates anchor points for all countries in the current game.
	 * The points are used for positioning country count and owner and
	 * all game controls referred to countries.
	 * 
	 * @param upperMap
	 * @param lowerMap
	 * @param world
	 */
	private void createCountryAnchors(BufferedImage upperMap, BufferedImage lowerMap, World world) {

	}
	
	/**
	 * Gets the point of the anchor for a country.
	 * 
	 * @param country country of point you're looking for.
	 * @return point of the country
	 */
	public Point getCountryAnchorPosition(Country country) {
		return this.countryAnchors.get(country);
	}
}
