package com.kernicky.gl_prototype.models;

import java.util.ArrayList;
import java.util.Iterator;

import com.kernicky.gl_prototype.SampleScene;
import com.kernicky.gl_prototype.math.MatrixOp;
import com.kernicky.gl_prototype.math.Quaternion;
import com.kernicky.gl_prototype.math.Vector;

public class Model {
	public int time = 0;
	public ArrayList<Transformation> transList = new ArrayList<Transformation>(10);
	public ArrayList<Model> subList = new ArrayList<Model>();
	
	protected int currentTick = 0;
	protected int maxTick = 1;
	
	public Quaternion position = new Quaternion(7, 0, 0, 0);
	protected float[] angle = MatrixOp.identity();
	protected float[] staticAngle = MatrixOp.identity();
	protected float[] initialAngle = MatrixOp.identity();
	
	public float radialEffect = 5.0f;
	protected float speed = .04f;
	
	public Model() {
		for(int n = 0; n < 10; n ++) {
			transList.add(new Transformation(MatrixOp.identity()));
		}
	}
	
	
	// rotate the model about an axis by a magnitude (speed)
	public void applyRot(float speed, float[] axis) {
		
		// normalize in order to perform computations on unit quaternions
		position.normalize();
		Quaternion prevPos = new Quaternion(position.toFloat());
		Quaternion q = Quaternion.rotate(-1*speed, prevPos.toFloat(), axis);
		
		float[] transform = Quaternion.rotateTo(prevPos, q);
		position = new Quaternion(MatrixOp.multiplyMV(transform, position.toFloat()));		
		angle = MatrixOp.multiplyMM(transform, angle);
		position.normalize();
		
		float[] prevUp = {0, 1, 0, 0};
		float[] newUp = MatrixOp.multiplyMV(transform, prevUp);
		//newUp = Vector.normalize(newUp);
		
		float[] invAngle = Quaternion.rotateTo(newUp, prevUp);
		
		float[] inputVector = {position.x-prevPos.x, position.y-prevPos.y, position.z-prevPos.z, 0};
		inputVector = Vector.normalize(inputVector);
//		inputVector = MatrixOp.multiplyMV(invAngle, inputVector);
//		inputVector = Vector.normalize(inputVector);
//		MatrixOp.printV(inputVector);
//		float[] newTransform = Quaternion.rotateTo(prevUp, inputVector);
		
		float[] newTransform = Quaternion.rotateTo(newUp, inputVector);
		//newTransform = Quaternion.rotateTo(newUp, prevUp);

		staticAngle = MatrixOp.multiplyMM(invAngle, newTransform);
		
		//inputVector = MatrixOp.multiplyMV(newTransform, inputVector);
		//inputVector[2] = 0;
		//
		//MatrixOp.printV(inputVector);
		//staticAngle = Quaternion.rotateTo(prevUp, inputVector);	
		
		//staticAngle = newTransform;
		//staticAngle = Quaternion.rotateTo(new float[]{0, 1, 0, 0}, new float[]{1, 0, 0, 0});
//		staticAngle = transform;
		
		//MatrixOp.printM(staticAngle);

		position.multiply(7);
		transList.set(0, new Transformation(position.x, position.y, position.z));
		transList.set(1, new Transformation(angle));
		transList.set(2, new Transformation(staticAngle));
	}

	// ai seek behavior
	public void seek(Quaternion q) {
		float[] axis = Vector.cross(q.toFloat(), position.toFloat());
		axis = Vector.normalize(axis);
		applyRot(.035f, axis);
	}

	// ai wander behavior
	public void wander() {
		float[] f = new float[3];
		f[0] = (float) Math.random();
		f[1] = (float) Math.random();
		f[2] = (float) Math.random();
		
		f = Vector.normalize(f);
		applyRot(.04f, f);
	
	
	}

	// find maximum tick among transformations
	public void updateMaxTick() {
		Iterator<Transformation> i = transList.iterator();
		if(i.hasNext()) {
			maxTick = i.next().getEndTick();
		}
		while(i.hasNext()) {
			int t = i.next().getEndTick();
			maxTick = Math.max(t,  maxTick);
		}
	}

	// modify model matrix to represent transformations
	public float[] applyTransforms(float[] mModel) {
		for(int n = 0; n < transList.size(); n ++) {
			Transformation t = transList.get(n);
			mModel = t.apply(mModel, currentTick);
		}
		return mModel;
	}

	// add new transformation to list
	public void addTransform(Transformation t) {
		transList.add(t);
		this.updateMaxTick();
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float f) {
		speed = f;
	}

}
