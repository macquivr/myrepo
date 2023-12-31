/*
 * Copyright (c) 2011 CA.
 * 
 * All rights reserved. 
 *  
 * This software, associated documentation, and all information contained
 * therein is confidential and proprietary and shall not be duplicated,
 * used, disclosed or disseminated in any way except as authorized by the
 * applicable license agreement, without the express written permission of CA.
 */
package com.example.demo.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class FileUtils {
	public static final String LINE_SEPARATOR = System.lineSeparator();
	public static String normalizePath(String path) {
		if (path == null)
			return null;
		else {
			int len = path.length();
			if (len > 0 && path.charAt(len - 1) == File.separatorChar)
				return path.substring(0, len - 1);
			else
				return path;
		}
	}

	public static String appendToPath(String path1, String path2) {
		return normalizePath(path1) + File.separator + path2;
	}

	public static String readFile(File file) throws IOException {

		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(LINE_SEPARATOR);
			}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// ignore
				}
		}
		return buffer.toString();
	}

    public static String readFileTail(File file, int maxLines) throws IOException {             
        List<String> lines = new ArrayList<>(maxLines);
        int linesCharCount = 0;
        ReversedLinesFileReader reader = null;
        try {
            reader = new ReversedLinesFileReader(file);
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (++lineCount > maxLines)
                    break;
                
                lines.add(line);
                linesCharCount += line.length();
            }
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
					// ignore
                }
        }
        
        int lineCount = lines.size();
        if (lineCount > 0) {
            int lineSeparatorsCharCount = (lineCount-1) * LINE_SEPARATOR.length();
                        
            StringBuilder buffer = new StringBuilder(linesCharCount + lineSeparatorsCharCount);
            for (int i=lineCount-1; i>=0; i--) {
                buffer.append(lines.get(i));            
                if (i > 0) {
                    buffer.append(LINE_SEPARATOR); //append line separator to all but last line
                }
            }                       
            return buffer.toString();
        }       
        return "";
    }

	public static void writeFile(String text, File file) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
		}
	}

	public static void writeFile(List<String> text, File file) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			for (String str : text) 
				writer.write(str + "\n");
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
		}
	}
	
	public static List<String> getFilenames(String path, boolean trimExtension) {
		List<String> filenames = new ArrayList<>();
		if (path != null) {
			File folder = new File(path);
			if (folder.exists()) {
				File[] dirs = folder.listFiles();
				if (dirs != null) {
					for (File file : dirs) {
						if (file.isFile()) {
							filenames.add(trimExtension ? FilenameUtils.getBaseName(file.getName()) : file.getName());
						}
					}
				}
			}
		}
		return filenames;
	}

	public static List<String> getFilenames(String path) {
		return getFilenames(path, false);
	}

	public static String createFilename(String name, String extension) {
		name = name.replaceAll(" ", "");
		name = name.replaceAll("\t", "");
		if (extension != null)
			name += "." + extension;
		return name;
	}

	public static List<String> getFilenames(String path, String extension) {
		List<String> filenames = new ArrayList<>();
		if (path != null) {
			File imagesFolder = new File(path);
			if (imagesFolder.exists()) {
				File[] dirs = imagesFolder.listFiles();
				if (dirs != null) {
					for (File file : dirs) {
						if (file.isFile()) {
							if (extension == null || FileUtils.endsWithIgnoreCase(file.getName(), "." + extension))
								filenames.add(file.getName());
						}
					}
				}
			}
		}
		return filenames;
	}

	public static boolean endsWithIgnoreCase(String text, String suffix) { // TODO refactor
		if (text != null && suffix != null) {
			int suffixLen = suffix.length();
			if (text.length() >= suffixLen) {
				int offset = text.length() - suffixLen;
				return suffix.compareToIgnoreCase(text.substring(offset, offset + suffixLen)) == 0;
			}
		}
		return false;
	}
	
	public static void createParentFolder(File file) {
	    File folder = file.getParentFile();
	    if (folder != null && !folder.exists())
	        folder.mkdirs();
	}
		
}
