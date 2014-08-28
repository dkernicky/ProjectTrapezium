//package com.kernicky.gl_prototype;
//
//import java.util.ArrayList;
//
//import android.opengl.Matrix;
//
//import com.kernicky.gl_prototype.models.BlackIco;
//import com.kernicky.gl_prototype.models.GoldenShip;
//import com.kernicky.gl_prototype.models.Lamp;
//import com.kernicky.gl_prototype.models.Model;
//import com.kernicky.gl_prototype.models.PhongCube;
//import com.kernicky.gl_prototype.models.Transformation;
//
//public class TestScene extends Scene {
//	private double lastUpdate = 0.0f;
//	private float frameRate = 30.0f;
//	
//	// initial view and proj matrices
//	private float[] mModel = new float[16];
//	private float[] mView = new float[16];
//	private float[] mModelView = new float[16];
//	private float[] mProj = new float[16];
//	private float[] mModelViewProj = new float[16];
//	private float[] mLightModel = new float[16];
//	
//	private float[] mELightPos = new float[9];
//	private float[] mELightPos_1 = new float[4];
//	private float[] mELightPos_2 = new float[4];
//	private float[] mELightPos_3 = new float[4];
//	private final float[] mLightPos_1 = {0.5f, 0.0f, 0.0f, 1.0f};
//	private final float[] mLightPos_2 = {0.0f, 0.0f, 0.0f, 1.0f};
//	private final float[] mLightPos_3 = {0.0f, 0.0f, 0.0f, 1.0f};
//	private float rot = 3.0f;
//
//	
//	
//
//	private float[] mViewerPos = { 0.0f, 0.0f, 2.0f };
//	private float[] mCenterPos = { 0.0f, 0.0f, 0.0f };
//	private float[] mUpV = { 0.0f, 1.0f, 0.0f };
//	
//	private float value = (float)(.5*Math.sqrt(3)/2.0);
//	
//	private ArrayList<Model> modelList = new ArrayList<Model>();
//	private ArrayList<Lamp> lightList = new ArrayList<Lamp>();
//	private ArrayList<Float> lightPosList = new ArrayList<Float>();
//
//	public TestScene() {
//		PhongCube cube = new PhongCube();
//		GoldenShip ship = new GoldenShip();
//
////		ship.addTransform(new Transformation(1.5f, 0.0f, 0.0f, 0, 30));
////		ship.addTransform(new Transformation(-3.0f, 0.0f, 0.0f, 30, 90));
////		ship.addTransform(new Transformation(1.5f, 0.0f, 0.0f, 90, 120));
//		
////		ship.addTransform(new Transformation(20.0f, 0, 1, 0, 0, 30));
////		ship.addTransform(new Transformation(-45.0f, 0, 1, 0, 30, 90));
////		ship.addTransform(new Transformation(20.0f, 0, 1, 0, 90, 120));
//		
////		ship.addTransform(new Transformation(-40.0f, 0, 0, 1, 15, 30));
////		ship.addTransform(new Transformation(40.0f, 0, 0, 1, 30, 45));
////		ship.addTransform(new Transformation(40.0f, 0, 0, 1, 75, 90));
////		ship.addTransform(new Transformation(-40.0f, 0, 0, 1, 90, 105));
//		
////		ship.addTransform(new Transformation(-180.0f, 0, 0, 1, 0, 30));
////		ship.addTransform(new Transformation(360.0f, 0, 0, 1, 30, 90));
////		ship.addTransform(new Transformation(-180.0f, 0, 0, 1, 90, 120));
//
//		
//
//		ship.addTransform(new Transformation(90.0f, 1, 0, 0));
//		ship.addTransform(new Transformation(0, 0, 0.25f));
//		
//		
//		
//		Lamp l1 = new Lamp(0.0f, 0.0f, 0.0f);
//		Lamp l2 = new Lamp(0.0f, 0.0f, 0.0f);
//		Lamp l3 = new Lamp(0.0f, 0.0f, 0.0f);
//		Lamp l4 = new Lamp(0.0f, 0.0f, 0.0f);
//		
//		BlackIco b = new BlackIco();
//		
//		//l1.addTransform(new Transformation(360.0f, 0, 1, 0, 0, 480));
//		
//		l1.addTransform(new Transformation(0.75f, 0.0f, 0.0f));
//		l1.addTransform(new Transformation(0.05f));
//		lightList.add(l1);
//		
//		l2.addTransform(new Transformation(-0.75f, 0.0f, 0.0f));
//		l2.addTransform(new Transformation(0.05f));
//		lightList.add(l2);
//
//		
//		l3.addTransform(new Transformation(0.0f, 0.0f, 0.75f));
//		l3.addTransform(new Transformation(0.05f));
//		lightList.add(l3);
//		
//		l4.addTransform(new Transformation(0.0f, 0.0f, -0.75f));
//		l4.addTransform(new Transformation(0.05f));
//		lightList.add(l4);
//		
//		//modelList.add(cube);
//
//
//
//
//		//modelList.add(ship);
//		//modelList.add(b);
//		//modelList.add(l1);
//	}
//	
//	public void draw() {
//		double currentTime = System.currentTimeMillis();
//		//System.out.println("************* " + (currentTime - lastUpdate));
//		//System.out.println(1000.0/frameRate);
//		if(true || currentTime - lastUpdate >= 1000.0f/frameRate) {
//		
//			Matrix.setLookAtM(mView, 0, mViewerPos[0], mViewerPos[1],
//					mViewerPos[2], mCenterPos[0], mCenterPos[1], mCenterPos[2],
//					mUpV[0], mUpV[1], mUpV[2]);
//			
////			Matrix.setIdentityM(mModel, 0);
////			Matrix.setIdentityM(mModelView, 0);
////			//Matrix.setIdentityM(mLightModel, 0);
////			Matrix.rotateM(mModel, 0, rot, 0, 0, 1);
////			//Matrix.translateM(mModel, 0, -0.5f, 0.0f, 0.0f);
////			//Matrix.translateM(mModel, 0, 0.0f, 2*0.5f, 0.0f);		
////			Matrix.multiplyMM(mModelView, 0, mView, 0, mModel, 0);
////			Matrix.multiplyMM(mModelViewProj, 0, mProj, 0, mModelView, 0);
////			Matrix.multiplyMV(mELightPos_1, 0, mModelView, 0, mLightPos_1, 0);
////
////			rot += 3;	
////
////			mELightPos[0] = mELightPos_1[0];
////			mELightPos[1] = mELightPos_1[1];
////			mELightPos[2] = mELightPos_1[2];
////			mELightPos[3] = mELightPos_2[0];
////			mELightPos[4] = mELightPos_2[1];
////			mELightPos[5] = mELightPos_2[2];
////			mELightPos[6] = mELightPos_3[0];
////			mELightPos[7] = mELightPos_3[1];
////			mELightPos[8] = mELightPos_3[2];
//
//
//			//for(Lamp l: lightList) {
//			lightPosList.clear();
//
//			for(int n = 0; n < lightList.size(); n ++) {
//				lightList.get(n).draw(mView, mProj);
//				
//
//				//for(float f: lightList.get(n).getMELightPos()) {
//				for(int m = 0; m < lightList.get(n).getMELightPos().length-1; m ++)	{
//					lightPosList.add(lightList.get(n).getMELightPos()[m]);
//////					//System.out.print(f + " ");
//				}
//				//System.out.println();
//			}
//			System.out.println(lightPosList.size() + "dasfskudghfQ");
//
//			float[] lightPos = new float[lightPosList.size()];
//			for(int n = 0; n < lightPos.length; n ++) {
//				lightPos[n] = lightPosList.get(n);
////				//System.out.print(lightPos[n] + " ");
//			}
////			//System.out.println();
//			for(Model m: modelList) {
//				m.draw(mView, mProj, lightPos);
//				//m.draw(mView, mProj, mELightPos);
//			}
//			
////			float[] f = lightList.get(0).getMELightPos();
////			System.out.println("************");
////			System.out.println(f[0] + " vs " + mELightPos[0]);
////			System.out.println(f[1] + " vs " + mELightPos[1]);
////			System.out.println(f[2] + " vs " + mELightPos[2]);
////			System.out.println("************");
//			
//			lastUpdate = currentTime;
//		}
//	}	
//	
//	public void setMProj(float[] mProj) {
//		this.mProj = mProj;
//	}
//	public float[] getMProj() {
//		return this.mProj;
//	}	
//}
