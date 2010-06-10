package de.hochschule.bremen.minerva.ui.gui.panels;

import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class TestPanel extends JLayeredPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TestPanel() {
		super();
		JPanel bg = new JPanel(new FlowLayout());
		this.setLayout(new FlowLayout());
		String path = "D:/testbg2.png";
		File file = new File(path);
		
		Image bgbild = null;
		try {
			bgbild = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bg.add(new JLabel(new ImageIcon(bgbild)));
		this.add(bg,new Integer(10));
		bg.updateUI();
	}
	

}
