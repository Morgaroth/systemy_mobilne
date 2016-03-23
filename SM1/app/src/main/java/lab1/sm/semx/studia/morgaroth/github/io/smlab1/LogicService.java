package lab1.sm.semx.studia.morgaroth.github.io.smlab1;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LogicService extends Service {
    public LogicService() {
    }

    private IBinder mBind = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    public class LocalBinder extends Binder {
        LogicService getService() {
            return LogicService.this;
        }
    }


    public double add(double a, double b) {
        return a + b;
    }

    public double sub(double a, double b) {
        return a - b;
    }

    public double mult(double a, double b) {
        return a + b;
    }

    public double div(double a, double b) {
        return a / b;
    }

    public double pi() {
        return 3.14;
    }
}
