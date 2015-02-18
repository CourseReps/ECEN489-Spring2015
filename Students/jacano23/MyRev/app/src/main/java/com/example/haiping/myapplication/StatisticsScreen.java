package com.example.haiping.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StatisticsScreen extends Activity {
    String[] scores = new String[18];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            scores = read();
            setContentView(R.layout.statistics_screen);
            ListAdapter newadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,scores);
            ListView statisticListView = (ListView) findViewById(R.id.statisticView);
            statisticListView.setAdapter(newadapter);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] read() throws ParserConfigurationException, IOException, SAXException {
        String[] score = new String[18];
        try {
            File fXmlFile = new File(Environment.getExternalStorageDirectory().getPath(),"schema.xml" );
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("level");

            System.out.println("----------------------------");

            int index = 0;
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    score[index] =  eElement.getAttribute("difficulty");
                    score[++index] =  eElement.getElementsByTagName("First").item(0).getTextContent();
                    score[++index] =  eElement.getElementsByTagName("Second").item(0).getTextContent();
                    score[++index] =  eElement.getElementsByTagName("Third").item(0).getTextContent();
                    score[++index] =  eElement.getElementsByTagName("Fourth").item(0).getTextContent();
                    score[++index] =   eElement.getElementsByTagName("Fifth").item(0).getTextContent();
                    ++index;


                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
}