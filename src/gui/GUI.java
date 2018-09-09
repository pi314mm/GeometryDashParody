package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import musicPlayer.Song;
import sprites.ImageTool;
import sprites.Player;
import sprites.Sprite;
import sprites.SpriteArray;

public class GUI extends Thread{
	private boolean isRunning = true;
	private Canvas canvas;
	private BufferStrategy strategy;
	private BufferedImage background;
	private Graphics2D backgroundGraphics;
	private Graphics2D graphics;
	private JFrame frame;
	private int width = 1000;
	private int height = 600;
	private int deaths = 0;
	private GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	private KeyListenerClass listener = KeyListenerClass.getInstance();
	private String songFileName;
	private InputStream songFile;
	private Song player;
	private SpriteArray sprites = null;
	
	// create a hardware accelerated image
	public final BufferedImage create(final int width, final int height, final boolean alpha){
		return config.createCompatibleImage(width,height,alpha?Transparency.TRANSLUCENT:Transparency.OPAQUE);
	}
	
	// Setup
	public GUI(String songFileName, String fileName){
		this.songFileName = songFileName;
		Thread thread = new Thread(){
			@Override
			public void run(){
				setSprites(new SpriteArray(fileName));
			}
		};
		thread.start();
	}
	
	@Override
	public void start(){
		songFile = GUI.class.getClassLoader().getResourceAsStream(songFileName);
		player = new Song(songFile);
		// JFrame
		frame = new JFrame();
		frame.addWindowListener(new FrameClose());
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setSize(width,height);
		frame.setResizable(false);
		frame.addKeyListener(listener);
		frame.setIconImage(new ImageIcon(Player.class.getClassLoader().getResource("Cube.png")).getImage());
		frame.setVisible(true);
		// Canvas
		canvas = new Canvas(config);
		canvas.addKeyListener(listener);
		canvas.setSize(width,height);
		frame.add(canvas,0);
		
		// Background & Buffer
		background = create(width,height,false);
		canvas.createBufferStrategy(2);
		do{
			strategy = canvas.getBufferStrategy();
		}while(strategy == null);
		super.start();
	}
	
	private class FrameClose extends WindowAdapter{
		@Override
		public void windowClosing(final WindowEvent e){
			isRunning = false;
		}
	}
	
	// Screen and buffer stuff
	private Graphics2D getBuffer(){
		if(graphics == null){
			try{
				graphics = (Graphics2D)strategy.getDrawGraphics();
			}catch(IllegalStateException e){
				return null;
			}
		}
		return graphics;
	}
	
	private boolean updateScreen(){
		graphics.dispose();
		graphics = null;
		try{
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			return(!strategy.contentsLost());
			
		}catch(NullPointerException e){
			return true;
			
		}catch(IllegalStateException e){
			return true;
		}
	}
	
	private void loadingScreenDisplay(){
		backgroundGraphics = (Graphics2D)background.getGraphics();
		do{
			Graphics2D bg = getBuffer();
			InputStream pic = GUI.class.getClassLoader().getResourceAsStream("loading.png");
			Image image = ImageTool.readImage(pic);
			backgroundGraphics.drawImage(image,0,0,width,height,null);
			bg.drawImage(background,0,0,null);
			bg.dispose();
		}while(!updateScreen());
	}
	
	public void run(){
		backgroundGraphics = (Graphics2D)background.getGraphics();
		Sprite.reset();
		while(sprites == null){//not done loading
			loadingScreenDisplay();
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		listener.start();
		Player.setCurrentPlayer(sprites.getPlayer());
		player.play();//play song
		long fpsWait = (long)(1.0 / 30 * 1000);
		main: while(isRunning){
			long renderStart = System.nanoTime();
			
			// Update Graphics
			do{
				Graphics2D bg = getBuffer();
				if(!isRunning){
					break main;
				}
				renderGame(backgroundGraphics); // this calls the draw method
				bg.drawImage(background,0,0,null);
				bg.dispose();
			}while(!updateScreen());
			
			updateGame();
			
			// Better do some FPS limiting here
			long renderTime = (System.nanoTime() - renderStart) / 1000000;
			try{
				Thread.sleep(Math.max(0,fpsWait - renderTime));
			}catch(InterruptedException e){
				Thread.interrupted();
				break;
			}
			renderTime = (System.nanoTime() - renderStart) / 1000000;
			
		}
		player.stop();
		frame.dispose();
		listener.stop();
		Sprite.reset();
		MainMenu.getInstance().display();
	}
	
	public void updateGame(){
		long time = System.currentTimeMillis();
		if(!player.isRunning()){
			JOptionPane.showMessageDialog(frame,"Congratulations! You win!");
			isRunning = false;
		}
		if(sprites.updateSprites()){
			//isRunning = false;
			//System.out.println("died");
			player.stop();
			deaths++;
			listener.stop();
			int n = JOptionPane.showConfirmDialog(frame,"You Died :(\nWould you like to try again? \nDeaths: " + deaths,"GAME OVER",JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.YES_OPTION){
				songFile = GUI.class.getClassLoader().getResourceAsStream(songFileName);
				player = new Song(songFile);
				player.play();
				listener.start();
			}else{
				isRunning = false;
			}
		}
		while(System.currentTimeMillis() < time + 10){
		}
		;
	}
	
	public void renderGame(Graphics2D g){
		g.setColor(Color.CYAN);
		g.fillRect(0,0,width,height);
		//File f = new File("pics/Cube.png");
		//System.out.println(f.exists());
		//System.out.println(sprites.ImageTool.readImage("pics/Cube.png"));
		//g.drawImage(sprites.ImageTool.readImage("pics/Cube.png"),0,0,50,50,null);
		for(Sprite s:sprites.getSprites()){
			g.drawImage(s.getImage(),s.getX(),s.getY(),s.getWidth(),s.getHeight(),null);
			//System.out.println(s);
		}
		//System.out.println("painting");
	}
	
	public void setSprites(SpriteArray spriteArray){
		this.sprites = spriteArray;
	}
}