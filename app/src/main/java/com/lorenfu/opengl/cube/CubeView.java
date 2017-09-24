package com.lorenfu.opengl.cube;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeView extends GLSurfaceView {
    private SceneRenderer mRenderer;

    public CubeView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements Renderer {
        Cube cube;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0, 0, 0, 1);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            cube = new Cube(CubeView.this);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            MatrixState.setProjectFrustum(-0.5f * ratio, 3.5f * ratio, -2, 2, 1, 10f);
            MatrixState.setCamera(0, 0, 3f, 0, 0, 0, 0, 1, 0);
            MatrixState.initObjMatrix();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //清除深度缓存和颜色缓存
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.pushMatrix();
            cube.drawSelf();
            MatrixState.popMatrix();

            MatrixState.pushMatrix();
            MatrixState.translate(3, 0, 0);
            MatrixState.rotate(30, 0, 0, 1);
            MatrixState.scale(1.5f, 1, 1);
            cube.drawSelf();
            MatrixState.popMatrix();
        }
    }
}
