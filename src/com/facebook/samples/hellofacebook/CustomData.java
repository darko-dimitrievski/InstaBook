package com.facebook.samples.hellofacebook;

import com.example.Infrastructure.ApplyEffect;

import android.graphics.Bitmap;

/**
 * This is just a simple class for holding data that is used to render our
 * custom view
 */
public class CustomData {
	private int mBackgroundColor;
	private String mText;
	private Bitmap b;
	private ApplyEffect applyEffect;

	public CustomData(int backgroundColor, String text, Bitmap bb) {
		mBackgroundColor = backgroundColor;
		mText = text;
		b = bb;
	}

	public CustomData(int backgroundColor, String text, Bitmap bb,
			ApplyEffect applyEffect) {
		mBackgroundColor = backgroundColor;
		mText = text;
		b = bb;
		this.applyEffect = applyEffect;
	}

	public CustomData(int backgroundColor, String text) {
		mBackgroundColor = backgroundColor;
		mText = text;

	}

	/**
	 * @return the backgroundColor
	 */
	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return mText;
	}

	public Bitmap getB() {
		return b;
	}

	public ApplyEffect getApplyEffect() {
		return applyEffect;
	}
}
