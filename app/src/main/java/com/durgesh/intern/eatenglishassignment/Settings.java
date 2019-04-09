package com.durgesh.intern.eatenglishassignment;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.durgesh.intern.eatenglishassignment.adapters.SeekBarAdapter;


public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SeekBar seekBar1,seekBar2;
    TextView textView1,textView2;
    SeekBarAdapter mydb;
    ToggleButton v1,v2,v3,v4;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();


    }




    void init()
    {
        mydb=new SeekBarAdapter(this);
        Cursor rs = mydb.getData();
        rs.moveToFirst();

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        textView1 = findViewById(R.id.speed1);
        spinner=findViewById(R.id.spinnerChoice);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.settings_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(rs.getInt(6));
        spinner.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        textView2 = findViewById(R.id.speed2);
        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        v1= findViewById(R.id.v1);
        v2= findViewById(R.id.v2);
        v3= findViewById(R.id.v3);
        v4= findViewById(R.id.v4);



        v1.setChecked((rs.getInt(2)==1)?true:false);
        v2.setChecked((rs.getInt(3)==1)?true:false);
        v3.setChecked((rs.getInt(4)==1)?true:false);
        v4.setChecked((rs.getInt(5)==1)?true:false);
        v1.setOnClickListener(listen);
        v2.setOnClickListener(listen);
        v3.setOnClickListener(listen);
        v4.setOnClickListener(listen);
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
                mydb.update(seekBar.getProgress(),Integer.parseInt(textView2.getText().toString()),v1.isChecked(),v2.isChecked(),v3.isChecked(),v4.isChecked(),spinner.getSelectedItemPosition());

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
                mydb.update(Integer.parseInt(textView1.getText().toString()),seekBar.getProgress(),v1.isChecked(),v2.isChecked(),v3.isChecked(),v4.isChecked(),spinner.getSelectedItemPosition());

            }
        });

    }
    View.OnClickListener listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mydb.update(seekBar1.getProgress(),seekBar2.getProgress(),v1.isChecked(),v2.isChecked(),v3.isChecked(),v4.isChecked(),spinner.getSelectedItemPosition());

        }
    };

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mydb.update(seekBar1.getProgress(),seekBar2.getProgress(),v1.isChecked(),v2.isChecked(),v3.isChecked(),v4.isChecked(),position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
