package it.prg.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

public class Utils {
	static Map<Path, List<String>> filesPerDirectoryMap = new HashMap<>();
	static List<String> fileNameList = new ArrayList<>();
	
	public static Map<Path, List<String>> readFilesFromPath(Path path) throws IOException {
		if(Files.notExists(path, LinkOption.NOFOLLOW_LINKS) || !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) 
			throw new RuntimeException("The specified path does not exist/is not a directory.");
		
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return super.preVisitDirectory(dir, attrs);
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String filename = file.toString();
				if("xml".equals(FilenameUtils.getExtension(filename))) {
					fileNameList.add(filename);
				}
				//System.out.println("Found file: " + filename);
				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				filesPerDirectoryMap.put(dir, fileNameList);
				return super.postVisitDirectory(dir, exc);
			}
			
		});
		
		return filesPerDirectoryMap;
		
	}
}
