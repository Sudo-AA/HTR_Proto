package com.example.htr_proto;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdpater extends RecyclerView.Adapter<ListAdpater.MyView>{

    private Context context;
    private ArrayList rec_id, rec_datetime, rec_tremor, rec_vibrate;
    ListAdpater(Context context,
                ArrayList rec_id,
                ArrayList rec_datetime,
                ArrayList rec_tremor,
                ArrayList rec_vibrate){
        this.context = context;
        this.rec_id = rec_id;
        this.rec_datetime = rec_datetime;
        this.rec_tremor = rec_tremor;
        this.rec_vibrate = rec_vibrate;



    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent , false);
        return new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.id_rec.setText(String.valueOf(rec_id.get(position)));
        holder.datetime_rec.setText(String.valueOf(rec_datetime.get(position)));
        holder.tremor_rec.setText(String.valueOf(rec_tremor.get(position)));
        holder.vibrate_rec.setText(String.valueOf(rec_vibrate.get(position)));

    }

    @Override
    public int getItemCount() {
        return rec_id.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        TextView id_rec, datetime_rec, tremor_rec, vibrate_rec;


        public MyView(@NonNull View itemView) {
            super(itemView);

            id_rec = itemView.findViewById(R.id.rec_id);
            datetime_rec = itemView.findViewById(R.id.rec_datetime);
            tremor_rec = itemView.findViewById(R.id.rec_tremor);
            vibrate_rec = itemView.findViewById(R.id.rec_vibrate);

        }
    }
}
