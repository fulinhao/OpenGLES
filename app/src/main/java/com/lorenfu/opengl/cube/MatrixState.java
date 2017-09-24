package com.lorenfu.opengl.cube;

import android.opengl.Matrix;

public class MatrixState {
    public static float[] mProjMatrix = new float[16];
    public static float[] mCameraMatrix = new float[16];
    public static float[] mObjMatrix = new float[16];

    private static float[][] mStack = new float[10][16];
    private static int mStackTop = -1;

    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz,
                                 float upx, float upy, float upz) {
        Matrix.setLookAtM(mCameraMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    public static void setProjectOrtho(float left, float right, float bottom, float top,
                                       float near, float far) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static void setProjectFrustum(float left, float right, float bottom, float top,
                                         float near, float far) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static float[] getFinalMatrix() {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, mObjMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public static void translate(float x, float y, float z) {
        Matrix.translateM(mObjMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mObjMatrix, 0, angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        Matrix.scaleM(mObjMatrix, 0, x, y, z);
    }

    public static void initObjMatrix() {
        mObjMatrix = new float[16];
        Matrix.setRotateM(mObjMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix() {
        if (mStackTop == mStack.length - 1) {
            return;
        }
        ++mStackTop;
        for (int i = 0; i < 16; ++i) {
            mStack[mStackTop][i] = mObjMatrix[i];
        }
    }

    public static void popMatrix() {
        if (mStackTop == -1) {
            return;
        }
        for (int i = 0; i < 16; ++i) {
            mObjMatrix[i] = mStack[mStackTop][i];
        }
        --mStackTop;
    }
}