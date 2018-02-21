/**
 * 
 */
package com.ltse.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author SWATI KHARKAR
 *
 */
public class FileProcessor {

	/**
	 * 
	 */
	public FileProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	public static Set<String> readTextFile(String filename) {
		Set<String> data = new HashSet<String>();
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				if (!line.isEmpty())
					data.add(line);
			}
			System.out.println(data.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		return data;
	}
	
	public static void writeToFile(String filename, TreeSet<String> collection) {
		File file = new File(filename);
		FileOutputStream outputStream = null;
		boolean fileCreated = Boolean.TRUE;
		try {
			if (!file.exists())
				fileCreated = file.createNewFile();
			if (fileCreated) {
			outputStream = new FileOutputStream(filename);
				for (String str : collection) {
					byte[] strToBytes = str.getBytes();
					outputStream.write(strToBytes);
					outputStream.write(System.lineSeparator().getBytes());
				}
			} else {
				System.out.println("Error while creating file. Please check disk permissions");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ex) {
				
			}
		}
	}

}
