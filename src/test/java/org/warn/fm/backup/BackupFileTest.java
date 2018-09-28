package org.warn.fm.backup;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class BackupFileTest extends TestCase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BackupFileTest.class );
	
	// if the other object is null, should return false
	public void testEqualsCase1() { 
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		assertFalse( f1.equals(null) );
	}
	
	// if other object is of different type, return false
	public void testEqualsCase2() { 
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		assertFalse( f1.equals( "/abc/def" ) );
	}
	
	// if both objects are non-null, but path is null in both, return true
	public void testEqualsCase3() { 
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile(null);
		assertTrue( f1.equals(f2) );
	}
	
	// if path is null in this object, but non-null in the other, return false
	public void testEqualsCase4() { 
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile( Paths.get("/abc/def") );
		assertFalse( f1.equals(f2) );
	}
	
	// if path is non-null in this object, but null in other object, return false 
	public void testEqualsCase5() { 
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile( null );
		assertFalse( f1.equals(f2) );
	}
	
	// different paths are set, should return false
	public void testEqualsCase6() { 
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile( Paths.get("/abc/ghi") );
		assertFalse( f1.equals(f2) );
	}
	
	// if paths are the same in both objects, return true
	public void testEqualsCase7() {
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile( Paths.get("/abc/def") );
		assertTrue( f1.equals(f2) );
	}
	
	// if path is null, should return a value without throwing a NPE
	public void testHashCodeCase1() {
		BackupFile f1 = new BackupFile(null);
		int hashCode = f1.hashCode();
		LOGGER.debug("testHashCodeCase1 - HashCode=" + hashCode);
	}
	
	// if paths are the same for two objects (equals method returns true), hashcode values should be equal
	public void testHashCodeCase2() {
		
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile( Paths.get("/abc/def") );
		assertTrue( f1.equals(f2) );
		
		int hashCode1 = f1.hashCode();
		int hashCode2 = f2.hashCode();
		assertTrue( hashCode1 == hashCode2 );
	}
	
	// if paths are the same for two objects (equals method returns true), hashcode values should be equal
	public void testHashCodeCase3() {
		
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile(null);
		assertTrue( f1.equals(f2) );
		
		int hashCode1 = f1.hashCode();
		int hashCode2 = f2.hashCode();
		assertTrue( hashCode1 == hashCode2 );
	}
	
	// if paths are different for two objects (equals method returns false), should ideally 
	// return distinct hashcode values
	public void testHashCodeCase4() {
		
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile( Paths.get("/abc/ghi") );
		assertFalse( f1.equals(f2) );
		
		int hashCode1 = f1.hashCode();
		int hashCode2 = f2.hashCode();
		assertFalse( hashCode1 == hashCode2 );
	}
	
	// if paths are different for two objects (equals method returns false), should ideally 
	// return distinct hashcode values
	public void testHashCodeCase5() {
		
		BackupFile f1 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f2 = new BackupFile(null);
		assertFalse( f1.equals(f2) );
		
		int hashCode1 = f1.hashCode();
		int hashCode2 = f2.hashCode();
		assertFalse( hashCode1 == hashCode2 );
	}
	
	// passing a null object to the compareTo method should throw NPE
	public void testtCompareToCase1() {
		BackupFile f1 = new BackupFile( Paths.get("/abc") );
		try {
			f1.compareTo(null);
		} catch( Exception e ) {
			assertTrue( e instanceof NullPointerException );
		}
	}
	
	// if path is null in first object, compareTo should return -1
	public void testtCompareToCase2() {
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile( Paths.get("/abc") );
		assertTrue( f1.compareTo(f2) == -1 );
		
		// sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y
		assertTrue( f1.compareTo(f2) == -f2.compareTo(f1) );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) );
	}
	
	// if path is null in first object, compareTo should return -1
	public void testtCompareToCase3() {
		BackupFile f1 = new BackupFile( Paths.get("/abc") );
		BackupFile f2 = new BackupFile(null);
		assertTrue( f1.compareTo(f2) == 1 );
		
		// sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y
		assertTrue( f1.compareTo(f2) == -f2.compareTo(f1) );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) );
	}
	
	// if first object is less than second object, compareTo should return a negative value
	public void testtCompareToCase4() {
		BackupFile f1 = new BackupFile( Paths.get("/abc") );
		BackupFile f2 = new BackupFile( Paths.get("/def") );
		assertTrue( f1.compareTo(f2) < 0 );
		
		// sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y
		assertTrue( f1.compareTo(f2) == -f2.compareTo(f1) );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) );
	}
	
	// if first object is greater than second object, compareTo should return a positive value
	public void testtCompareToCase5() {
		BackupFile f1 = new BackupFile( Paths.get("/def") );
		BackupFile f2 = new BackupFile( Paths.get("/abc") );
		assertTrue( f1.compareTo(f2) > 0 );
		
		// sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y
		assertTrue( f1.compareTo(f2) == -f2.compareTo(f1) );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) );
	}
	
	// if two objects are equal, compareTo should return 0
	public void testtCompareToCase6() {
		BackupFile f1 = new BackupFile( Paths.get("/abc") );
		BackupFile f2 = new BackupFile( Paths.get("/abc") );
		assertTrue( f1.compareTo(f2) == 0 );
		
		// x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z
		assertTrue( f1.compareTo(f2) == f2.compareTo(f1) );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) );
	}
	
	// if path is null in both objects, compareTo should return 0
	public void testtCompareToCase7() {
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile(null);
		assertTrue( f1.compareTo(f2) == 0 );
		
		// natural ordering should (ideally) by consistent with equals
		assertTrue( ( f1.compareTo(f2)==0 ) == ( f1.equals(f2) ) ); 
	}
	
	public void testtSortingCase1() {
		
		BackupFile f1 = new BackupFile(null);
		BackupFile f2 = new BackupFile( Paths.get("") );
		BackupFile f3 = new BackupFile( Paths.get("/abc") );
		BackupFile f4 = new BackupFile( Paths.get("/def") );
		BackupFile f5 = new BackupFile( Paths.get("/ghi") );
		BackupFile f6 = new BackupFile( Paths.get("/abc/def") );
		BackupFile f7 = new BackupFile( Paths.get("/abc/ghi") );
		
		List<BackupFile> files = new ArrayList<BackupFile>();
		files.add(f7);
		files.add(f6);
		files.add(f5);
		files.add(f4);
		files.add(f3);
		files.add(f2);
		files.add(f1);
		LOGGER.debug("Before sorting - " + files);
		
		Collections.sort(files);
		LOGGER.debug("After sorting - " + files);
		
		assertTrue( files.get(0) == f1 ); // null
		assertTrue( files.get(1) == f2 ); // ""
		assertTrue( files.get(2) == f3 ); // /abc
		assertTrue( files.get(3) == f6 ); // /abc/def
		assertTrue( files.get(4) == f7 ); // /abc/ghi
		assertTrue( files.get(5) == f4 ); // /def
		assertTrue( files.get(6) == f5 ); // /ghi
	}
}
