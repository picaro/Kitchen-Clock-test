package com.op.kclock.test;

import kankan.wheel.widget.WheelView;

import com.jayway.android.robotium.solo.Solo;
import com.op.kclock.MainActivity;
import com.op.kclock.R;
import com.op.kclock.dialogs.TimePickDialog;
import com.op.kclock.model.AlarmClock;
import com.op.kclock.utils.DBHelper;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
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
	Context context;
	DBHelper dbHelper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		activity = getActivity();
		context = activity.getApplicationContext();
		mInstrumentation = getInstrumentation();
		// activity.openContextMenu(view)
		//check last history;
		dbHelper = new DBHelper(context);
		dbHelper.open();
		dbHelper.truncateHistory();
	}

	public void testAddFromDialog() {
		assertNotNull(activity);
		mInstrumentation.waitForIdleSync();
		assertTrue("Could not find the dialog!", solo.searchText("m"));

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


		final AlarmClock alarm = activity.getAlarmList().get(0);
		//************* CLICK ON ALARM *******************
		//final LinearLayout alarmL = (LinearLayout) alarmsList.getChildAt(0);
		assertTrue(alarm.getWidget().getCurrentTextColor() == context
				.getResources().getColor(R.color.white));
		activity.runOnUiThread(new Runnable() {
			public void run() {
				alarm.setState(AlarmClock.TimerState.PAUSED);
			}
		});	
		mInstrumentation.waitForIdleSync();
		assertTrue(alarm.getWidget().getCurrentTextColor() == context
				.getResources().getColor(R.color.gray));
		//****************************************************
		
		
		//****************  Context menu edit *****************
//		solo.clickLongOnView(alarm.getElement());
//		solo.clickOnText("Set time");
//		mInstrumentation.waitForIdleSync();
//		solo.sleep(200);
//		activity.runOnUiThread(new Runnable() {
//			public void run() {
//				hours.setCurrentItem(12);
//			}
//		});
//		mInstrumentation.waitForIdleSync();		
//		solo.sleep(700);
//		solo.clickOnText("Set");
//		assertEquals(12, activity.getAlarmList().get(0).getHour());
		//*******************************************
		
			
		//*************** second Click - run 
		activity.runOnUiThread(new Runnable() {
			public void run() {
				alarm.setState(AlarmClock.TimerState.RUNNING);
			}
		});	
		mInstrumentation.waitForIdleSync();
		assertTrue(alarm.getWidget().getCurrentTextColor() == context
				.getResources().getColor(R.color.white));
		//********************

		alarm.setName("test");
		assertEquals(0, dbHelper.getHistoryList().size());
		//****************  Context menu  delete *****************
		solo.clickLongOnView(alarm.getElement());
		solo.clickOnText("Delete");
		solo.sleep(500);
		mInstrumentation.waitForIdleSync();
		assertEquals(0, activity.getAlarmList().size());
		//*******************************************
		
		assertEquals(1, dbHelper.getHistoryList().size());
		assertEquals(2, dbHelper.getHistoryList().get(0).getHour());

		//add new alarm via ActionBar
		//deleteall - need two in history
		
		solo.finishOpenedActivities();
	}



	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		if (dbHelper != null) 
			dbHelper.close();
		if (solo != null)
			solo.finishOpenedActivities();
	}
}
