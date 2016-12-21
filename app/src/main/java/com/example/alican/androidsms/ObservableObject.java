package com.example.alican.androidsms;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */
public class ObservableObject extends Observable{
    private static ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance() {
        return instance;
    }//getInstance

    private ObservableObject() {

    }//Observable

    /**
     * notifies the observer with updates
     * @param from
     * @param message
     */
    public void updateValue(String from, String message){
        synchronized(this){
            setChanged();
            notifyObservers(from+"%"+message);
        }//synchronized
    }//update
}//class
