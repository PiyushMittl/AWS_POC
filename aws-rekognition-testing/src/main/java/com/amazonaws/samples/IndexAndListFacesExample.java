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
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import com.amazonaws.services.rekognition.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IndexAndListFacesExample {
	public static final String COLLECTION_ID = "planb";
	public static final String S3_BUCKET = "arihant";

	public static void main(String[] args) throws Exception {
		AWSCredentials credentials;
		try {
			credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA", "Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in valid format.", e);
		}

		ObjectMapper objectMapper = new ObjectMapper();

		AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

//		String a[] = { "C:\\Users\\PiyushMittal\\Downloads\\Photos\\img1.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img2.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img3.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img4.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img5.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img6.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img7.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img8.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img8.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img10.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img11.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img12.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\img13.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\lipi_sonam_dilip.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\sam1.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\sam2.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\sam3.jpg",
//				"C:\\Users\\PiyushMittal\\Downloads\\Photos\\sam4.jpg" };

		//String a[]={"img1.jpg","img2.jpg","img3.jpg","img4.jpg","img5.jpg","img6.jpg","img7.jpg","img8.jpg","img9.jpg","img10.jpg","img11.jpg","img12.jpg","img13.jpg","lipi_sonam_dilip.jpg","sam1.jpg","sam2.jpg","sam3.jpg","sam4.jpg"};
		//String a[]={"aman.jpg","piyush.jpg","joshiji.jpg","rishi.jpg","tripti.jpg","ankur.jpg","atin.jpg","deepak.jpg","sushant.jpg","vipul.jpg"};
//		String a[]={"tushar.jpg"};
//		
//		
//		
//		// 1. Index face 1
//		for (String imname : a) {
//			Image image = getImageUtil(S3_BUCKET, imname);
//			String externalImageId = imname;
//			IndexFacesResult indexFacesResult = callIndexFaces(COLLECTION_ID, externalImageId, "ALL", image,
//					amazonRekognition);
//			System.out.println(externalImageId + " added");
//			List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
//			for (FaceRecord faceRecord : faceRecords) {
//				System.out.println("Face detected: Faceid is " + faceRecord.getFace().getFaceId());
//				System.out.println("Face detected: Faceid is " + faceRecord.getFace().getImageId());
//				
//			}
//		}
		// 2. Index face 2
		// indexFacesResult = null;
		// faceRecords = null;
		// Image image2 = getImageUtil(S3_BUCKET, "sam1.jpg");
		// String externalImageId2 = "sam1.jpg";
		// System.out.println(externalImageId2 + " added");
		// indexFacesResult = callIndexFaces(COLLECTION_ID, externalImageId2,
		// "ALL", image2, amazonRekognition);
		// faceRecords = indexFacesResult.getFaceRecords();
		// for (FaceRecord faceRecord: faceRecords) {
		// System.out.println("Face detected. Faceid is " +
		// faceRecord.getFace().getFaceId());
		// }

		// 3. Page through the faces with ListFaces
		ListFacesResult listFacesResult = null;
		System.out.println("Faces in collection " + COLLECTION_ID);

		
		new DeleteFacesRequest().withCollectionId("planb")
		.withFaceIds("f2e39f68-8157-5a2a-a41f-c2b95979e964");
		
		String paginationToken = null;
		do {
			if (listFacesResult != null) {
				paginationToken = listFacesResult.getNextToken();
			}
			listFacesResult = callListFaces(COLLECTION_ID, 1, paginationToken, amazonRekognition);
			List<Face> faces = listFacesResult.getFaces();
			for (Face face : faces) {
				
				if(face.getExternalImageId().contains("test"))
				{
					new DeleteFacesRequest().withCollectionId("planb")
					.withFaceIds(face.getFaceId());
				}
				System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
			}
		} while (listFacesResult != null && listFacesResult.getNextToken() != null);
	}

	private static IndexFacesResult callIndexFaces(String collectionId, String externalImageId, String attributes,
			Image image, AmazonRekognition amazonRekognition) {
		IndexFacesRequest indexFacesRequest = new IndexFacesRequest().withImage(image).withCollectionId(collectionId)
				.withExternalImageId(externalImageId).withDetectionAttributes(attributes);
		return amazonRekognition.indexFaces(indexFacesRequest);

	}

	private static ListFacesResult callListFaces(String collectionId, int limit, String paginationToken,
			AmazonRekognition amazonRekognition) {
		ListFacesRequest listFacesRequest = new ListFacesRequest().withCollectionId(collectionId).withMaxResults(limit)
				.withNextToken(paginationToken);
		return amazonRekognition.listFaces(listFacesRequest);
	}

	private static Image getImageUtil(String bucket, String key) {
		return new Image().withS3Object(new S3Object().withBucket(bucket).withName(key));
	}

}
