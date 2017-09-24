package com.lorenfu.opengl.activity;

import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.lorenfu.opengl.Ball.BallView;
import com.lorenfu.opengl.Ball.MatrixState;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        mGLView = new MyGLView(this);
//        mGLView = new SixStarView(this);
//        mGLView = new CubeView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setLayoutParams(new LinearLayout.LayoutParams(-1, 100));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MatrixState.setLightLocation((progress - 50) / 10f, 1.5f, 3f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        linearLayout.addView(seekBar);

        mGLView = new BallView(this);
        mGLView.requestFocus();
        mGLView.setFocusableInTouchMode(true);
        linearLayout.addView(mGLView);
        setContentView(linearLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}