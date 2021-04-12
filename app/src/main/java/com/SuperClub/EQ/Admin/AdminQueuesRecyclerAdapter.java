package com.SuperClub.EQ.Admin;

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
import com.SuperClub.EQ.Menu.ManageQueueActivity;
import com.SuperClub.EQ.Menu.QueueInfoActivity;
import com.SuperClub.EQ.R;

public class AdminQueuesRecyclerAdapter extends RecyclerView.Adapter<AdminQueuesRecyclerAdapter.ViewHolder> {


    Context context;

    public AdminQueuesRecyclerAdapter(Context context) {
        this.context = context;
        Application.getInstance(context).addMyQueuesListener(() -> {
            notifyDataSetChanged();
        });
    }


    @NonNull
    @Override
    public AdminQueuesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.admin_queues_list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminQueuesRecyclerAdapter.ViewHolder holder, int position) {

        QueueInfo queueInfo = Application.getInstance(context).adminQueues.get(position);

        holder.titleText.setText(queueInfo.title);
        holder.startTimeText.setText(queueInfo.getStartTimeString());
        holder.endTimeText.setText(queueInfo.getEndTimeString());
        holder.startDateText.setText(queueInfo.getStartDateString());
        holder.endDateText.setText(queueInfo.getEndDateString());
        holder.peopleText.setText(queueInfo.getLiveUserCount() + context.getString(R.string.people_waiting_text));
        holder.queueInfo = queueInfo;

    }

    @Override
    public int getItemCount() {
        return Application.getInstance(context).adminQueues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startDateText;
        TextView endDateText;
        TextView startTimeText;
        TextView endTimeText;
        TextView peopleText;
        TextView titleText;
        QueueInfo queueInfo;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            startDateText = itemView.findViewById(R.id.start_date_text);
            endDateText = itemView.findViewById(R.id.end_date_text);

            startTimeText = itemView.findViewById(R.id.start_time_text);
            endTimeText = itemView.findViewById(R.id.end_time_text);

            peopleText = itemView.findViewById(R.id.people_waiting);

            layout = itemView.findViewById(R.id.admin_element_layout);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Application.getInstance(context).passedQueue = queueInfo;
            Intent intent = new Intent(context, ManageQueueActivity.class);
            intent.putExtra("QueueID", queueInfo.id);
            context.startActivity(intent);
        }
    }
}
