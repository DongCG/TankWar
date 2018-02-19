package programe;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Tank {

	MainWin mw;
	int x, y, speed;
	boolean isAlive = true, isMe, isStop = true;
	int dir;
	BufferedImage img = Util.getImage("resource/tank.png");

	Tank(int x, int y, int speed, int dir, boolean isme, MainWin mw) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.isMe = isme;
		this.speed = speed;
		this.mw = mw;
	}

	int lastX, lastY;

	public void keyPressed(KeyEvent e) {
		int keycode = e.getKeyCode();
		switch (keycode) {
		case KeyEvent.VK_LEFT:
			this.isStop = false;
			this.dir = Util.LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			this.isStop = false;
			this.dir = Util.RIGHT;
			break;
		case KeyEvent.VK_UP:
			this.isStop = false;
			this.dir = Util.UP;
			break;
		case KeyEvent.VK_DOWN:
			this.isStop = false;
			this.dir = Util.DOWN;
			break;
		case KeyEvent.VK_F:
			fire();
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keycode = e.getKeyCode();
		switch (keycode) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			this.isStop = true;
		}
	}

	public void fire() {
		mw.bulletList.add(new Bullet(x + 11, y + 11, 8, dir, this.isMe, mw));
	}

	// 随机函数
	static Random r = new Random();
	// 朝某一个方向走的步数
	static int step = r.nextInt(14) + 3;// 【3,16】

	public void move() {
		lastX = x;
		lastY = y;
		if (this.isStop && this.isMe)
			return;
		switch (dir) {
		case Util.LEFT:
			x -= speed;
			break;
		case Util.RIGHT:
			x += speed;
			break;
		case Util.DOWN:
			y += speed;
			break;
		case Util.UP:
			y -= speed;
			break;
		}
		if (x <= 0)
			x = 0;// 左
		if (y <= 60)
			y = 60;// 上
		if (x >= Util.GAME_WIDTH - 24)
			x = Util.GAME_WIDTH - 24;// 右
		if (y >= Util.GAME_HEIGHT - 24)
			y = Util.GAME_HEIGHT - 24;// 下
		if (!isMe) {
			if (step == 0) {// 朝原来的方向步数走完了.
				// 总的方向
				// 随机求一个方向
				dir = r.nextInt(4);
				// 在次确定走的步数
				step = r.nextInt(14) + 3;
			}
			step--;// 每隔0.1s走一步

			// 处理坦克随机开火
			if (r.nextInt(40) % 12 == 0)
				fire();
		}
	}

	public void draw(Graphics g) {
		if (!isAlive) {
			mw.tankList.remove(this);
			return;
		}
		int yy = isMe ? 0 : 24;
		int xx = dir * 24;
		g.drawImage(img.getSubimage(xx, yy, 24, 24), x, y, null);
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, 24, 24);
	}

	public boolean hitBrick(Brick b) {
		return getRect().intersects(b.getRect());
	}

	public void hitAllBricks() {
		for (int i = 0; i < mw.brickList.size(); i++) {
			Brick b = mw.brickList.get(i);
			if (hitBrick(b)) {
				x = lastX;
				y = lastY;
			}
		}
	}
}
