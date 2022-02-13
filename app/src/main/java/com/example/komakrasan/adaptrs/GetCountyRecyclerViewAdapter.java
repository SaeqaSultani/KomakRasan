package com.example.komakrasan.adaptrs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.komakrasan.R;
import com.example.komakrasan.activities.CountyActivity;
import com.example.komakrasan.activities.ResultActivity;
import com.example.komakrasan.model.ClassCounty;
import com.example.komakrasan.model.ClassProvince;

import java.util.List;


public class GetCountyRecyclerViewAdapter extends RecyclerView.Adapter<GetCountyRecyclerViewAdapter.ViewHolder> {

    private List<ClassCounty> countyList;
    private Context context;

    public GetCountyRecyclerViewAdapter(List<ClassCounty> classCounties, Context context) {
        this.countyList = classCounties;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        holder.tv_city.setText(countyList.get(position).getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("id",countyList.get(position).getId());
//                Log.i("goog", "onClick: gop"+countyList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  countyList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_city;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

           tv_city = itemView.findViewById(R.id.item_maine_TextView_city);
            relativeLayout = itemView.findViewById(R.id.rl);

        }
    }
}