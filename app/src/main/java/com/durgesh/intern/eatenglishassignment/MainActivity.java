package com.durgesh.intern.eatenglishassignment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText pass;
    Context context;
    private String TAG = MainActivity.class.getSimpleName();
   public Button log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        log = findViewById(R.id.login);

        log.setOnClickListener(listen);
    }
    private View.OnClickListener listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String uname= user.getText().toString();
            String pword = pass.getText().toString();
            if ((uname==null && pword==null)||(uname=="" && pword=="")) {

              /*  Intent i = new Intent(MainActivity.this, Login.class);
                i.putExtra("user", uname);
                i.putExtra("pass", pword);
                startActivity(i);*/
                Toast.makeText(MainActivity.this,"Please Enter Valid Credentials",Toast.LENGTH_LONG).show();
            }
            else
            {
                new Authenticate(context).execute(uname, pword);
            }





        }
    };

}
class Authenticate extends AsyncTask<Object,Void,String> {
    private final WeakReference<Context> contextReference;

    String status = "";
    Context context;
    //String Session_id ;
    String username;
    public  Authenticate(Context context)
    {
        this.contextReference = new WeakReference<Context>(context);

    }

    @Override
    protected String doInBackground(Object[] objects) {
         username = (String) objects[0];
        String password = (String) objects[1];

        Context context = this.contextReference.get();
        if(context != null) {
            // Do your work

            if (username == "" || password == "") {
                return "";
            }
            try {
                String link2 = "http://4thidioteducation.com/intern/login_android.php?Log_Username=" + username + "&&Log_Password=" + password;
                HttpManager hm = new HttpManager();
                String res = hm.getData(link2);
                if (res==null)
                {
                    return null;
                }
                if (res.contains("success")) {
                    Log.e("Status", "Success");
                    status = "success";

                    String  Session_id = res.substring(37, res.length() - 3);
                    Log.e("Session ID", Session_id);
                    //String[] abc= {username,status,Session_id};
                    return Session_id;


                } else {
                    Log.e("Status", "Failed");
                    return "";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            //String link = "http://192.168.43.233:8080/andro/login_android.php?Log_Username="+username+"&&Log_Password="+password;
            //String link1 = "http://eatenglish.in/intern/andro1/a_login_android.php?Log_Username=admin4&&Log_Password=dddd";

        }


        return "";
    }

    @Override
    protected void onPreExecute() {
       // super.onPreExecute();
        Log.e("Message", "Authenticating the credentials...");
    }

   @Override
    protected void onPostExecute( String result) {
      //  super.onPostExecute(result);
       Context context = this.contextReference.get();
       if (result==null)
       {
           Toast.makeText(context,"Connection Problem !! Please try again.",Toast.LENGTH_LONG).show();
       }
       if(context != null) {
           // Do your work
           try {
               if (!result.equalsIgnoreCase(""))
               {
                   Log.e("Message", "Login Successful");
                   context.startActivity(new Intent(context,Browse.class).putExtra("id",result).putExtra("user",username));


               }
               else
               {

               }
           }
           catch (Exception e)
           {
               Log.e("Error",e.getMessage());
           }
       }



    }

}

