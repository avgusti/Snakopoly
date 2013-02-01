package my.monopoly.dialogs;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

public class ThemeLoadDialog extends Dialog {
	private ThemeLoadCallback callback;

	public ThemeLoadDialog(Context context, ThemeLoadCallback callback) {
		super(context);
		this.callback = callback;
	}

	public interface ThemeLoadCallback {
		public void onLoad();
	}

	private class DownloadFile extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... sUrl) {
			try {
				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(
						"/sdcard/file_name.extension");

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;
		}
	}

	private class loadImages extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... images) {
			try {
				for (String fname : images) {
					Bitmap bitmap = BitmapFactory.decodeFile(fname);
					// this will be useful so that you can show a typical 0-100%
					// progress bar
				}
			} catch (Exception e) {
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
