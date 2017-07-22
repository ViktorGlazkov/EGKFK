package com.example.viktor.kfkservice.connection.service;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

import com.example.viktor.kfkservice.connection.Connection;
import com.example.viktor.kfkservice.constant.Constants;

import java.io.IOException;
import java.util.List;

public class ConnectionServiceImpl implements ConnectionService {
    private Connection connection;
    private Handler handler = new Handler();

    public ConnectionServiceImpl() {
        connection = new Connection();

        connection.setBtAdapter(BluetoothAdapter.getDefaultAdapter());
        connection.setDevice(connection.getBtAdapter().getRemoteDevice(Constants.BLUETOOTH_MAC_ADDRESS));

        for (int i = 0; i < Constants.QUE_SIZE; i++) {
            connection.getQue().add(i, 0);
        }

        try {
            connection.setBtSocket(
                    connection.getDevice().createRfcommSocketToServiceRecord(Constants.MY_UUID)
            );

            connection.getBtSocket().connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Runnable readData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    connection.setInputStream(connection.getBtSocket().getInputStream());

                    StringBuilder sb = new StringBuilder();

                    while (!connection.isCompletedLine()) {
                        connection.setCompletedLine(writeToQue(sb, connection.isCompletedLine()));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 10);
            }
        };

        return runnable;

    }

    private boolean writeToQue(StringBuilder sb, boolean k) {
        try {
            byte[] buffer = new byte[1];
            int bytes = connection.getInputStream().read(buffer);

            String strIncom = new String(buffer, 0, bytes);

            sb.append(strIncom); // собираем символы в строку
            int endOfLineIndex = sb.indexOf("\r\n"); // определяем конец строки

            if (endOfLineIndex > 0) {
                writeLine(sb, endOfLineIndex);
            }

        } catch (IOException e) {

        }
        return k;
    }

    private void writeLine(StringBuilder sb, int endOfLineIndex) {
        int index = sb.indexOf("A0 ");

        if (index != -1) {
            String value = sb.substring(index + 3, endOfLineIndex);
            addToQue(Integer.valueOf(value));
            connection.setCompletedLine(true);
        }

        sb.delete(0, sb.length());
    }

    public double getAverage() {
        int average = 0;

        List<Integer> que = connection.getQue();

        if (que.size() > 40) {
            for (int i = 0; i < que.size() - 1; i++) {
                average += que.get(i);
            }

            return average / que.size();

        } else {
            return 0;
        }
    }

    private synchronized void addToQue(int value) {
        connection.getQue().add(value);
        while(connection.getQue().size() > 50) {
            connection.getQue().remove(0);
        }
    }
}

