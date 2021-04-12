package com.SuperClub.EQ.Menu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.SuperClub.EQ.Application.Application;
import com.SuperClub.EQ.Data.QueueInfo;
import com.SuperClub.EQ.R;

public class MyQueuesRecyclerAdapter extends RecyclerView.Adapter<MyQueuesRecyclerAdapter.ViewHolder> {


    Context context;

    public MyQueuesRecyclerAdapter(Context context) {
        this.context = context;
        Application.getInstance(context).addMyQueuesListener(() -> {
            notifyDataSetChanged();
        });
    }


    @NonNull
    @Override
    public MyQueuesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_queues_list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyQueuesRecyclerAdapter.ViewHolder holder, int position) {

        QueueInfo queueInfo = Application.getInstance(context).myQueues.get(position);
        holder.textView.setText(queueInfo.title);
        holder.queueInfo = queueInfo;
        holder.timeText.setText(queueInfo.getExpectedTime());
        holder.sizeText.setText(String.valueOf(queueInfo.usersBeforeMe + 1));
    }

    @Override
    public int getItemCount() {
        return Application.getInstance(context).myQueues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ConstraintLayout layout;
        QueueInfo queueInfo;
        TextView timeText;
        TextView sizeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_text);
            layout = itemView.findViewById(R.id.element_layout);
            timeText = itemView.findViewById(R.id.end_time_text);
            sizeText = itemView.findViewById(R.id.start_time_text);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Application.getInstance(context).passedQueue = queueInfo;
            Intent intent = new Intent(context, QueueInfoActivity.class);
            intent.putExtra("QueueID", queueInfo.id);
            context.startActivity(intent);
        }
    }
}
