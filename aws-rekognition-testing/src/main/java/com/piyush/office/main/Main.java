package com.piyush.office.main;

import com.piyush.office.s3.UploadObjectSingleOperation;

import java.util.List;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.piyush.office.polly.Polly;
import com.piyush.office.rekognition.IndexAndListFaces;
import com.piyush.office.rekognition.SearchFaces;
import com.piyush.office.rpi.camera.Camera;;

public class Main {

	static UploadObjectSingleOperation s3Obj=new UploadObjectSingleOperation();
	static IndexAndListFaces indexAndListFaces=new IndexAndListFaces();
	static SearchFaces searchFaces=new SearchFaces();
	public static void main(String[] args) throws Exception {
		Polly polly=new Polly(Region.getRegion(Regions.US_EAST_1));
	
		//take a pic
//		System.out.println("smile please :)");
		polly.speak("please smile we are taking your photograph");
//		Camera camera=new Camera();
//		camera.takePic("test", "jpg");
//		System.out.println("we are done thanks");
		
		
		
		
//		polly.speak("we are done thanks");
		//Prerequisites step1
	//step1	
		//upload image of every employee in s3 bucket
		//create collection with the employee name 
		//listing and indexing same image 
		
	//step2 when we take a pic
		//step2.1 take a picture
		//step2.2 save pic in physical location
		//step2.3 upload pic in s3 bucket
		//step2.4 create collection or save in already saved collection
		//step2.5 listing and indexing same image 
	
	//step3 processing/searching image identity
		//step3.1 get all collection from bucket and iterate one by one
		//step3.2 compare pic with containing image in each collection 
		//step3.3 print collection name if match found
		
		
		
		
		//step2.1 to 2.3
	//	polly.speak("please wait we are uploading your image");
		if(s3Obj.uploadImage("arihant","test.jpg","test.jpg"))
		{
	//		polly.speak("uploading image done");
			System.out.println();
			System.out.println("image uploaded successfully");
		}
		else{
			System.out.println("error in uploading image");
			System.exit(1);
		}
		
		//step2.4 create collection or save in already saved collection
		//already created no need to create collection
		
		//step2.5 listing and indexing same image 
		//IndexAndListing(String collectionId,String bucketName,String imageName)
		System.out.println();
		System.out.println("indexing and listing image");
	//	polly.speak("please wait we are indexing and listing your image");
		indexAndListFaces.IndexAndListing("test", "arihant","test.jpg");
		System.out.println("indexing and listing image successfully");
//		polly.speak("indexing and listing image done");
		
		
		//step3.1 get all collection from bucket and iterate one by one
		//step3.2 compare pic with containing image in each collection 
		//step3.3 print collection name if match found
		
		//SearchImage(String collectionName,String bucketName,String imageName)
		System.out.println();
		System.out.println("searcing image");
	//	polly.speak("please wait we are identifying your image");
		List<String> peopleAvailable= searchFaces.SearchImage("test","arihant","test.jpg");
		//System.out.println("identification of image done!!   please wait for result");
		
		
		if(!peopleAvailable.isEmpty())
		{
			polly.speak("object has been identified as "+peopleAvailable.get(0));
		}
		else{
			polly.speak("object is not identified");
		}
		
	}

}
