package main;

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

//import net.webservicex.GeoIP;
//import net.webservicex.GeoIPService;
//import net.webservicex.GeoIPServiceSoap;

public class Main {
// Works here
//  static GeoIPService service = new GeoIPService();
//  static GeoIPServiceSoap client = service.getGeoIPServiceSoap();

    public static void main(String[] args) {
        // Works here too
//        GeoIPService service = new GeoIPService();
//        final GeoIPServiceSoap client = service.getGeoIPServiceSoap();

        final GpioController gpioPIR = GpioFactory.getInstance();
        final GpioPinDigitalInput sensorPir = gpioPIR.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);

        final GpioController gpioREL1 = GpioFactory.getInstance();           
        final GpioPinDigitalOutput rel1 = gpioREL1.provisionDigitalOutputPin(RaspiPin.GPIO_08, "REL", PinState.HIGH);  
//        final GpioController gpioREL2 = GpioFactory.getInstance();           
//        final GpioPinDigitalOutput rel2 = gpioREL2.provisionDigitalOutputPin(RaspiPin.GPIO_09, "REL", PinState.HIGH);         
//
//        rel1.high();
//        rel2.high();

        sensorPir.addListener(new GpioPinListenerDigital() {
        	 Process p;

            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState().isHigh()) {
                    System.out.println("Motion!!");
                    try {
                    	
                    	String fileName=""+System.currentTimeMillis();
                    	
                    	//take photo
						//Runtime.getRuntime().exec("raspistill -q 100 -w 800 -h 600 -o "+fileName+".jpg -t 1 -th 0:0:0");
						
						//take video
                    p=Runtime.getRuntime().exec("raspivid -t 30000 -w 640 -h 480 -fps 25 -b 1200000 -p 0,0,640,480 -o "+fileName+".h264");
                    p.waitFor();
                    System.out.println("taking video complete");
                    p=Runtime.getRuntime().exec("MP4Box -add "+fileName+".h264 "+fileName+".mp4");
                    p.waitFor();
                    System.out.println("converting video to mp4 complete");
                    Runtime.getRuntime().exec("rm "+fileName+".h264");
					System.out.println("raw file deleted");
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
// Not works here
//                  GeoIPService service = new GeoIPService();
//                  GeoIPServiceSoap client = service.getGeoIPServiceSoap();
//                    GeoIP geoIp = client.getGeoIP("212.58.246.92");
//                    System.out.println(geoIp.getCountryName());
//                    rel1.low();
//                    rel2.high();
 catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                if (event.getState().isLow()) {
                    System.out.println("Quiet...");
//                    rel1.high();
//                    rel2.low();
                }
            }
        });

        try {
            for (;;) {
               Thread.sleep(3000);
            }
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }

    }
}