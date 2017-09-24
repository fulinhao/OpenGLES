package com.lorenfu.opengl.Ball;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class BallView extends GLSurfaceView {
    private static final float UNIT_SIZE = 0.5f;
    private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    private SceneRenderer mRenderer;
    private float mLastX, mLastY;

    public BallView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    float xAngle, yAngle, zAngle;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;
                xAngle += dy * TOUCH_SCALE_FACTOR;
                yAngle += dx * TOUCH_SCALE_FACTOR;
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private class SceneRenderer implements Renderer {
        Ball ball;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            ball = new Ball(BallView.this);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-UNIT_SIZE * ratio, UNIT_SIZE * ratio, -UNIT_SIZE, UNIT_SIZE, 1, 10f);
            MatrixState.setCamera(0, 0, 10f, 0, 0, 0, 0, 1, 0);
            MatrixState.initObjMatrix();
            MatrixState.setLightLocation(-50 / 10f, 1.5f, 3f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓存和颜色缓存
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.pushMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(-3f, 0, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            MatrixState.rotate(yAngle, 0, 1, 0);
            ball.drawSelf();
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(3f, 0, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            MatrixState.rotate(yAngle, 0, 1, 0);
            ball.drawSelf();
            MatrixState.popMatrix();

            MatrixState.popMatrix();
        }
    }
}
