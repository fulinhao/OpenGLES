package com.lorenfu.opengl.Ball;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MatrixState {
    public static FloatBuffer sLightLocBuf;
    private static ByteBuffer sLightLocBB = ByteBuffer.allocateDirect(3 * 4);
    private static float[] sLightLocation = new float[3];

    public static FloatBuffer sCameraLocBuf;
    private static ByteBuffer sCameraLocBB = ByteBuffer.allocateDirect(3 * 4);
    private static float[] sCameraLocation = new float[3];

    public static float[] sProjMatrix = new float[16];
    public static float[] sCameraMatrix = new float[16];
    public static float[] sObjMatrix = new float[16];

    private static float[][] mStack = new float[10][16];
    private static int mStackTop = -1;

    public static void setLightLocation(float x, float y, float z) {
        sLightLocation[0] = x;
        sLightLocation[1] = y;
        sLightLocation[2] = z;
        sLightLocBB.clear();
        sLightLocBB.order(ByteOrder.nativeOrder());
        sLightLocBuf = sLightLocBB.asFloatBuffer();
        sLightLocBuf.put(sLightLocation);
        sLightLocBuf.position(0);
    }

    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz,
                                 float upx, float upy, float upz) {
        Matrix.setLookAtM(sCameraMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
        sCameraLocation[0] = cx;
        sCameraLocation[1] = cy;
        sCameraLocation[2] = cz;
        sCameraLocBB.clear();
        sCameraLocBB.order(ByteOrder.nativeOrder());
        sCameraLocBuf = sCameraLocBB.asFloatBuffer();
        sCameraLocBuf.put(sCameraLocation);
        sCameraLocBuf.position(0);
    }

    public static void setProjectOrtho(float left, float right, float bottom, float top,
                                       float near, float far) {
        Matrix.orthoM(sProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static void setProjectFrustum(float left, float right, float bottom, float top,
                                         float near, float far) {
        Matrix.frustumM(sProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static float[] getFinalMatrix() {
        float[] mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, sCameraMatrix, 0, sObjMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, sProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public static void translate(float x, float y, float z) {
        Matrix.translateM(sObjMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(sObjMatrix, 0, angle, x, y, z);
    }

    public static void scale(float x, float y, float z) {
        Matrix.scaleM(sObjMatrix, 0, x, y, z);
    }

    public static void initObjMatrix() {
        sObjMatrix = new float[16];
        Matrix.setRotateM(sObjMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix() {
        if (mStackTop == mStack.length - 1) {
            return;
        }
        ++mStackTop;
        for (int i = 0; i < 16; ++i) {
            mStack[mStackTop][i] = sObjMatrix[i];
        }
    }

    public static void popMatrix() {
        if (mStackTop == -1) {
            return;
        }
        for (int i = 0; i < 16; ++i) {
            sObjMatrix[i] = mStack[mStackTop][i];
        }
        --mStackTop;
    }
}