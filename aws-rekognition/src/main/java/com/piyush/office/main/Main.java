package com.piyush.office.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import com.piyush.office.polly.Polly;
import com.piyush.office.rpi.camera.Camera;
import com.piyush.office.rpi.camera.RaspiStill;
import com.piyush.office.s3.UploadObjectSingleOperation;
import com.piyush.poc.texttomp3.PollyForTextToMp3;
import com.piyush.poc.texttomp3.ResourceHelper;
import com.piyush.poc.writetofile.WriteToFile;
import com.piyush.constant.Constant;
//main v2
public class Main {

	public static final String COLLECTION_ID = "planb";
	public static final String S3_BUCKET = "arihant";
	static UploadObjectSingleOperation s3Obj = new UploadObjectSingleOperation();
	static Logger _logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) throws Exception {

		Polly polly = new Polly(Region.getRegion(Regions.US_EAST_1));
		PollyForTextToMp3 pollyForTextToMp3 = new PollyForTextToMp3();
		InputStream is = null;
		String yn = "n";

		AWSCredentials credentials;
		try {
			credentials = new BasicAWSCredentials(Constant.accessKey,Constant.secretKey);
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
		}

		while (new File("test.jpg").exists()) {
			new File("test.jpg").delete();
			_logger.info("image already exists file deleted");

		}
		do {
			Camera camera = new Camera();
			RaspiStill raspiCamera = new RaspiStill();

			_logger.info("Capturing your image");

			is = ResourceHelper.getResourceAsIS("audio/taking_your_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			camera.takePic("test", "jpg", raspiCamera);

			while (!new File("test.jpg").exists()) {
				System.out.print(".  ");
				Thread.sleep(500);
			}

			_logger.info("\n" + "done");
			is = ResourceHelper.getResourceAsIS("audio/taken_your_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			List<String> matchedFaces = new ArrayList<String>();
			List<String> faceWithHighestConfidence = new ArrayList<String>();
			Float highestConfidence = (float) 0.00;
			Float curretConfidence;

			Float threshold = 70F;
			int maxFaces = 10;

			// step2.1 to 2.3
			// polly.speak("please wait we are uploading your image");

			AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard()
					.withRegion(Regions.US_WEST_2).withCredentials(new AWSStaticCredentialsProvider(credentials))
					.build();

			// face indexing starts (storing 3 faces)

			is = ResourceHelper.getResourceAsIS("audio/indexing_listing_photo.mp3");
			pollyForTextToMp3.playMp3(is);

			IndexFacesResult indexFacesResult = callIndexFacesByteBuffer(COLLECTION_ID, amazonRekognition, "test.jpg");

			if (indexFacesResult != null && indexFacesResult.getFaceRecords().isEmpty()) {
				is = ResourceHelper.getResourceAsIS("audio/no_face_found.mp3");
				pollyForTextToMp3.playMp3(is);
			} else {
				is = ResourceHelper.getResourceAsIS("audio/indx_list_done.mp3");
				pollyForTextToMp3.playMp3(is);

				List<FaceRecord> fr = indexFacesResult.getFaceRecords();
				List<String> faceIds = new ArrayList<String>();
				for (FaceRecord eachfr : fr) {
					String faceId = eachfr.getFace().getFaceId();
					faceIds.add(faceId);
					_logger.info("Faces matching FaceId: " + faceId);

					SearchFacesResult searchFacesResult = callSearchFaces("planb", faceId, threshold, maxFaces,
							amazonRekognition);
					List<FaceMatch> faceMatches = searchFacesResult.getFaceMatches();
					for (FaceMatch face : faceMatches) {

						_logger.info(face.getFace().toString());
						System.out.println("name =" + face.getFace().getExternalImageId().replace(".jpg", "")
								+ "   confidance" + face.getFace().getConfidence());

						if (face.getFace().getConfidence() > highestConfidence) {
							faceWithHighestConfidence.add(0, face.getFace().getExternalImageId().replace(".jpg", ""));
							highestConfidence = face.getFace().getConfidence();
						}
					}
					highestConfidence=(float) 0.0;
					if (!faceWithHighestConfidence.isEmpty()) {
						System.out.println("adding  "+faceWithHighestConfidence);
						matchedFaces.addAll(faceWithHighestConfidence);
					}
					faceWithHighestConfidence = new ArrayList();
				}

				deleteFacesById("planb", faceIds, amazonRekognition);

				if (matchedFaces.isEmpty()) {
					is = ResourceHelper.getResourceAsIS("audio/no_match.mp3");
					_logger.info("no match found");
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
						_logger.info("found " + face);
						System.out.println(face);
						pollyForTextToMp3.playMp3(is);
					}

					// pollyForTextToMp3.playMp3(is);

					Thread.sleep(2000);
					is = ResourceHelper.getResourceAsIS("audio/continue_y_n.mp3");
					_logger.info("writing to a file");
					WriteToFile.pushAvailablePeople(faces);
				}
				File file = new File("test.jpg");
				while (file.exists()) {
					file.delete();
					_logger.info("file deleted");
				}
			}
			is = ResourceHelper.getResourceAsIS("audio/continue_y_n.mp3");
			pollyForTextToMp3.playMp3(is);
			System.out.println("press y for continue and n for terminate");
			_logger.info("press y for continue and n for terminate");

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

	private static IndexFacesResult callIndexFacesByteBuffer(String collectionId, AmazonRekognition amazonRekognition,
			String file) {
		InputStream is;
		IndexFacesRequest req = null;
		try {
			is = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(is);
			req = new IndexFacesRequest().withImage(new Image().withBytes(ByteBuffer.wrap(bytes)))
					.withCollectionId(collectionId).withExternalImageId(file);

		} catch (IOException e) {
			e.printStackTrace();
		}

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
