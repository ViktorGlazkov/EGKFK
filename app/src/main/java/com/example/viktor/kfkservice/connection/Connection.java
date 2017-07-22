package com.example.viktor.kfkservice.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connection {
    private BluetoothDevice device;
    private BluetoothSocket btSocket;
    private InputStream inputStream;
    private boolean isCompletedLine = false;
    List<Integer> que = Collections.synchronizedList(new ArrayList<Integer>());

    private BluetoothAdapter btAdapter;

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public void setBtSocket(BluetoothSocket btSocket) {
        this.btSocket = btSocket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isCompletedLine() {
        return isCompletedLine;
    }

    public void setCompletedLine(boolean completedLine) {
        isCompletedLine = completedLine;
    }

    public synchronized List<Integer> getQue() {
        return que;
    }

    public void setQue(List<Integer> que) {
        this.que = que;
    }

    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    public void setBtAdapter(BluetoothAdapter btAdapter) {
        this.btAdapter = btAdapter;
    }
}

