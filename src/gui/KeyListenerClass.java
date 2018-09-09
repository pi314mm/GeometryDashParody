package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import sprites.Player;

public class KeyListenerClass implements KeyListener{
	private static KeyListenerClass instance = new KeyListenerClass();
	
	private void stopJumping(){
		spaceIsPressed = false;
		upIsPressed = false;
	}
	
	private boolean spaceIsPressed = false;
	private boolean upIsPressed = false;
	private boolean isPaused = false;
	
	private KeyListenerClass(){
		Thread thread = new Thread(){
			@Override
			public void run(){
				while(true){
					if((spaceIsPressed || upIsPressed) && Player.getCurrentPlayer() != null){
						Player.getCurrentPlayer().jump();
					}
					try{
						Thread.sleep(1);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		if(isPaused){
			return;
		}
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP){
			upIsPressed = true;
		}else if(keyCode == KeyEvent.VK_SPACE){
			spaceIsPressed = true;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e){
		if(isPaused){
			return;
		}
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_UP){
			upIsPressed = false;
		}else if(keyCode == KeyEvent.VK_SPACE){
			spaceIsPressed = false;
		}else if(keyCode==KeyEvent.VK_P){
			Player.switchInvincibilty();
		}
	}
	
	public static KeyListenerClass getInstance(){
		return instance;
	}
	
	public boolean isJumping(){
		return spaceIsPressed || upIsPressed;
	}
	
	public void stop(){
		this.isPaused = true;
		stopJumping();
	}
	
	public void start(){
		this.isPaused = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e){
	}
}
