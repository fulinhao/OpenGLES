package com.lorenfu.opengl.rotate_triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLView extends GLSurfaceView {
    private SceneRenderer mRenderer;

    public MyGLView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements Renderer {
        Triangle triangle;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            triangle = new Triangle(MyGLView.this);
            new RotateThread().start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            Matrix.frustumM(triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            Matrix.setLookAtM(triangle.mCameraMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓存和颜色缓存
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            triangle.drawSelf();
        }
    }

    private class RotateThread extends Thread {
        boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                mRenderer.triangle.xAngle += 0.375f;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
