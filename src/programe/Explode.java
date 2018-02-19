package programe;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Explode {

	int count = 0;
	BufferedImage img = Util.getImage("resource/explosion.png");
	MainWin mw;
	int x, y;

	public Explode(int x, int y, MainWin mw) {
		this.mw = mw;
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g) {
		g.drawImage(img.getSubimage(24 * count, 0, 24, 24), x, y, null);
		count++;
		if (count == 8)
			mw.explodeList.remove(Explode.this);
	}

}
