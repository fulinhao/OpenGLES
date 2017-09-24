package com.lorenfu.opengl.rotate_triangle;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.lorenfu.opengl.util.MyGLUtil;

import java.nio.FloatBuffer;

public class Triangle {
    public static float[] mProjMatrix = new float[16];
    public static float[] mCameraMatrix = new float[16];
    private static float[] mMVPMatrix;
    private static float[] mObjMatrix = new float[16];

    private static final float UNIT_SIZE = 0.2f;

    private int mProgram;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;

    private float[] vertices = {
            -4 * UNIT_SIZE, 0, 0,
            0, -4 * UNIT_SIZE, 0,
            4 * UNIT_SIZE, 0, 0
    };

    private float[] colors = {
            1, 1, 1, 0,
            0, 0, 1, 0,
            0, 1, 0, 0
    };

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    public float xAngle;

    public Triangle(MyGLView glView) {
        initVertexData();
        initShader(glView);
    }

    private void initVertexData() {
        mVertexBuffer = MyGLUtil.allocFloatBuffer(vertices);
        mColorBuffer = MyGLUtil.allocFloatBuffer(colors);
    }

    private void initShader(MyGLView glView) {
        String vertexShader = MyGLUtil.loadShaderFromAssets("vertex.glsl", glView.getResources());
        String fragmentShader = MyGLUtil.loadShaderFromAssets("fragment.glsl", glView.getResources());
        mProgram = MyGLUtil.createProgram(vertexShader, fragmentShader);
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        mColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mObjMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mObjMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mObjMatrix, 0, xAngle, 1, 0, 0);
        //将变换矩阵传入渲染管线
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFinalMatrix(mObjMatrix), 0);
        GLES30.glVertexAttribPointer(mPositionHandle, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES30.glVertexAttribPointer(mColorHandle, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        GLES30.glEnableVertexAttribArray(mColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
    }

    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
