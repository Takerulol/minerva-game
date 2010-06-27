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

package de.hochschule.bremen.minerva.ui.gui.panels;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.manager.AccountManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.persistence.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.PlayerInitPanel;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.WorldInitPanel;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * Main panel for game initialization
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class GameInitPanel extends JLayeredPane {

	private Player player;
	private Background background;
	private PlayerInitPanel playerInitPanel;
	private WorldInitPanel worldInitPanel;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8901679483780034723L;
	
	
	public GameInitPanel(Player player) {
		this.player = player;
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		//TODO: change image
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		//player list
		// TODO: How to handle the player icons here???
		// TODO: Set the gamemaster
		
		Vector<Player> players = new Vector<Player>();
		this.player.setMaster(true);
		players.add(this.player);
		
		Player carina = null;
		try {
			carina = AccountManager.getInstance().getPlayer("cstrempel");
		} catch (PlayerDoesNotExistException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DataAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		players.add(carina);

		this.playerInitPanel = new PlayerInitPanel(players);
		this.playerInitPanel.setOpaque(false);
		this.playerInitPanel.setBounds(90, 180, 350, 240);
		this.playerInitPanel.setBorder(BorderFactory.createEmptyBorder());
	
		//game init
		Vector<World> worlds = new Vector<World>();
		try {
			worlds = WorldManager.getInstance().getList();
		} catch (DataAccessException e) {
			// TODO: Handle the DataAccessException in a correct way.
			MMessageBox.show(e.getMessage());
			Runtime.getRuntime().exit(ERROR);
		}
		
		// TODO: Pass the gamemaster
		this.worldInitPanel = new WorldInitPanel(players.get(0), worlds);
		this.worldInitPanel.setOpaque(false);
		this.worldInitPanel.setBounds(590, 160, 300, 300);
		this.worldInitPanel.setBorder(BorderFactory.createEmptyBorder());
		
		
		//adding panels to stage
		this.add(this.playerInitPanel,20);
		this.add(this.worldInitPanel,20);
		this.add(this.background,10);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
}
