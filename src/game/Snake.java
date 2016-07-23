package game;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Snake extends JFrame {
	
	public Snake() {
		
		try {
			add(new SnakeBoard());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setResizable(false);
		pack();
		setTitle("Snake");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new Snake();
				frame.setVisible(true);
				
			}
		});
	}
}
