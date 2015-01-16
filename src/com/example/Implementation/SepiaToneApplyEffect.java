package com.example.Implementation;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.Infrastructure.ApplyEffect;
import com.facebook.samples.hellofacebook.Instabook;

public class SepiaToneApplyEffect extends BasicApplyEffect implements ApplyEffect{

	
	
	public SepiaToneApplyEffect(Instabook mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
		super.addToList(this);
	}
	private Bitmap sepiaTone(Bitmap image, int depth, double red, double green,
			double blue) {

		Bitmap newImage = Bitmap.createBitmap(image.getWidth(),
				image.getHeight(), image.getConfig());
		// kreira novu sliku praznu

		int A, R, G, B;
		int pixel;

		// Color.a
		for (int i = 0; i < image.getWidth(); i++) {

			for (int j = 0; j < image.getHeight(); j++) {
				// forovi za da gu izmine celu matricu

				pixel = image.getPixel(i, j);
				// zima ga daden piksel

				A = Color.alpha(pixel);// vadi ga alfa kanal
				R = Color.red(pixel);// vadi ga red kanal
				G = Color.green(pixel);// ovdeka su staveni green i blue kanali
										// 0 za da mi gu dade fakticki celu
										// sliku sz crvenu boju
				B = Color.blue(pixel);

				int r = (int) ((R * 0.393) + (G * 0.769) + (B * 0.189));
				int g = (int) ((R * 0.349) + (G * 0.686) + (B * 0.168));
				int b = (int) ((R * 0.272) + (G * 0.534) + (B * 0.131));
				if (r > 255)
					r = 255;
				if (g > 255)
					g = 255;
				if (b > 255)
					b = 255;

				newImage.setPixel(i, j, Color.argb(A, r, g, b));
				// Puni gu novu sliku sz pikseli od staru preraboteni :D

			}
		}

		return newImage;
	}

	@Override
	public Bitmap applyEffect(Bitmap source) {
		Bitmap returnBitmap = sepiaTone(source, 20, 30, 50, 70);
		return returnBitmap;
	}
	
	@Override
	public String toString() {
		return "SepiaToneApplyEffect";
	}

}
