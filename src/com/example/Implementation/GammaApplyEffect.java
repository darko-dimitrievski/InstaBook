package com.example.Implementation;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public class GammaApplyEffect extends BasicApplyEffect implements ApplyEffect {

	public GammaApplyEffect(Instabook mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
		super.addToList(this);
	}

	@Override
	public Bitmap applyEffect(Bitmap source) {
		// create output image

		double red = 1.6;
		double green = 1.6;
		double blue = 1.6;
		Bitmap bmOut = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), source.getConfig());
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		// color information
		int A, R, G, B;
		int pixel;
		// constant value curve
		final int MAX_SIZE = 256;
		final double MAX_VALUE_DBL = 255.0;
		final int MAX_VALUE_INT = 255;
		final double REVERSE = 1.0;

		// gamma arrays
		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		// setting values for every gamma channels
		for (int i = 0; i < MAX_SIZE; ++i) {
			gammaR[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ red)) + 0.5));
			gammaG[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ green)) + 0.5));
			gammaB[i] = (int) Math.min(
					MAX_VALUE_INT,
					(int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE
							/ blue)) + 0.5));
		}

		// apply gamma table
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get pixel color
				pixel = source.getPixel(x, y);
				A = Color.alpha(pixel);
				// look up gamma
				R = gammaR[Color.red(pixel)];
				G = gammaG[Color.green(pixel)];
				B = gammaB[Color.blue(pixel)];
				// set new color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;

	}

	@Override
	public String toString() {
		return "GammaApplyEffect";
	}
	

}
