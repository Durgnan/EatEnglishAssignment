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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("User Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        textView1 = findViewById(R.id.status);
        textView2 = findViewById(R.id.devid);
        textView1.setText("Successful");
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String uname = bundle.getString("user");

        textView2.setText(id);


       new Member(this).execute(uname,id);

        //Assigning Values
        Log.e("Details",MemberDetails.username1+"\n"+MemberDetails.lastaccesstime1+"\n"+MemberDetails.logintime1+"\n"+MemberDetails.loggeddevice1);




    }



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
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
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






        return response;
    }

    @Override
    protected void onPostExecute(String s) {

        delegate.processFinish(s);



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
