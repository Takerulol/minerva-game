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

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Background extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2926270274740692246L;

	private Image image;
	
	/**
	 * 
	 * @param name
	 * @param datatype
	 */
	public Background(String path, Class<? extends JPanel> concernedPanel, String datatype) {
		super();
		
		try {
			image = ImageIO.read(new File(path + concernedPanel.getName() + datatype));
		} catch (IOException e) {
			e.printStackTrace();
			Runtime.getRuntime().exit(ERROR);
		}

		this.repaint();
	}
	
	/**
	 * DOCME
	 * 
	 */
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this.image.getWidth(this), this.image.getHeight(this), this);
	}
}
