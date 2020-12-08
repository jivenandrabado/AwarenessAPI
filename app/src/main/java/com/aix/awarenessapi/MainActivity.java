package com.aix.awarenessapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.BeaconFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WABBLER";
    public static final String FENCE_KEY = "walking_fence";

    public static final String EXIT_KEY = "exit_fence";

    private PendingIntent pendingIntent, pendingIntent1,pendingIntent2;
    private LatLng officeLatlng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        officeLatlng = new LatLng(14.592408,120.987933);






//        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
//            AwarenessFence walkingFence = DetectedActivityFence.during(DetectedActivity.IN_VEHICLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        AwarenessFence walkingFence = LocationFence.in(officeLatlng.latitude, officeLatlng.longitude, 100,5L);
        AwarenessFence exitFence = LocationFence.exiting(officeLatlng.latitude, officeLatlng.longitude,100);
        AwarenessFence entering = LocationFence.entering(officeLatlng.latitude, officeLatlng.longitude, 100);


        List<BeaconState.TypeFilter> BEACON_TYPE_FILTERS =
                Arrays.asList( BeaconState.TypeFilter.with( "88aa7908ec7c86a0a901", "int"));

//        AwarenessFence walkingFence = BeaconFence.near(BEACON_TYPE_FILTERS);
        Intent intent = new Intent(this,FenceReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentt = new Intent(this,ExitFenceReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(this, 0, intentt, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intenttt = new Intent(this,ExitFenceReceiver.class);
        pendingIntent2 = PendingIntent.getBroadcast(this, 0, intenttt, PendingIntent.FLAG_UPDATE_CURRENT);

        Awareness.getFenceClient(this).updateFences(new FenceUpdateRequest.Builder()
                .addFence(FENCE_KEY, walkingFence, pendingIntent)
                .build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Entrance Fence was successfully registered.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Fence could not be registered: " + e);
                    }
                });

        Awareness.getFenceClient(this).updateFences(new FenceUpdateRequest.Builder()
                .addFence(EXIT_KEY, exitFence, pendingIntent1)
                .build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Exit Fence was successfully registered.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Fence could not be registered: " + e);
                    }
                });

        Awareness.getFenceClient(this).updateFences(new FenceUpdateRequest.Builder()
                .addFence(EXIT_KEY, entering, pendingIntent2)
                .build())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Entering Fence was successfully registered.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Fence could not be registered: " + e);
                    }
                });

    }



}