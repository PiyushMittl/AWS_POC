public class CameraTest
{
  // Define the number of photos to take.
  private static final long _numberOfImages = 1;
  // Define the interval between photos.
  private static final int _delayInterval = 5000;

  public static void main(String[] args)
  {
    try
    {
      // Create a new RaspiStill object.
      RaspiStill camera = new RaspiStill();
      // Loop through the number of images to take.
     // for (long i = 0; i < _numberOfImages; ++i)
      //{
        // Capture the image.
        camera.TakePicture("test" + ".jpg",800,600);
        // Pause after each photo.
        Thread.sleep(_delayInterval);
      //}
    }
    catch (Exception e)
    {
      // Exit the application with the exception's hash code.
      System.exit(e.hashCode());
    }
  }
}