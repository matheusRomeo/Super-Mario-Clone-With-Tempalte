package com.azuryPlays.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.azuryPlays.entities.interfaces.Idamageable;
import com.azuryPlays.entities.interfaces.Ikillable;
import com.azuryPlays.main.Game;
import com.azuryPlays.world.World;

public class Enemy extends Entity implements Idamageable, Ikillable{
	
	private int life = 1;
	
	public boolean right = true, left = false;
	public int dir=0;
	public int rightDir=0, leftDir =1;
	
	//graficos ANIMAÇÃO
		private BufferedImage[] rightAnimin; 
		private BufferedImage[] leftAnimin;
		private int frames =0; private int maxFrames =8;
		private int index=0; private int maxIndex = 4;

	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		rightAnimin = new BufferedImage[4];
		rightAnimin[0] = Game.spritesheet.getSprite(16*4, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		rightAnimin[1] = Game.spritesheet.getSprite(16*3, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		rightAnimin[2] = Game.spritesheet.getSprite(16*4, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		rightAnimin[3] = Game.spritesheet.getSprite(16*3, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		leftAnimin = new BufferedImage[4];
		leftAnimin[0] = Game.spritesheet.getSprite(16*5, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		leftAnimin[1] = Game.spritesheet.getSprite(16*6, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		leftAnimin[2] = Game.spritesheet.getSprite(16*5, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		leftAnimin[3] = Game.spritesheet.getSprite(16*6, 0, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
	}
	
	public void tick() {
		depth = 1;
		
		if(World.isFree((int)x, (int)(y+World.GRAVITY)))
			y+=World.GRAVITY;
		
		if(right && World.isFree((int)(x+speed), (int)y)) {
			x+=speed;
			dir = rightDir;	
			if(World.isFree((int)(x+World.TILE_SIZE), (int)y+1)||!World.isFree((int)(x+speed), (int)y)) {
				right = false;
				left = true;
			}
			
			
		}else if(left && World.isFree((int)(x-speed), (int)y)) {
			x-=speed;
			dir = leftDir;
			if(World.isFree((int)(x-World.TILE_SIZE), (int)y+1)||!World.isFree((int)(x-speed), (int)y)) {
				right = true;
				left = false;
			}
			
		}
		
		frames++;
		if(frames == maxFrames) {
			frames =0;
			index++;
			if(index >= maxIndex) 
				index=0;					
		}
	}

	public void render(Graphics g) {
		if(dir == rightDir) {
			sprite = rightAnimin[index];
			
		}else if(dir == leftDir) {
			sprite = leftAnimin[index];			
		}
		super.render(g);
	}

	@Override
	public void kill() {
		Game.entities.remove(this);
		
	}

	@Override
	public void damage(int hurt) {
		life-= hurt;
		if(life <=0) {
			kill();
		}
	}
}
