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
package de.hochschule.bremen.minerva.ui.gui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.vo.Player;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class MPlayerIcon extends JPanel implements MControl, TextResources {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7238829407887665957L;
	
	private static final Color FONT_COLOR_DEFAULT = new Color(139, 140, 142);
	private static final Color FONT_COLOR_GAMEMASTER = new Color(2, 175, 252);
	
	private Player player = null;

	// UI elements
	private JLabel name;
	private JLabel gamemaster;
	private JLabel username;
	private JLabel email;
	
	private JPanel iconArea;
	private JPanel dataArea;
	
	/**
	 * DOCME
	 * 
	 * @param player
	 * 
	 */
	public MPlayerIcon(Player player) {
		this.player = player;

		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		
		this.iconArea = new JPanel();
		this.iconArea.setOpaque(false);
		this.iconArea.add(new JLabel("ICON"));
		
		this.dataArea = new JPanel();
		this.dataArea.setLayout(new BoxLayout(this.dataArea, BoxLayout.PAGE_AXIS));
		this.dataArea.setOpaque(false);
		
		this.name = new JLabel();
		this.name.setFont(new Font(FONT.getName(), Font.BOLD, FONT.getSize()));
		this.name.setForeground(Color.WHITE);

		this.gamemaster = new JLabel();
		this.gamemaster.setFont(FONT);
		this.gamemaster.setForeground(FONT_COLOR_GAMEMASTER);

		this.username = new JLabel();
		this.username.setFont(FONT);
		this.username.setForeground(FONT_COLOR_DEFAULT);

		this.email = new JLabel();
		this.email.setFont(FONT);
		this.email.setForeground(FONT_COLOR_DEFAULT);
		
		this.gamemaster.setVisible(false);
		
		this.dataArea.add(this.name);
		this.dataArea.add(this.gamemaster);
		this.dataArea.add(this.username);
		this.dataArea.add(this.email);

		this.add(this.iconArea, BorderLayout.WEST);
		this.add(this.dataArea, BorderLayout.CENTER);

		this.refresh();
	}

	/**
	 * DOCME
	 * 
	 */
	private void refresh() {
		this.name.setText(this.player.getFirstName() + " " + this.player.getLastName());
		
		if (this.player.isMaster()) {
			this.gamemaster.setText(MPLAYERICON_GAMEMASTER);
			this.gamemaster.setVisible(true);
		}
		
		this.username.setText(MPLAYERICON_USERNAME + " " + this.player.getUsername());
		this.email.setText(MPLAYERICON_EMAIL + " " + this.player.getEmail());
		
		// TODO: Icon refresh
	}

	/**
	 * Returns the player object, which represents
	 * the MPlayerIcon model.
	 * 
	 * @return Player The player model.
	 * 
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Sets a player that represents the model.
	 * 
	 * @param player The player model.
	 * 
	 */
	public void setPlayer(Player player) {
		this.player = player;
		
		this.refresh();
	}	
}
