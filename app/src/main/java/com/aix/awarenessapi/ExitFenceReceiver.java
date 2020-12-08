package com.aix.awarenessapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import java.util.Date;

public class ExitFenceReceiver extends BroadcastReceiver {
    NotificationHelper notificationHelper;
    private static final String TAG = "WABBLER";

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        notificationHelper = new NotificationHelper(context);

        if (TextUtils.equals(fenceState.getFenceKey(), MainActivity.EXIT_KEY)) {
            String fenceStateStr;
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    fenceStateStr = "true";
                    break;
                case FenceState.FALSE:
                    fenceStateStr = "false";
                    break;
                case FenceState.UNKNOWN:
                    fenceStateStr = "unknown";
                    break;
                default:
                    fenceStateStr = "unknown value";
            }
            notificationHelper.sendHighPriorityNotification("Awareness Notification", "Exited geofence "+ new Date()+ " - " +fenceStateStr, MainActivity.class);
            Log.d(TAG, "onReceive: "+ fenceStateStr);
        }
    }

}