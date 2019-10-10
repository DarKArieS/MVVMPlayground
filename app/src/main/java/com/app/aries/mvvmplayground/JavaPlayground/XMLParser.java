package com.app.aries.mvvmplayground.JavaPlayground;

import android.content.Context;
import timber.log.Timber;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class XMLParser {

    XMLParser(Context context){

    }

    void run(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        }catch (Throwable e){
            Timber.tag("xmlParser").d("GGWP");
        }
    }

}
