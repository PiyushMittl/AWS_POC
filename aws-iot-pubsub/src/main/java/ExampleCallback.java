
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * Created by Ozkan Can on 10/04/16.
 */

class ExampleCallback implements MqttCallback {

	// get a handle to the GPIO controller
	final GpioController gpio = GpioFactory.getInstance();

	// creating the pin with parameter PinState.HIGH
	// will instantly power up the pin
	final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PinLED", PinState.HIGH);

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleCallback.class);

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.info("Connection Lost.", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOGGER.info("Message arrived on topic {}. Contents: {}", topic, new String(message.getPayload()));

		if (new String(message.getPayload()) != null && new String(message.getPayload()).contains("on")) {
			System.out.println("--------------> state on");
			
			onPin();
		}
		if (new String(message.getPayload()) != null && new String(message.getPayload()).contains("off")) {
		System.out.println("--------------> state off");
			
			offPin();
		}

		// testLed();
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		LOGGER.info("Completed delivery of message with id {}", token.getMessageId());
	}

	public void testLed() throws InterruptedException {

		System.out.println("light is: ON");

		// wait 2 seconds
		Thread.sleep(2000);

		// turn off GPIO 1
		pin.low();
		System.out.println("light is: OFF");
		// wait 1 second
		Thread.sleep(1000);
		// turn on GPIO 1 for 1 second and then off
		System.out.println("light is: ON for 1 second");
		pin.pulse(1000, true);

		// release the GPIO controller resources
		gpio.shutdown();

	}

	public void onPin() throws InterruptedException {
		//pin.pulse(1000, true);
		pin.high();
		gpio.shutdown();

	}

	public void offPin() throws InterruptedException {
		pin.low();
		gpio.shutdown();

	}

}
