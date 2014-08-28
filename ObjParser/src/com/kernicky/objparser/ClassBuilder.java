package com.kernicky.objparser;

public class ClassBuilder {

	
	public static String buildClass() {
		StringBuilder builder = new StringBuilder();
		builder.append("class" + "NewClass" + "extends ReflectiveModel { \n");
		builder.append("public static final float[] ModelCoords = { " + formatCoords() + "} \n");
		builder.append("public static final float[] ModelCoords = { " + formatNormals() + "} \n");
		builder.append("} \n");
		
		String str = builder.toString();
		System.out.println(str);
		return str;
	}
	
	private static String formatCoords() {
		return "";
	}
	
	private static String formatNormals() {
		return "";
	}
}
