package com.azuryPlays.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.azuryPlays.entities.Enemy;
import com.azuryPlays.entities.Entity;
import com.azuryPlays.entities.Player;
import com.azuryPlays.entities.itens.Moeda;
import com.azuryPlays.main.Game;
import com.azuryPlays.world.tiles.CenaryTile;
import com.azuryPlays.world.tiles.SolidTile;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	public static final double GRAVITY =4;
	
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new CenaryTile(xx*TILE_SIZE,yy*TILE_SIZE,Tile.TILE_CENARY);
									
					/* '' Lógica de Renderização de sprites no mapa ''*/
					if(pixelAtual == 0xFF000000) {//PRETO
						tiles[xx+(yy * WIDTH)] = new  CenaryTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_CENARY);
						
					}else if(pixelAtual == 0xFFFFFFFF) {//BRANCO
						tiles[xx+(yy * WIDTH)] = new  SolidTile(xx*TILE_SIZE, yy*TILE_SIZE, Tile.TILE_WALL);
						if(yy-1 >=0 && pixels[xx+ (yy-1)* map.getWidth()]== 0xFFFFFFFF) {
							tiles[xx+(yy * WIDTH)]  =new SolidTile(xx*TILE_SIZE, yy*TILE_SIZE, Game.spritesheet.getSprite(16, 16, TILE_SIZE, TILE_SIZE));
						}
					}else if(pixelAtual == 0xFF0000FF) {
						//Jogador
						Game.player.setX(xx*Player.PLAYER_SIZE);
						Game.player.setY(yy*Player.PLAYER_SIZE);
					
					}else if(pixelAtual == 0xFFFF0000) {
						//Inimigo
						Enemy enemy = new Enemy(xx*Entity.ENTITY_SIZE, yy*Entity.ENTITY_SIZE, Entity.ENTITY_SIZE,Entity.ENTITY_SIZE,1, Entity.ENEMY1);
						Game.entities.add(enemy);
					
					}else if(pixelAtual == 0xFFFFD800) {
						//Moedas
						Moeda moeda = new Moeda(xx*Entity.ENTITY_SIZE, yy*Entity.ENTITY_SIZE, Entity.ENTITY_SIZE,Entity.ENTITY_SIZE,0, Entity.COIN);
						Game.entities.add(moeda);
					
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof SolidTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof SolidTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof SolidTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof SolidTile));
	}
	
	public static void restartGame(){
		/* refatorar objetos de importancia no jogo*/
		Game.player = new Player(0,0,Player.PLAYER_SIZE,Player.PLAYER_SIZE,0,Game.spritesheet.getSprite(0,0,Player.PLAYER_SIZE,Player.PLAYER_SIZE));
		
		/*Limpar todas as listas de entidade */
		Game.entities.clear();
		
		/*reabrir listas de entidade */
		Game.entities.add(Game.player);
		
		return;
	}
	
	public void render(Graphics g){
		int xstart = Camera.x /TILE_SIZE;
		int ystart = Camera.y /TILE_SIZE;
		
		/*REENDERIZANDO MUNDO A PARTIR DO TAMANHO DA TELA DO JOGO*/
		int xfinal = xstart + (Game.WIDTH /TILE_SIZE);
		int yfinal = ystart + (Game.HEIGHT /TILE_SIZE);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
