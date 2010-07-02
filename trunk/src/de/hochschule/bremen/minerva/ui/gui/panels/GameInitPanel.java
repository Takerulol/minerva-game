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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.core.logic.Game;
import de.hochschule.bremen.minerva.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.manager.SessionManager;
import de.hochschule.bremen.minerva.manager.WorldManager;
import de.hochschule.bremen.minerva.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.ui.gui.controls.MButton;
import de.hochschule.bremen.minerva.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseListener;
import de.hochschule.bremen.minerva.ui.gui.listener.MMouseMotionListener;
import de.hochschule.bremen.minerva.ui.gui.panels.prototype.GamePanel;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.PlayerInitPanel;
import de.hochschule.bremen.minerva.ui.gui.panels.subpanels.WorldInitPanel;
import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * Main panel for game initialization
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class GameInitPanel extends JLayeredPane implements TextResources {

	private Background background;
	private PlayerInitPanel playerInitPanel;
	private WorldInitPanel worldInitPanel;
	private MButton buttonStartGame;

	private static final long serialVersionUID = -8901679483780034723L;
	
	public GameInitPanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		// player list
		this.playerInitPanel = new PlayerInitPanel();
		this.playerInitPanel.setOpaque(false);
		this.playerInitPanel.setBounds(40, 150, 350, 500);
		this.playerInitPanel.setBorder(BorderFactory.createEmptyBorder());
	
		// game init
		Vector<World> worlds = new Vector<World>();
		try {
			worlds = WorldManager.getInstance().getList();
		} catch (DataAccessException e) {
			// TODO: Handle the DataAccessException in a correct way.
			MMessageBox.show(e.getMessage());
			Runtime.getRuntime().exit(ERROR);
		}

		// TODO: Pass the gamemaster
		Player gamemaster = SessionManager.get(MinervaGUI.getSessionId()).getMaster();
		this.worldInitPanel = new WorldInitPanel(gamemaster, worlds);
		this.worldInitPanel.setOpaque(false);
		this.worldInitPanel.setBounds(585, 140, 300, 350);

		this.buttonStartGame = new MButton(GAME_INIT_PANEL_BUTTON_START_GAME);
		this.buttonStartGame.setBounds(720, 500, 150, 20);

		//adding panels to stage
		this.add(this.playerInitPanel, 20);
		this.add(this.worldInitPanel, 20);
		this.add(this.buttonStartGame, 20);
		this.add(this.background,10);
		
		this.addListeners();
	}

	/**
	 * Check if the coordinates represent the player add link.
	 * 
	 * @param x The checkable x coordinate.
	 * @param y The checkable y coordinate.
	 *
	 * @return Is inside the "player add link rectangle"?
	 *
	 */
	private boolean isPlayerAddLink(int x, int y) {
		if ((x < 505) && (x > 350) && (y < 123) && (y > 110)) {
			return true;
		}
		return false;
	}

	/**
	 * Register the event handler.
	 * 
	 */
	private void addListeners() {
		this.buttonStartGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JLayeredPane nextPanel;

				Game game = SessionManager.get(MinervaGUI.getSessionId());

				World selectedWorld = GameInitPanel.this.worldInitPanel.getSelectedWorld();
				game.setWorld(selectedWorld);

				try {
					game.start();
					nextPanel = new GamePanel();
				} catch (NotEnoughPlayersLoggedInException e) {
					// Okay, back to the login panel
					MMessageBox.show(e.getMessage());
					nextPanel = new LoginPanel();
				} catch (NoPlayerLoggedInException e) {
					// Okay, back to the login panel
					MMessageBox.show(e.getMessage());
					nextPanel = new LoginPanel();
				} catch (WorldNotDefinedException e) {
					// Okay, back to the game init panel
					MMessageBox.show(e.getMessage());
					nextPanel = new GameInitPanel();
				}

				MinervaGUI.getInstance().changePanel(nextPanel);
			}
		});

		// MMouseListener for handling the click on the "player add link".
		this.addMouseListener(new MMouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (GameInitPanel.this.isPlayerAddLink(e.getX(), e.getY())) {
					MinervaGUI.getInstance().changePanel(new LoginPanel());					
				}
			}
		});

		// MMouseMotionListener for sending a interaction feedback to the
		// user if he move the mouse over the "add player link"
		this.addMouseMotionListener(new MMouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				System.out.println("move");
				if (GameInitPanel.this.isPlayerAddLink(e.getX(), e.getY())) {
					GameInitPanel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					GameInitPanel.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}
}