
import de.ozzc.iot.util.IoTConfig;
import de.ozzc.iot.util.SslUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSocketFactory;

import static de.ozzc.iot.util.IoTConfig.ConfigFields.*;

/**
 * Simple MQTT Client Example for Publish/Subscribe on AWS IoT. This example
 * should serve as a starting point for using AWS IoT with Java.
 *
 * <ul>
 * <li>The client connects to the endpoint specified in the config file.</li>
 * <li>Subscribes to the topic "MyTopic".</li>
 * <li>Publishes a "Hello World" message to the topic "MyTopic.</li>
 * <li>Closes the connection.</li>
 * <li>This example should serve as a starting point for using AWS IoT with
 * Java.</li>
 * </ul>
 * Created by Ozkan Can on 04/09/2016.
 */
public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	private static final int QOS_LEVEL = 0;
	private static final String TOPIC = "$aws/things/thing2/shadow/update";
	private static final String MESSAGE = "{\n" + "    \"state\" : {\n" + "        \"desired\" : {\n"
			+ "            \"welcome\" : \"aws-iot-changed44\",\n" + "\"test\":\"wel\"\n" + "\n" + "         }\n"
			+ "     }\n" + "} ";
	private static final long QUIESCE_TIMEOUT = 5000;

	public static void main(String[] args) {

		if (args.length < 1) {
		//	showHelp();
		}

		try {
			IoTConfig config = new IoTConfig("config-example.properties");
			SSLSocketFactory sslSocketFactory = SslUtil.getSocketFactory(config.get(AWS_IOT_ROOT_CA_FILENAME),
					config.get(AWS_IOT_CERTIFICATE_FILENAME), config.get(AWS_IOT_PRIVATE_KEY_FILENAME));
			MqttConnectOptions options = new MqttConnectOptions();
			options.setSocketFactory(sslSocketFactory);
			options.setCleanSession(true);

			final String serverUrl = "ssl://" + config.get(AWS_IOT_MQTT_HOST) + ":" + config.get(AWS_IOT_MQTT_PORT);
//			final String clientId = config.get(AWS_IOT_MQTT_CLIENT_ID);
			final String clientId = ""+System.currentTimeMillis();

			MqttClient client = new MqttClient(serverUrl, clientId);
			client.setCallback(new ExampleCallback());
			client.connect(options);
			while (true) {
				client.subscribe("$aws/things/thing2/shadow/update", QOS_LEVEL);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(-1);
		}
	}

	private static void showHelp() {
		System.out.println("Usage: java -jar aws-iot-java-example.jar <config-file>");
		System.out.println("\nSee config-example.properties for an example of a config file.");
		System.exit(0);
	}
}
