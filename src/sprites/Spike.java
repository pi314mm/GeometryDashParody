package sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.io.InputStream;

public class Spike extends Sprite{
	private static String IMAGE_FILE = "Spike.png";
	
	private static Sprite createSprite(int x, int y, int direction) throws Exception{
		if(direction < 0 || direction > 3){
			throw new Exception("Direction not valid");
		}
		
		//import image for spike
		InputStream stream = Spike.class.getClassLoader().getResourceAsStream(IMAGE_FILE);
		Image image = ImageTool.readImage(stream);
		image = ImageTool.resize(image,Sprite.SIZE,Sprite.SIZE);
		
		//set hitbox dimensions
		Polygon hitbox = new Polygon();
		switch(direction){
			case 0:
				/*
				 * --#
				 * -# #
				 * #####
				 */
				hitbox.addPoint(0,Sprite.SIZE);
				hitbox.addPoint(Sprite.SIZE / 2,0);
				hitbox.addPoint(Sprite.SIZE,Sprite.SIZE);
				break;
			case 1:
				/*
				 * #
				 * ###
				 * #
				 */
				image = ImageTool.rotate(image,90);
				hitbox.addPoint(0,0);
				hitbox.addPoint(Sprite.SIZE,Sprite.SIZE / 2);
				hitbox.addPoint(0,Sprite.SIZE);
				break;
			case 2:
				/*
				 * #####
				 * -# #
				 * --#
				 */
				image = ImageTool.rotate(image,180);
				hitbox.addPoint(0,0);
				hitbox.addPoint(Sprite.SIZE,0);
				hitbox.addPoint(Sprite.SIZE / 2,Sprite.SIZE);
				break;
			case 3:
				/*
				 * --#
				 * ###
				 * --#
				 */
				image = ImageTool.rotate(image,270);
				hitbox.addPoint(Sprite.SIZE,0);
				hitbox.addPoint(Sprite.SIZE,Sprite.SIZE);
				hitbox.addPoint(0,Sprite.SIZE / 2);
				break;
		}
		
		//create sprite
		Sprite sprite = null;
		try{
			sprite = new Sprite(x,y,image,hitbox);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sprite;
	}
	
	
	/**
	 * Creates a spike object with a top left rectangle corner at x,y and pointing in the specified
	 * direction
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 *            0-pointing up
	 *            1-pointing right
	 *            2-pointing down
	 *            3-pointing left
	 */
	public Spike(int x, int y, int direction) throws Exception{
		this(createSprite(x,y,direction));
	}
	
	/**
	 * Creates a spike object with a top left rectangle corner at point p and pointing in the
	 * specified direction
	 * 
	 * @param p
	 *            Point for top left corner
	 * @param direction
	 *            0-pointing up
	 *            1-pointing right
	 *            2-pointing down
	 *            3-pointing left
	 */
	public Spike(Point p, int direction) throws Exception{
		this(p.x,p.y,direction);
	}
	
	public Spike(Sprite sprite){
		super(sprite);
	}
}
