package com.kernicky.gl_prototype.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.kernicky.gl_prototype.MyGLRenderer;

public abstract class MultiColourModel extends EmissiveModel {
	
	private int mDiffHandle;
	private int mSpecHandle;
	private int mShinHandle;
	private int mRadialHandle;
	private int mTransparencyHandle;
	protected float spec[];
	protected float diff[];
	private float shine[];
	private float transparency;
	
	private FloatBuffer ambientBuffer, specularBuffer, diffuseBuffer, shininessBuffer;
	
	@Override
	public void setProgram() {
		this.setProgramID(MyGLRenderer.multiColorProgram);
	}
	
	@Override
	public void initBuffers(){
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bbp = ByteBuffer.allocateDirect(
		// (# of coordinate values * 4 bytes per float)
				getCoords().length * 4);
		bbp.order(ByteOrder.nativeOrder());
		vertexBuffer = bbp.asFloatBuffer();
		vertexBuffer.put(getCoords());
		vertexBuffer.position(0);

		ByteBuffer bbn = ByteBuffer.allocateDirect(getNormals().length * 4);
		bbn.order(ByteOrder.nativeOrder());
		setNormalBuffer(bbn.asFloatBuffer());
		getNormalBuffer().put(getNormals());
		getNormalBuffer().position(0);
		
		ByteBuffer bba = ByteBuffer.allocateDirect(getAmb().length * 4);
		bba.order(ByteOrder.nativeOrder());
		ambientBuffer = bba.asFloatBuffer();
		ambientBuffer.put(getAmb());
		ambientBuffer.position(0);

		ByteBuffer bbd = ByteBuffer.allocateDirect(getDiff().length * 4);
		bbd.order(ByteOrder.nativeOrder());
		diffuseBuffer = bbd.asFloatBuffer();
		diffuseBuffer.put(getDiff());
		diffuseBuffer.position(0);

		ByteBuffer bbsp = ByteBuffer.allocateDirect(getSpec().length * 4);
		bbsp.order(ByteOrder.nativeOrder());
		specularBuffer = bbsp.asFloatBuffer();
		specularBuffer.put(getSpec());
		specularBuffer.position(0);

		ByteBuffer bbsh = ByteBuffer.allocateDirect(getShine().length * 4);
		bbsh.order(ByteOrder.nativeOrder());
		shininessBuffer = bbsp.asFloatBuffer();
		shininessBuffer.put(getShine());
		shininessBuffer.position(0);
	}
	
