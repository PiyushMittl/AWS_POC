package com.piyush.office.polly;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
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

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Polly {

	private final AmazonPollyClient polly;
	private final Voice voice;
	private static final String SAMPLE = "Aastha paagal hai";
	
	AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA", "Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");

	public Polly(Region region) {
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
		SynthesizeSpeechRequest synthReq = 
		new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
				.withOutputFormat(format);
		SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}

	public void speak(String textToSpeak) throws Exception {
		
//		Scanner sc=new Scanner(System.in);
//		final String SAMPLE=sc.nextLine();
		
		//create the test class
		Polly helloWorld = new Polly(Region.getRegion(Regions.US_EAST_1));
		//get the audio stream
		InputStream speechStream = helloWorld.synthesize(textToSpeak, OutputFormat.Mp3);

		//create an MP3 player
		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackStarted(PlaybackEvent evt) {
				System.out.println("Playback started");
				//System.out.println(SAMPLE);
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