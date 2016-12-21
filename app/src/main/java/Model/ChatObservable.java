package Model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Alican on 2016-12-15.
 * Good luck, Commander!
 */
public class ChatObservable extends Observable{
    private static ChatObservable instance = new ChatObservable();

    public static ChatObservable getInstance() {
        return instance;
    }//getInstance

    private ChatObservable() {

    }//Observable

    /**
     * notifies the observer with updates
     */
    public void updateValue(ArrayList<String> list){
        synchronized(this){
            setChanged();
            notifyObservers(list);
        }//synchronized
    }//update
}//class
