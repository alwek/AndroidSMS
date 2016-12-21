package Model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Alican on 2016-12-21.
 * Good luck, Commander!
 */

public class SaveContactMessage implements Runnable {
    private ArrayList<String> list;
    private Context context;
    private boolean toRead;
    private String filename;
    private boolean option = false;

    //to write
    public SaveContactMessage(ArrayList<String> list, Context context, String filename){
        this.list = list;
        this.context = context;
        this.filename = filename;
        this.toRead = false;
    }

    //to read
    public SaveContactMessage(Context context, String filename){
        this.context = context;
        this.filename = filename;
        this.toRead = true;
    }

    public SaveContactMessage(ArrayList<String> list, Context context){
        this.context = context;
        this.list = list;
        this.option = true;
    }

    @Override
    public void run() {
        if(option)
            writeBackgroundMessage();
        else if(toRead)
            readFromFile();
        else
            writeToFile();
    }//run

    private void readFromFile(){
        String line = "";
        try{
            list = new ArrayList<>();
            File file = new File("/" + filename + ".txt");
            FileInputStream input = new FileInputStream(context.getFilesDir().toString() + file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while ((line = reader.readLine()) != null) {
                list.add(line);
            }//while

            ChatObservable.getInstance().updateValue(list);
        }//try
        catch(IOException e){
            e.printStackTrace();
        }
    }//read

    private void writeToFile(){
        try{
            File file = new File("/" + filename + ".txt");
            FileOutputStream fOut = new FileOutputStream(context.getFilesDir().toString() + file, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for(int i = 0; i < list.size(); i++) {
                bw.write(list.get(i));
                bw.newLine();
            }//for

            bw.close();
            fOut.close();

            //ChatObservable.getInstance().updateValue(list);
        }//try
        catch(IOException e) {
            e.printStackTrace();
        }//catch
    }//write

    private void writeBackgroundMessage(){
        try{
            String[] splitted = list.get(0).split(" ");
            String number = splitted[0].replace(":", "");

            File file = new File("/" + number + ".txt");
            FileOutputStream fOut = new FileOutputStream(context.getFilesDir().toString() + file, true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            for(int i = 0; i < list.size(); i++){
                splitted = list.get(i).split(" ");
                splitted[0] = splitted[0].replace(":", "");
                if(splitted[0].equals(number)){
                    bw.write(splitted[0] + ": " + splitted[1]);
                    bw.newLine();
                    list.remove(i);
                }//if
            }//for
            if(list.size() != 0)
                writeBackgroundMessage();

        }//try
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}//class
