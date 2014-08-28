package com.kernicky.objparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

public class Parser {
	private Hashtable<Integer, Double[]> vertexTable = new Hashtable<Integer, Double[]>();
	private Hashtable<Integer, Double[]> normalTable = new Hashtable<Integer, Double[]>();
	private String currentGroup = "";
	private ArrayList<Double> currentCoords = new ArrayList<Double>();
	private ArrayList<Double> currentNormals = new ArrayList<Double>();
	private double[] currentDiff = new double[3];
	private double[] currentSpec = new double[3];
	private double[] currentAmb = new double[3];
	private int currentShininess;
	private double currentAlpha;
	private String filename;

	private boolean smoothing = false;

	public static void main(String[] args) throws IOException {
		Parser p = new Parser();
		p.parseObj("myObject.obj");
	}

	public void parseObj(String filename) throws IOException {
		this.filename = filename;
		int vertexCount = 1;
		int normalCount = 1;
		Scanner fileScanner;
		File file = new File(filename);
		System.out.println(file.getAbsolutePath().toString());
		fileScanner = new Scanner(new FileReader(file));
		while (fileScanner.hasNextLine()) {

			Scanner lineScanner = new Scanner(fileScanner.nextLine());
			if (lineScanner.hasNext()) {
				String title = lineScanner.next();
				//System.out.println(title);
				if (title.equals("v")) {

					// Move vertices into hashtable
					double x = lineScanner.nextDouble();
					double y = lineScanner.nextDouble();
					double z = lineScanner.nextDouble();
					System.out.println("Adding to vertexTable");
					vertexTable.put(vertexCount, new Double[] { x, y, z });
					vertexCount++;

				} else if (title.equals("vn")) {

					// Move normals into hashtable
					double x = lineScanner.nextDouble();
					double y = lineScanner.nextDouble();
					double z = lineScanner.nextDouble();
					System.out.println("Adding to normalTable");
					normalTable.put(normalCount, new Double[] { x, y, z });
					normalCount++;

				} else if (title.equals("g")) {
					if (!currentGroup.equals("")) {
						System.out.println("Creating class for group "
								+ currentGroup);
						buildClass();
						dumpGroup();
					}
					currentGroup = lineScanner.next();
				} else if (title.equals("f")) {
					String s = lineScanner.nextLine();
					Double[] coords = getCoords(s);
					Double[] normals = getNormals(s);

					Collections.addAll(currentCoords, coords);
					Collections.addAll(currentNormals, normals);
				} else if (title.equals("mtllib")) {
					getMaterials(lineScanner.next());
				}
			}
		}
		if (!currentGroup.equals("")) {
			System.out
					.println("Creating class for final group " + currentGroup);
			dumpGroup();
		}
		fileScanner.close();
	}

	private Double[] getCoords(String s) {
		// split given line into its vertices' indices
		String[] indices = s.split("/\\d*/\\d*");
		for(int n = 0; n < indices.length; n ++) {
			indices[n] = indices[n].replace(" ", "");
		}

		// retrieve coords from hashtable for given index
		Double[] v1 = vertexTable.get(Integer.parseInt(indices[0]));
		Double[] v2 = vertexTable.get(Integer.parseInt(indices[1]));
		Double[] v3 = vertexTable.get(Integer.parseInt(indices[2]));

		if (smoothing) {

		}

		// TODO: lolz at this
		Double[] coords = new Double[] { v1[0], v1[1], v1[2], v2[0], v2[1],
				v2[2], v3[0], v3[1], v3[2] };
		return coords;
	}

	private Double[] getNormals(String s) {
		System.out.println(s);

		// split given line into its vertices' indices
		String[] indices = s.split("\\d*/\\d*/");
		ArrayList<Integer> trimList = new ArrayList<Integer>();
		for(int n = 0; n < indices.length; n ++) {
			indices[n] = indices[n].replace(" ", "");
			if(indices[n].length() == 0) {
				System.out.println("gsdfjklag");
				trimList.add(n);
			}
		}
		for(int n = trimList.size()-1; n >= 0; n--) {
			System.out.println("fdsa " + trimList.get(n));
			indices = removeElement(indices, trimList.get(n));
		}
		
		System.out.println(indices.length);
		System.out.println(indices[0] + ";" + indices[1] + ";" + indices[2]);
		
		System.out.println(Integer.parseInt(indices[0]));
		
		// retrieve coords from hashtable for given index
		Double[] vn1 = normalTable.get(Integer.parseInt(indices[0]));
		Double[] vn2 = normalTable.get(Integer.parseInt(indices[1]));
		Double[] vn3 = normalTable.get(Integer.parseInt(indices[2]));
		
		System.out.println(vn1[0]);
		// TODO: lolz at this
		Double[] normals = new Double[] { vn1[0], vn1[1], vn1[2], vn2[0],
				vn2[1], vn2[2], vn3[0], vn3[1], vn3[2] };
		return normals;
	}

