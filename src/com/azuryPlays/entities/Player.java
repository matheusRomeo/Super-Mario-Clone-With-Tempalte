package com.azuryPlays.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.azuryPlays.entities.itens.Moeda;
import com.azuryPlays.main.Game;
import com.azuryPlays.world.Camera;
import com.azuryPlays.world.World;

public class Player extends Entity{
	public static final int PLAYER_SIZE = 16;
	
	public static int currentCoins;
	public static int maxCoins =100;
	
	//Movimentos
	public double speed =2;
	public boolean right, left;
	public boolean groundCheck;
	public boolean jump = false, isJumping= false;	
	public int jumpHeight = 48;
	public int  jumpFrames = 0;
	/*smooth Gravity*/
	private double gravity = 0.3;
	private double verticalSpeed =0;
	
	public int dir=0;
	public int rightDir=0, leftDir =1;
	public boolean moved = false;
	
	//graficos ANIMAÇÃO
	private BufferedImage[] rightPlayer; 
	private BufferedImage[] leftPlayer;
	private int frames =0; private int maxFrames =8;
	private int index=0; private int maxIndex = 4;

	
	
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		
		rightPlayer = new BufferedImage[4];
		rightPlayer[0] = Game.spritesheet.getSprite(48, 48, 16, 16);
		rightPlayer[1] = Game.spritesheet.getSprite(32, 48, 16, 16);
		rightPlayer[2] = Game.spritesheet.getSprite(48, 48, 16, 16);
		rightPlayer[3] = Game.spritesheet.getSprite(64, 48, 16, 16);
		leftPlayer = new BufferedImage[4];
		leftPlayer[0] = Game.spritesheet.getSprite(16, 32, 16, 16);
		leftPlayer[1] = Game.spritesheet.getSprite(0, 32, 16, 16);
		leftPlayer[2] = Game.spritesheet.getSprite(16, 32, 16, 16);
		leftPlayer[3] = Game.spritesheet.getSprite(32, 32, 16, 16);
		

	}
	
	public void tick(){
		depth = 0;
		if(groundCheck) {
			jumpHeight = 48;
		}
		
		smoothGravity();
		
		/*
		if(World.isFree((int)x, (int)(y+World.GRAVITY)) && isJumping == false) {
			y+=World.GRAVITY;
			groundCheck = true;
			for(int i =0; i< Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Enemy) {
					if(Entity.isColidding(this, e)) {
						isJumping = true;
						jumpHeight = 24;
						((Enemy) e).damage(1);
					}
				}
			}
		}
		*/
		
		
		if(right && World.isFree((int)(x+speed), (int)y)) {
			
			moved =true;
			x+=speed;
			dir = rightDir;
		
			
		}else if(left && World.isFree((int)(x-speed), (int)y)) {
			
			moved=true;
			x-=speed;
			dir = leftDir;
			
		}
		
		
		if(right ==false && left == false) {
			moved = false;
		}
		if(jump) {
			if(!World.isFree(this.getX(), this.getY()+1)) {
				isJumping = true;				
			}else {
				jump = false;
			}
		}
		if(isJumping) {
			groundCheck = false;
			if(World.isFree(this.getX(), this.getY()-2)) {
				y-=4;
				jumpFrames+=4;
				if(jumpFrames >= jumpHeight) {
					isJumping = false;
					jump =false;
					jumpFrames = 0;
				}
				
			}else {
				isJumping = false;
				jump =false;
				jumpFrames = 0;
			}
		}
		
		//colisão com objetos
		for(int i =0; i< Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Moeda) {
				if(Entity.isColidding(this, e)) {
					Game.entities.remove(e);
					currentCoins++;
				}
			}
		}
		
		// animation
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames =0;
				index++;
				if(index >= maxIndex) 
					index=0;					
			}
		}
		
		//Camera
		Camera.x = Camera.clamp((int)x - Game.WIDTH /2, 0, World.WIDTH*16 -Game.WIDTH);
		Camera.y = Camera.clamp((int)y - Game.HEIGHT /2, 0, World.HEIGHT*16 -Game.HEIGHT);
		
		//game over
		
	}
	
	private void smoothGravity() {
		verticalSpeed+=gravity;
		if(!World.isFree((int)x, (int)(y+1)) && jump) {
			verticalSpeed = -5; //altura do pulo (quanto menor o numero mais alto o pulo)
			jump = false;
		}
		if(!World.isFree((int)x, (int)(y+verticalSpeed))) {
			//correção de salto, para que o jogador toque definitivamente no chão ou no teto
			int sing = 0;
			if(verticalSpeed >= 0) {
				sing = 1;
			}else {
				sing = -1;
			}
			while(World.isFree((int)x, (int)(y+sing))) {
				this.y = y+sing;
			}
			verticalSpeed = 0;
		}
		y = y+ verticalSpeed;
	}
	
	public void render(Graphics g) {
		if(!moved) {
			if(dir == rightDir)
				if(!isJumping)
					sprite = Game.spritesheet.getSprite(16*3, 16*3, PLAYER_SIZE, PLAYER_SIZE);
				else
					sprite = Game.spritesheet.getSprite(0, 16*3, PLAYER_SIZE, PLAYER_SIZE);
			else
				if(!isJumping)
					sprite = Game.spritesheet.getSprite(16*1, 16*2, PLAYER_SIZE, PLAYER_SIZE);
				else
					sprite = Game.spritesheet.getSprite(16*4, 16*2, PLAYER_SIZE, PLAYER_SIZE);
		}else {
			if(dir == rightDir) {
				sprite = rightPlayer[index];
			
			}else if(dir == leftDir) {
				sprite = leftPlayer[index];			
			}
		}
		super.render(g);
	}
	

	


}
