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

package de.hochschule.bremen.minerva.ui.gui.panels.subpanels;

import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class WorldInitPanel extends JPanel {

	private static final long serialVersionUID = -7818223033543151286L;

	private JComboBox worldSelector; 
	
	/**
	 * DOCME
	 * 
	 * @param worlds
	 * 
	 */
	public WorldInitPanel(Vector<World> worlds) {
		this.worldSelector = new JComboBox();
		
		for (World world : worlds) {
			this.worldSelector.addItem(world.getName());
		}
		
		this.add(worldSelector);
	}
}
