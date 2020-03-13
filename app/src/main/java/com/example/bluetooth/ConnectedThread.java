package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.agrosup_app.StartTreatmentActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private Handler handler; // handler that gets info from Bluetooth service

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        this.handler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        mmBuffer = new byte[20];
        String data = new String();
        int numBytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {

                Arrays.fill(mmBuffer, (byte)0);
                // Read from the InputStream.
                numBytes = mmInStream.read(mmBuffer);
                Log.d("BT", "bajtow:"+numBytes);

//                    // Send the obtained bytes to the UI activity.
//                    Message readMsg = handler.obtainMessage(
//                            MessageConstants.MESSAGE_READ, numBytes, -1,
//                            mmBuffer);
//                    readMsg.sendToTarget();
                Log.d("BT", "data:"+data);
                data = checkData(mmBuffer,numBytes,data);
                //Log.d("wiad",new String(mmBuffer, Charset.defaultCharset())+","+numBytes);
                //sendMessage(5,);
            } catch (IOException e) {
                Log.d("BT", "Input stream was disconnected"+ e.toString());
                break;
            }
        }

    }

    public String checkData(byte[] array, int size, String data) {
        String input = new String(mmBuffer, Charset.defaultCharset());
        StringBuilder output = new StringBuilder(data);
        Log.d("BT","wejscie: "+data);
        for(int i=0;i<input.length();i++) {
            //Log.d("BT", "znak"+input.charAt(i)+","+array[i]);
            if(!( (input.charAt(i) >= 48 && input.charAt(i) <= 57) || input.charAt(i) == 44 || input.charAt(i) == 83 || input.charAt(i) == 75)) {
                continue;
            }
            if( input.charAt(i) == 'K') {
                sendMessage(6, new BluetoothDataModel(output.toString()));
                output = new StringBuilder();
                continue;
            }
            if(input.charAt(i) == 'S') {
                continue;
            } else {
                //Log.d("BT","dodalem");
                output.append(input.charAt(i));
            }
        }
        return output.toString();
    }
    // Call this from the main activity to send data to the remote device.
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            Log.d("BT", "wyslalem");
            // Share the sent message with the UI activity.
//            Message writtenMsg = handler.obtainMessage(
//                    MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
//            writtenMsg.sendToTarget();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);

            // Send a failure message back to the activity.
            Message writeErrorMsg =
                    handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString("toast",
                    "Couldn't send data to the other device");
            writeErrorMsg.setData(bundle);
            handler.sendMessage(writeErrorMsg);
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

    // Send message to handler
    private <T> void sendMessage(int code, T object) {
        Message msg = new Message();
        msg.what = code;
        msg.obj = object;
        handler.sendMessage(msg);
    }
}
