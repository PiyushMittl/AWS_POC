package com.piyush.rpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;


public class CompareFacesExample {

   public static void main(String[] args) throws Exception{
       Float similarityThreshold = 70F;
       String sourceImage = "source.jpg";
       String targetImage = "sam3.jpg";
       ByteBuffer sourceImageBytes=null;
       ByteBuffer targetImageBytes=null;


       AWSCredentials credentials;
       try {
           credentials = new BasicAWSCredentials("AKIAJLJP7L62ZLAAMARA", "Tul8iMnfHla8k9N4N3ZSX1z+edsw3ojutF/a5oHO");
       } catch (Exception e) {
           throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                   + "Please make sure that your credentials file is at the correct "
                   + "location (/Users/userid/.aws/credentials), and is in valid format.", e);
       }

//       EndpointConfiguration endpoint=new EndpointConfiguration("https://sns.us-west-1.amazonaws.com",
//              "us-west-1");

//       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
//               .standard()
//               .withEndpointConfiguration(endpoint)
//               .withCredentials(new AWSStaticCredentialsProvider(credentials))
//               .build();
       AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
               .standard()
               .withRegion(Regions.US_WEST_2)
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .build();


       //Load source and target images and create input parameters
       try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
          sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load source image " + sourceImage);
           System.exit(1);
       }
       try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
           targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
       }
       catch(Exception e)
       {
           System.out.println("Failed to load target images: " + targetImage);
           System.exit(1);
       }

       Image source=new Image()
       		.withBytes(sourceImageBytes);
       Image target=new Image()
       		.withBytes(targetImageBytes);

       CompareFacesRequest request = new CompareFacesRequest()
               .withSourceImage(source)
               .withTargetImage(target)
               .withSimilarityThreshold(similarityThreshold);

       // Call operation
       CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);


       // Display results
       List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
       for (CompareFacesMatch match: faceDetails){
       	ComparedFace face= match.getFace();
       	BoundingBox position = face.getBoundingBox();
       	System.out.println("Face at " + position.getLeft().toString()
       			+ " " + position.getTop()
       			+ " matches with " + face.getConfidence().toString()
       			+ "% confidence.");

       }
       List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

       System.out.println("There were " + uncompared.size()
       		+ " that did not match");
       System.out.println("Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
       System.out.println("target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
   }
}