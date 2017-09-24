package com.lorenfu.opengl.Ball;

import android.opengl.GLES30;

import com.lorenfu.opengl.util.MyGLUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Ball {
    private static final float UNIT_SIZE = 1f;

    private FloatBuffer mVertexBuffer;
    private IntBuffer mIndexBuffer;

    private int mProgram;
    private int aPosition;
    private int uMVPMatrix;
    private int uObjMatrix;
    private int uLightLocation;
    private int uCameraLocation;
    private int uRadius;
    private int iCount;

    private float R = 2.0f;
    public static final int ANGLE_SPAN = 10;

    public Ball(BallView view) {
        initVertexData();
        initShader(view);
    }

    private void initVertexData() {
        List<Float> vList = new ArrayList<>();
        List<Integer> iList = new ArrayList<>();
        int index = 0;
        for (int vAngle = -90; vAngle < 90; vAngle += ANGLE_SPAN) {
            for (int hAngle = 0; hAngle <= 360; hAngle += ANGLE_SPAN) {
                addCoordinate(vList, vAngle, hAngle);
                addCoordinate(vList, vAngle, hAngle + ANGLE_SPAN);
                addCoordinate(vList, vAngle + ANGLE_SPAN, hAngle);
                addCoordinate(vList, vAngle + ANGLE_SPAN, hAngle + ANGLE_SPAN);

                iList.add(index);
                iList.add(index + 3);
                iList.add(index + 1);
                iList.add(index);
                iList.add(index + 2);
                iList.add(index + 3);
                index += 4;
            }
        }
        iCount = iList.size();
        float[] vertices = new float[vList.size()];
        for (int i = 0; i < vList.size(); ++i)
            vertices[i] = vList.get(i);
        mVertexBuffer = MyGLUtil.allocFloatBuffer(vertices);

        int[] indices = new int[iList.size()];
        for (int i = 0; i < iList.size(); ++i)
            indices[i] = iList.get(i);
        mIndexBuffer = MyGLUtil.allocIntBuffer(indices);
    }

    private void addCoordinate(List<Float> list, int vAngle, int hAngle) {
        float x = (float) (UNIT_SIZE * R * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math.toRadians(hAngle)));
        float y = (float) (UNIT_SIZE * R * Math.sin(Math.toRadians(vAngle)));
        float z = (float) (UNIT_SIZE * R * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math.toRadians(hAngle)));
        list.add(x);
        list.add(y);
        list.add(z);
    }

    public void initShader(BallView view) {
        String vertexShader = MyGLUtil.loadShaderFromAssets("ball_vertex.glsl", view.getResources());
        String fragmentShader = MyGLUtil.loadShaderFromAssets("ball_fragment.glsl", view.getResources());
        mProgram = MyGLUtil.createProgram(vertexShader, fragmentShader);
        aPosition = GLES30.glGetAttribLocation(mProgram, "aPosition");
        uMVPMatrix = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        uObjMatrix = GLES30.glGetUniformLocation(mProgram, "uObjMatrix");
        uLightLocation = GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        uCameraLocation = GLES30.glGetUniformLocation(mProgram, "uCameraLocation");
        uRadius = GLES30.glGetUniformLocation(mProgram, "uRadius");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glUniformMatrix4fv(uObjMatrix, 1, false, MatrixState.sObjMatrix, 0);
        GLES30.glUniform3fv(uLightLocation, 1, MatrixState.sLightLocBuf);
        GLES30.glUniform3fv(uCameraLocation, 1, MatrixState.sCameraLocBuf);
        GLES30.glUniform1f(uRadius, UNIT_SIZE * R);
        GLES30.glVertexAttribPointer(aPosition, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES30.glEnableVertexAttribArray(aPosition);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount, GLES30.GL_UNSIGNED_INT, mIndexBuffer);
    }


}
