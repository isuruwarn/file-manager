package org.warn.fm.file;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Finder extends SimpleFileVisitor<Path> {
	
	//private static final Logger LOGGER = LoggerFactory.getLogger( Finder.class );
	private final PathMatcher matcher;
	private int matchedCount = 0;
	private List<Path> matchedFiles;

	public Finder( String pattern ) {
		 matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		 matchedFiles = new ArrayList<Path>();
	}
	
	void find(Path file) {
		Path name = file.getFileName();
		if( name != null && matcher.matches(name) ) {
			matchedFiles.add(file);
			matchedCount++;
			//LOGGER.debug(file);
		}
	}

	@Override
	public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) {
		find(file);
		return CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) {
		find(dir);
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed( Path file, IOException exc ) {
		System.err.println(exc);
		return CONTINUE;
	}

	public int getMatchedCount() {
		return matchedCount;
	}

	public List<Path> getMatchedFiles() {
		return matchedFiles;
	}
	
 }