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
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import de.hochschule.bremen.minerva.vo.Country;
import de.hochschule.bremen.minerva.vo.Player;

public class ArmyCountIcon extends JPanel {
	
	private Color color;
	private int armyCount = 0;
	private boolean marked = false;
	private Color markColor;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1865891788129661164L;
	
	public ArmyCountIcon(Color color, Point p) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		this.color = color;
		this.setSize(30,30);
		
		this.setBounds(p.x - 15, p.y - 15, 24, 24);
		
	}
	
	public void setPlayer(Country country, Player player) {
		this.armyCount = country.getArmyCount();
		this.color = player.getColor();
		this.repaint();
	}
	
	public void mark(Color color) {
		this.marked = true;
		this.markColor = color;
		this.repaint();
	}
	
	public void disableMark() {
		marked = false;
		this.repaint();
	}
	
	public void paint(Graphics g) {
		g.setColor(this.color);
		g.fillArc(1, 1, 20, 20, 0, 360);
		
		
		g.setColor(Color.black);
		g.drawArc(1, 1, 20, 20, 0, 360);
	
		g.drawString(String.valueOf(this.armyCount), 11 - 4 * String.valueOf(this.armyCount).length(), 15);
		
		if (marked) {
			g.setColor(this.markColor);
			g.drawArc(-1, -1, 24, 24, 0, 360);
			g.drawArc(0, 0, 22, 22, 0, 360);
		}
	}

}
