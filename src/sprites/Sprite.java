package sprites;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Sprite{
	private static int x = 0;
	private int xInitial, yInitial;// defines the top left hand corner of the Sprite
	private Image image;// image to display for Sprite
	private Polygon hitbox;// shape of the Sprite
	public static final int HORIZONTAL_MOVEMENT = 11;// the number of pixels moved per update
	public static final int SIZE = 50;//size of image height and width
	
	public void setColor(Color c){
		image = ImageTool.recolor(image,c);
	}
	
	
	/**
	 * Moves objects to the left the distance given by HORIZONTAL_MOVEMENT
	 * 
	 */
	public static void update(){
		x -= HORIZONTAL_MOVEMENT;
	}
	
	/**
	 * Sprite constructor
	 * 
	 * @param x
	 *            the top left corner x cord
	 * @param y
	 *            the top left corner y cord
	 * @param image
	 *            image used for appearance
	 * @param hitbox
	 *            hitbox for collision detection
	 * @throws Exception
	 *             thrown if hitbox bounding box and image bounding box are
	 *             different
	 */
	public Sprite(int x, int y, Image image, Polygon hitbox) throws Exception{
		this.xInitial = x;
		this.yInitial = y;
		this.image = image;
		this.hitbox = hitbox;
		/*
		 * if(image != null && hitbox != null && image.getWidth(null)!=-1 && image.getWidth(null) !=
		 * hitbox.getBounds().width && image.getHeight(null) != hitbox.getBounds().height){
		 * System.err.println("Image: " + image.getWidth(null) + ", " + image.getHeight(null));
		 * System.err.println("Hitbox: " + hitbox.getBounds().width + ", " +
		 * hitbox.getBounds().height);
		 * throw new Exception("The image and hitbox dimensions do not match");
		 * }
		 */
	}
	
	/**
	 * Sprite constructor
	 * 
	 * @param p
	 *            top left hand corner
	 * @param image
	 *            image used for appearance
	 * @param hitbox
	 *            hitbox for collision detection
	 * @throws Exception
	 *             thrown if hitbox bounding box and image bounding box are
	 *             different
	 */
	public Sprite(Point p, Image image, Polygon hitbox) throws Exception{
		this(p.x,p.y,image,hitbox);
	}
	
	/**
	 * Creates a sprite identical to the one passed into it
	 * 
	 * @param sprite
	 */
	public Sprite(Sprite sprite){
		this.xInitial = sprite.getX();
		this.yInitial = sprite.getY();
		this.image = sprite.getImage();
		this.hitbox = sprite.getInitHitbox();
	}
	
	private Polygon getInitHitbox(){
		return hitbox;
	}
	
	
	public void setImage(Image image){
		this.image = image;
	}
	
	public void setHitbox(Polygon hitbox){
		this.hitbox = hitbox;
	}
	
	public void setX(int x){
		this.xInitial = x;
	}
	
	public int getX(){
		return xInitial + x;
	}
	
	public void setY(int y){
		this.yInitial = y;
	}
	
	public int getY(){
		return yInitial;
	}
	
	public int getWidth(){
		return image.getWidth(null);
	}
	
	public int getHeight(){
		return image.getHeight(null);
	}
	
	public Image getImage(){
		return image;
	}
	
	@Override
	public String toString(){
		String s = String.format("%s at (%d,%d)\n",this.getClass().toString().replaceAll("class sprites.",""),this.getX(),this.getY());
		s += "hitbox: ";
		Polygon h = this.getHitbox();
		for(int i = 0; i < h.npoints; i++){
			s += String.format("(%d,%d)",h.xpoints[i],h.ypoints[i]);
		}
		return s;
	}
	
	/**
	 * Returns the bounding rectangle
	 * 
	 * @return the bounding box of the Sprite as a Rectangle
	 */
	public Rectangle getBoundingRect(){
		return new Rectangle(xInitial,yInitial,image.getWidth(null),image.getHeight(null));
	}
	
	public Polygon getHitbox(){
		int[] xpoints = new int[hitbox.npoints];
		int[] ypoints = new int[hitbox.npoints];
		for(int i = 0; i < hitbox.npoints; i++){
			xpoints[i] = hitbox.xpoints[i] + this.getX();
			ypoints[i] = hitbox.ypoints[i] + this.getY();
		}
		return new Polygon(xpoints,ypoints,hitbox.npoints);
	}


	public static void reset(){
		x=0;
	}
}