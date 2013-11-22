package pro.burgerz.wsm.mods.carrierlabel;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpnConfig extends Activity implements OnClickListener {

	   static final String SETTING_PREFIX = "MOBILE_OPERATOR_NAME_";
	   
	   private ContentObserver mAirplaneModeObserver = new ContentObserver(new Handler()) {
	      @Override
		   public void onChange(boolean var1) {
	    	  observe();
	      }
	   };
	   private Button mOffButton;
	   private Button mOnButton;
	   private String mOperator;
	   private EditText mOperatorName;
	   private Button mSaveButton;
	   private TelephonyManager mTelephonyManager;

	   private void observe() {
	      boolean var1;
	      if(System.getInt(getContentResolver(), "airplane_mode_on", 0) == 1) {
	         var1 = true;
	      } else {
	         var1 = false;
	      }

	      Button var2 = mOnButton;
	      boolean var3 = false;
	      if(!var1) {
	         var3 = true;
	      }

	      var2.setEnabled(var3);
	      mOffButton.setEnabled(var1);
	   }
	   
	   @Override
	   public void onClick(View var1) {
	      switch(var1.getId()) {
	      case R.id.btn_save:
	         System.putString(getContentResolver(), "MOBILE_OPERATOR_NAME_" + mOperator, mOperatorName.getText().toString());
	         return;
	      case R.id.btn_airplane_on:
	         System.putInt(getContentResolver(), "airplane_mode_on", 1);
	         Intent var6 = new Intent("android.intent.action.AIRPLANE_MODE");
	         var6.putExtra("state", true);
	         sendBroadcast(var6);
	         return;
	      case R.id.btn_airplane_off:
	         System.putInt(getContentResolver(), "airplane_mode_on", 0);
	         Intent var3 = new Intent("android.intent.action.AIRPLANE_MODE");
	         var3.putExtra("state", false);
	         sendBroadcast(var3);
	         return;
	      default:
	      }
	   }
	   
	   @Override
	   public void onCreate(Bundle var1) {
	      super.onCreate(var1);
	      setTheme(miui.R.style.V5_Theme_Light);
	      setContentView(R.layout.main);
	      mTelephonyManager = (TelephonyManager)getSystemService("phone");
	      mOperatorName = (EditText)findViewById(R.id.edit_text);
	      mSaveButton = (Button)findViewById(R.id.btn_save);
	      mOnButton = (Button)findViewById(R.id.btn_airplane_on);
	      mOffButton = (Button)findViewById(R.id.btn_airplane_off);
	      mSaveButton.setOnClickListener(this);
	      mOnButton.setOnClickListener(this);
	      mOffButton.setOnClickListener(this);
	   }
	   
	   @Override
	   protected void onPause() {
	      getContentResolver().unregisterContentObserver(mAirplaneModeObserver);
	      super.onPause();
	   }
	   
	   @Override
	   protected void onResume() {
	      mOperator = mTelephonyManager.getSimOperator();
	      if(TextUtils.isEmpty(mOperator)) {
	         ((TextView)findViewById(R.id.info_text)).setText(R.string.no_sim_service);
	         mOperatorName.setEnabled(false);
	         mSaveButton.setEnabled(false);
	      } else {
	         ((TextView)findViewById(R.id.info_text)).setText(R.string.info);
	         String var1 = System.getString(getContentResolver(), "MOBILE_OPERATOR_NAME_" + mOperator);
	         if(!TextUtils.isEmpty(var1)) {
	            mOperatorName.setText(var1);
	         }

	         mOperatorName.setEnabled(true);
	         mSaveButton.setEnabled(true);
	      }

	      observe();
	      getContentResolver().registerContentObserver(System.getUriFor("airplane_mode_on"), true, mAirplaneModeObserver);
	      super.onResume();
	   }
	}