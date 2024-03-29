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
package de.hochschule.bremen.minerva.server.persistence.file;

import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.Neighbour;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.Persistence;
import de.hochschule.bremen.minerva.server.persistence.file.handler.ContinentHandler;
import de.hochschule.bremen.minerva.server.persistence.file.handler.CountryHandler;
import de.hochschule.bremen.minerva.server.persistence.file.handler.NeighbourHandler;
import de.hochschule.bremen.minerva.server.persistence.file.handler.PlayerHandler;
import de.hochschule.bremen.minerva.server.persistence.file.handler.WorldHandler;

public class FilebasedPersistence implements Persistence {

	@Override
	public Handler createHandler(Class<?> type) {
		if (type == World.class) {
			return new WorldHandler();
		} else if (type == Country.class) {
			return new CountryHandler();
		} else if (type == Continent.class) {
			return new ContinentHandler();
		} else if (type == Neighbour.class) {
			return new NeighbourHandler();
		} else if (type == Player.class) {
			return new PlayerHandler();
		}
		return null;
	}

}
