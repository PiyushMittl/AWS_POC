package utils;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.DeleteFacesResult;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import com.piyush.constant.Constant;

public class Utils {

	public static void main(String[] args) {

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

	}

	public static DeleteFacesResult deleteFacesById(String collectionId, List<String> faceId,
			AmazonRekognition amazonRekognition) {
		DeleteFacesRequest deleteFacesRequest = new DeleteFacesRequest().withCollectionId(collectionId)
				.withFaceIds(faceId);
		return amazonRekognition.deleteFaces(deleteFacesRequest);
	}

	private static ListFacesResult callListFaces(String collectionId, int limit, String paginationToken,
			AmazonRekognition amazonRekognition) {
		ListFacesRequest listFacesRequest = new ListFacesRequest().withCollectionId(collectionId).withMaxResults(limit)
				.withNextToken(paginationToken);
		return amazonRekognition.listFaces(listFacesRequest);
	}

}
