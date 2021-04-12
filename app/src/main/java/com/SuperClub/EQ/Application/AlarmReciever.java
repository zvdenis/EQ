package com.SuperClub.EQ.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.SuperClub.EQ.network.RequestController;

public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RequestController.getInstance(context).updateMyQueues();
    }
}