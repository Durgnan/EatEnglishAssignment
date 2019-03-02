package com.durgesh.intern.eatenglishassignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.StringTokenizer;


public class Login extends AppCompatActivity implements Member.AsyncResponse
{

    TextView textView1;
    TextView textView2;
    TextView username;
    TextView lot;
    TextView lat;
    TextView lod;
    Button browse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent i;


        //ID's
        Button logout = findViewById(R.id.log);
        textView1 = findViewById(R.id.status);
        textView2 = findViewById(R.id.devid);
        browse = findViewById(R.id.Browse);
        final String status = textView1.getText().toString();
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(Login.this, Browse.class);
                    startActivity(i);

            }
        });

        logout.setOnClickListener(listen);
        textView1.setText("Successful");
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String uname = bundle.getString("user");

        Log.e("M!",uname+"  "+id);
        textView2.setText(id);
        Activity act = getParent();


       new Member(this).execute(uname,id);

        //Assigning Values
        Log.e("Details",MemberDetails.username1+"\n"+MemberDetails.lastaccesstime1+"\n"+MemberDetails.logintime1+"\n"+MemberDetails.loggeddevice1);




    }
    private View.OnClickListener listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Login.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login.this,MainActivity.class);

            MemberDetails.username1="";
            MemberDetails.loggeddevice1="";
            MemberDetails.lastaccesstime1="";
            MemberDetails.logintime1="";
            textView1.setText("Failed");
            textView2.setText("");

            startActivity(i);
            finish();
        }
    };


    @Override
    public void processFinish(String output) {
         username = findViewById(R.id.uview);
         lod = findViewById(R.id.lodview);
          lat = findViewById(R.id.latview);
         lot = findViewById(R.id.lotview);
        username.setText(MemberDetails.username1);
        lod.setText(MemberDetails.loggeddevice1);
        lat.setText(MemberDetails.lastaccesstime1);
        lot.setText(MemberDetails.logintime1);


    }
}
class Member extends AsyncTask<Object,Void,String>
{
    private Activity activity;
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String output);
    }


    public Member(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        String user = (String) objects[0];
        String session = (String) objects[1];

        String memberlink = "http://4thidioteducation.com/intern/member.php?Log_Username="+user+"&&Session_ID="+session;
        HttpManager hm = new HttpManager();
        String response = hm.getData(memberlink);

        StringTokenizer st = new StringTokenizer(response,",");
        String[] tokens = new String[7];
        int i = 0;
        while (st.hasMoreTokens())
        {
            tokens[i++]=st.nextToken();
        }
        try {
            MemberDetails.logintime1 = tokens[1].substring(tokens[1].indexOf(':')+1);
            MemberDetails.lastaccesstime1 = tokens[2].substring(tokens[2].indexOf(':')+1);
            MemberDetails.loggeddevice1 = tokens[4].substring(tokens[4].indexOf(':')+2,tokens[4].indexOf(':')+12);
            MemberDetails.username1 = user;
        }catch (NullPointerException n)
        {
            n.printStackTrace();

        }


        //Log.e("M6",MemberDetails.logintime1);



        //Log.e("Details",MemberDetails.username1+"\n"+MemberDetails.lastaccesstime1+"\n"+MemberDetails.logintime1+"\n"+MemberDetails.loggeddevice1);




        return response;
    }

    @Override
    protected void onPostExecute(String s) {

       //
        delegate.processFinish(s);



      //  return;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("M3","Loading");
    }
}

 class MemberDetails
{
    static String username1;
    static String logintime1;
    static String loggeddevice1;
    static String lastaccesstime1;

}
