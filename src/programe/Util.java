package programe;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class Util {

	static int MAXLEVEL = 5;
	static Hero[] heros = new Hero[6];
	static boolean canSave = true;
	static final int UP = 0;
	static final int RIGHT = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;

	public static int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

	public static int GAME_WIDTH = 480;
	public static int GAME_HEIGHT = 540;

	public static void addRecord(int score, String time) {
		try {
			FileReader fr = new FileReader("hero.txt");
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			for (int i = 0; i < 5; i++) {
				line = br.readLine();
				String[] data = line.split(",");
				Hero h = new Hero(data[0], Integer.valueOf(data[1]), data[2]);
				heros[i] = h;
			}
			br.close();
			if (score > heros[4].getScore() && canSave) {
				String name = JOptionPane.showInputDialog(null, "Input your name:", "Congratulation",
						JOptionPane.PLAIN_MESSAGE);
				Hero h = new Hero(name, score, time);
				heros[5] = h;
				for (int i = 5; i > 0; i--) {
					for (int j = 0; j < i; j++) {
						if (heros[j].getScore() < heros[j + 1].getScore()) {
							Hero t = heros[j];
							heros[j] = heros[j + 1];
							heros[j + 1] = t;
						}
					}
				}
				canSave = false;
				FileWriter fw = new FileWriter("hero.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("");
				fw = new FileWriter("hero.txt");
				bw = new BufferedWriter(fw);
				for (int i = 0; i < 5; i++) {
					bw.append(heros[i].getName() + "," + heros[i].getScore() + "," + heros[i].getTime() + "\r\n");
				}
				bw.flush();
				bw.close();
				JOptionPane.showMessageDialog(null, Util.getRecord(), "Top 5", JOptionPane.DEFAULT_OPTION);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getRecord() {
		String result = "<html><table><tr><td>Rank</td><td>Name</td><td>Score</td><td>Time</td></tr>";
		try {
			FileReader fr = new FileReader("hero.txt");
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; i < 5; i++) {
				String[] data = br.readLine().split(",");
				result += "<tr><td>" + (i + 1) + "</td><td>" + data[0] + "</td><td>" + data[1] + "</td><td>" + data[2]
						+ "</td></tr>";
			}
			result += "</table></html>";
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("MM.dd HH:MM");
		return df.format(new Date());
	}

	public static BufferedImage getImage(String path) {
		URL url = Util.class.getClassLoader().getResource(path);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}

	public static char[][] readMap(String path) {
		char[][] array = new char[20][20];
		try {
			URL url = Test.class.getClassLoader().getResource(path);
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			for (int i = 0; i < 20; i++, line = br.readLine()) {
				array[i] = line.toCharArray();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return array;
	}
}
