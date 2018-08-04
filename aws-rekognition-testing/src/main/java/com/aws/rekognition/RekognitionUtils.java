package com.aws.rekognition;

import java.util.List;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;

public class RekognitionUtils {

	public static void main(String[] args) {

	}

	private static CreateCollectionResult callCreateCollection(String collectionId,
			AmazonRekognition amazonRekognition) {
		CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(collectionId);

		System.out.println("CollectionArn : " + amazonRekognition.createCollection(request).getCollectionArn());
		System.out.println("Status code : " + amazonRekognition.createCollection(request).getStatusCode().toString());

		return amazonRekognition.createCollection(request);
	}

	private static DeleteCollectionResult callDeleteCollection(String collectionId,
			AmazonRekognition amazonRekognition) {
		DeleteCollectionRequest request = new DeleteCollectionRequest().withCollectionId(collectionId);
		System.out
				.println(collectionId + ": " + amazonRekognition.deleteCollection(request).getStatusCode().toString());
		return amazonRekognition.deleteCollection(request);
	}

	private static DeleteFacesResult deleteFacesById(String collectionId, List<String> faceId,
			AmazonRekognition amazonRekognition) {
		DeleteFacesRequest deleteFacesRequest = new DeleteFacesRequest().withCollectionId(collectionId)
				.withFaceIds(faceId);
		return amazonRekognition.deleteFaces(deleteFacesRequest);
	}

}
