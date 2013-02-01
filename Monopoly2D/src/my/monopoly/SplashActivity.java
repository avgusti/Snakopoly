package my.monopoly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {
	SplashHandler mHandler = new SplashHandler();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        
        //Create an object of type SplashHandler
        
        // set the layout for this activity
        setContentView(R.layout.splash);
        // Create a Message object
        Message msg = new Message();
        //Assign a unique code to the message.
        //Later, this code will be used to identify the message in Handler class.
        msg.what = 0;
        // Send the message with a delay of 3 seconds(3000 = 3 sec).
        mHandler.sendMessageDelayed(msg, 1000);
    }     
    
    
//    @Override
//	public boolean onTouchEvent(MotionEvent event) {
//    	if(event.getAction()==MotionEvent.ACTION_UP) 
//    	{Message msg = new Message();
//         //Assign a unique code to the message.
//         //Later, this code will be used to identify the message in Handler class.
//         msg.what = 0;
//         // Send the message with a delay of 3 seconds(3000 = 3 sec).
//        mHandler.handleMessage(msg);
//    	return false;
//    	}
//    	return true;
//    	//return super.onTouchEvent(event);
//	}


	// Handler class implementation to handle the message
    private class SplashHandler extends Handler {
        
        //This method is used to handle received messages
        public void handleMessage(Message msg)
          {
            // switch to identify the message by its code
            switch (msg.what)
            {
            default:
            case 0:
              super.handleMessage(msg);
              
              //Create an intent to start the new activity.
              // Our intention is to start MainActivity
              Intent intent = new Intent();
              intent.setClass(SplashActivity.this,MenuActivity.class);
              startActivity(intent);
              // finish the current activity
              SplashActivity.this.finish();
            }
          }
    }    
   
}