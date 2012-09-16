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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SortingTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public SortingTest(Class<MainActivity> activityClass) {
		super(activityClass);
	}

	public SortingTest() {
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
		dbHelper = new DBHelper(context);
		dbHelper.open();
		dbHelper.truncateHistory();
	}

	public void testSorting() {
		assertNotNull(activity);
		mInstrumentation.waitForIdleSync();

		// settings to default
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();// add three alarms

		// check unsorted
		MainActTest.addAlarm(activity, 2, 55, 0);
		MainActTest.addAlarm(activity, 1, 55, 0);
		TimePickDialog tPicker = activity.getTimePickDialog();
		MainActTest.addAlarm(activity, 3, 0, 0);
		solo.sleep(1000);
		assertEquals(2, activity.getAlarmList().get(0).getHour());
		// change to runned first
		editor.putString(context.getString(R.string.pref_sortlist_key),
				MainActivity.SMALLFIRST);
		editor.commit();
		String sortType = prefs.getString(
				context.getString(R.string.pref_sortlist_key),
				MainActivity.UNSORTED);
		assertEquals(MainActivity.SMALLFIRST, sortType);

		mInstrumentation.waitForIdleSync();
		assertEquals(1, activity.getAlarmList().get(0).getHour());
		MainActTest.addAlarm(activity, 0, 30, 0);
		mInstrumentation.waitForIdleSync();
		assertEquals(0, activity.getAlarmList().get(0).getHour());

		final AlarmClock alarm = activity.getAlarmList().get(0);
		mInstrumentation.waitForIdleSync();

		dbHelper.close();
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
