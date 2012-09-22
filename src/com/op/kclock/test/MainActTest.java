package com.op.kclock.test;

import kankan.wheel.widget.WheelView;
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jayway.android.robotium.solo.Solo;
import com.op.kclock.MainActivity;
import com.op.kclock.R;
import com.op.kclock.SettingsActivity;
import com.op.kclock.dialogs.TimePickDialog;
import com.op.kclock.model.AlarmClock;
import com.op.kclock.model.AlarmClock.TimerState;
import com.op.kclock.utils.DBHelper;

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

		addAlarm(activity, 2,0,0);
		mInstrumentation.waitForIdleSync();

		LinearLayout alarmsList = (LinearLayout) activity
				.findViewById(R.id.alarm_layout);
		assertEquals(1, alarmsList.getChildCount());


		//************* CLICK ON ALARM *******************
		//final LinearLayout alarmL = (LinearLayout) alarmsList.getChildAt(0);
		solo.sleep(400);
		final AlarmClock alarm = tapOnAlarm(activity, 0, AlarmClock.TimerState.PAUSED);	
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
		tapOnAlarm(activity, 0, AlarmClock.TimerState.RUNNING);	
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
		addAlarm(activity, 4,0,0);
		mInstrumentation.waitForIdleSync();
		solo.sleep(1000);
		mInstrumentation.waitForIdleSync();
		//deleteall - need two in history *now trbl
		solo.clickOnMenuItem("Delete all");		
		assertEquals(2, dbHelper.getHistoryList().size());
				
		solo.clickOnMenuItem("Settings");		
		solo.assertCurrentActivity("not settings activity", SettingsActivity.class);
		dbHelper.close();
		solo.finishOpenedActivities();
	}

	public static AlarmClock tapOnAlarm(MainActivity activity, int alarmIndex, final TimerState state) {
		final AlarmClock alarm = activity.getAlarmList().get(alarmIndex);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				alarm.setState(state);
			}
		});
		return alarm;
	}

	public static Button addAlarm(MainActivity activity,final int hour,final int min, final int sec) {
		TimePickDialog tPicker = activity.getTimePickDialog();
		assertNotNull(tPicker);

		final WheelView hours = (WheelView) tPicker.findViewById(R.id.hour);
		final WheelView mins = (WheelView) tPicker.findViewById(R.id.mins);
		final WheelView secs = (WheelView) tPicker.findViewById(R.id.secs);


		activity.runOnUiThread(new Runnable() {
			public void run() {
				hours.setCurrentItem(hour);
				mins.setCurrentItem(min);
				secs.setCurrentItem(sec);
			}
		});
		final Button settimerbtn = (Button) tPicker
				.findViewById(R.id.settimerbtn);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				settimerbtn.performClick();
			}
		});
		return settimerbtn;
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
