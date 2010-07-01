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

import javax.swing.JOptionPane;

import de.hochschule.bremen.minerva.ui.gui.resources.TextResources;

/**
 * DOCME
 * 
 * @version $Id$
 * @since 1.0
 *
 */
public class MMessageBox implements TextResources {

	private static final long serialVersionUID = -5590613142565702637L;

	/**
	 * DOCME
	 * 
	 * @param message
	 */
	public static void show(String message) {
		JOptionPane.showMessageDialog(null, message, MMESSAGE_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
		/*JDialog d = new JDialog();
		d.add( new JTextField(message) );
		d.setVisible(true);
		d.setModal(true);*/
	}

	/**
	 * DOCME
	 * 
	 * @param message
	 *
	 */
	public static void error(String message) {
		JOptionPane.showMessageDialog(null, message, MMESSAGE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}
	
}
