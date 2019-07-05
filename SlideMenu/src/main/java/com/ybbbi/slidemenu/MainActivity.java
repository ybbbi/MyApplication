package com.ybbbi.slidemenu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView main_listview = findViewById(R.id.main_listview);
        ListView menu_listview = findViewById(R.id.menu_listview);
        SlideMenu slide = findViewById(R.id.slide);
        main_listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Constants.NAMES));
        menu_listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Constants.sCheeseStrings){

            @Override
            public View getView(int position, View convertView,  ViewGroup parent) {
                TextView tv= (TextView) super.getView(position,convertView,parent);
                tv.setTextColor(Color.GRAY);
                return tv;
            }
        });
        slide.setOnSlideChangeListener(new SlideMenu.OnSlideChangeListener() {
            @Override
            public void onOpen() {
                Toast.makeText(MainActivity.this,"打开",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose() {
                Toast.makeText(MainActivity.this,"关闭",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
