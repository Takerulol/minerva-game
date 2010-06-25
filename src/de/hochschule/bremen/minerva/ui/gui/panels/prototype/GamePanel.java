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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.core.Game;
import de.hochschule.bremen.minerva.core.Turn;
import de.hochschule.bremen.minerva.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WorldDoesNotExistException;
import de.hochschule.bremen.minerva.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.util.ColorTool;
import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;
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
	public HashMap<Country, ArmyCountIcon> armyIcons = new HashMap<Country, ArmyCountIcon>();
	public World world;
	public Game game;
	public Turn currentTurn;
	public CountryAnchorMap countryAnchors;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906065533734117968L;

	private void testGame() {
		Player p1 = new Player();
		p1.setUsername("akoenig");
		p1.setPassword("akoenig");
		try {
			AccountManager.getInstance().login(p1);
		} catch (PlayerAlreadyLoggedInException e1) {
		} catch (WrongPasswordException e1) {
		} catch (PlayerDoesNotExistException e1) {
		} catch (DataAccessException e1) {
		}
		Vector<Player> players = new Vector<Player>();
		players.add(MinervaGUI.getInstance().getPlayer());
		players.add(p1);
		
		try {
			this.game = new Game(this.world,players);
		} catch (NoPlayerLoggedInException e) {
			e.printStackTrace();
		} catch (NotEnoughPlayersLoggedInException e) {
			e.printStackTrace();
		}
		
	}
	
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
				e1.printStackTrace();
			}
		} catch (DataAccessException e1) {
			e1.printStackTrace();
		}
		
		String filepath;
		
		//lower map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + world.getMapUnderlay();
		this.lowerMap = new MapPanel(filepath);
		this.lowerMap.setBounds(0,0,500,500);
		
		
		//upper map
		filepath = ApplicationConfigurationManager.get().getWorldsAssetsDirectory() + world.getMap();
		this.upperMap = new MapPanel(filepath);
		this.upperMap.setBounds(0,0,500,500);
		
		//control bar
		MSlidePanel cbp = new MSlidePanel(new ControlBarPanel());
		cbp.setBounds(0, this.getPreferredSize().height - cbp.getRelativeHeight(), cbp.getPreferredSize().width,cbp.getPreferredSize().height);
		
		//aci.setBounds(200, 200, 30, 30);
		CountryGameOption ac = new CountryGameOption(new Country());
		ac.setBounds(400,400,20,20);
		//this.add(aci,100);
		
		
		
		
		this.upperMap.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				Color color = ColorTool.fromInteger(GamePanel.this.lowerMap.getMapImage().getRGB(e.getX(), e.getY()));
				String hexcode = ColorTool.toHexCode(color);
				Country country = world.getCountry(color);

				System.out.println("Farbe: "+hexcode+" "+country);
			}
		});
		
		this.countryAnchors = new CountryAnchorMap(this.upperMap, this.lowerMap, this.world);
		
		for (Country country : this.world.getCountries()) {
			ArmyCountIcon aci = new ArmyCountIcon(Color.RED, this.countryAnchors.getCountryAnchorPosition(country));
			this.armyIcons.put(country,aci);
			this.add(aci,100);
		}
		
		
		this.testGame();
		this.currentTurn = this.game.nextTurn();
		this.refreshArmyCounts();
		
		//Adding everything up
		this.add(cbp, 1000);
		this.add(this.upperMap,-20000);
		this.add(this.lowerMap,-30000);
		this.validate();
		
		this.updateUI();
	}

	/**
	 * Updates whole panel, when something changed during the game
	 */
	public void updatePanel() {
		//TODO:implementation
		this.updateUI();
	}
	
	/**
	 * Repaints all ArmyCountIcons with correct parameters
	 */
	@SuppressWarnings("unchecked")
	public void refreshArmyCounts() {
		Iterator iter = this.armyIcons.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry)iter.next();
			((ArmyCountIcon)pairs.getValue()).setPlayer(((Country)pairs.getKey()), this.game.getPlayer(((Country)pairs.getKey())));
		}
	}
	
//	private void attack() {
//		//turn.attack(..);
//		AttackResult rs = new AttackResult(null, null, 1, 1, null, null, false);
//	}
	
}
