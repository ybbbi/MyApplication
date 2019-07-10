package com.ybbbi.indexbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ArrayList<Friend> friends = new ArrayList<>();
    private ListView listView;
    private MyAdapter myAdapter;
    private IndexBar indexbar;
    private TextView toast;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        indexbar.setOnChangeListener(new IndexBar.OnChangeListener() {
            @Override
            public void onChange(String s) {
                handler.removeCallbacksAndMessages(null);
                for (int i = 0; i < friends.size(); i++) {
                    String pinyin = friends.get(i).pinyin;
                    if (pinyin.equals(s)) {
                        listView.setSelection(i);
                        break;
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.setVisibility(View.GONE);
                    }
                }, 1000);
                toast.setText(s);
                toast.setVisibility(View.VISIBLE);
            }
        });
        listView.setAdapter(myAdapter);


    }

    private void init() {
        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(this)));
        toast = findViewById(R.id.tv_toast);
        indexbar = findViewById(R.id.indexbar);
        prepareData();
        Collections.sort(friends);
        listView = findViewById(R.id.listview);
        myAdapter = new MyAdapter(friends);
    }

    private void prepareData() {
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
        friends.add(new Friend("单田芳"));
    }
}
