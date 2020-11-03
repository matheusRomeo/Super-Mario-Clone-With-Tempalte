package com.azuryPlays.entities.itens;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.azuryPlays.entities.Entity;
import com.azuryPlays.entities.Iten;
import com.azuryPlays.entities.interfaces.Icollectable;
import com.azuryPlays.main.Game;

public class Moeda extends Iten implements Icollectable{
	
	private BufferedImage[] Animin = new  BufferedImage[4];
	private int frames =0; private int maxFrames =12;
	private int index=0; private int maxIndex = 4;

	public Moeda(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		for(int i=0; i<maxIndex; i++) {
			Animin[i] = Game.spritesheet.getSprite(16*(2+i), 16, Entity.ENTITY_SIZE, Entity.ENTITY_SIZE);
		}
	}
	
	public void tick() {
		frames++;
		if(frames == maxFrames) {
			frames =0;
			index++;
			if(index >= maxIndex) 
				index=0;					
		}
	}
	
	
	public void render(Graphics g) {
		sprite = Animin[index];
		super.render(g);
	}

	
	//Implemented Methods
	@Override
	public void collect() {
		// TODO Auto-generated method stub
		
	}
}
