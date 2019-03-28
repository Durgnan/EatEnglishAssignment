package com.durgesh.intern.eatenglishassignment;

import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class ShowContent extends AppCompatActivity {
    JSONArray jsonArray;
    String url;
    TextView t1;
    TextView t2;
    Toolbar toolbar;
    TextToSpeech textToSpeech;
    TextToSpeech textToSpeech1;
    SeekBarAdapter mydb;
    int speed1,speed2;
    String id,uname;
    int i=0;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        t1 = findViewById(R.id.q2);
        t2 = findViewById(R.id.a2);
        b1 = findViewById(R.id.press);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mydb=new SeekBarAdapter(this);
        Cursor rs = mydb.getData();
        rs.moveToFirst();
        speed1 = rs.getInt(0);
        speed2=rs.getInt(1);
        try{
            textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status!=TextToSpeech.ERROR){
                        textToSpeech.setLanguage(Locale.US);
                    }

                }
            });
            textToSpeech1=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status!=TextToSpeech.ERROR){
                        textToSpeech1.setLanguage(new Locale("hin","IND"));
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }catch (Error error)
        {
            error.printStackTrace();
        }


        b1.setOnClickListener(speak);


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        uname = bundle.getString("user");


        try {
            url = bundle.getString("url");

            jsonArray = new JSONArray(bundle.getString("json"));
            for (int i=0;i<jsonArray.length();i++)
            {
                Log.e("Question",jsonArray.getJSONObject(i).getString("q2"));
                Log.e("Answer",jsonArray.getJSONObject(i).getString("a2"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    View.OnClickListener speak = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e("",i+"");
            b1.setText("Next");
            if (url.equalsIgnoreCase("READ"))
            {
                Log.e("Message","ReadNotes.php");
                Button b2 = findViewById(R.id.listenthis);
                b2.setEnabled(true);
                b2.setVisibility(View.VISIBLE);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        textToSpeech1.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);


                    }
                });


                readFunction();
            }
            if(url.equalsIgnoreCase("DO"))
            {
                Log.e("Message","Do.php");
                doAssignment();
            }




            if (i>=jsonArray.length())
            {
                b1.setText("End");
               b1.setEnabled(false);
            }





       }

    };
    void doAssignment()
    {

        try {
            t1.setText(jsonArray.getJSONObject(i).getString("q2"));
            t2.setText("");
            textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            t2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        t2.setText(jsonArray.getJSONObject(i-1).getString("a2"));
                        textToSpeech1.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },5000);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        i++;
        if (i>=jsonArray.length())
        {
            b1.setText("End");
            b1.setEnabled(false);
        }


    }

    void readFunction()

    {

        try {
            t1.setText(jsonArray.getJSONObject(i).getString("q2"));
            t2.setText("");
         //   textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            t2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        t2.setText(jsonArray.getJSONObject(i-1).getString("a2"));
           //             textToSpeech.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },5000);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        i++;
        if (i>=jsonArray.length())
        {
            b1.setText("End");
            b1.setEnabled(false);
        }

    }
    @Override
    protected void onPause() {
        if (textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent i = new Intent(ShowContent.this,Browse.class).putExtra("id",id).putExtra("user",uname);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id1=item.getItemId();
        if (id1 == R.id.userdata)
        {
            Intent i = new Intent(ShowContent.this,Login.class).putExtra("id",id).putExtra("user",uname);
            startActivity(i);
        }
        if (id1 == R.id.logout)
        {
            Toast.makeText(ShowContent.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();

            Intent i = new Intent(ShowContent.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if (id1 == R.id.settings)
        {
            Intent i = new Intent(ShowContent.this,Settings.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

}
