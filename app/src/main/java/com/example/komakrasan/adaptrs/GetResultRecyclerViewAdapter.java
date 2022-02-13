package com.example.komakrasan.adaptrs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.komakrasan.R;
import com.example.komakrasan.model.ClassCounty;
import com.example.komakrasan.model.ClassResult;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;


public class GetResultRecyclerViewAdapter extends RecyclerView.Adapter<GetResultRecyclerViewAdapter.ViewHolder> {

    private SparseBooleanArray expandState = new SparseBooleanArray();
    private List<ClassResult> classResults;
    private Context context;

    public GetResultRecyclerViewAdapter(List<ClassResult> classResults, Context context) {
        this.classResults = classResults;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {


        holder.tv_name.setText(classResults.get(position).getName());
        holder.tv_num_family.setText(classResults.get(position).getNumber_Family());
        holder.tv_place.setText(classResults.get(position).getPlace());
        holder.tv_help.setText(classResults.get(position).getAnswer());
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.relativeLayout, 0f, 180f).start();
                expandState.put(position, true);

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.relativeLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.relativeLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    @Override
    public int getItemCount() {

        return  classResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_num_family,tv_city,tv_county,tv_place,tv_help;
        ExpandableLinearLayout expandableLayout;
        RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.item_result_TextView_name);
            tv_num_family = itemView.findViewById(R.id.item_result_TextView_member);
            tv_place = itemView.findViewById(R.id.item_result_textView_place);
            tv_help = itemView.findViewById(R.id.item_result_TextView_helps);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            relativeLayout = itemView.findViewById(R.id.button);
      }
    }

    //methods that are dependent on expandableLayout
    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}