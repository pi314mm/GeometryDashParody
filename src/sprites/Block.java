package sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.io.InputStream;

public class Block extends Sprite{
	private static String IMAGE_FILE = "Block.png";
	
	private static Sprite createSprite(int x, int y){
		//import image for block
		InputStream stream = Block.class.getClassLoader().getResourceAsStream(IMAGE_FILE);
		Image image = ImageTool.readImage(stream);
		image = ImageTool.resize(image,Sprite.SIZE,Sprite.SIZE);
		
		//set hitbox dimensions
		Polygon hitbox = new Polygon();
		hitbox.addPoint(0,0);
		hitbox.addPoint(Sprite.SIZE,0);
		hitbox.addPoint(Sprite.SIZE,Sprite.SIZE);
		hitbox.addPoint(0,Sprite.SIZE);
		
		//create sprite
		Sprite sprite = null;
		try{
			sprite = new Sprite(x,y,image,hitbox);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sprite;
	}
	
	public Block(int x, int y){		
		this(createSprite(x,y));
	}
	
	public Block(Point p){
		this(p.x,p.y);
	}

	public Block(Sprite sprite){
		super(sprite);
	}
}
