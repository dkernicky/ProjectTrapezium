package com.kernicky.gl_prototype.math;

public class Quaternion {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	public Quaternion(float[] v) {
		this.x = v[0];
		this.y = v[1];
		this.z = v[2];
		this.w = v[3];
	}
	public void normalize() {
		double mag = Math.sqrt(x*x+y*y+z*z+w*w);
		x /= mag;
		y /= mag;
		z /= mag;
		w /= mag;
	}
	public Quaternion multiply(Quaternion r) {
		float x = this.y*r.z - this.z*r.y + r.w*this.x + this.w*r.x;
		float y = this.z*r.x - this.x*r.z + r.w*this.y + this.w*r.y;
		float z = this.x*r.y - this.y*r.x + r.w*this.z + this.w*r.z;
		float w = this.w*r.w - this.x*r.x - this.y*r.y - this.z*r.z;
		return new Quaternion(x, y, z, w);
	}
	public Quaternion multiply(Quaternion q, Quaternion r) {
		float x = q.y*r.z - q.z*r.y + r.w*q.x + q.w*r.x;
		float y = q.z*r.x - q.x*r.z + r.w*q.y + q.w*r.y;
		float z = q.x*r.y - q.y*r.x + r.w*q.z + q.w*r.z;
		float w = q.w*r.w - q.x*r.x - q.y*r.y - q.z*r.z;
		return new Quaternion(x, y, z, w);
	}
	public void multiply(float c) {
		this.x *= c;
		this.y *= c;
		this.z *= c;
		this.w *= c;
	}
	
	public Quaternion add(Quaternion r) {
		return new Quaternion(this.x+r.x, this.y+r.y, this.z+r.z, this.w+r.w);
	}
	
	public Quaternion conjugate() {
		return new Quaternion(-1.0f*this.x, -1.0f*this.y, -1.0f*this.z, this.w);
	}
	
	public double norm() {
		return Math.sqrt(x*x+y*y+z*z+w*w);
	}
	
	public Quaternion identity() {
		return new Quaternion(0, 0, 0, 1);
	}
	
	public Quaternion inverse() {
		float c = (float) this.norm();
		c = 1/(c*c);
		Quaternion q = conjugate();
		return new Quaternion(c*q.x, c*q.y, c*q.z, c*q.w);
	}
	

	
	public float[] toMatrix() {
		float s = (float) (2/norm());
		float[] m = { 1-2*(y*y+z*z), 2*(x*y-w*z), 2*(x*z+w*y), 0,
					  2*(x*y+w*z), 1-2*(x*x+z*z), 2*(y*z-w*x), 0,
					  2*(x*z-w*y), 2*(y*z+w*x), 1-2*(x*x+y*y), 0,
					  0, 0, 0, 1
		};
		return m;
	}
	
	// a column-major order matrix
	public float[] toMatrixCM() {
		float s = (float) (2/norm());
		float[] m = { 1-2*(y*y+z*z), 2*(x*y+w*z)  , 2*(x*z-w*y)  , 0 ,
					  2*(x*y-w*z)  , 1-2*(x*x+z*z), 2*(y*z+w*x)  , 0 ,
				      2*(x*z+w*y)  , 2*(y*z-w*x)  , 1-2*(x*x+y*y), 0 ,
					  0			   , 0			  , 0			 , 1
		};
		return m;
	}
	public static Quaternion rotate(float theta, float[] p, float[] u) {
		float c1 = (float) Math.sin(theta/2.0);
		float c2 = (float) Math.cos(theta/2.0);
		Quaternion q_quat = new Quaternion(c1*u[0], c1*u[1], c1*u[2], c2);
		Quaternion q_quat_in = q_quat.inverse();
		Quaternion p_quat = new Quaternion(p[0], p[1], p[2], p[3]);
		return q_quat.multiply(p_quat).multiply(q_quat_in);
	}
	public void rotate(float theta, float[] u) {
		float c1 = (float) Math.sin(theta/2.0);
		float c2 = (float) Math.cos(theta/2.0);
		Quaternion q_quat = new Quaternion(c1*u[0], c1*u[1], c1*u[2], c2);
		Quaternion q_quat_in = q_quat.inverse();
		//Quaternion r = q_quat.multiply(this).multiply(q_quat.inverse());
		Quaternion r = q_quat.multiply(this).multiply(q_quat_in);

		this.x = r.x;
		this.y = r.y;
		this.z = r.z;
		this.w = r.w;
	}
	public static Quaternion slerp(Quaternion q, Quaternion r, float t) {
		double theta = Math.acos(q.x*r.x + q.y*r.y + q.z*r.z + q.w*r.w);
		float c1 = (float) (Math.sin(theta*(1-t))/Math.sin(theta));
		float c2 = (float) (Math.sin(theta*t)/Math.sin(theta));
		q.multiply(c1);
		r.multiply(c2);
		return q.add(r);
	}
	
	// column major order
	public static float[] rotateTo(float[] s, float[] t) {
		float[] v = Vector.cross(s, t);
		float e = Vector.dot(s, t);
		float h = 1/(1+e);
		return new float[]{ e+h*v[0]*v[0], h*v[0]*v[1] + v[2], h*v[0]*v[2]-v[1], 0,
							h*v[0]*v[1]-v[2], e + h*v[1]*v[1], h*v[1]*v[2]+v[0], 0,
							h*v[0]*v[2]+v[1], h*v[1]*v[2]-v[0], e + h*v[2]*v[2], 0,
							0, 0, 0, 1};
	}
	public static float[] rotateTo(Quaternion q, Quaternion r) {
		float[] s = q.toFloat();
		float[] t = r.toFloat();
		float[] v = Vector.cross(s, t);
		float e = Vector.dot(s, t);
		float h = 1/(1+e);
		return new float[]{ e+h*v[0]*v[0], h*v[0]*v[1] + v[2], h*v[0]*v[2]-v[1], 0,
							h*v[0]*v[1]-v[2], e + h*v[1]*v[1], h*v[1]*v[2]+v[0], 0,
							h*v[0]*v[2]+v[1], h*v[1]*v[2]-v[0], e + h*v[2]*v[2], 0,
							0, 0, 0, 1};
	}
	
	public float[] toFloat() {
		return new float[]{x, y, z, w};
	}
	
	
	
	public void print() {
		System.out.println(x + " " + y + " " + z + " " + w);
	}
	
	
}
