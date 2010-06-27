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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import de.hochschule.bremen.minerva.ui.gui.controls.MControl;
import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.vo.Player;
import de.hochschule.bremen.minerva.vo.World;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class WorldInitPanel extends JPanel implements MControl, TextResources {

	private static final long serialVersionUID = -7818223033543151286L;

	private static final Color FONT_COLOR_DEFAULT = new Color(139, 140, 142);
	
	private JComboBox worldComboBox; 

	private Vector<World> worlds = new Vector<World>();
	
	private JLabel currentWorldThumbnail = new JLabel();
	private JLabel currentWorldName = new JLabel();
	private JLabel currentWorldDescription = new JLabel();
	private JLabel currentWorldVersion = new JLabel();
	private JLabel currentWorldAuthor = new JLabel();

	/**
	 * DOCME
	 * 
	 * @param worlds
	 * 
	 */
	public WorldInitPanel(Player gamemaster, Vector<World> worlds) {
		this.worlds = worlds;
		
		// - initialize miglayout manager
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new MigLayout("fillx", "[]10[]"));
		this.setOpaque(false);
		
		// - introduction
		JLabel introduction = new JLabel();
		introduction.setText(WORLD_INIT_PANEL_INTRODUCTION.replace("{gm}", gamemaster.getFirstName()));
		introduction.setFont(FONT);
		introduction.setForeground(FONT_COLOR_DEFAULT);
		this.add(introduction, "span, width ::300");
		
		// - world selector
		JLabel selectorLabel = new JLabel();
		selectorLabel.setText(WORLD_INIT_PANEL_SELECTION);
		selectorLabel.setFont(FONT);
		selectorLabel.setForeground(FONT_COLOR_DEFAULT);
		this.add(selectorLabel, "gaptop 30");
		
		this.worldComboBox = new JComboBox();
		this.worldComboBox.setFont(FONT);
		for (World world : this.worlds) {
			this.worldComboBox.addItem(world.getName());
		}
		this.add(this.worldComboBox, "wrap 30");
		
		// - world info
		this.currentWorldThumbnail.setText("ICO");
		this.add(this.currentWorldThumbnail, "span 1 5, gapright 10");
		
		this.currentWorldName.setFont(new Font(FONT.getFamily(), Font.BOLD, FONT.getSize()));
		this.currentWorldName.setForeground(Color.WHITE);
		this.add(this.currentWorldName, "gaptop 20, wrap 5");
		
		this.currentWorldDescription.setFont(FONT);
		this.add(this.currentWorldDescription, "width ::200, wrap 20");
		
		JLabel worldVersionLabel = new JLabel(WORLD_INIT_PANEL_VERSION);
		worldVersionLabel.setFont(FONT);
		this.add(worldVersionLabel);

		this.currentWorldVersion.setFont(FONT);
		this.add(this.currentWorldVersion, "span, wrap 10");

		JLabel worldAuthorLabel = new JLabel(WORLD_INIT_PANEL_AUTHOR);
		worldAuthorLabel .setFont(FONT);
		this.add(worldAuthorLabel );

		this.currentWorldAuthor.setFont(FONT);
		this.add(this.currentWorldAuthor, "span, wrap 5");

		this.addListeners();
		
		this.fillWorldInfo();
	}
	
	/**
	 * Returns the selected world object.
	 * 
	 * @return The selected world object.
	 * 
	 */
	public World getSelectedWorld() {
		return this.worlds.get(this.worldComboBox.getSelectedIndex());
	}
	
	/**
	 * Fill the world info fields with the values
	 * defined by the selected world.
	 * 
	 */
	private void fillWorldInfo() {
		World selectedWorld = this.getSelectedWorld();
		
		this.currentWorldName.setText(selectedWorld.getName());
		this.currentWorldDescription.setText(selectedWorld.getDescription());
		this.currentWorldVersion.setText(selectedWorld.getVersion());
		this.currentWorldAuthor.setText(selectedWorld.getAuthor());
	}

	private void addListeners() {
		this.worldComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WorldInitPanel.this.fillWorldInfo();
			}
		});
	}
}
