package com.azuryPlays.world.astar;

public class Vector2 {

	public int x,y;
	
	public Vector2(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object object) {
		Vector2 vec = (Vector2) object;
		if(vec.x == this.x && vec.y == this.y) {
			return true;
		}
		
		return false;
	}
	
}
