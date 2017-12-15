package com.apporio.demotaxiappdriver.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.apporio.demotaxiappdriver.Config;
import com.apporio.demotaxiappdriver.R;

import java.io.IOException;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {

        String ride_status = intent.getExtras().getString("ride_status");
        String ride_id = intent.getExtras().getString("ride_id");

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_tone));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            mediaPlayer.prepare();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                }
            });
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent i = new Intent();
        if(ride_status.equals("1")){  //   ride booked for normal type
            i.setClassName("com.apporio.demotaxiappdriver", "com.apporio.demotaxiappdriver.TrialReceivePassengerActivity");
        }else if (ride_status.equals("10")){  //  ride booked for rental type
            i.setClassName("com.apporio.demotaxiappdriver", "com.apporio.demotaxiappdriver.ReceiveRentalPassengerActivity");
        }else {
            Log.e("**" , "Please MybroadCastReceiver class ");
        }

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(""+ Config.IntentKeys.RIDE_ID, ride_id);
        context.startActivity(i);


    }
}
