package wigets;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class FPSView extends View{

	private Paint mPaint;
	public FPSView(Context context) {
		super(context);
		mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(30);
        mPaint.setTypeface(Typeface.SERIF);
		
	}

	
	private long lasttime=0, fps=0, fpso=0; 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long time=Calendar.getInstance().getTimeInMillis();
		if(time-lasttime>1000){fpso=fps;lasttime=time;fps=0;}
		fps++;
		Paint p=mPaint;
		
		canvas.drawText(fpso+"", 0f,0f, p);
	}
	

}
