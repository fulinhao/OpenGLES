package com.lorenfu.opengl.six_star;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SixStarView extends GLSurfaceView {
    private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    private SceneRenderer mRenderer;
    private float mLastX, mLastY;

    public SixStarView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                for (SixStar star : mRenderer.stars) {
                    star.xAngle += dy * TOUCH_SCALE_FACTOR;
                    star.yAngle += dx * TOUCH_SCALE_FACTOR;
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private class SceneRenderer implements Renderer {
        SixStar[] stars = new SixStar[6];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            for (int i = 0; i < stars.length; ++i) {
                stars[i] = new SixStar(SixStarView.this, 0.5f, 0.2f, -0.7f * i);
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
//            MatrixState.setProjectOrtho(-2 * ratio, 2 * ratio, -2, 2, 1, 10f);
            MatrixState.setProjectFrustum(-2 * ratio, 2 * ratio, -2, 2, 1, 10f);
            MatrixState.setCamera(0, 0, 3f, 0, 0, 0, 0, 1, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓存和颜色缓存
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            for (SixStar star : stars) {
                star.drawSelf();
            }
        }
    }
}