	@Override
	public void draw(float[] mView, float[] mProj, float[] mLightPos) {

		float[] mModel = new float[16];
		float[] mModelView = new float[16];
		float[] mModelViewProj = new float[16];
		
		Matrix.setIdentityM(mModel, 0);
		mModel = applyTransforms(mModel);
		
		Matrix.multiplyMM(mModelView, 0, mView, 0, mModel, 0);
		Matrix.multiplyMM(mModelViewProj, 0, mProj, 0, mModelView, 0);	
		
		GLES20.glUseProgram(getProgramID());

//		vertexBuffer.position(0);
//		mPositionHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Position");
//		GLES20.glEnableVertexAttribArray(mPositionHandle);
//		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
//				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
//
//		setmNormalHandle(GLES20.glGetAttribLocation(getProgramID(), "a_Normal"));
//		GLES20.glEnableVertexAttribArray(getmNormalHandle());
//		GLES20.glVertexAttribPointer(getmNormalHandle(), COORDS_PER_VERTEX,
//				GLES20.GL_FLOAT, false, vertexStride, getNormalBuffer());
//		
//		setmAmbHandle(GLES20.glGetUniformLocation(getProgramID(), "u_Ambient"));
//		GLES20.glUniform3fv(getmAmbHandle(), 1, getAmb(), 0);
		
		mTransparencyHandle = GLES20.glGetUniformLocation(getProgramID(), "u_Transparency");
		GLES20.glUniform1f(mTransparencyHandle, transparency);
		
		mRadialHandle = GLES20.glGetUniformLocation(getProgramID(), "u_Radial");
		GLES20.glUniform1f(mRadialHandle, radialEffect);
		vertexBuffer.position(0);
		mPositionHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Position");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
		MyGLRenderer.checkGlError("position");

		setmNormalHandle(GLES20.glGetAttribLocation(getProgramID(), "a_Normal"));
		//System.out.println("N**************" + mNormalHandle);
		GLES20.glEnableVertexAttribArray(getmNormalHandle());
		GLES20.glVertexAttribPointer(getmNormalHandle(), COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, normalBuffer);
		MyGLRenderer.checkGlError("normal");

		setmAmbHandle(GLES20.glGetAttribLocation(getProgramID(), "a_Ambient"));
		// System.out.println("A**************" + mAmbHandle);
		GLES20.glEnableVertexAttribArray(getmAmbHandle());
		GLES20.glVertexAttribPointer(getmAmbHandle(), COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, ambientBuffer);
		MyGLRenderer.checkGlError("ambient");

		mDiffHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Diffuse");
		// System.out.println("D**************" + mDiffHandle);
		GLES20.glEnableVertexAttribArray(mDiffHandle);
		GLES20.glVertexAttribPointer(mDiffHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, diffuseBuffer);
		MyGLRenderer.checkGlError("diffuse");

		mSpecHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Specular");
		// System.out.println("S**************" + mSpecHandle);
		GLES20.glEnableVertexAttribArray(mSpecHandle);
		GLES20.glVertexAttribPointer(mSpecHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, specularBuffer);
		MyGLRenderer.checkGlError("specular");

		mShinHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Shininess");
		// System.out.println("Sh**************" + mShinHandle);
		GLES20.glEnableVertexAttribArray(mShinHandle);
		GLES20.glVertexAttribPointer(mShinHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, 4, shininessBuffer);
		MyGLRenderer.checkGlError("shininess");
		
//		mDiffHandle = GLES20.glGetUniformLocation(getProgramID(), "u_Diffuse");
//		GLES20.glUniform3fv(mDiffHandle, 1, getDiff(), 0);
//
//		mSpecHandle = GLES20.glGetUniformLocation(getProgramID(), "u_Specular");
//		GLES20.glUniform3fv(mSpecHandle, 1, getSpec(), 0);
//
//		mShinHandle = GLES20.glGetUniformLocation(getProgramID(), "u_Shininess");
//		GLES20.glUniform1f(mShinHandle, getShine());

		mMVPMatrixHandle = GLES20.glGetUniformLocation(getProgramID(), "u_MVPMatrix");
		MyGLRenderer.checkGlError("glGetUniformLocation");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mModelViewProj, 0);

		mMVMatrixHandle = GLES20.glGetUniformLocation(getProgramID(), "u_MVMatrix");
		MyGLRenderer.checkGlError("glGetUniformLocation");
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mModelView, 0);

		setmLightPosHandle(GLES20.glGetUniformLocation(getProgramID(), "u_LightPos"));
		GLES20.glUniform3fv(getmLightPosHandle(), mLightPos.length/3, mLightPos, 0);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, getCoords().length / 3);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		
		currentTick = (currentTick + 1) % maxTick;
	}
	
	protected float[] getSpec() {
		return spec;
	}

	protected void setSpec(float spec[]) {
		this.spec = spec;
	}

	protected float[] getDiff() {
		return diff;
	}

	protected void setDiff(float diff[]) {
		this.diff = diff;
	}
	
	protected float[] getShine() {
		return shine;
	}

	protected void setShine(float[] shine) {
		this.shine = shine;
	}
	
	protected float getTransparency() {
		return transparency;
	}
	
	protected void setTransparency(float transparency) {
		this.transparency = transparency;
	}

}
