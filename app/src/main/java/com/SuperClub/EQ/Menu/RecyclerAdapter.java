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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    Context context;

    RecyclerAdapter(Context context) {
        this.context = context;
        Application.getInstance(context).addMyQueuesListener(() -> {
            notifyDataSetChanged();
        });
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        QueueInfo queueInfo = Application.getInstance(context).myQueues.get(position);
        holder.textView.setText(queueInfo.title);
        holder.queueInfo = queueInfo;
        holder.timeText.setText(queueInfo.expectedTime);
        holder.sizeText.setText(queueInfo.usersBeforeMe);
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
            textView = itemView.findViewById(R.id.code_text);
            layout = itemView.findViewById(R.id.element_layout);
            timeText = itemView.findViewById(R.id.time_left_number);
            sizeText = itemView.findViewById(R.id.queue_num);
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
