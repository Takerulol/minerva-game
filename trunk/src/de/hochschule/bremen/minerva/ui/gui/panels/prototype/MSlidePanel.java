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

import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class MSlidePanel extends JPanel {

	private ControlBarPanel controlBar;
	private boolean minimized = true;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9178493186977704484L;
	

	/**
	 * 
	 * @param controlBar
	 */
	public MSlidePanel(ControlBarPanel controlBar) {
		this.controlBar = controlBar;
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		
		this.setup();
		controlBar.setSlidePanel(this);
		this.add(controlBar);
		
		this.updateUI();
	}
	
	private void setup() {
		
	}
	
	public void slideUp() {
		if (this.isMinimized()) {
			this.minimized = false;
			Rectangle r = this.getBounds();
			System.out.println(r.y);
			this.setBounds(0, r.y - this.controlBar.getRelativeHeight(), this.getPreferredSize().width, this.getPreferredSize().height);
		}
	}
	
	public void slideDown() {
		if (!this.isMinimized()) {
			this.minimized = true;
			Rectangle r = this.getBounds();
			this.setBounds(0, r.y + this.controlBar.getRelativeHeight(), this.getPreferredSize().width, this.getPreferredSize().height);
		}
	}
	
	public boolean isMinimized() {
		return this.minimized;
	}
	
	/**
	 * Returns embedded ControlBarPanel.
	 * @return control bar panel
	 */
	public ControlBarPanel getControlBar() {
		return controlBar;
	}
	
	public int getRelativeHeight() {
		return this.controlBar.getRelativeHeight();	
	}

}
