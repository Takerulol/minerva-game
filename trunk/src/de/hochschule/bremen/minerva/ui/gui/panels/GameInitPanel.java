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

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.border.BevelBorder;

import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.PlayerInitPanel;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.WorldInitPanel;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * Main panel for game initialization
 * @author Takeru
 *
 */
public class GameInitPanel extends JLayeredPane {

	private Player player;
	private Background background;
	private PlayerInitPanel playerInit;
	private WorldInitPanel worldInit;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8901679483780034723L;
	
	
	public GameInitPanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		//TODO: change image
		//background
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		//player list
		this.playerInit = new PlayerInitPanel();
		this.playerInit.setBounds(50, 200, 300, 300);
		this.playerInit.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		//game init
		this.worldInit = new WorldInitPanel();
		this.worldInit.setBounds(600, 200, 300, 300);
		this.worldInit.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		
		//adding panels to stage
		this.add(this.playerInit,20);
		this.add(this.worldInit,20);
		this.add(this.background,10);
	}


	public void setPlayer(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return player;
	}
}
