import java.util.ArrayList;

public class gameStart {
	private FileRead file;
	private ArrayList<String> answerList;
	private String answer;

	public void start() {
		readFile();
		saveAnswer();
	}
	
	public void readFile() {
		file = new FileRead();
		file.read();
	}

	public void saveAnswer() {
		answerList = file.getAnswer();
	}

	public String getAnswer() {
		return answer;
	}

	public void print() {
		for (int i = 0; i < answerList.size(); i++) {
			System.out.println(answerList.get(i));
		}
	}
	
	public boolean hasMoreAnswer() {
		if (answerList.size() != 0) {
			int index = (int) (Math.random() * answerList.size());
			answer = answerList.get(index);
			answerList.remove(index);
			return true;
		}
		else return false;
	}
	
	public static void main(String[] args) {
		gameStart gs = new gameStart();
		gs.start();
		gs.getAnswer();
	}
}
