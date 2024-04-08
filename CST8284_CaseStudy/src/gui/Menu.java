package gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {
	private final JMenuBar mainMenu = new JMenuBar();
	private final JMenu fileMenu = new JMenu("File");
	private final JMenuItem newGame = new JMenuItem("New Game");
	private final JMenuItem open = new JMenuItem("Open");
	private final JMenuItem save = new JMenuItem("Save");
	private final JMenuItem exit = new JMenuItem("Exit");
	
	public Menu(JFrame frame) {
		
		// Layout Menu		
		fileMenu.add(newGame);
		fileMenu.addSeparator();
		fileMenu.add(open);
		fileMenu.add(save);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		
		// newGame Button clicked
		newGame.addActionListener(event -> {
			System.out.println("Clicked newGame.");
		});
		
		open.addActionListener(event -> {
			System.out.println("Clicked open.");
		});
		
		save.addActionListener(event -> {
			System.out.println("Clicked save.");
		});
		
		exit.addActionListener(event -> {
			System.exit(0);
		});
		
		// Add File menu and set the menu bar.		
		mainMenu.add(fileMenu);	
		frame.setJMenuBar(mainMenu);
	}

}
