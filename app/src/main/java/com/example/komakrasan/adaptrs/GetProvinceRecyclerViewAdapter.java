package com.example.komakrasan.adaptrs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.komakrasan.R;
import com.example.komakrasan.activities.CountyActivity;
import com.example.komakrasan.model.ClassProvince;

import java.util.List;


public class GetProvinceRecyclerViewAdapter extends RecyclerView.Adapter<GetProvinceRecyclerViewAdapter.ViewHolder> {

    private List<ClassProvince> provinceList;
    private Context context;

    public GetProvinceRecyclerViewAdapter(List<ClassProvince> classProvinces, Context context) {
        this.provinceList = classProvinces;
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

        holder.tv_city.setText(provinceList.get(position).getName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CountyActivity.class);
                intent.putExtra("id",provinceList.get(position).getId());
                Log.i("goog", "onClick: gop"+provinceList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return  provinceList.size();
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