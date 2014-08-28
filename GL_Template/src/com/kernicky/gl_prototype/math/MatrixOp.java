package com.kernicky.gl_prototype.math;

public class MatrixOp {
	public static float[] addMM(float[] lhs, float[] rhs) {
		float[] m = new float[16];
		for(int n = 0; n < 16; n ++) {
			m[n] = lhs[n]+rhs[n];
		}
		return m;
	}
	public static float[] identity() {
		return new float[]{ 1, 0, 0, 0,
						    0, 1, 0, 0, 
						    0, 0, 1, 0,
						    0, 0, 0, 1
						  };
	}
	
	public static float[] multiplyMM(float[] lhs, float[] rhs) {
//		float[] m = {
//				lhs[0] * rhs[0]  + lhs[4] * rhs[1]  + lhs[8]  * rhs[2]  + lhs[12] * rhs[3],
//				lhs[0] * rhs[4]  + lhs[4] * rhs[5]  + lhs[8]  * rhs[6]  + lhs[12] * rhs[7],
//				lhs[0] * rhs[8]  + lhs[4] * rhs[9]  + lhs[8]  * rhs[10] + lhs[12] * rhs[11],
//				lhs[0] * rhs[12] + lhs[4] * rhs[13] + lhs[8]  * rhs[14] + lhs[12] * rhs[15],
//				lhs[1] * rhs[0]  + lhs[5] * rhs[1]  + lhs[9]  * rhs[2]  + lhs[13] * rhs[3],
//				lhs[1] * rhs[4]  + lhs[5] * rhs[5]  + lhs[9]  * rhs[6]  + lhs[13] * rhs[7],
//				lhs[1] * rhs[8]  + lhs[5] * rhs[9]  + lhs[9]  * rhs[10] + lhs[13] * rhs[11],
//				lhs[1] * rhs[12] + lhs[5] * rhs[13] + lhs[9]  * rhs[14] + lhs[13] * rhs[15],
//				lhs[2] * rhs[0]  + lhs[6] * rhs[1]  + lhs[10] * rhs[2]  + lhs[14] * rhs[3],
//				lhs[2] * rhs[4]  + lhs[6] * rhs[5]  + lhs[10] * rhs[6]  + lhs[14] * rhs[7],
//				lhs[2] * rhs[8]  + lhs[6] * rhs[9]  + lhs[10] * rhs[10] + lhs[14] * rhs[11],
//				lhs[2] * rhs[12] + lhs[6] * rhs[13] + lhs[10] * rhs[14] + lhs[14] * rhs[15],
//				lhs[3] * rhs[0]  + lhs[7] * rhs[1]  + lhs[11] * rhs[2]  + lhs[15] * rhs[3],
//				lhs[3] * rhs[4]  + lhs[7] * rhs[5]  + lhs[11] * rhs[6]  + lhs[15] * rhs[7],
//				lhs[3] * rhs[8]  + lhs[7] * rhs[9]  + lhs[11] * rhs[10] + lhs[15] * rhs[11],
//				lhs[3] * rhs[12] + lhs[7] * rhs[13] + lhs[11] * rhs[14] + lhs[15] * rhs[15] };
		float[] m = {
				lhs[0] * rhs[0]  + lhs[4] * rhs[1]  + lhs[8]  * rhs[2]  + lhs[12] * rhs[3],
				lhs[1] * rhs[0]  + lhs[5] * rhs[1]  + lhs[9]  * rhs[2]  + lhs[13] * rhs[3],
				lhs[2] * rhs[0]  + lhs[6] * rhs[1]  + lhs[10] * rhs[2]  + lhs[14] * rhs[3],
				lhs[3] * rhs[0]  + lhs[7] * rhs[1]  + lhs[11] * rhs[2]  + lhs[15] * rhs[3],
				lhs[0] * rhs[4]  + lhs[4] * rhs[5]  + lhs[8]  * rhs[6]  + lhs[12] * rhs[7],
				lhs[1] * rhs[4]  + lhs[5] * rhs[5]  + lhs[9]  * rhs[6]  + lhs[13] * rhs[7],
				lhs[2] * rhs[4]  + lhs[6] * rhs[5]  + lhs[10] * rhs[6]  + lhs[14] * rhs[7],
				lhs[3] * rhs[4]  + lhs[7] * rhs[5]  + lhs[11] * rhs[6]  + lhs[15] * rhs[7],
				lhs[0] * rhs[8]  + lhs[4] * rhs[9]  + lhs[8]  * rhs[10] + lhs[12] * rhs[11],
				lhs[1] * rhs[8]  + lhs[5] * rhs[9]  + lhs[9]  * rhs[10] + lhs[13] * rhs[11],
				lhs[2] * rhs[8]  + lhs[6] * rhs[9]  + lhs[10] * rhs[10] + lhs[14] * rhs[11],
				lhs[3] * rhs[8]  + lhs[7] * rhs[9]  + lhs[11] * rhs[10] + lhs[15] * rhs[11],
				lhs[0] * rhs[12] + lhs[4] * rhs[13] + lhs[8]  * rhs[14] + lhs[12] * rhs[15],
				lhs[1] * rhs[12] + lhs[5] * rhs[13] + lhs[9]  * rhs[14] + lhs[13] * rhs[15],
				lhs[2] * rhs[12] + lhs[6] * rhs[13] + lhs[10] * rhs[14] + lhs[14] * rhs[15],
				lhs[3] * rhs[12] + lhs[7] * rhs[13] + lhs[11] * rhs[14] + lhs[15] * rhs[15] };

		return m;
	}

	public static float[] multiplyMV(float[] lhs, float[] rhs) {
		float[] m = new float[4];
		for (int n = 0; n < 4; n++) {
			m[n] = lhs[n] * rhs[0] + lhs[n + 4] * rhs[1] + lhs[n + 8] * rhs[2]
					+ lhs[n + 12] * rhs[3];
		}
		return m;
	}

	public static void printV(float[] v) {
		//System.out.println(v[0] + " " + v[1] + " " + v[2] + " " + v[3]);
		for(float f: v) {
			System.out.print(f + " ");
		}
		System.out.println();
	}

	public static void printM(float[] m) {
		System.out.println("****************");
		System.out.println(m[0] + " " + m[4] + " " + m[8] + " " + m[12]);
		System.out.println(m[1] + " " + m[5] + " " + m[9] + " " + m[13]);
		System.out.println(m[2] + " " + m[6] + " " + m[10] + " " + m[14]);
		System.out.println(m[3] + " " + m[7] + " " + m[11] + " " + m[15]);
		System.out.println("****************");

	}
}
