package com.piyush.office.rpi.camera;
public class Camera
{
  // Define the number of photos to take.
  private static final long _numberOfImages = 1;
  // Define the interval between photos.
  private static final int _delayInterval = 0;

  public void takePic(String imageName,String imageExtention,RaspiStill camera)
  {
    try
    {
      // Create a new RaspiStill object.
     // RaspiStill camera = new RaspiStill();
      // Loop through the number of images to take.
//      for (long i = 0; i < _numberOfImages; ++i)
//      {
//         Capture the image.
        camera.TakePicture(imageName +"."+ imageExtention,800,600);
//         Pause after each photo.
//        Thread.sleep(_delayInterval);
//      }
    }
    catch (Exception e)
    {
      // Exit the application with the exception's hash code.
      System.exit(e.hashCode());
    }
  }
}