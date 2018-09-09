package sprites;

import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.io.InputStream;

public class Player extends Sprite{
	private static boolean invincibilityMode = false;//for debugging, also a neat bonus feature
	private static Player currentPlayer;
	
	private int xInitial, yInitial;// defines the top left hand corner of the Sprite
	
	public static final double GRAVITY = 10; //gravity for calculating jump, positive because in down is in the positive y direction
	public static final int HEIGHT_OF_JUMP = 125; //height of jump in pixels
	public static final int LENGTH_OF_JUMP = 200; //length of jump in pixels
	
	private int lastY = this.getY();
	
	private double velocity = 0;//since up is in the negative y direction, the velocity must be negative when jumping up
	private boolean canJump = true; //is set to true when the player is on the floor
	private static final double AIR_TIME = 2 * Math.sqrt(2 * GRAVITY * HEIGHT_OF_JUMP) / GRAVITY; // t = 2 * v_y / g
	private static final double deltaT = Sprite.HORIZONTAL_MOVEMENT * AIR_TIME / LENGTH_OF_JUMP; //time difference between each update interval

	private static final String IMAGE_FILE = "Cube.png";
	
	public static void switchInvincibilty(){
		invincibilityMode=!invincibilityMode;
	}
	@Override
	public int getX(){
		return xInitial;
	}
	
	/**
	 * Checks if collided with another sprite
	 * Also checks special collision with a block in case the block is sliding along
	 * 
	 * @return true if crashed into sprite and died
	 */
	public boolean checkCollision(Sprite sprite){
		Area area = new Area(this.getHitbox());
		area.intersect(new Area(sprite.getHitbox()));
		if(Player.class.equals(sprite.getClass())){//can't die on itself
			return false;
		}
		if(!area.isEmpty() && Block.class.equals(sprite.getClass())){//crashes into a block while jumping
			//System.out.println("ON TOP OF BLOCK");
			//System.out.println(this);
			//System.out.println(sprite);
			//first calculate the vertical distance traveled in last update
			//double tempVelocity = velocity - GRAVITY * deltaT;//v_f = v_i + a*delta t
			//System.out.println(tempVelocity);
			//if this method isn't working properly, it is likely that this line is not supposed to be negative
			//int deltaY = (int)Math.round(tempVelocity * deltaT + .5 * GRAVITY * deltaT * deltaT);//deltay = -(vt + .5at^2) negative because the inverted y axis
			int deltaY = this.getY() - lastY;
			//System.out.println(deltaY);
			//System.out.println((lastY + Sprite.SIZE));
			if((lastY + Sprite.SIZE) - sprite.getY() < deltaY){//if the player is within tolerance level of the block
				//System.out.println("it worked once");
				this.setY(lastY);//move it back to the surface of the block
				//System.out.println(this.getY());
				canJump = true;
				velocity = 0;
				return false;
			}else{
				return !invincibilityMode;//crashed into side or bottom of block
			}
		}
		if(invincibilityMode){
			return false;
		}
		return !area.isEmpty();
	}
	
	private static Sprite createSprite(int x, int y){
		//import image for player
		InputStream stream = Player.class.getClassLoader().getResourceAsStream(IMAGE_FILE);
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
	
	public void gravity(){
		lastY = this.getY();
		setY((int)Math.round(getY() + velocity * deltaT + .5 * GRAVITY * deltaT * deltaT));//y = y_i + vt + .5at^2
		velocity = velocity + GRAVITY * deltaT;//v_f = v_i + a*delta t
		canJump=false;//if fell of block
	}
	
	/**
	 * This method should be called by an event listener checking for jumps
	 */
	public void jump(){
		if(canJump){
			canJump = false;
			velocity = -Math.sqrt(2 * GRAVITY * HEIGHT_OF_JUMP); //v_f^2=v_i^2+2ad
			//System.out.println(velocity);
		}
	}
	public void resetPlayer(){
		this.setY(yInitial);
		this.setX(xInitial);
	}
	public Player(int x, int y){
		this(createSprite(x,y));
		this.xInitial = x;
		this.yInitial = y;
	}
	
	public Player(Point p){
		this(p.x,p.y);
	}
	
	public Player(Sprite sprite){
		super(sprite);
	}

	public static Player getCurrentPlayer(){
		return currentPlayer;
	}

	public static void setCurrentPlayer(Player currentPlayer){
		Player.currentPlayer = currentPlayer;
	}
}
