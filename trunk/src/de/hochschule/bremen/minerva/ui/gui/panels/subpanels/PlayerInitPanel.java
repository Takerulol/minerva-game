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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.manager.SessionManager;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MButton;
import de.hochschule.bremen.minerva.ui.gui.controls.MPlayerIcon;
import de.hochschule.bremen.minerva.ui.gui.panels.LoginPanel;
import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class PlayerInitPanel extends JPanel implements TextResources {

	private static final long serialVersionUID = -344922633298919424L;
	
	private MButton buttonAddPlayer;
	
	/**
	 * DOCME
	 * 
	 * @param playerIcons
	 */
	public PlayerInitPanel() {
		Vector<Player> players = SessionManager.get(MinervaGUI.getSessionId()).getPlayers();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setOpaque(false);

		this.buttonAddPlayer = new MButton(PLAYER_INIT_PANEL_BUTTON_ADD_PLAYER);
		
		for (Player player : players) {
			this.add(new MPlayerIcon(player));
		}
		
		this.add(this.buttonAddPlayer);
		this.addListeners();
	}
	
	private void addListeners() {
		// Handling the login event if the user pressed on the login button. 
		this.buttonAddPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MinervaGUI.getInstance().changePanel(new LoginPanel());
			}
		});
	}
}