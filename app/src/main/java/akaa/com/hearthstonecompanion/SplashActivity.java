package akaa.com.hearthstonecompanion;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by confo on 10/11/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(this, MainActivity.class);

        int splashScreenDuration = 3000;

        TimerTask splashEndTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };

        Timer splashEndTimer = new Timer();
        splashEndTimer.schedule(splashEndTask, splashScreenDuration);
    }
}
