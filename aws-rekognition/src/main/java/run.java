import java.io.IOException;

public class run {

	public static void main(String[] args) throws InterruptedException, IOException {
		while (true) {
			try {
				Thread.sleep(30000);
				Runtime.getRuntime().exec("java -jar aws-rekognition-0.0.1-SNAPSHOT-jar-with-dependencies.jar");
			} catch (IOException e) {
				e.printStackTrace();
				Runtime.getRuntime().exec("java run");
			}
		}
	}
}