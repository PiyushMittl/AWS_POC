import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.Celebrity;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult;
import com.amazonaws.util.IOUtils;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Main {

	private AmazonPollyClient polly;
	private Voice voice;
	// private static final String SAMPLE = "Congratulations. You have
	// successfully built this working demo ";

	AWSCredentials credentials = new BasicAWSCredentials("AKIAJZYQTVQDCA6L62AA",
			"ATpmddzzcg/aICGdLF2PlEAvWWkOez44BeXbbQND");

	public Main(Region region) {
		// create an Amazon Polly client in a specific region
		polly = new AmazonPollyClient(credentials);
		polly.setRegion(region);
		// Create describe voices request.
		DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

		// Synchronously ask Amazon Polly to describe available TTS voices.
		DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
		voice = describeVoicesResult.getVoices().get(0);
	}

	public InputStream synthesize(String text, OutputFormat format) throws IOException {
		SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
				.withOutputFormat(format);
		SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}

	public static String getString() {
		String returnCelebName = null;
		String photo = "input3.jpg";
		AWSCredentials credentials;
		try {
			// credentials = new
			// ProfileCredentialsProvider("AdminUser").getCredentials();
			credentials = new BasicAWSCredentials("AKIAJZYQTVQDCA6L62AA", "ATpmddzzcg/aICGdLF2PlEAvWWkOez44BeXbbQND");
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/<userid>/.aws/credentials), and is in valid format.", e);
		}

		ByteBuffer imageBytes = null;
		try (InputStream inputStream = new FileInputStream(new File(photo))) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		} catch (Exception e) {
			System.out.println("Failed to load file " + photo);
			System.exit(1);
		}

		AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

		RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest()
				.withImage(new Image().withBytes(imageBytes));

		System.out.println("Looking for celebrities in image " + photo + "\n");

		RecognizeCelebritiesResult result = amazonRekognition.recognizeCelebrities(request);

		// Display recognized celebrity information
		List<Celebrity> celebs = result.getCelebrityFaces();
		System.out.println(celebs.size() + " celebrity(s) were recognized.\n");

		for (Celebrity celebrity : celebs) {
			returnCelebName = celebrity.getName();

			System.out.println("Celebrity recognized: " + celebrity.getName());
			System.out.println("Celebrity ID: " + celebrity.getId());
			BoundingBox boundingBox = celebrity.getFace().getBoundingBox();
			System.out.println("position: " + boundingBox.getLeft().toString() + " " + boundingBox.getTop().toString());
			System.out.println("Further information (if available):");
			for (String url : celebrity.getUrls()) {
				System.out.println(url);
			}
			System.out.println();
		}
		System.out.println(result.getUnrecognizedFaces().size() + " face(s) were unrecognized.");
		return returnCelebName;
	}

	public static void main(String[] args) throws JavaLayerException, IOException {

		final String SAMPLE = getString();

		// Scanner sc=new Scanner(System.in);
		// final String SAMPLE=sc.nextLine();

		// create the test class
		Main helloWorld = new Main(Region.getRegion(Regions.US_EAST_1));
		// get the audio stream
		InputStream speechStream = helloWorld.synthesize(SAMPLE, OutputFormat.Mp3);

		// create an MP3 player
		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackStarted(PlaybackEvent evt) {
				System.out.println("Playback started");
				System.out.println(SAMPLE);
			}

			@Override
			public void playbackFinished(PlaybackEvent evt) {
				System.out.println("Playback finished");
			}
		});

		// play it!
		player.play();

	}

}
