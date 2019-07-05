package com.ybbbi.swipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(new Adapter()) ;
}
       public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder>{


            @Override
            public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v=View.inflate(MainActivity.this,R.layout.recyclecontent,null);
                return new Viewholder(v);
            }

           @Override
           public void onBindViewHolder(@NonNull Adapter.Viewholder viewHolder, int i) {
                viewHolder.tv_name.setText(Constants.NAMES[i]);
           }


           @Override
            public int getItemCount() {
                return Constants.NAMES.length;
            }

            class Viewholder extends RecyclerView.ViewHolder {

                protected final TextView tv_name;

                public Viewholder(@NonNull View itemView) {
                    super(itemView);
                    tv_name = itemView.findViewById(R.id.tv_name);
                }
            }


        }

    }
