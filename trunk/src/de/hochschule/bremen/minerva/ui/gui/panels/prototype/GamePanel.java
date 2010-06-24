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
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.core.Game;
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
	public MapPanel upperMap;
	public World world;
	public Game game;
	public CountryAnchorMap countryAnchors;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906065533734117968L;
	
	public GamePanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		//this.setBackground(Color.LIGHT_GRAY);
		
		Vector<World> worlds;
		try {
			worlds = WorldManager.getInstance().getList(true);
			try {
				world = WorldManager.getInstance().get(worlds.get(3));
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
		
		this.lowerMap = new MapPanel(filepath);
		this.lowerMap.setBounds(0,0,500,500);
		
		
		
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + world.getMap();
		
		
		
		this.upperMap = new MapPanel(filepath);
		this.upperMap.setBounds(0,0,500,500);
		
		//control bar
		MSlidePanel cbp = new MSlidePanel(new ControlBarPanel());
		cbp.setBounds(0, this.getPreferredSize().height - cbp.getRelativeHeight(), cbp.getPreferredSize().width,cbp.getPreferredSize().height);

		System.out.println(cbp.getBounds());
		
		//Adding everything up
		this.add(cbp, 1000);
		this.add(this.upperMap,-20000);
		this.add(this.lowerMap,-30000);
		
		this.upperMap.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				Color color = ColorTool.fromInteger(GamePanel.this.lowerMap.getMapImage().getRGB(e.getX(), e.getY()));
				String hexcode = ColorTool.toHexCode(color);
				Country country = world.getCountry(color);

				System.out.println("Farbe: "+hexcode+" "+country);
			}
		});

		this.countryAnchors = new CountryAnchorMap(this.upperMap, this.lowerMap, this.world);
		
		this.updateUI();
	}

	/**
	 * Updates whole panel, when something changed during the game
	 */
	public void updatePanel() {
		//TODO:implementation
		this.updateUI();
	}
	
}
