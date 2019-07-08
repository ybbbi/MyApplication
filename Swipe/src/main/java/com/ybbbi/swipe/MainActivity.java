package com.ybbbi.swipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        recyclerview.setAdapter(new Adapter());
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(currentLayout!=null){
                    currentLayout.close();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    SwipeLayout currentLayout;

    public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {



        @Override
        public Viewholder onCreateViewHolder(ViewGroup viewGroup,  int i) {
            View v = View.inflate(MainActivity.this, R.layout.recyclecontent, null);

            return new Viewholder(v);
        }

        @Override
        public void onBindViewHolder(Viewholder viewHolder, final int i) {
            viewHolder.tv_name.setText(Constants.NAMES[i]);
            viewHolder.swipelayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {

                @Override
                public void open(SwipeLayout layout) {
                    if (currentLayout != null && layout != currentLayout) {
                        //关闭前一个
                        currentLayout.close();
                    }
                    currentLayout = layout;
                }

                @Override
                public void close(SwipeLayout layout) {
                    if (layout == currentLayout) {
                        currentLayout = null;
                    }
                }
            });
            viewHolder.swipelayout.setonSwipeClickListener(new SwipeLayout.onSwipeClickListener() {
                @Override
                public void onClick() {

                    Toast.makeText(MainActivity.this,"点击"+i+"个条目",Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return Constants.NAMES.length;
        }



        class Viewholder extends RecyclerView.ViewHolder {

            @BindView(R.id.swipelayout)
            SwipeLayout swipelayout;

            protected final TextView tv_name;

            public Viewholder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                tv_name = itemView.findViewById(R.id.tv_name);
            }
        }
    }

}
