package my.monopoly.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class RotateDrawable {
	static public Drawable rotateDrawable(float angle, Drawable drawable) {
		Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		Bitmap canvasBitmap = Bitmap.createBitmap(bmp.getHeight(),
				bmp.getWidth(), Bitmap.Config.ARGB_8888);
		canvasBitmap.eraseColor(0xFFFF0000);
		Canvas canvas2 = new Canvas(canvasBitmap);
		canvas2.rotate(90);
		canvas2.drawBitmap(bmp,  0,-canvas.getHeight(), null);
		
		return new BitmapDrawable(canvasBitmap);
	}
}
