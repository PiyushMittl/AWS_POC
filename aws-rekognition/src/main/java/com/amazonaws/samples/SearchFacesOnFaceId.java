package com.amazonaws.samples;

import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;
import com.piyush.constant.Constant;

public class SearchFacesOnFaceId {

	public static final String COLLECTION_ID = "test2";
	public static final String S3_BUCKET = "arihant";

	public static void main(String[] args) throws Exception {

		AWSCredentials credentials;
		try {
			credentials = new BasicAWSCredentials(Constant.accessKey,Constant.secretKey);
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
		}

		AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

		// face indexing starts (storing 3 faces)

		IndexFacesResult indexFacesResult = callIndexFaces(COLLECTION_ID, amazonRekognition, "img8.jpg");

		// 2. Retrieve face ID of the 1st face added.
		String faceId = (String) indexFacesResult.getFaceRecords().stream().map(f -> f.getFace().getFaceId()).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No face found"));

		callIndexFaces(COLLECTION_ID, amazonRekognition, "piyush.jpg");
		callIndexFaces(COLLECTION_ID, amazonRekognition, "aman.jpg");

		// face indexing ends (storing 3 faces)

		Float threshold = 70F;
		int maxFaces = 10;

		
		
		List<FaceRecord> fr=indexFacesResult.getFaceRecords();
		for(FaceRecord eachfr:fr)
		{
			System.out.println("Faces matching FaceId: " + eachfr.getFace().getFaceId());
			
			SearchFacesResult searchFacesResult = callSearchFaces("test", faceId, threshold, maxFaces,
					amazonRekognition);
			List<FaceMatch> faceMatches = searchFacesResult.getFaceMatches();
			for (FaceMatch face : faceMatches) {
				System.out.println(face.getFace().toString());
				System.out.println();
			}
		}
		
		
		// 3. Search similar faces for a give face (identified by face ID).
		System.out.println("Faces matching FaceId: " + faceId);
		SearchFacesResult searchFacesResult = callSearchFaces(COLLECTION_ID, faceId, threshold, maxFaces,
				amazonRekognition);
 		List<FaceMatch> faceMatches = searchFacesResult.getFaceMatches();
		for (FaceMatch face : faceMatches) {
			System.out.println(face.getFace().toString());
			System.out.println();
		}

		// 4. Get an image object in S3 bucket.
		String fileName = "img5.jpg";
		Image image = getImageUtil(S3_BUCKET, fileName);

		// 5. Search collection for faces similar to the largest face in the
		// image.
		SearchFacesByImageResult searchFacesByImageResult = callSearchFacesByImage(COLLECTION_ID, image, threshold,
				maxFaces, amazonRekognition);

		System.out.println("Faces matching largest face in image  " + fileName);
		List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
		for (FaceMatch face : faceImageMatches) {
			System.out.println(face.getFace().toString());
			System.out.println();
		}

		System.out.println("program ends --------------------------------------------------------------------");

	}

	private static IndexFacesResult callIndexFaces(String collectionId, AmazonRekognition amazonRekognition,
			String name) {
		IndexFacesRequest req = new IndexFacesRequest().withImage(getImageUtil(S3_BUCKET, name))
				.withCollectionId(collectionId).withExternalImageId(name);

		return amazonRekognition.indexFaces(req);
	}

	private static SearchFacesByImageResult callSearchFacesByImage(String collectionId, Image image, Float threshold,
			int maxFaces, AmazonRekognition amazonRekognition) {
		SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
				.withCollectionId(collectionId).withImage(image).withFaceMatchThreshold(threshold)
				.withMaxFaces(maxFaces);
		return amazonRekognition.searchFacesByImage(searchFacesByImageRequest);
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

}
