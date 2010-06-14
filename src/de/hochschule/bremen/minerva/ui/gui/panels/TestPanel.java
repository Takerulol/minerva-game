package de.hochschule.bremen.minerva.ui.gui.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import de.hochschule.bremen.minerva.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.vo.ApplicationConfiguration;


public class TestPanel extends JLayeredPane {
	Image bgbild = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TestPanel() {
		super();
		this.setSize(1000, 700);

		//JPanel bg = new JPanel(new FlowLayout());
		this.setLayout(new FlowLayout());
		//String path = "D:/testbg2.png";
//		String path = "D:/workspace/minerva-game/assets/userinterface/login.jpg";
//		File file = new File(path);
//		
//		
//		try {
//			bgbild = ImageIO.read(file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	
		this.add(new Background(this.getClass()));
//		JLabel test1 = new JLabel(new ImageIcon(bgbild));
//		test1.setBorder(new EmptyBorder(null));
		//this.add(new JLabel(new ImageIcon(bgbild)));
		
		//this.add(bg,new Integer(10));
		//this.add(new Background("login","jpg"),new Integer(10));
		//bg.updateUI();
		//this.repaint();
		this.updateUI();
	}
	
//	public void paint (Graphics g) {
//		g.drawImage(bgbild, 0, 0, 1000, 700, this);
//	}
	

}
