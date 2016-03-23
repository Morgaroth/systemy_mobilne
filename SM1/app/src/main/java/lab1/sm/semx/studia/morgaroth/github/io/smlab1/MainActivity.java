package lab1.sm.semx.studia.morgaroth.github.io.smlab1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText num1 = null;
    private EditText num2 = null;
    private EditText result = null;
    private LogicService mLogic = null;
    private ProgressBar pb;


    private ServiceConnection logicConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogicService.LocalBinder binder = (LogicService.LocalBinder) service;
            mLogic = binder.getService();
            Toast.makeText(MainActivity.this, "Logic available!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLogic = null;
            Toast.makeText(MainActivity.this, "Logic UNavailable!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num1 = (EditText) findViewById(R.id.num1);
        num2 = (EditText) findViewById(R.id.num2);
        result = (EditText) findViewById(R.id.result);
        pb = (ProgressBar) findViewById(R.id.pb1);

        if (mLogic == null) {
            this.bindService(new Intent(MainActivity.this, LogicService.class), logicConn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLogic != null) {
            unbindService(logicConn);
        }
    }

    public void multiply(View e) {
        try {
            setResult(getNum1() * getNum2());
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void add(View e) {
        try {
            setResult(getNum1() + getNum2());
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void sub(View e) {
        try {
            setResult(getNum1() - getNum2());
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void div(View e) {
        try {
            setResult(getNum1() / getNum2());
        } catch (IllegalArgumentException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setResult(double v) {
        String text = Double.toString(v);
        result.setText(text);
    }

    private double getNum1() {
        String stringVal = num1.getText().toString();
        if (stringVal.equals("")) {
            stringVal = "empty";
        }
        try {
            return Double.parseDouble(stringVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("First '" + stringVal + "' is not a number!", e);
        }
    }

    private double getNum2() {
        String stringVal = num2.getText().toString();
        if (stringVal.equals("")) {
            stringVal = "empty";
        }
        try {
            return Double.parseDouble(stringVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Second '" + stringVal + "' is not a number!", e);
        }
    }

    class PiCalcTask extends AsyncTask<Void, Double, Double> {

        Random r = new Random(System.currentTimeMillis());
        int rounds = 100000;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setProgress(0);
            pb.setEnabled(true);
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            pb.setEnabled(false);
            pb.setProgress(0);
            setResult(aDouble);
        }

        @Override
        protected Double doInBackground(Void... params){
            double in = 0;
            for (double i = 0; i < rounds; i++) {
                double x = r.nextDouble();
                double y = r.nextDouble();
                if (x * x + y * y <= 1) {
                    in++;
                }
                publishProgress(i * 10000 / rounds);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("in " + in);
            return 4 * in / rounds;
        }

        @Override
        protected void onProgressUpdate(final Double... values) {
            super.onProgressUpdate(values);
            pb.setProgress(values[0].intValue());
        }
    }

    public void calcPi(View e) {
        new PiCalcTask().execute();
    }
}
