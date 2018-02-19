package programe;

public class Hero {

	String name;
	int score;
	String time;

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public String getTime() {
		return time;
	}

	public Hero(String name, int score, String time) {
		this.name = name;
		this.score = score;
		this.time = time;
	}

}
