package com.lorenfu.opengl.six_star;

import android.opengl.Matrix;

public class MatrixState {
    public static float[] mProjMatrix = new float[16];
    public static float[] mCameraMatrix = new float[16];

    public static void setCamera(float cx, float cy, float cz,
                                 float tx, float ty, float tz,
                                 float upx, float upy, float upz) {
        Matrix.setLookAtM(mCameraMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    public static void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static float[] getFinalMatrix(float[] spec) {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mCameraMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}