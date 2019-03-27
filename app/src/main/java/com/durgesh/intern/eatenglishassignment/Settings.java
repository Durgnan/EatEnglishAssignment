package com.durgesh.intern.eatenglishassignment;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends AppCompatActivity {
    SeekBar seekBar1,seekBar2;
    TextView textView1,textView2;
    SeekBarAdapter mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void init()
    {
        mydb=new SeekBarAdapter(this);
        Cursor rs = mydb.getData();
        rs.moveToFirst();
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView1 = findViewById(R.id.speed1);
        textView2 = findViewById(R.id.speed2);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        textView1.setText(rs.getString(0));
        textView2.setText(rs.getString(1));
        seekBar1.setProgress(rs.getInt(0));
        seekBar2.setProgress(rs.getInt(1));
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView1.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mydb.update(seekBar.getProgress(),Integer.parseInt(textView2.getText().toString()));

            }
        });
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textView2.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mydb.update(Integer.parseInt(textView1.getText().toString()),seekBar.getProgress());

            }
        });

    }
}
