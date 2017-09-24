package com.lorenfu.opengl.cube;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.lorenfu.opengl.util.MyGLUtil;

import java.nio.FloatBuffer;

public class Cube {
    private static final float UNIT_SIZE = 1f;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;

    private int mProgram;
    private int aPosition;
    private int aColor;
    private int uMVPMatrix;
    private int vCount;

    public Cube(CubeView view) {
        initVertexData();
        initShader(view);
    }

    public void initVertexData() {
        float[] vertices = {0, 0, 0, 1, 0, 0, 1, 1, 0,
                0, 0, 0, 1, 1, 0, 0, 1, 0
        };
        vCount = vertices.length / 3;
        mVertexBuffer = MyGLUtil.allocFloatBuffer(vertices);
        float[] colors = new float[vCount * 4];
        for (int i = 0; i < vCount; ++i) {
            colors[i * 4] = (i / 3) / 5f + 0.3f;
            colors[i * 4 + 1] = 0;
            colors[i * 4 + 2] = 0;
            colors[i * 4 + 3] = 0;
        }
        mColorBuffer = MyGLUtil.allocFloatBuffer(colors);
    }


    public void initShader(GLSurfaceView view) {
        String vertexShader = MyGLUtil.loadShaderFromAssets("vertex.glsl", view.getResources());
        String fragmentShader = MyGLUtil.loadShaderFromAssets("fragment.glsl", view.getResources());
        mProgram = MyGLUtil.createProgram(vertexShader, fragmentShader);
        aPosition = GLES30.glGetAttribLocation(mProgram, "aPosition");
        aColor = GLES30.glGetAttribLocation(mProgram, "aColor");
        uMVPMatrix = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(uMVPMatrix, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glVertexAttribPointer(aPosition, 3, GLES30.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES30.glVertexAttribPointer(aColor, 4, GLES30.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(aPosition);
        GLES30.glEnableVertexAttribArray(aColor);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
