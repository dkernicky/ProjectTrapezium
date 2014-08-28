package com.kernicky.gl_prototype;

import java.util.ArrayList;

import android.opengl.Matrix;

import com.kernicky.gl_prototype.math.MatrixOp;
import com.kernicky.gl_prototype.math.Quaternion;
import com.kernicky.gl_prototype.models.Cube;
import com.kernicky.gl_prototype.models.EmissiveModel;
import com.kernicky.gl_prototype.models.Lamp;
import com.kernicky.gl_prototype.models.ReflectiveModel;
import com.kernicky.gl_prototype.models.Transformation;

public class SampleScene extends Scene {
	private double lastUpdate = 0.0f;
	private float frameRate = 30.0f;

	// initial view and proj matrices
	private float[] mView = new float[16];
	private float[] mProj = new float[16];

	//public static float[] mViewerPos = { 0.0f, 0.0f, 3.0f, 1.0f };
	private float[] mCenterPos = { 0.0f, 0.0f, 0.0f, 1.0f };
	public static float[] mUpV = { 0.0f, 1.0f, 0.0f, 1.0f };
	//public static float[] shipPosition = { 0.0f, 0.0f, 2.0f, 0.0f };
	public static float[] shipAngle = {1, 0, 0, 0,
									   0, 1, 0, 0,
									   0, 0, 1, 0,
									   0, 0, 0, 1};
	public static float[] staticAngle = {1, 0, 0, 0,
		   0, 1, 0, 0,
		   0, 0, 1, 0,
		   0, 0, 0, 1};
	
	public static Quaternion viewQ = new Quaternion(0.0f, 0.0f, 5.0f, 0.0f);
	//public static Quaternion shipQ = new Quaternion(0.0f, 0.0f, 7.0f, 0.0f);
	public static Quaternion shipDirQ = new Quaternion(0.0f, 1.0f, 0.0f, 0.0f);
	public static final float viewDist = 10.0f;
	public static final float shipDist = 7.0f;
	
	

	private ArrayList<ReflectiveModel> modelList = new ArrayList<ReflectiveModel>();

	private ArrayList<Lamp> lightList = new ArrayList<Lamp>();
	private ArrayList<Float> lightPosList = new ArrayList<Float>();
	private double lastTime = 0.0f;
	private int updateCount = 0;

	public SampleScene() {

		Cube c = new Cube(0, 0, 0);
		c.transList.set(0, new Transformation(2));
		modelList.add(c);
		// The ship "turret" 
		// TODO: Place this within ship class, with code to ensure lighting updates in scene
		Lamp lo = new Lamp(3.0f, 0.0f, 0.0f);
		lo.transList.set(0, new Transformation(MatrixOp.identity()));
		lo.transList.set(1, new Transformation(MatrixOp.identity()));
		lo.transList.set(2, new Transformation(MatrixOp.identity()));
		lo.transList.set(3, new Transformation(0, 0, 3));	
		lo.transList.set(4, new Transformation(0.05f));
		lightList.add(lo);
		

	}

	

	
	
	public void draw() {
		
			
		
		render();
	}
	
	public void checkCollisions() {
		
	}
	
	public void render() {
		
		Matrix.setLookAtM(mView, 0, viewQ.x, viewQ.y,
				viewQ.z, mCenterPos[0], mCenterPos[1], mCenterPos[2],
				0, 1, 0);

		lightPosList.clear();
		lightList.get(0).draw(mView, mProj);
		
		for (int m = 0; m < lightList.get(0).getMELightPos().length - 1; m++) {
			lightPosList.add(lightList.get(0).getMELightPos()[m]);
		}

		float[] lightPos = new float[lightPosList.size()];
		for (int n = 0; n < lightPos.length; n++) {
			lightPos[n] = lightPosList.get(n);
		}
		
		for (ReflectiveModel m : modelList) {
			m.draw(mView, mProj, lightPos);
		}
		
	
		
//		for(EmissiveModel m: modelList) {
//			m.draw(mView, mProj, lightPos);
//		}
	
	}

	public void setMProj(float[] mProj) {
		this.mProj = mProj;
	}

	public float[] getMProj() {
		return this.mProj;
	}
}
