package programe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Heart {
	
	int x,y;
	boolean isAlive = true;
	BufferedImage img = Util.getImage("resource/heart.png");
	Heart(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g){
		if(!isAlive)
			return;
		g.drawImage(img,x,y,null);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, 24, 24);
	}

}
