package game;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeBoard extends JPanel implements ActionListener {
	
	private final int GAME_WIDTH = 600;
	private final int GAME_HEIGHT = 600;
	private final int DOT_SIZE = 10;
	private final int ALL_DOTS = (GAME_WIDTH * GAME_HEIGHT) / (DOT_SIZE * DOT_SIZE);
	private final int RAND_POS = 29;
	private final int DELAY = 140;
	
	private final int x[] = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	
	private static final String GAME_OVER = "Game Over";
	
	private int dots;
	private int cherry_x;
	private int cherry_y;
	
	private boolean left = false;
	private boolean right = true;
	private boolean up = false;
	private boolean down = false;
	private boolean inGame = true;
	
	private Timer timer;
	private Image ball;
	private Image cherry;
	private Image head;
	
	private int score = 0;
	
	public SnakeBoard() throws Exception {
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				keypressAdapter(e);
				
			}
		});
		
		setBackground(Color.BLACK);
		setFocusable(true);
		
		setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		loadImages();
		initGame();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		doDrawing(g);
	}
	
	private void doDrawing(Graphics g) {
		
		if (inGame) {
			g.drawImage(cherry, cherry_x, cherry_y, this);
			
			for (int z = 0; z < dots; z++) {
				if (z == 0) {
					g.drawImage(head, x[z], y[z], this);
				} else {
					g.drawImage(ball, x[z], y[z], this);
				}
			}
			
			Toolkit.getDefaultToolkit().sync();
		} else {
			gameOver(g);
		}
		
	}
	
	private void gameOver(Graphics g) {
		
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);
		
		JButton newGame = new JButton("New Game");
		newGame.setBounds(250, 150, 100, 30);
		newGame.setVisible(true);
		newGame.addActionListener(this);
		add(newGame);
		
		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(GAME_OVER + ". Score = " + score, (GAME_WIDTH - metr.stringWidth(GAME_OVER + ". Score = " + score)) / 2, GAME_HEIGHT / 2);
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (inGame) {
			
			checkApple();
			checkCollision();
			move();
		}
		
		if (null != e && null != e.getActionCommand() && e.getActionCommand().equals("New Game")) {
			Snake.main(null);
		}
		
		repaint();
		
	}
	
	private void initGame() {
		dots = 3;
		
		for (int z = 0; z < dots; z++) {
			x[z] = 50 - z * 10;
			y[z] = 50;
		}
		
		locateApple();
		
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	private void locateApple() {
		
		int x = (int)(Math.random() * RAND_POS);
		cherry_x = ((x * DOT_SIZE));
		
		int y = (int)(Math.random() * RAND_POS);
		cherry_y = ((y * DOT_SIZE));
		
	}
	
	private void checkApple() {
		
		if ((x[0] == cherry_x) && (y[0] == cherry_y)) {
			dots++;
			score++;
			locateApple();
		}
		
	}
	
	private void move() {
		
		for (int z = dots; z > 0; z--) {
			x[z] = x[(z - 1)];
			y[z] = y[(z - 1)];
		}
		
		if (left) {
			x[0] -= DOT_SIZE;
		}
		
		if (right) {
			x[0] += DOT_SIZE;
		}
		
		if (up) {
			y[0] -= DOT_SIZE;
		}
		
		if (down) {
			y[0] += DOT_SIZE;
		}
		
	}
	
	private void checkCollision() {
		
		for (int z = dots; z > 0; z--) {
			if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
				inGame = false;
			}
		}
		
		if (y[0] >= GAME_HEIGHT) {
			inGame = false;
		}
		
		if (y[0] < 0) {
			inGame = false;
		}
		
		if (x[0] < 0) {
			inGame = false;
		}
		if (x[0] >= GAME_WIDTH) {
			inGame = false;
		}
		
		if (!inGame) {
			timer.stop();
		}
	}
	
	private void loadImages() throws Exception {
		try {
		
			String ballRes = "dot.png";
			Image ballImage = ImageIO.read(getClass().getResourceAsStream(ballRes));
			ImageIcon iid = new ImageIcon(ballImage);
			ball = iid.getImage();
			
			String cherryRes = "apple.png";
			Image cherryImage = ImageIO.read(getClass().getResourceAsStream(cherryRes));
			ImageIcon iia = new ImageIcon(cherryImage);
			cherry = iia.getImage();
			
			String headRes = "head.png";
			Image headImage = ImageIO.read(getClass().getResourceAsStream(headRes));
			ImageIcon iih = new ImageIcon(headImage);
			head = iih.getImage();
			
		} catch (Exception e) {
			throw new Exception("Failed to load images.");
		}
	}
	
	private void keypressAdapter(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT && !right) {
			left = true;
			right = false;
			down = false;
			up = false;
		}
		
		if (key == KeyEvent.VK_RIGHT && !left) {
			left = false;
			right = true;
			down = false;
			up = false;
		}
		
		if (key == KeyEvent.VK_DOWN && !up) {
			left = false;
			right = false;
			down = true;
			up = false;
		}
		
		if (key == KeyEvent.VK_UP && !down) {
			left = false;
			right = false;
			down = false;
			up = true;
		}
		
	}

}
