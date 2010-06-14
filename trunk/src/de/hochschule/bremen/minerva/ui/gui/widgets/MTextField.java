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
package de.hochschule.bremen.minerva.ui.gui.widgets;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import de.hochschule.bremen.minerva.util.ColorTool;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class MTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024127729084675588L;

	// DOCME
	private static String BORDER_COLOR = "01aefd";
	
	private static Font font = new Font("Tahoma", 0, 11);
	
	/**
	 * DOCME
	 * 
	 */
	public MTextField() {
		super();
		this.init();
	}

	public MTextField(int size) {
		super(size);
		this.init();
	}

	/**
	 * DOCME
	 * 
	 */
	private void init() {
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorTool.fromHexCode(MTextField.BORDER_COLOR)), BorderFactory.createLineBorder(Color.WHITE, 5)));
		this.setFont(MTextField.font);
	}
}
