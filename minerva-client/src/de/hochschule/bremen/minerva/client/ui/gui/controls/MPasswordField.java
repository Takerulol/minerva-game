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
package de.hochschule.bremen.minerva.client.ui.gui.controls;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;

import de.hochschule.bremen.minerva.commons.util.ColorTool;

/**
 * Our special MPasswordField. The "M" stands for Minerva ;)
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class MPasswordField extends JPasswordField implements MControl {

	private static final long serialVersionUID = -4307631737327385357L;

	private static String BORDER_COLOR = "01aefd";
	
	private static String BORDER_COLOR_INVALID = "cc0000";
	
	/**
	 * Constructor
	 * 
	 */
	public MPasswordField() {
		super();
		this.init();
	}

	/**
	 * Constructor with column param.
	 * 
	 * @param columns The visible columns.
	 * @see JPasswordField
	 * 
	 */
	public MPasswordField(int columns) {
		super(columns);
		this.init();
	}

	/**
	 * Sets the textfields border and padding.
	 * 
	 */
	private void init() {
		this.setValid(true);
	}

	/**
	 * Sets the textfield invalid.
	 * Border in a different color.
	 * 
	 * @param invalid
	 * 
	 */
	public void setValid(boolean valid) {
		if (valid) {
			this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorTool.fromHexCode(BORDER_COLOR)), BorderFactory.createLineBorder(Color.WHITE, 5)));
		} else {
			this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorTool.fromHexCode(BORDER_COLOR_INVALID)), BorderFactory.createLineBorder(Color.WHITE, 5)));
			this.requestFocus();
		}
	}
}
