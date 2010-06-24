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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel sliced
 *
 * @version $Id$
 * @since 1.0
 */
public class ControlBarPanel extends JPanel {

	private int relativeHeight;
	private MSlidePanel slidePanel;
	private JButton slideButton;
	private JPanel upperHalf;
	private JPanel lowerHalf;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2420450895831821731L;
	
	/**
	 * 
	 */
	public ControlBarPanel() {
		init();
	}
	
	/**
	 * 
	 * @param slidePanel
	 */
	public ControlBarPanel(MSlidePanel slidePanel) {
		this.init();
	}
	
	/**
	 * Initializes ControlBarPanel.
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		this.setSize(200,30);
		//upper half
		this.upperHalf = new JPanel();
		this.upperHalf.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.upperHalf.add(new JButton("Test"));
		this.upperHalf.add(new JButton("Test"));
		this.upperHalf.add(new JButton("Test"));
		this.upperHalf.add(new JButton("Test"));
		
		
		this.lowerHalf = new JPanel();
		this.lowerHalf.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		this.lowerHalf.add(new JButton("Test"));
		
		
		
		this.add(this.upperHalf, BorderLayout.NORTH);
		this.add(this.lowerHalf, BorderLayout.SOUTH);
		this.updateUI();
	}
	
	public void setSlidePanel(MSlidePanel slidePanel) {
		this.slidePanel = slidePanel;
		this.relativeHeight = (int) lowerHalf.getPreferredSize().getHeight();
		this.slideButton = new JButton("Slide");
		this.upperHalf.add(this.slideButton);
		this.addSlideListeners();
	}
	
	
	
	
	
	
	/**
	 * Adds action listeners to
	 */
	private void addSlideListeners() {
		this.slideButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ControlBarPanel.this.slidePanel.isMinimized()) {
					ControlBarPanel.this.slidePanel.slideUp();
				} else {
					ControlBarPanel.this.slidePanel.slideDown();
				}			
			}
		});
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRelativeHeight() {
		return this.relativeHeight;	
	}
}
