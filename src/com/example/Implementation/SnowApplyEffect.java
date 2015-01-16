package com.example.Implementation;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public class SnowApplyEffect extends BasicApplyEffect implements ApplyEffect {

    public static final int COLOR_MIN = 0x00;
    public static final int COLOR_MAX = 0xFF;
    
	public SnowApplyEffect(Instabook mainActivity) {
		super(mainActivity);
		super.addToList(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Bitmap applyEffect(Bitmap source) {
	        // get image size
	        int width = source.getWidth();
	        int height = source.getHeight();
	        int[] pixels = new int[width * height];
	        // get pixel array from source
	        source.getPixels(pixels, 0, width, 0, 0, width, height);
	        // random object
	        Random random = new Random();

	        int R, G, B, index = 0, thresHold = 50;
	        // iteration through pixels
	        for(int y = 0; y < height; ++y) {
	            for(int x = 0; x < width; ++x) {
	                // get current index in 2D-matrix
	                index = y * width + x;
	                // get color
	                R = Color.red(pixels[index]);
	                G = Color.green(pixels[index]);
	                B = Color.blue(pixels[index]);
	                // generate threshold
	                thresHold = random.nextInt(COLOR_MAX);
	                if(R > thresHold && G > thresHold && B > thresHold) {
	                    pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
	                }
	            }
	        }
	        // output bitmap
	        Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
	        return bmOut;
	    
	}

	@Override
	public String toString() {
		return "SnowApplyEffect";
	}
	
	

}
