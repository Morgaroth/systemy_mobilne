package lab2.sm.semx.studia.morgaroth.github.io.smlab2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private EditText msgEdit;
    private ListView chat;
    private Handler myHandler;
    private String login;

    private String broker;
    private String topic;
    private MqttClient sampleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        msgEdit = (EditText) findViewById(R.id.send_msg);
        TextView loginBar = (TextView) findViewById(R.id.login_view);
        chat = (ListView) findViewById(R.id.listView);

        login = getIntent().getStringExtra(LoginActivity.NICK);
        String ip = getIntent().getStringExtra(LoginActivity.IP);
        if (loginBar != null) {
            loginBar.setText(getIntent().getStringExtra(LoginActivity.NICK));
        }

        broker = "tcp://" + ip + ":1883";
        topic = login;

        final ArrayList<String> listItems = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        chat.setAdapter(adapter);

        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                listItems.add("[" + msg.getData().getString("NICK") + "] " +
                        msg.getData().getString("MSG"));
                adapter.notifyDataSetChanged();
                chat.setSelection(listItems.size() - 1);
            }
        };

        new Thread() {
            @Override
            public void run() {
                try {
                    sampleClient = new MqttClient(broker, login, new MemoryPersistence());
                    sampleClient.setCallback(new SampleChatCallback() {
                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            Message msg = myHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("NICK", topic);
                            b.putString("MSG", new String(message.getPayload()));
                            msg.setData(b);
                            myHandler.sendMessage(msg);
                        }
                    });
                    MqttConnectOptions connOpts = new MqttConnectOptions();
                    connOpts.setCleanSession(true);
                    sampleClient.connect(connOpts);
                    sampleClient.subscribe("#");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void send(View e) throws MqttException {
        if (sampleClient != null && sampleClient.isConnected()) {
            MqttMessage message = new MqttMessage(msgEdit.getText().toString().getBytes());
            message.setQos(0);
            sampleClient.publish(topic, message);
            msgEdit.setText("");
        } else {
            Toast.makeText(this, "Queue not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    public abstract class SampleChatCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable arg0) {
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
        }

        @Override
        public abstract void messageArrived(String topic, MqttMessage message) throws Exception;
    }


}
