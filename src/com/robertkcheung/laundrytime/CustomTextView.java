package com.robertkcheung.laundrytime;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView{
	private final static int sego = 0;
	private final static int aroni = 1;

	public CustomTextView(Context context) {
		super(context);
		this.isInEditMode();
		// TODO Auto-generated constructor stub
	}
	public CustomTextView(Context context, AttributeSet attrs){
		super(context, attrs);
		parseAttributes(context,attrs);
		this.isInEditMode();
	}
	public CustomTextView(Context context, AttributeSet attrs, int defstyle){
		super(context, attrs,defstyle);
		parseAttributes(context,attrs);
		this.isInEditMode();
	}
	private void parseAttributes(Context context, AttributeSet attrs) {
	    TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.customfont);

	    int typeface = values.getInt(R.styleable.customfont_typeface, 2);
	    switch(typeface) {
	        case sego: default:
	        	Typeface lato3 = Typeface.createFromAsset(this.getContext().getAssets(), "sego.ttf");
	            setTypeface(lato3); 
	            break;
	        case aroni:
	        	Typeface lato1 = Typeface.createFromAsset(this.getContext().getAssets(), "aroni.ttf");
	            setTypeface(lato1);
	            break;
	        
	    }
	}

}
