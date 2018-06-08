package com.example.chapmac.rakkan.android_demo_mqtt_app;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HOST = "com.example.chapmac.rakkan.android_demo_mqtt_app.EXTRA_HOST";
    public static final String EXTRA_USERNAME = "com.example.chapmac.rakkan.android_demo_mqtt_app.EXTRA_USERNAME";
    public static final String EXTRA_PASSWORD = "com.example.chapmac.rakkan.android_demo_mqtt_app.EXTRA_PASSWORD";

    private String host;
    private String user;
    private String pass;

    public DatabaseReference myRef;
    TextView textView;

    private ProgressBar proBar;

    MqttAndroidClient client;
    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proBar = findViewById(R.id.progressBar);
        proBar.setVisibility(View.GONE);

        textView = findViewById(R.id.textView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("connections");

        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Activity3.class);
                startActivity(intent);
            }
        });

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Map map = (Map)dataSnapshot.getValue();
//                String Value = String.valueOf(map.get("555"));
//                textView.setText(Value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    public void connect(View v){
        proBar.setVisibility(View.VISIBLE);

        EditText editText1 = findViewById(R.id.editText1);
        host = editText1.getText().toString();

        EditText editText2 = findViewById(R.id.editText2);
        user = editText2.getText().toString();

        EditText editText3 = findViewById(R.id.editText3);
        pass = editText3.getText().toString();

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
                    Toast.makeText(MainActivity.this,"Connected!!",Toast.LENGTH_LONG).show();
//                    updateDatabase();
                    openActivity2();
                    proBar.setVisibility(View.GONE);
                    try {
                        IMqttToken token = client.disconnect();
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
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Connected Failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void updateDatabase() {
        Map<String,Object> value = new HashMap<>();
        value.put("host",host);
        value.put("user",user);
        value.put("pass",pass);
        myRef.push().setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Record!",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"Record Failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void openActivity2() {
        Intent intent = new Intent(this,Activity2.class);
        intent.putExtra(EXTRA_HOST,host);
        intent.putExtra(EXTRA_USERNAME,user);
        intent.putExtra(EXTRA_PASSWORD,pass);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}

