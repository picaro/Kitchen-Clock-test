package com.op.kclock.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(MainActTest.class);
		suite.addTestSuite(SortingTest.class);
//		suite.addTestSuite(AlarmingTest.class);
//		suite.addTestSuite(HistoryAndLogsTest.class);
		//suite.addTestSuite(SaveSessionTest.class);
		//$JUnit-END$
		return suite;
	}

}
