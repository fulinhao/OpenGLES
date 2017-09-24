package com.lorenfu.opengl.util;

import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MyGLUtil {
    public static final String ES_ERROR_TAG = "ES30_ERROR";

    private static int loadShader(int shaderType, String source) {
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, source);
            GLES30.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(ES_ERROR_TAG, "Could not compile shader " + shaderType + ":");
                Log.e(ES_ERROR_TAG, GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) return 0;
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) return 0;
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            checkGLError("glAttachShader");
            GLES30.glAttachShader(program, fragmentShader);
            checkGLError("glAttachShader");
            GLES30.glLinkProgram(program);
            int[] linked = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linked, 0);
            if (linked[0] == 0) {
                Log.e(ES_ERROR_TAG, "Could not link program: ");
                Log.e(ES_ERROR_TAG, GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static String loadShaderFromAssets(String fileName, Resources res) {
        String source = null;
        try {
            InputStream is = res.getAssets().open(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                baos.write(ch);
            }
            source = new String(baos.toByteArray(), "utf-8");
            source = source.replace("\\r\\n", "\n");
            baos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }

    public static void checkGLError(String op) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(ES_ERROR_TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public static FloatBuffer allocFloatBuffer(float[] vertices) {
        ByteBuffer buf = ByteBuffer.allocateDirect(vertices.length * 4);
        buf.order(ByteOrder.nativeOrder());
        FloatBuffer fb = buf.asFloatBuffer();
        fb.put(vertices);
        fb.position(0);
        return fb;
    }

    public static IntBuffer allocIntBuffer(int[] indices) {
        ByteBuffer buf = ByteBuffer.allocateDirect(indices.length * 4);
        buf.order(ByteOrder.nativeOrder());
        IntBuffer ib = buf.asIntBuffer();
        ib.put(indices);
        ib.position(0);
        return ib;
    }
}