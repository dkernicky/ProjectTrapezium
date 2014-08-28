package com.kernicky.gl_prototype;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "MyGLRenderer";
	private Scene s;
	
	public static int reflectiveProgram;
	public static int emissiveProgram;
	public static int multiColorProgram;
	
	private static final double fps = 1000.0/120.0;
	private double lastUpdate = System.currentTimeMillis();
	
	private final float[] mProjMatrix = new float[16];

	// Declare as volatile because we are updating it from another thread
	public volatile float mAngle;

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//mTriangle = new Triangle();
		//mShip = new PhongCube();
		createShaderPrograms();
		s = new SampleScene();

	}

	private void createShaderPrograms() {
		// prepare shaders and OpenGL program
		int mVert = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				ShaderData.singleColorVertexShaderCode);
		int mFrag = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				ShaderData.singleColorFragmentShaderCode);

		reflectiveProgram = GLES20.glCreateProgram(); 					// create empty OpenGL Program

		GLES20.glAttachShader(reflectiveProgram, mVert); 			// add vertex shader									
		GLES20.glAttachShader(reflectiveProgram, mFrag); 		// add fragment shader												
		
		//GLES20.glBindAttribLocation(mProgram, 1, "a_Position");
		//GLES20.glBindAttribLocation(mProgram, 2, "a_Normal");
		GLES20.glLinkProgram(reflectiveProgram); 						// create OpenGL program executables

		
		
		mVert = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				ShaderData.lightVertexShaderCode);
		mFrag = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				ShaderData.lightFragmentShaderCode);

		emissiveProgram = GLES20.glCreateProgram(); 					// create empty OpenGL Program

		GLES20.glAttachShader(emissiveProgram, mVert); 			// add vertex shader									
		GLES20.glAttachShader(emissiveProgram, mFrag); 		// add fragment shader												
		

		GLES20.glLinkProgram(emissiveProgram); 
		
		
		mVert = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				ShaderData.multiColorVertexShaderCode);
		mFrag = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				ShaderData.multiColorFragmentShaderCode);

		multiColorProgram = GLES20.glCreateProgram(); 					// create empty OpenGL Program

		GLES20.glAttachShader(multiColorProgram, mVert); 			// add vertex shader									
		GLES20.glAttachShader(multiColorProgram, mFrag); 		// add fragment shader												
		

		GLES20.glLinkProgram(multiColorProgram); 			
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		//GLES20.glEnable( GLES20.GL_CULL_FACE);
		//GLES20.glCullFace(GLES20.GL_BACK);
		GLES20.glEnable( GLES20.GL_BLEND);
	   //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		//s.draw(mShip);
		
//		double currentTime = (double) System.currentTimeMillis();
//		if(currentTime - lastUpdate > fps) {
			//lastUpdate = currentTime;
			s.draw();
		
		//}

	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		
		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 25);
		s.setMProj(mProjMatrix);

	}

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * 
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, &quot;vColor&quot;);
	 * MyGLRenderer.checkGlError(&quot;glGetUniformLocation&quot;);
	 * </pre>
	 * 
	 * If the operation is not successful, the check throws an error.
	 * 
	 * @param glOperation
	 *            - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}
}
