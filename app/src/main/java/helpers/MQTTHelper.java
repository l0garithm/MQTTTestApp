package helpers;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.content.Context;
import android.util.Log;

public class MQTTHelper {

    public MqttAndroidClient mqttAndroidClient;

    String serverAddress = "tcp://test.loganshome.com";

    String clientId = "ExampleAndroidClient1";
    String subscriptionTopic = "";


    private String userName = "owntracks";
    private String passWord = "1234";

    public MQTTHelper(Context context) {
        MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(context, "tcp://test.loganshome.com", "1234");
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {

        mqttAndroidClient.setCallback(callback);

    }

    private void connect() {

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(userName);
        mqttConnectOptions.setPassword(passWord.toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Log.w("Mqtt", "Failed to connect to: " + serverAddress + exception.toString());

                }
            });

        } catch(MqttException exception){

            exception.printStackTrace();

        }

    }

    public void publishToTopic(String publishToTopic, String commandInJSON){

        try{

            MqttMessage commandToSend = new MqttMessage(commandInJSON.getBytes());
            commandToSend.setQos(2);
            mqttAndroidClient.publish(publishToTopic, commandToSend);

        } catch (MqttException exception){

            System.err.println("Exception publishing");
            exception.printStackTrace();

        }

    }

}
