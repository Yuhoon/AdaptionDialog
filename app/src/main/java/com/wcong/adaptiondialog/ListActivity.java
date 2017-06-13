package com.wcong.adaptiondialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        data = initData();

        RecyclerView mRecyclerView;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ListAdapter());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
    }

    private List<String> initData() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            list.add("Test\t" + i);
        }
        return list;
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ListActivity.this).inflate(R.layout.item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.txt.setText(data.get(position));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(ListActivity.this).inflate(R.layout.layout_window, null);
                    ((TextView) view.findViewById(R.id.txt_msg)).setText("This is " + data.get(position));
                    new AdaptionDialog(ListActivity.this, v, view).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txt;

            LinearLayout ll;

            public MyViewHolder(View view) {
                super(view);
                txt = (TextView) view.findViewById(R.id.txt);
                ll = (LinearLayout) view.findViewById(R.id.ll);
            }
        }
    }
}

