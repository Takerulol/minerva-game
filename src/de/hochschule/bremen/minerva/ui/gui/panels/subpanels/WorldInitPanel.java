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

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.ui.gui.controls.MButton;
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
	
	private JLabel introduction;
	
	private JComboBox worldSelector; 
	
	/**
	 * DOCME
	 * 
	 * @param worlds
	 * 
	 */
	public WorldInitPanel(Player gamemaster, Vector<World> worlds) {

		this.setLayout(new BorderLayout(20, 20));
		this.setOpaque(false);
		
		// introduction
		this.introduction = new JLabel();
		this.introduction.setText(WORLD_INIT_PANEL_INTRODUCTION.replace("{gm}", gamemaster.getFirstName()));
		this.introduction.setFont(FONT);
		this.introduction.setForeground(FONT_COLOR_DEFAULT);
		this.add(this.introduction, BorderLayout.NORTH);

		JLabel selectorLabel = new JLabel();
		selectorLabel.setText(WORLD_INIT_PANEL_SELECTION);
		selectorLabel.setFont(FONT);
		selectorLabel.setForeground(FONT_COLOR_DEFAULT);
		this.add(selectorLabel, BorderLayout.WEST);
		
		this.worldSelector = new JComboBox();
		this.worldSelector.setFont(FONT);
		this.worldSelector.setBounds(0, 0, (int)this.worldSelector.getPreferredSize().getWidth(), (int)this.worldSelector.getPreferredSize().getHeight());
		
		for (World world : worlds) {
			this.worldSelector.addItem(world.getName());
		}

		this.add(this.worldSelector, BorderLayout.CENTER);

		JPanel worldInfo = new JPanel();
		worldInfo.setBorder(BorderFactory.createLineBorder(new Color(35, 36, 40), 1));
		worldInfo.setBackground(new Color(14, 15, 16));
		worldInfo.setOpaque(false);
		worldInfo.setLayout(new BorderLayout(20, 20));
		worldInfo.add(new JLabel("Thumbnail"), BorderLayout.WEST);
		
		JPanel worldDetails = new JPanel();
		worldDetails.setOpaque(false);
		worldDetails.setLayout(new BoxLayout(worldDetails, BoxLayout.PAGE_AXIS));
		
		worldDetails.add(new JLabel("Title"));
		worldDetails.add(new JLabel("<html>BeschreibungstextBeschreibungstextBeschreibungstextBeschreibungstextBeschreibungstext</html>"));
		worldDetails.add(new JLabel("Version: 1.0"));
		worldDetails.add(new JLabel("Autoren: ..."));
		
		worldInfo.add(worldDetails, BorderLayout.CENTER);

		MButton buttonStartGame = new MButton("Spiel starten ...");
		worldInfo.add(buttonStartGame, BorderLayout.SOUTH);
		
		this.add(worldInfo, BorderLayout.SOUTH);

	}
}
