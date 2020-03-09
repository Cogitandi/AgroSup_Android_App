package com.example.bluetooth;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDataModel {
    private float totalCombustion;
    private float currentCombustion;
    private float area;
    private float velocity;
    private float temperature;
    private float humidity;

    public BluetoothDataModel(String data) {
       // Log.d("BTM", data);
        List<Integer> commaIndex = new ArrayList<>();
        //Log.d("DATA", data);
        for(int i=0;i<data.length();i++) {
            if(data.charAt(i) == ',') {
                commaIndex.add(new Integer(i));
            }
        }
        try {
            totalCombustion = (float) new Integer( data.substring(0,(commaIndex.get(0))) ) / 100;
            currentCombustion = (float) new Integer( data.substring(commaIndex.get(0)+1,(commaIndex.get(1))) ) / 100;
            area = (float) new Integer( data.substring(commaIndex.get(1)+1,(commaIndex.get(2))) ) / 100;
            velocity = (float) new Integer( data.substring(commaIndex.get(2)+1,(commaIndex.get(3))) ) / 100;
            temperature = (float) new Integer( data.substring(commaIndex.get(3)+1,(commaIndex.get(4))) ) / 100;
            humidity = (float) new Integer( data.substring(commaIndex.get(4)+1,(commaIndex.get(5))) ) / 100;
            //Log.d("BTM",totalCombustion+","+currentCombustion+","+area+","+velocity+","+temperature+","+humidity );
        } catch (Exception e) {
            Log.d("BluetoothDataModel", "exception: "+e.toString());
        }




    }

    public float getTotalCombustion() {
        return totalCombustion;
    }

    public float getCurrentCombustion() {
        return currentCombustion;
    }

    public float getArea() {
        return area;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }


}
