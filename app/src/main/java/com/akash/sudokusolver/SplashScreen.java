package com.akash.sudokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private TextView s, _u, d, o, k ,u;
    private Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        runAnimation();
    }

    private  void runAnimation(){
        s = (TextView) findViewById(R.id.s);
        Animation bounce_from_top = AnimationUtils.loadAnimation(this, R.anim.bounce_from_top);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        s.startAnimation(bounce_from_top);
        _u = (TextView) findViewById(R.id.u_);
        bounce_from_top.setDuration(1200);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        _u.startAnimation(bounce_from_top);
        d = (TextView) findViewById(R.id.d);
        bounce_from_top.setDuration(1400);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        d.startAnimation(bounce_from_top);
        o = (TextView) findViewById(R.id.o);
        bounce_from_top.setDuration(1600);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        o.startAnimation(bounce_from_top);
        k = (TextView) findViewById(R.id.k);
        bounce_from_top.setDuration(1800);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        k.startAnimation(bounce_from_top);
        u = (TextView) findViewById(R.id.u);
        bounce_from_top.setDuration(2000);
        bounce_from_top.setFillEnabled(true);
        bounce_from_top.setFillAfter(true);
        bounce_from_top.setInterpolator(new BounceInterpolator());
        u.startAnimation(bounce_from_top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        }, 3500);
    }
}
