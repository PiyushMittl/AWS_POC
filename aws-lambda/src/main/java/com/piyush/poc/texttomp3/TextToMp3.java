package com.piyush.poc.texttomp3;

import java.io.InputStream;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class TextToMp3 {

	public static void main(String[] args) throws Exception {
		PollyForTextToMp3 polly = new PollyForTextToMp3(Region.getRegion(Regions.US_EAST_1));
		polly.convert("i am taking your photograph", "taking_your_photo.mp3");
//		polly.convert("i will inform you once it is done", "inform_you_once_done.mp3");
//		polly.convert("i have taken you photograph", "taken_your_photo.mp3");
//
//		polly.convert("please wait till save your photograph to our database", "taking_photo.mp3");
//		polly.convert("saving has been completed", "save_done.mp3");
//
//		polly.convert("please wait i am indexing and listing your photograph", "indexing_listing_photo.mp3");
//		polly.convert("indexing and listing has been completed", "indx_list_done.mp3");
//
//		polly.convert("matching your photograph with ourdatabase it make some time", "matching.mp3");
//
	polly.convert("i have not found any match", "no_match.mp3");

		InputStream is = ResourceHelper.getResourceAsIS("audio/taking_your_photo.mp3");
		polly.playMp3(is);
	}

}
