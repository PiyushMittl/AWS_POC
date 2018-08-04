package com.piyush.office.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;
import com.amazonaws.util.IOUtils;
import com.piyush.constant.Constant;
import com.piyush.office.polly.Polly;
import com.piyush.office.rpi.camera.Camera;
import com.piyush.office.rpi.camera.RaspiStill;
import com.piyush.office.s3.UploadObjectSingleOperation;
import com.piyush.poc.texttomp3.PollyForTextToMp3;
import com.piyush.poc.texttomp3.ResourceHelper;
import com.piyush.poc.writetofile.WriteToFile;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



//main v1
public class MainV1 {

	public static final String COLLECTION_ID = "planb";
	public static final String S3_BUCKET = "arihant";
	static UploadObjectSingleOperation s3Obj = new UploadObjectSingleOperation();

	public static void main(String[] args) throws Exception {

		Polly polly = new Polly(Region.getRegion(Regions.US_EAST_1));
		PollyForTextToMp3 pollyForTextToMp3 = new PollyForTextToMp3();
		InputStream is = null;

		String yn = "n";
		while (new File("test.jpg").exists()) {
			new File("test.jpg").delete();
			System.out.println("image already exists file deleted");
		}
		do {
			Camera camera = new Camera();
			RaspiStill raspiCamera = new RaspiStill();

			System.out.println("Capturing your image");

			is = ResourceHelper.getResourceAsIS("audio/taking_your_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			camera.takePic("test", "jpg", raspiCamera);

			while (!new File("test.jpg").exists()) {
				System.out.print(".  ");
				Thread.sleep(500);
			}

			System.out.println("\n" + "done");
			is = ResourceHelper.getResourceAsIS("audio/taken_your_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			List<String> matchedFaces = new ArrayList<String>();

			Float threshold = 70F;
			int maxFaces = 10;
			AWSCredentials credentials;
			try {
				credentials = new BasicAWSCredentials(Constant.accessKey,Constant.secretKey);
			} catch (Exception e) {
				throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
						+ "Please make sure that your credentials file is at the correct "
						+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
			}

			// step2.1 to 2.3
			// polly.speak("please wait we are uploading your image");

			is = ResourceHelper.getResourceAsIS("audio/taking_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			if (s3Obj.uploadImage("arihant", "test.jpg", "test.jpg")) {
				// polly.speak("uploading image done");
				System.out.println();
				System.out.println("image uploaded successfully");
				is = ResourceHelper.getResourceAsIS("audio/save_done.mp3");
				pollyForTextToMp3.playMp3(is);

			} else {
				System.out.println("error in uploading image");
				System.exit(1);
			}

			AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard()
					.withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(credentials))
					.build();

			// face indexing starts (storing 3 faces)

			is = ResourceHelper.getResourceAsIS("audio/indexing_listing_photo.mp3");
			pollyForTextToMp3.playMp3(is);

//			IndexFacesResult indexFacesResult = callIndexFaces(COLLECTION_ID, amazonRekognition, "test.jpg");
			IndexFacesResult indexFacesResult = callIndexFacesByteBuffer(amazonRekognition, "img1.jpg");

			
			
			
			
			is = ResourceHelper.getResourceAsIS("audio/indx_list_done.mp3");
			pollyForTextToMp3.playMp3(is);

			List<FaceRecord> fr = indexFacesResult.getFaceRecords();
			List<String> faceIds = new ArrayList<String>();
			for (FaceRecord eachfr : fr) {
				String faceId = eachfr.getFace().getFaceId();
				faceIds.add(faceId);
				System.out.println("Faces matching FaceId: " + faceId);

				SearchFacesResult searchFacesResult = callSearchFaces("planb", faceId, threshold, maxFaces,
						amazonRekognition);
				List<FaceMatch> faceMatches = searchFacesResult.getFaceMatches();
				for (FaceMatch face : faceMatches) {
					System.out.println(face.getFace().toString());

					matchedFaces.add(face.getFace().getExternalImageId().replace(".jpg", ""));

					System.out.println();
				}
			}

			deleteFacesById("planb", faceIds, amazonRekognition);

			if (matchedFaces.isEmpty()) {
				is = ResourceHelper.getResourceAsIS("audio/no_match.mp3");
				System.out.println("no match found");
				WriteToFile.pushAvailablePeople("no match found");
				pollyForTextToMp3.playMp3(is);

			} else {
				is = ResourceHelper.getResourceAsIS("audio/found.mp3");
				pollyForTextToMp3.playMp3(is);
				String faces = "";
				for (String face : matchedFaces) {
					faces = faces + "  " + face;
					// todo check mp3 exists or not
					is = ResourceHelper.getResourceAsIS("audio/" + face + ".mp3");
					System.out.println("found " + face);
					// pollyForTextToMp3.playMp3(is);
				}

				polly.speak("i have found " + faces);
				System.out.println("writing to a file");
				WriteToFile.pushAvailablePeople(faces);
			}
			File file = new File("test.jpg");
			while (file.exists()) {
				file.delete();
				System.out.println("file deleted");
			}

			System.out.println("press y for continue and n for terminate");

			Scanner in = new Scanner(System.in);
			yn = in.nextLine();
			// yn="y";
		} while (yn.equalsIgnoreCase("y"));

	}

	private static IndexFacesResult callIndexFaces(String collectionId, AmazonRekognition amazonRekognition,
			String name) {
		IndexFacesRequest req = new IndexFacesRequest().withImage(getImageUtil(S3_BUCKET, name))
				.withCollectionId(collectionId).withExternalImageId(name);

		return amazonRekognition.indexFaces(req);
	}

	private static IndexFacesResult callIndexFacesByteBuffer(AmazonRekognition amazonRekognition, String file) {
		InputStream is;
		IndexFacesRequest req = null;
		try {
			is = new FileInputStream("img1.jpg");
			byte[] bytes = IOUtils.toByteArray(is);
			req = new IndexFacesRequest().withImage(new Image().withBytes(ByteBuffer.wrap(bytes)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// req = new IndexFacesRequest().withImage(getImageUtil(S3_BUCKET,
		// name))
		// .withCollectionId(collectionId).withExternalImageId(name);

		return amazonRekognition.indexFaces(req);
	}

	private static SearchFacesResult callSearchFaces(String collectionId, String faceId, Float threshold, int maxFaces,
			AmazonRekognition amazonRekognition) {
		SearchFacesRequest searchFacesRequest = new SearchFacesRequest().withCollectionId(collectionId)
				.withFaceId(faceId).withFaceMatchThreshold(threshold).withMaxFaces(maxFaces);
		return amazonRekognition.searchFaces(searchFacesRequest);
	}

	private static Image getImageUtil(String bucket, String key) {
		return new Image().withS3Object(new S3Object().withBucket(bucket).withName(key));
	}

	private static DeleteFacesResult deleteFacesById(String collectionId, List<String> faceId,
			AmazonRekognition amazonRekognition) {
		DeleteFacesRequest deleteFacesRequest = new DeleteFacesRequest().withCollectionId(collectionId)
				.withFaceIds(faceId);
		return amazonRekognition.deleteFaces(deleteFacesRequest);
	}

}