	private String[] removeElement(String[] array, int index) {
		String[] newString = new String[array.length-1];
		int count = 0;
		for(int n = 0; n < array.length; n ++) {
			if(n != index) {
				newString[count] = array[n];
				count ++;
			}
		}
		return newString;
	}
	
	private void dumpGroup() {

		// clear all data associated with this group
		this.currentGroup = "";
		this.currentAmb = new double[] {};
		this.currentDiff = new double[] {};
		this.currentSpec = new double[] {};
		this.currentCoords.clear();
		this.currentNormals.clear();
	}

	private void getMaterials(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner matFileScanner = new Scanner(new FileReader(file));
		while (matFileScanner.hasNextLine()) {

			Scanner matLineScanner = new Scanner(matFileScanner.nextLine());
			String title = matLineScanner.next();

			if (title.equals("Ns")) {
				currentShininess = (int) matLineScanner.nextDouble();
			} else if (title.equals("Ka")) {
				double r = matLineScanner.nextDouble();
				double g = matLineScanner.nextDouble();
				double b = matLineScanner.nextDouble();
				currentAmb = new double[] { r, g, b };
			} else if (title.equals("Kd")) {
				double r = matLineScanner.nextDouble();
				double g = matLineScanner.nextDouble();
				double b = matLineScanner.nextDouble();
				currentDiff = new double[] { r, g, b };
			} else if (title.equals("Ks")) {
				double r = matLineScanner.nextDouble();
				double g = matLineScanner.nextDouble();
				double b = matLineScanner.nextDouble();
				currentSpec = new double[] { r, g, b };
			}
		}
	}

	private String toUpperCase(String s) {
		char[] chars = s.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	public String buildClass() {
		StringBuilder builder = new StringBuilder();
		builder.append("class " + toUpperCase(filename)
				+ toUpperCase(currentGroup) + " extends ReflectiveModel { \n\n");
		builder.append("	public static final float[] ModelCoords = { "
				+ formatCoords() + "} \n");
		builder.append("	public static final float[] ModelNormals = { "
				+ formatNormals() + "} \n\n");
		builder.append("	@Override \n");
		builder.append("	public void setData() { \n");
		builder.append("		setAmb(new float[]{" + formatAmb() + "}); \n");
		builder.append("		setDiff(new float[]{" + formatDiff() + "}); \n");
		builder.append("		setSpec(new float[]{" + formatSpec() + "}); \n");
		builder.append("		setCoords(ModelCoords); \n");
		builder.append("		setNormals(ModelNormals); \n");
		builder.append("		setTransparency(1.0f); \n");
		builder.append("		setShine(" + currentShininess + "); \n");
		builder.append("	} \n\n");
		builder.append("} \n");

		String str = builder.toString();
		System.out.println(str);
		return str;
	}

	private String formatCoords() {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < currentCoords.size() - 1; n++) {
			builder.append(currentCoords.get(n) + ", ");
		}
		builder.append(currentCoords.get(currentCoords.size() - 1));
		return builder.toString();
	}

	private String formatNormals() {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < currentNormals.size() - 1; n++) {
			builder.append(currentNormals.get(n) + ", ");
		}
		builder.append(currentNormals.get(currentNormals.size() - 1));
		return builder.toString();
	}

	private String formatAmb() {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < currentAmb.length - 1; n++) {
			builder.append(currentAmb[n] + ", ");
		}
		builder.append(currentAmb[currentAmb.length - 1]);
		return builder.toString();
	}

	private String formatDiff() {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < currentDiff.length - 1; n++) {
			builder.append(currentDiff[n] + ", ");
		}
		builder.append(currentDiff[currentDiff.length - 1]);
		return builder.toString();
	}

	private String formatSpec() {
		StringBuilder builder = new StringBuilder();
		for (int n = 0; n < currentSpec.length - 1; n++) {
			builder.append(currentSpec[n] + ", ");
		}
		builder.append(currentSpec[currentSpec.length - 1]);
		return builder.toString();
	}

}
