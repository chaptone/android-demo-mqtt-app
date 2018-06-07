package com.example.chapmac.rakkan.android_demo_mqtt_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Activity2 extends AppCompatActivity {

    private String topic;
    private String subTopic;

    MqttAndroidClient client;
    MqttConnectOptions options;

    TextView subText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Intent intent = getIntent();
        String host = intent.getStringExtra(MainActivity.EXTRA_HOST);
        String user = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        String pass = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);


        TextView textView1 = findViewById(R.id.textView1);
        textView1.setText(host);

        subText = findViewById(R.id.subText);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), host, clientId);

        options = new MqttConnectOptions();
        options.setUserName(user);
        options.setPassword(pass.toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                subText.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void publish(View v){
        EditText editText1 = findViewById(R.id.editText1);
        topic = editText1.getText().toString();

        EditText editText2 = findViewById(R.id.editText2);
        String message = editText2.getText().toString();

        if(topic.equals("")||message.equals("")){
            Toast.makeText(Activity2.this,"Input topic or payload for publish",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                client.publish(topic, message.getBytes(),0,false);
                Toast.makeText(Activity2.this,"Publish "+topic,Toast.LENGTH_LONG).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe(View v) {
        EditText editText3 = findViewById(R.id.editText3);
        subTopic = editText3.getText().toString();
        if(subTopic.equals("")){
            Toast.makeText(Activity2.this,"Input topic for subscribe"+subTopic,Toast.LENGTH_LONG).show();
        }else {
            int qos = 1;
            try {
                IMqttToken subToken = client.subscribe(subTopic, qos);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Toast.makeText(Activity2.this,"Subscribe to "+subTopic,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(View v){
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Activity2.this,"Disconnected!",Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(Activity2.this,"Couldn't disconnect",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}

