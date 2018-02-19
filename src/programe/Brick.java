package programe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Brick{

	int x,y;
	boolean isAlive = true;
	MainWin mw;
	BufferedImage img = Util.getImage("resource/brick.png");
	public Brick(int x, int y,MainWin mw) {
		this.x = x;
		this.y = y;
		this.mw = mw;
	}
	
	public void draw(Graphics g){
		if(!isAlive){
			mw.brickList.remove(this);
			return;
		}
		g.drawImage(img, x,y,null);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, 24, 24);
	}

}
