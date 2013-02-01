package resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import my.monopoly.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class FileManager implements Runnable{
	private Context context;
	private Bitmap bmp; 
	
	public FileManager(Context context) {
		super();
		this.context = context;
		bmp = LoadBMPsdcard("/sdcard/cardimage.gif");  
		new Thread(this).start();  
	}

	private Handler handler = new Handler();  
	    //the bitmap to be downloaded  
	 

	private Bitmap LoadBMPsdcard(String path)  
	    {  
	        //creates a 'File' object, to check if the image exists (Thanks to Jbeerdev for the tip)  
	        File imageFile = new File(path);  
	  
	        //if the file exists  
	        if(imageFile.exists())  
	        {  
	            //load the bitmap from the given path  
	            return BitmapFactory.decodeFile(path);  
	        }  
	        else  
	        {  
	        
	        	//return the standard 'Image not found' bitmap placed on the res folder  
	            return BitmapFactory.decodeResource(context.getResources(), R.drawable.f404);  
	        }  
	    }  
	
	  //This method is responsible for downloading the image from a remote location  
    private Bitmap DownloadBMP(String url) throws IOException  
    {  
        //create a URL object using the passed string  
        URL location = new URL(url);  
        //create a InputStream object, to read data from a remote location  
        InputStream is = location.openStream();  
        //use the BitmapFactory to decode this downloaded stream into a bitmap  
        Bitmap returnedBMP = BitmapFactory.decodeStream(is);  
        //close the InputStream  
        is.close();  
        //returns the downloaded bitmap  
        return returnedBMP;  
    }  
       
    //this method must be overridden, as we are implementing the runnable interface  
    @Override  
    public void run()  
    {  
        //update the canvas from this thread  
        handler.post(new Runnable()  
        {  
            @Override  
            public void run()  
            {  
                //try to download the image from a remote location  
                try  
                {  
                    bmp = DownloadBMP("http://img41.imageshack.us/img41/426/bricks.gif");  
                }  
                //if the image couldn't be downloaded, return the standard 'Image not found' bitmap  
                catch (IOException e)  
                {  
                    bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.f404);  
                }  
            }  
        });  
    }  
}
