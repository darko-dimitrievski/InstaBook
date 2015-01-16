package com.example.Implementation;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public class SunnyApplyEffect extends BasicApplyEffect implements ApplyEffect{

	
	public SunnyApplyEffect(Instabook mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
		super.addToList(this);
	}
	
	 public  Bitmap applyBrightnessEffect(Bitmap src, int value) {
	        // image size
	        int width = src.getWidth();
	        int height = src.getHeight();
	        // create output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	        // color information
	        int A, R, G, B;
	        int pixel;

	        // scan through all pixels
	        for(int x = 0; x < width; ++x) {
	            for(int y = 0; y < height; ++y) {
	                // get pixel color
	                pixel = src.getPixel(x, y);
	                A = Color.alpha(pixel);
	                R = Color.red(pixel);
	                G = Color.green(pixel);
	                B = Color.blue(pixel);

	                // increase/decrease each channel
	                R += value;
	                if(R > 255) { R = 255; }
	                else if(R < 0) { R = 0; }

	                G += value;
	                if(G > 255) { G = 255; }
	                else if(G < 0) { G = 0; }

	                B += value;
	                if(B > 255) { B = 255; }
	                else if(B < 0) { B = 0; }

	                // apply new pixel color to output bitmap
	                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	            }
	        }

	        // return final image
	        return bmOut;
	    }

	@Override
	public Bitmap applyEffect(Bitmap source) {
		// TODO Auto-generated method stub
		return applyBrightnessEffect(source, 50);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Sunny effect";
	}
}
