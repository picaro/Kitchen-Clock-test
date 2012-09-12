package com.op.kclock.test;

import com.op.kclock.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActTest(Class<MainActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public MainActTest() {
	    super("com.pyjioh", MainActivity.class);
	  }
	
	MainActivity activity;
	
	  @Override
	  protected void setUp() throws Exception {
	    // TODO Auto-generated method stub
	    super.setUp();
	    activity = getActivity();
	    //activity.openContextMenu(view)
//	    editKmPerHour = (EditText) activity
//	        .findViewById(com.pyjioh.R.id.editKmPerHour);
//	    editMeterPerSec = (EditText) activity
//	        .findViewById(com.pyjioh.R.id.editMetersPerSec);
	  }
	  
	  public void testControlsCreated() {
		    assertNotNull(activity);
		    //assertNotNull(editKmPerHour);
		    //assertNotNull(editMeterPerSec);
     }
}
