package com.lorenfu.opengl.six_star;

import android.opengl.GLES30;
import android.opengl.Matrix;

import com.lorenfu.opengl.util.MyGLUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SixStar {
    public float xAngle, yAngle;
    private static final float UNIT_SIZE = 1f;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;

    private int mProgram;
    private int aPosition;
    private int aColor;
    private int uMVPMatrix;
    private int vCount;

    private static float[] mObjMatrix = new float[16];

    public SixStar(SixStarView view, float R, float r, float z) {
        initVertexData(R, r, z);
        initShader(view);
    }

    public void initVertexData(float R, float r, float z) {
        List<Float> list = new ArrayList<>();
        float delta = 360 / 6;
        for (float angle = 0; angle < 360; angle += delta) {
            //第一个三角形
            //第一个点
            list.add(0f);
            list.add(0f);
            list.add(z);
            //第二个点
            list.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle))));
            list.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle))));
            list.add(z);
            //第三个点
            list.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle
                    + delta / 2))));
            list.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle
                    + delta / 2))));
            list.add(z);
            //第二个三角形
            //第一个点
            list.add(0f);
            list.add(0f);
            list.add(z);
            //第二个点
            list.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle
                    + delta / 2))));
            list.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle
                    + delta / 2))));
            list.add(z);
            //第三个点
            list.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle
                    + delta))));
            list.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle
                    + delta))));
            list.add(z);
        }
        vCount = list.size() / 3;
        float[] vertices = new float[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            vertices[i] = list.get(i);
        }
        mVertexBuffer = MyGLUtil.allocFloatBuffer(vertices);
        float[] colors = new float[vCount * 4];
        for (int i = 0; i < vCount; ++i) {
            if (i % 3 == 0) {   //中心点
                colors[i * 4] = 1;
                colors[i * 4 + 1] = 1;
                colors[i * 4 + 2] = 1;
                colors[i * 4 + 3] = 0;
            } else {
                colors[i * 4] = 0.45f;
                colors[i * 4 + 1] = 0.75f;
                colors[i * 4 + 2] = 0.75f;
                colors[i * 4 + 3] = 0;
            }
        }
        mColorBuffer = MyGLUtil.allocFloatBuffer(colors);
    }


    public void initShader(SixStarView view) {
        String vertexShader = MyGLUtil.loadShaderFromAssets("vertex.glsl", view.getResources());
        String fragmentShader = MyGLUtil.loadShaderFromAssets("fragment.glsl", view.getResources());
        mProgram = MyGLUtil.createProgram(vertexShader, fragmentShader);
        aPosition = GLES30.glGetAttribLocation(mProgram, "aPosition");
        aColor = GLES30.glGetAttribLocation(mProgram, "aColor");
        uMVPMatrix = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }


    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mObjMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mObjMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mObjMatrix, 0, xAngle, 1, 0, 0);
        Matrix.rotateM(mObjMatrix, 0, yAngle, 0, 1, 0);
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(mObjMatrix), 0);
        GLES30.glVertexAttribPointer(aPosition, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES30.glVertexAttribPointer(aColor, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(aPosition);
        GLES30.glEnableVertexAttribArray(aColor);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
