package com.op.kclock.test;

import kankan.wheel.widget.WheelView;

import com.jayway.android.robotium.solo.Solo;
import com.op.kclock.MainActivity;
import com.op.kclock.R;
import com.op.kclock.dialogs.TimePickDialog;
import com.op.kclock.model.AlarmClock;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public MainActTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}

	public MainActTest() {
		super(MainActivity.class);
	}

	MainActivity activity;
	Instrumentation mInstrumentation;
	Solo solo;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		activity = getActivity();
		mInstrumentation = getInstrumentation();
		// activity.openContextMenu(view)
	}

	public void testAddFromDialog() {
		assertNotNull(activity);
		mInstrumentation.waitForIdleSync();
		assertTrue("Could not find the dialog!", solo.searchText("min"));

		TimePickDialog tPicker = activity.getTimePickDialog();
		assertNotNull(tPicker);

		final WheelView hours = (WheelView) tPicker.findViewById(R.id.hour);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				hours.setCurrentItem(2);
			}
		});
		final Button settimerbtn = (Button) tPicker
				.findViewById(R.id.settimerbtn);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				settimerbtn.performClick();
			}
		});

		mInstrumentation.waitForIdleSync();

		LinearLayout alarmsList = (LinearLayout) activity
				.findViewById(R.id.alarm_layout);
		assertEquals(1, alarmsList.getChildCount());
		solo.finishOpenedActivities();
	}
	
	public void testPauseAlarm() {
//		LinearLayout alarmsList = (LinearLayout) activity
//				.findViewById(R.id.alarm_layout);
//		Context context = activity.getApplicationContext();
//		final LinearLayout alarmL = (LinearLayout)alarmsList.getChildAt(0);
//		AlarmClock alarm = new AlarmClock(context);
//		alarm.setTime(100);
//		alarm.setElement(alarmL);
//		assertTrue(alarm.getWidget().getCurrentTextColor() == 
//				context.getResources().getColor(R.color.white));		
//		activity.runOnUiThread(new Runnable() {
//			public void run() {
//				alarmL.performClick();
//			}
//		});
////		mInstrumentation.waitForIdleSync();
//		
//		assertTrue(alarm.getWidget().getCurrentTextColor() == 
//				context.getResources().getColor(R.color.gray));		
//		
	}
	
	
	@Override
	public void tearDown() throws Exception{
		super.tearDown();
		if (solo != null) solo.finishOpenedActivities();
	}
}
