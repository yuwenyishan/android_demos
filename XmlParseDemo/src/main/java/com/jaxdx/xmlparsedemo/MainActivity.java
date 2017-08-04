package com.jaxdx.xmlparsedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.dom).setOnClickListener(this);
        findViewById(R.id.sax).setOnClickListener(this);
        findViewById(R.id.pull).setOnClickListener(this);
        showResult = (TextView) findViewById(R.id.result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dom:
                DomHelper domHelper = new DomHelper();
                try {
                    List<Person> persons = domHelper.getPerson(getAssets().open("person.xml"));
                    StringBuilder stringBuilder = new StringBuilder("dom parse xml :\n");
                    for (Person person : persons) {
                        stringBuilder.append(person.toString()).append("\n");
                    }
                    showResult.setText(stringBuilder.append(showResult.getText().toString()).toString());
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sax:
                SaxHelper saxHelper = new SaxHelper();
                try {
                    List<Person> persons = saxHelper.parsePersons(getAssets().open("person.xml"));
                    StringBuilder stringBuilder = new StringBuilder("sax parse xml :\n");
                    for (Person person : persons) {
                        stringBuilder.append(person.toString()).append("\n");
                    }
                    showResult.setText(stringBuilder.append(showResult.getText().toString()).toString());
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.pull:

                PullHelper pullHelper = new PullHelper();
                try {
                    List<Person> persons = pullHelper.parsePersons(getAssets().open("person.xml"));
                    StringBuilder stringBuilder = new StringBuilder("pull parse xml :\n");
                    for (Person person : persons) {
                        stringBuilder.append(person.toString()).append("\n");
                    }
                    showResult.setText(stringBuilder.append(showResult.getText().toString()).toString());
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
