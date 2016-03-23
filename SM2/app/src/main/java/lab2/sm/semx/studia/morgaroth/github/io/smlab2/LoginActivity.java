package lab2.sm.semx.studia.morgaroth.github.io.smlab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String NICK = "login";
    public static final String IP = "ip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void login(View e) {
        Intent i = new Intent();
        i.setClass(this, ChatActivity.class);
        TextView nickView = (TextView) findViewById(R.id.nick);
        TextView addressView = (TextView) findViewById(R.id.ip_address);
        if (nickView != null) {
            i.putExtra(NICK, nickView.getText().toString());
        } else {
            Toast.makeText(this, "Fill NICK field!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (addressView != null) {
            i.putExtra(IP, addressView.getText().toString());
        } else {
            Toast.makeText(this, "Fill NICK field!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(i);
    }
}
