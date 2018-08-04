package com.piyush.poc.texttomp3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import com.stream.converter.TeeInputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class Polly {

	private final AmazonPollyClient polly;
	private final Voice voice;

	AWSCredentials credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA",
			"Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");

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
	
	
	public Polly() {
		// create an Amazon Polly client in a specific region
		polly = new AmazonPollyClient(credentials);
		polly.setRegion(Region.getRegion(Regions.US_EAST_1));
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

	public void convert(String textToConvert,String fileName) throws Exception {

		// create the test class
		Polly helloWorld = new Polly(Region.getRegion(Regions.US_EAST_1));
		// get the audio stream
		InputStream speechStream = helloWorld.synthesize(textToConvert, OutputFormat.Mp3);

		
		//save stream to a file
		BufferedInputStream bis = new BufferedInputStream(speechStream);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));  
		byte[] buffer = new byte[8 * 1024];  
	    try {
	        int bytesRead;
	        while ((bytesRead = bis.read(buffer)) != -1) {
	          out.write(buffer, 0, bytesRead);
	        }
	      } finally {
	        out.close();
	      }
		
		// BasicPlayer player = new BasicPlayer();
		// player.open(tis);
		// player.play();

		

	}

	void playMp3(InputStream speechStream) throws JavaLayerException {
		// create an MP3 player
		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackStarted(PlaybackEvent evt) {
				System.out.println("Playback started");
				// System.out.println(SAMPLE);
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