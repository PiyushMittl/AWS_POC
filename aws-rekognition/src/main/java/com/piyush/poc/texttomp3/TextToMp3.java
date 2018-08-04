package com.piyush.poc.texttomp3;

import java.io.InputStream;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class TextToMp3 {

	public static void main(String[] args) throws Exception {
		PollyForTextToMp3 polly = new PollyForTextToMp3(Region.getRegion(Regions.US_EAST_1));
//		polly.convert("aman achan", "aman achan.mp3");
//		polly.convert("anjali", "anjali.mp3");
//		polly.convert("ankit arora", "ankit arora.mp3");
//		polly.convert("ankit asthana", "ankit asthana.mp3");
//		polly.convert("ankit kumar", "ankit kumar.mp3");
//		polly.convert("anurag", "anurag.mp3");
//		polly.convert("apoorva", "apoorva.mp3");
//		polly.convert("balesh", "balesh.mp3");
//		polly.convert("daanish", "daanish.mp3");
//		polly.convert("devendra", "devendra.mp3");
//		polly.convert("dharmendra", "dharmendra.mp3");
//		polly.convert("isha", "isha.mp3");
//		polly.convert("jatin", "jatin.mp3");
//		polly.convert("manish", "manish.mp3");
//		polly.convert("navdeep", "navdeep.mp3");
//		polly.convert("neeraj", "neeraj.mp3");
//		polly.convert("nitin", "nitin.mp3");
//		polly.convert("pankaj", "pankaj.mp3");
//		polly.convert("prakhar", "prakhar.mp3");
//		polly.convert("ramandeep", "ramandeep.mp3");
//		polly.convert("sachin", "sachin.mp3");
//		polly.convert("samara", "samara.mp3");
//		polly.convert("sachin", "sachin.mp3");
//		polly.convert("sanjeet", "sanjeet.mp3");
//		polly.convert("sanjeev", "sanjeev.mp3");
//		polly.convert("shaahid", "shaahid.mp3");
//		polly.convert("shimpy", "shimpy.mp3");
//		polly.convert("suchit", "suchit.mp3");
//		polly.convert("sunil", "sunil.mp3");
//		polly.convert("surya", "surya.mp3");
//		polly.convert("swati", "swati.mp3");
//		polly.convert("umesh", "umesh.mp3");
//		polly.convert("vipul aggarwal", "vipul aggarwal.mp3");
//		polly.convert("vishal", "vihal.mp3");
//		polly.convert("yogendra", "yogendra.mp3");
		
//		polly.convert("piyush please press y for continue n for terminate the program", "continue_y_n.mp3");
		polly.convert("palash", "palash.mp3");
		polly.convert("ankur", "ankur_jain.mp3");
		
		
System.out.println("done");
		
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
//		polly.convert("i have not found any match", "no_match.mp3");

//		InputStream is = ResourceHelper.getResourceAsIS("audio/taking_your_photo.mp3");
//		polly.playMp3(is);
	}

}
