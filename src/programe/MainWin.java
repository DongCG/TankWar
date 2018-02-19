package programe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MainWin extends JFrame {
	private static final long serialVersionUID = 1L;
	int level = 1;
	int score = 0;
	boolean flag;
	Tank tank;//玩家坦克
	Heart heart;//玩家基地
	List<Bullet> bulletList = null;//子弹集合
	List<Tank> tankList = null;//敌方坦克集合
	List<Explode> explodeList = null;//爆炸集合
	List<Brick> brickList = null;//砖块集合
	Image offScreen = new BufferedImage(Util.GAME_WIDTH, Util.GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
	Graphics drawOffScreen = offScreen.getGraphics();

	MainWin() {
		this.setTitle("Tank War");
		this.setSize(Util.GAME_WIDTH, Util.GAME_HEIGHT);
		this.setLocation((Util.SCREEN_WIDTH - Util.GAME_WIDTH) / 2, (Util.SCREEN_HEIGHT - Util.GAME_HEIGHT) / 2);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		brickList = new ArrayList<Brick>();
		bulletList = new ArrayList<Bullet>();
		tankList = new ArrayList<Tank>();
		explodeList = new ArrayList<Explode>();
		initMap("resource/map_" + level + ".txt");
		this.addKeyListener(new MyKeyListener());
		new Thread(new PaintThread()).start();
		this.setVisible(true);
	}

	public void initMap(String filename) {
		flag = true;
		char[][] array = Util.readMap(filename);
		this.brickList.clear();
		this.tankList.clear();
		this.bulletList.clear();
		this.explodeList.clear();
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				switch (array[i][j]) {
				case 'B':
					this.brickList.add(new Brick(j * 24, 60 + i * 24, this));
					break;
				case 'O':
					this.tankList.add(new Tank(j * 24, 60 + i * 24, 3, Util.UP, false, this));
					break;
				case 'M':
					this.tank = new Tank(j * 24, 60 + i * 24, 3, Util.DOWN, true, this);
					break;
				case 'H':
					this.heart = new Heart(j * 24, 60 + i * 24);
					break;
				}
			}
		}
		flag = false;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		drawOffScreen.setColor(Color.WHITE);
		drawOffScreen.fillRect(0, 0, Util.GAME_WIDTH, Util.GAME_HEIGHT);
		tank.draw(drawOffScreen);
		heart.draw(drawOffScreen);
		drawOffScreen.setColor(Color.BLACK);
		drawOffScreen.setFont(new Font("宋体",Font.BOLD, 20));
		drawOffScreen.drawString("LEVEL:" + level, 10, 50);
		drawOffScreen.drawString("SCORE:"+score, 350, 50);
		tank.hitAllBricks();
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).draw(drawOffScreen);
		}
		for (int i = 0; i < tankList.size(); i++) {
			tankList.get(i).draw(drawOffScreen);
		}
		for (int i = 0; i < explodeList.size(); i++) {
			explodeList.get(i).draw(drawOffScreen);
		}
		for (int i = 0; i < brickList.size(); i++) {
			try {
				brickList.get(i).draw(drawOffScreen);// 数组很长会出现线程不安全
			} catch (Exception e) {

			}
		}
		if(!tank.isAlive||!heart.isAlive)
		{
			drawOffScreen.setColor(Color.BLACK);
			drawOffScreen.setFont(new Font("黑体",0, 80));
			drawOffScreen.drawString("GAME OVER", 50, 300);
			drawOffScreen.setFont(new Font("宋体",Font.BOLD, 20));
			drawOffScreen.drawString("Press R to Resume", 140, 330);
		}
		g.drawImage(offScreen, 0, 0, null);
	}

	class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_R&&(!tank.isAlive||!heart.isAlive))
			{
				score = 0;
				level = 1;
				initMap("resource/map_" + level + ".txt");
				Util.canSave = true;
			}else if (e.getKeyCode() == KeyEvent.VK_H) {
				JOptionPane.showMessageDialog(null,Util.getRecord(), "Top 5",JOptionPane.DEFAULT_OPTION);
			}
			else
				tank.keyPressed(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			tank.keyReleased(e);
		}
	}

	class PaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				if (tankList.size() == 0 && explodeList.size() == 0&&tank.isAlive&&!flag) {
					level++;
					if (level == Util.MAXLEVEL+1)
						level = 1;
					initMap("resource/map_" + level + ".txt");
				} else {
					try {
						tank.move();
						for (int i = 0; i < bulletList.size(); i++) {
							try{
								bulletList.get(i).move();
								bulletList.get(i).hitAllBrick();
								bulletList.get(i).hitAllTank();
								bulletList.get(i).hitHeart(heart);
							}catch(Exception e){
								
							}
						}
						for (int i = 0; i < tankList.size(); i++) {
							tankList.get(i).move();
							tankList.get(i).hitAllBricks();
						}
						Thread.sleep(100);
						repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
