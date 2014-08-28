package com.kernicky.gl_prototype.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.kernicky.gl_prototype.MyGLRenderer;
import com.kernicky.gl_prototype.math.MatrixOp;
import com.kernicky.gl_prototype.math.Quaternion;


public abstract class EmissiveModel extends Model {
	
//	protected int mProgram;
//	protected int mVert;
//	protected int mFrag;
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer normalBuffer;

	protected int mPositionHandle;
	protected int mNormalHandle;
	protected int mAmbHandle;
	
	protected int mMVPMatrixHandle;
	protected int mMVMatrixHandle;
	private int mLightPosHandle;
	private float coords[], normals[], amb[]; 

	
	private int programID;

	protected final int COORDS_PER_VERTEX = 3;
	protected final int vertexStride = COORDS_PER_VERTEX * 4;
	

	
	


	EmissiveModel() {
		setData();
		setProgram();

		initBuffers();

	}
	abstract void setData();
	public void setProgram() {
		setProgramID(MyGLRenderer.emissiveProgram);
	}

	public void applyMovement() {
		
	}
	
	public void initBuffers() {
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bbp = ByteBuffer.allocateDirect(
		// (# of coordinate values * 4 bytes per float)
				getCoords().length * 4);
		bbp.order(ByteOrder.nativeOrder());
		vertexBuffer = bbp.asFloatBuffer();
		vertexBuffer.put(getCoords());
		vertexBuffer.position(0);
	}
	
	public void draw(float[] mModel, float[] mView, float[] mProj, float[] mLightPos) {
		transList.set(0, new Transformation(mModel));
		draw(mView, mProj, mLightPos);
	}
	
	public void draw(float[] mView, float[] mProj, float[] mLightPos) {
		float[] mModel = new float[16];
		float[] mModelView = new float[16];
		float[] mModelViewProj = new float[16];
		
		Matrix.setIdentityM(mModel, 0);
		mModel = applyTransforms(mModel);
		
		Matrix.multiplyMM(mModelView, 0, mView, 0, mModel, 0);
		Matrix.multiplyMM(mModelViewProj, 0, mProj, 0, mModelView, 0);	
		
		GLES20.glUseProgram(getProgramID());

		vertexBuffer.position(0);
		mPositionHandle = GLES20.glGetAttribLocation(getProgramID(), "a_Position");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		
		setmAmbHandle(GLES20.glGetUniformLocation(getProgramID(), "u_Ambient"));
		GLES20.glUniform3fv(getmAmbHandle(), 1, getAmb(), 0);
		
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
	
	protected float[] getCoords() {
		return coords;
	}
	protected void setCoords(float coords[]) {
		this.coords = coords;
	}
	protected float[] getNormals() {
		return normals;
	}
	protected void setNormals(float normals[]) {
		this.normals = normals;
	}
	protected float[] getAmb() {
		return amb;
	}
	public void setAmb(float amb[]) {
		this.amb = amb;
	}
	public int getProgramID() {
		return programID;
	}
	public void setProgramID(int programID) {
		this.programID = programID;
	}
	public int getmNormalHandle() {
		return mNormalHandle;
	}
	public void setmNormalHandle(int mNormalHandle) {
		this.mNormalHandle = mNormalHandle;
	}
	public FloatBuffer getNormalBuffer() {
		return normalBuffer;
	}
	public void setNormalBuffer(FloatBuffer normalBuffer) {
		this.normalBuffer = normalBuffer;
	}
	public int getmAmbHandle() {
		return mAmbHandle;
	}
	public void setmAmbHandle(int mAmbHandle) {
		this.mAmbHandle = mAmbHandle;
	}
	public int getmLightPosHandle() {
		return mLightPosHandle;
	}
	public void setmLightPosHandle(int mLightPosHandle) {
		this.mLightPosHandle = mLightPosHandle;
	}

	
	
}
