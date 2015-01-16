package com.example.Implementation;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public class GreyScaleApplyEffect extends BasicApplyEffect implements ApplyEffect{

	public GreyScaleApplyEffect(Instabook mainActivity){
		super(mainActivity);
		super.addToList(this);
	}
	@Override
	public Bitmap applyEffect(Bitmap source) {
		// TODO Auto-generated method stub
		return applyGreyscaleEffect(source);
	}
	
	 public Bitmap applyGreyscaleEffect(Bitmap src) {
	        // constant factors
	        final double GS_RED = 0.299;
	        final double GS_GREEN = 0.587;
	        final double GS_BLUE = 0.114;

	        // create output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	        // pixel information
	        int A, R, G, B;
	        int pixel;

	        // get image size
	        int width = src.getWidth();
	        int height = src.getHeight();

	        // scan through every single pixel
	        for (int x = 0; x < width; ++x) {
	            for (int y = 0; y < height; ++y) {
	                // get one pixel color
	                pixel = src.getPixel(x, y);
	                // retrieve color of all channels
	                A = Color.alpha(pixel);
	                R = Color.red(pixel);
	                G = Color.green(pixel);
	                B = Color.blue(pixel);
	                // take conversion up to one single value
	                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	                // set new pixel color to output bitmap
	                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	            }
	        }

	        // return final image
	        return bmOut;
	    }

@Override
public String toString() {
	// TODO Auto-generated method stub
	return "GreyScale";
}
}
