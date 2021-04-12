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
import com.SuperClub.EQ.Data.UserInfo;
import com.SuperClub.EQ.Menu.QueueInfoActivity;
import com.SuperClub.EQ.R;

import java.util.ArrayList;

public class PeopleInQueueRecyclerAdapter extends RecyclerView.Adapter<PeopleInQueueRecyclerAdapter.ViewHolder> {


    ArrayList<UserInfo> users;
    Context context;

    public PeopleInQueueRecyclerAdapter(Context context, ArrayList<UserInfo> names) {
        this.context = context;
        this.users = names;
    }


    @NonNull
    @Override
    public PeopleInQueueRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.queue_member_list_element, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleInQueueRecyclerAdapter.ViewHolder holder, int position) {

        holder.textView.setText(users.get(position).name);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_text);
        }

    }
}
