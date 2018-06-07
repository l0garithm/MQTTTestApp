package com.loganshome.test.homeautomationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.SeekBar;
import helpers.MQTTHelper;

public class Main extends AppCompatActivity {

    public  int brightness = 0, currentValue = 0;
    public  String topic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toggleButton = (Button) findViewById(R.id.toggleButtonOnOff);
        final EditText topicPicker = (EditText) findViewById(R.id.editTextTopic);
        final SeekBar brightnessSelector = (SeekBar) findViewById(R.id.seekBarBrightnessSetting);


        brightnessSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                brightness = progress;
                topic = topicPicker.getText().toString();
                transmitOverMQTT(brightness, topic);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                topic = topicPicker.getText().toString();

                if(currentValue == 0)
                {
                    brightness = 100;
                    transmitOverMQTT(brightness, topic);
                    currentValue = 1;
                }
                else{
                    brightness = 0;
                    transmitOverMQTT(brightness, topic);
                    currentValue = 0;
                }

            }

        });
    }

    private void transmitOverMQTT(int brightness, String topic){

        MQTTHelper mqttHelper = new MQTTHelper((getApplicationContext()));

        TextView resultsWindow = (TextView) findViewById(R.id.resultsTextView);

        String payload = String.valueOf(brightness);

        resultsWindow.setText("Brightness: " + brightness + "\n" + "Topic: " + topic + "\n");

        mqttHelper.publishToTopic(topic,  payload);

    }
}
