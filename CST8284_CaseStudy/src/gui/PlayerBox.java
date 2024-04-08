package gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerBox {
	// UI Components for Player	
	private final JPanel panel = new JPanel(new BorderLayout(0,0));
	private final JLabel playerTitle = new JLabel("BLACK");
	private final JLabel pointDiff = new JLabel();
	private final JPanel capturedPiecesArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	
	public JLabel getPlayerTitle() {return this.playerTitle;}
	public JPanel getCapturedPiecesArea() {return this.capturedPiecesArea;}
	
	public void setPlayerTitle(boolean isLightPieces) {
		if (isLightPieces) this.playerTitle.setText("WHITE");
	}
	
	public void setPointLabel(int diff) {
		if (diff > 0) {
			pointDiff.setText("+"+diff);
		} else {
			pointDiff.setText(null);
		}
	}
	
	public void addToGUI(JPanel gui, String position) {
		this.panel.setPreferredSize(new Dimension(gui.getWidth(), 40));
		this.panel.add(this.playerTitle, BorderLayout.PAGE_START);
		
		this.capturedPiecesArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.capturedPiecesArea.add(pointDiff);
		
		this.panel.add(capturedPiecesArea, BorderLayout.PAGE_END);
		gui.add(this.panel, position);
	}
}
