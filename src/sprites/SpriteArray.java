package sprites;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SpriteArray{
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	private Player player;
	
	public void setColor(Color c){
		for(Sprite s:sprites){
			s.setColor(c);
		}
	}
	
	/*@formatter:off
	public static void main(String[] args){//for testing the array
		SpriteArray test = new SpriteArray();
		for(int i=0; i<10; i++){
			System.out.println("Update:"+(i+1));
			System.out.println("Player Height: "+test.getPlayer().getY());
			if(test.updateSprites()){
				System.out.println("died");
			}
			//test.printSprites();
		}
	}@formatter:on*/
	public void printSprites(){
		for(Sprite s:sprites){
			System.out.println(s);
		}
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public SpriteArray(String mapFileName){
		//sprites.clear();
		InputStream map = SpriteArray.class.getClassLoader().getResourceAsStream(mapFileName);
		try{
			Scanner scan = new Scanner(map);
			int height = 0;
			while(scan.hasNext()){
				String line = scan.nextLine();
				for(int length = 0; length < line.length(); length++){
					switch(line.charAt(length)){
						case 'P':
							player = new Player(length * Sprite.SIZE,height);
							sprites.add(player);
							break;
						case 'B':
							sprites.add(new Block(length * Sprite.SIZE,height));
							break;
						case '^':
							try{
								sprites.add(new Spike(length * Sprite.SIZE,height,0));
							}catch(Exception e){
								System.err.println("Error: Spike in file has an incorrect value for the direction");
								e.printStackTrace();
							}
							break;
						case '>':
							try{
								sprites.add(new Spike(length * Sprite.SIZE,height,1));
							}catch(Exception e){
								System.err.println("Error: Spike in file has an incorrect value for the direction");
								e.printStackTrace();
							}
							break;
						case 'v':
							try{
								sprites.add(new Spike(length * Sprite.SIZE,height,2));
							}catch(Exception e){
								System.err.println("Error: Spike in file has an incorrect value for the direction");
								e.printStackTrace();
							}
							break;
						case '<':
							try{
								sprites.add(new Spike(length * Sprite.SIZE,height,3));
							}catch(Exception e){
								System.err.println("Error: Spike in file has an incorrect value for the direction");
								e.printStackTrace();
							}
							break;
					}
				}
				height += Sprite.SIZE;
			}
			scan.close();
		}catch(Exception e){
			System.err.println("Error: " + map + " can't be opened");
			e.printStackTrace();
		}
		//setColor(new Color(51,51,204));//set everything to a blue color initially
	}
	
	/**
	 * Updates all the sprites and the player
	 * 
	 * @return true if player died
	 */
	public boolean updateSprites(){
		Sprite.update();
		player.gravity();
		for(Sprite s:sprites){
			if(player.checkCollision(s)){
				playerDied(s);
				return true;
			}
		}
		return false;
	}
	
	private void playerDied(Sprite s){
		System.out.println("died on " + s);
		System.out.println(player);
		Sprite.reset();
		player.resetPlayer();
	}

	public ArrayList<Sprite> getSprites(){
		return sprites;
	}
	
	public void jump(){
		player.jump();
	}
}
