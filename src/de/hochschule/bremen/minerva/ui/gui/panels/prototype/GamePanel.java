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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.exceptions.WorldDoesNotExistException;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.World;

/**
 * Prototype of the actual game screen.
 * 
 * @version 
 * @since
 *
 */
public class GamePanel extends JLayeredPane {
	public MapPanel lowerMap;
	public World world;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906065533734117968L;
	
	public GamePanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		
		this.setLayout(new BorderLayout());
		
		Vector<World> worlds;
		try {
			worlds = WorldManager.getInstance().getList(true);
			try {
				world = WorldManager.getInstance().get(worlds.get(2));
			} catch (WorldDoesNotExistException e1) {
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (DataAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + world.getMapUnderlay();
		lowerMap = new MapPanel(filepath);
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + world.getMap();
		this.add(lowerMap,BorderLayout.NORTH,10);
		MapPanel upperMap = new MapPanel(filepath);;
		this.add(upperMap,BorderLayout.NORTH,20);
		
		upperMap.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				Color color = ColorTool.fromInteger(GamePanel.this.lowerMap.getMapImage().getRGB(e.getX(), e.getY()));
				String hexcode = ColorTool.toHexCode(color);
				Country country = world.getCountry(color);

				System.out.println("Farbe: "+hexcode+" "+country);
			}
		});
		
		
		
		
		
		this.updateUI();
	}

}
