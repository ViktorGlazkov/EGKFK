package com.example.viktor.kfkservice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.viktor.kfkservice.R;
import com.example.viktor.kfkservice.connection.service.ConnectionService;
import com.example.viktor.kfkservice.connection.service.ConnectionServiceImpl;
import com.example.viktor.kfkservice.constant.Constants;
import com.example.viktor.kfkservice.kfk.Kfk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MeasurementActivity extends Activity {

    private TextView textP;
    private TextView textG;
    private TextView textE;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            double pvalue = getkfk().getP((Double) msg.obj);
            double evalue = 2 - Math.log10(Math.abs(pvalue));
            textP.setText("" + String.format("%.2f", pvalue) + " %");
            textG.setText("" + String.format("%.4f", msg.obj) + " B");
            textE.setText("" + String.format("%.4f", evalue) + " Aбс");
        }
    };

    private ConnectionService connectionService;
    private Kfk kfk;

    private synchronized Kfk getkfk() {
        return kfk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_measurement);
        textG = (TextView) findViewById(R.id.text_g);
        textP = (TextView) findViewById(R.id.text_p);
        textE = (TextView) findViewById(R.id.text_e);

        connectionService = new ConnectionServiceImpl();
        kfk = new Kfk();

        String zVal = String.format("%.4f", getkfk().getZero()) + " B";
        ((TextView) findViewById(R.id.text_nul)).setText(zVal);

        startThreads();
    }

    public void startThreads() {
        List<Runnable> runnableList = new ArrayList<>();

        runnableList.add(connectionService.readData());
        // runnableList.add(getGRunnable());
        runnableList.add(getPRunnable());

        for (int i = 0; i < runnableList.size(); i++) {
            Thread thread = new Thread(runnableList.get(i));

            thread.start();
        }
    }

    public Runnable getPRunnable() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double readedVal = read();

                Message pmsg = handler.obtainMessage();

                pmsg.obj = readedVal;

                handler.sendMessage(pmsg);
                handler.postDelayed(this, 20);
            }
        };

        return runnable;
    }

    public void listenG(View view) {
        getkfk().setU100(read());
    }

    public void listenZero(View view) {
        TextView text = ((TextView) findViewById(R.id.text_nul));
        getkfk().setZero(read());
        double value = getkfk().getZero();

        text.setText("" + String.format("%.4f", value) + " B");
    }

    private double read() {
        double data = connectionService.getAverage();
        return (data * Constants.V0) / 1000;
    }
}
