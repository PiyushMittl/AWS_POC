import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.Celebrity;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult;
import com.amazonaws.util.IOUtils;
import com.piyush.constant.Constant;

   public class Celebs {

      public static void main(String[] args) {
         String photo = "input.jpg";
         AWSCredentials credentials;
         try {
           // credentials = new ProfileCredentialsProvider("AdminUser").getCredentials();
            credentials=    new BasicAWSCredentials(Constant.accessKey,Constant.secretKey);
         } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
               + "Please make sure that your credentials file is at the correct "
               + "location (/Users/<userid>/.aws/credentials), and is in valid format.", e);
         }

     ByteBuffer imageBytes=null;
         try (InputStream inputStream = new FileInputStream(new File(photo))) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
         }
         catch(Exception e)
         {
             System.out.println("Failed to load file " + photo);
             System.exit(1);
         }

         AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
            .standard()
            .withRegion(Regions.US_WEST_2)
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    //     AWSCredentials credentials1 = new BasicAWSCredentials("AKIAJZYQTVQDCA6L62AA", "ATpmddzzcg/aICGdLF2PlEAvWWkOez44BeXbbQND");
     //    AmazonRekognition amazonRekognition2= new AmazonRekognitionClient(credentials1);
         
         
         RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest()
            .withImage(new Image()
            .withBytes(imageBytes));

         System.out.println("Looking for celebrities in image " + photo + "\n");

         RecognizeCelebritiesResult result=amazonRekognition.recognizeCelebrities(request);

         //Display recognized celebrity information
         List<Celebrity> celebs=result.getCelebrityFaces();
         System.out.println(celebs.size() + " celebrity(s) were recognized.\n");

         for (Celebrity celebrity: celebs) {
             System.out.println("Celebrity recognized: " + celebrity.getName());
             System.out.println("Celebrity ID: " + celebrity.getId());
             BoundingBox boundingBox=celebrity.getFace().getBoundingBox();
             System.out.println("position: " +
                boundingBox.getLeft().toString() + " " +
                boundingBox.getTop().toString());
             System.out.println("Further information (if available):");
             for (String url: celebrity.getUrls()){
                System.out.println(url);
             }
             System.out.println();
          }
          System.out.println(result.getUnrecognizedFaces().size() + " face(s) were unrecognized.");
      }
   }