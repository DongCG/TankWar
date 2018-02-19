package programe;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Bullet{

	int x,y,speed,dir;
	boolean isAlive,isMe;
	MainWin mw;
	public Bullet(int x, int y, int speed, int dir,boolean isMe,MainWin mw) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.dir = dir;
		isAlive = true;
		this.isMe = isMe;
		this.mw = mw;
	}
	
	public void draw(Graphics g){
		if(!isAlive){
			mw.bulletList.remove(this);
		}
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 2, 2);
	}
	
	public void move(){
		switch(dir){
		case Util.LEFT:
			x-=speed; break;
		case Util.RIGHT:
			x+=speed; break;
		case Util.DOWN:
			y+=speed; break;
		case Util.UP:
			y-=speed; break;
		}
		
		if (x <= 0||y <= 60||x >= Util.GAME_WIDTH|| y >= Util.GAME_HEIGHT)
		    isAlive = false;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, 2, 2);
	}
	
	
	public boolean hitTank(Tank t){
		return getRect().intersects(t.getRect())&&(this.isMe!=t.isMe);
	}
	
	public void hitAllTank(){
		if(hitTank(mw.tank)&&mw.tank.isAlive){
			isAlive = false;
			mw.tank.isAlive = false;
			mw.explodeList.add(new Explode(mw.tank.x,mw.tank.y,mw));
			Util.addRecord(mw.score,Util.getCurrentTime());
		}
		for(int i=0;i<mw.tankList.size();i++){
			Tank t = mw.tankList.get(i);
			if(hitTank(t)){
				isAlive = false;
				mw.score+=10;
				mw.tankList.remove(t);
				mw.explodeList.add(new Explode(t.x,t.y,mw));
			}
		}
	}
	
	
	public boolean hitBrick(Brick t){
		return getRect().intersects(t.getRect());
	}
	public void hitHeart(Heart h){
		if(getRect().intersects(h.getRect())){
			isAlive = false;
			h.isAlive = false;
			Util.addRecord(mw.score,Util.getCurrentTime());
		}
	}
	
	public void hitAllBrick(){
		for(int i=0;i<mw.brickList.size();i++){
			Brick b = mw.brickList.get(i);
			if(hitBrick(b)){
				isAlive = false;
				mw.brickList.remove(b);
			}
		}
	}

}
