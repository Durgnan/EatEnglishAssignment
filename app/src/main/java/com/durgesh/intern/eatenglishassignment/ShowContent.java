package com.durgesh.intern.eatenglishassignment;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.durgesh.intern.eatenglishassignment.adapters.SeekBarAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class ShowContent extends AppCompatActivity {
    JSONArray jsonArray;
    String url,id,uname;
    TextView t1, t2;
    Toolbar toolbar;
    TextToSpeech textToSpeech, textToSpeech1;
    SeekBarAdapter mydb;
    MediaPlayer mp;
    int speed1,speed2,v1,v2,v3,v4,i=0;
    Cursor rs;
    Button b1;
    Animation animation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        t1 = findViewById(R.id.q2);
        t2 = findViewById(R.id.a2);
        b1 = findViewById(R.id.press);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.audiofile1);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_leave);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mydb=new SeekBarAdapter(this);
        rs = mydb.getData();
        rs.moveToFirst();
        speed1 = rs.getInt(0);
        speed2=rs.getInt(1);
        v1=rs.getInt(2);
        v2=rs.getInt(3);
        v3=rs.getInt(4);
        v4=rs.getInt(5);


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
                textToSpeech1.setSpeechRate((float)(rs.getFloat(1)*2)/100);
                Log.e("Message","ReadNotes.php");
                Button b2 = findViewById(R.id.listenthis);
                b2.setEnabled(true);
                b2.setVisibility(View.VISIBLE);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v3==1)
                        textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        if (v4==1)
                        textToSpeech1.speak(t2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);


                    }
                });


                readFunction();
            }
            if(url.equalsIgnoreCase("DO"))
            {
                b1.setVisibility(View.INVISIBLE);
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
        textToSpeech1.setSpeechRate((float)(rs.getFloat(1)*2)/100);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (v1==1)
                        mp.start();

                    String question = "[Q"+i+"] "+jsonArray.getJSONObject(i).getString("q1")+" "+jsonArray.getJSONObject(i).getString("q2")+"\n"+jsonArray.getJSONObject(i).getString("q3");
                    t1.setText(question);
                    t1.startAnimation(animation2);
                    t2.setText("");
                    if (v3==1)
                        textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    t1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (v2==1)
                                    mp.start();
                                String answer = "[A"+(i-1)+"] "+jsonArray.getJSONObject(i-1).getString("a1")+" "+jsonArray.getJSONObject(i-1).getString("a2")+"\n"+jsonArray.getJSONObject(i-1).getString("a3");
                                t1.setText(answer);
                                t1.startAnimation(animation2);
                                if (v4==1)
                                    textToSpeech1.speak(t1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },5000);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i++;
                if (i>jsonArray.length())
                {
                    i=0;
                    handler.removeCallbacks(this);
                    doAssignment();
                }
                else
                    handler.postDelayed(this,10*1000);
            }
        });





    }

    void readFunction()

    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {



                try {
                    if (v1==1)
                        mp.start();
                    String question = "[Q"+i+"] "+jsonArray.getJSONObject(i).getString("q1")+" "+jsonArray.getJSONObject(i).getString("q2")+"\n"+jsonArray.getJSONObject(i).getString("q3");
                    t1.setText(question);
                    t1.startAnimation(animation2);
                    t2.setText("");
                    t2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (v2==1)
                            mp.start();
                        String answer = "[A"+(i-1)+"] "+jsonArray.getJSONObject(i-1).getString("a1")+" "+jsonArray.getJSONObject(i-1).getString("a2")+"\n"+jsonArray.getJSONObject(i-1).getString("a3");
                        t2.setText(answer);
                        t2.startAnimation(animation2);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },4500);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        i++;
                if (i>jsonArray.length())
                {
                    i=0;
                    handler.removeCallbacks(this);
                    readFunction();

                }
                else
                    handler.postDelayed(this,7500);
            }
        });

    }
    @Override
    protected void onPause() {
        if (textToSpeech1!=null)
        {
            textToSpeech1.stop();
            textToSpeech1.shutdown();
        }
        mp.pause();
        mp.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mp.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
