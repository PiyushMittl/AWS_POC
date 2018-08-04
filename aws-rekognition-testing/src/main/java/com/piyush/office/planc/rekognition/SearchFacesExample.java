package com.piyush.office.planc.rekognition;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;

public class SearchFacesExample {

	public static final String COLLECTION_ID = "planb";
	public static final String S3_BUCKET = "arihant";

	public static void main(String[] args) throws Exception {
		
		List<String> matchedFaces=new ArrayList<String>();
		
		Float threshold = 70F;
		int maxFaces = 10;
		AWSCredentials credentials;
		try {
			credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA", "Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
		}

		AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

		// face indexing starts (storing 3 faces)

		IndexFacesResult indexFacesResult = callIndexFaces(COLLECTION_ID, amazonRekognition, "test2.jpg");

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

		for(String face:matchedFaces)
		{
			System.out.println("faces found of "+face);			
		}
		
		System.out.println("program ends --------------------------------------------------------------------");

	}

	private static IndexFacesResult callIndexFaces(String collectionId, AmazonRekognition amazonRekognition,
			String name) {
		IndexFacesRequest req = new IndexFacesRequest().withImage(getImageUtil(S3_BUCKET, name))
				.withCollectionId(collectionId).withExternalImageId(name);

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
