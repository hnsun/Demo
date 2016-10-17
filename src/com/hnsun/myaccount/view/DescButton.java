package com.hnsun.myaccount.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 含有描述的按钮
 * @author hnsun
 * @date 2016/09/02
 */
public class DescButton extends Button {

	public DescButton(Context context) {
		this(context, null);
	}

	public DescButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DescButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(attrs, defStyleAttr);
	}

	/**
	 * MeasureSpec由大小和模式组成，比onDraw先执行，封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
	 * 它有三种模式：
	 * 		UNSPECIFIED(未指定)，父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小；
	 * 		EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
	 * 		AT_MOST(至多)，子元素至多达到指定大小的值。
	 * 它常用的三个函数：
	 * 		1.static int getMode(int measureSpec)：根据提供的测量值(格式)提取模式(上述三个模式之一)
	 * 		2.static int getSize(int measureSpec)：根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
	 * 		3.static int makeMeasureSpec(int size, int mode)：根据提供的大小值和模式创建一个测量值(格式)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = 0, measuredHeight = 0;
		int specMode = 0;
		int specSize = 0;
		
		//measuredWidth
		specMode = MeasureSpec.getMode(widthMeasureSpec);
		specSize = MeasureSpec.getSize(widthMeasureSpec);
		if(specMode == MeasureSpec.EXACTLY) measuredWidth = specSize; //确定的
		else {
			measuredWidth = (int) Math.max(namePaint.measureText(nameText), infoPaint.measureText(infoText)) +getPaddingLeft() + getPaddingRight();
			if(specMode == MeasureSpec.AT_MOST) measuredWidth = specSize; //至多 保持原样
		}
		
		namePaint.setTextSize(Math.max(nameSize, infoSize)); //有改变过，防止重绘时大小影响

		//measuredHeight
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if(specMode == MeasureSpec.EXACTLY) measuredHeight = specSize; //确定的
		else {
			measuredHeight = 2 * (int) (nameSize > infoSize ? (namePaint.descent() - namePaint.ascent()) : (infoPaint.descent() - infoPaint.ascent())); 
			measuredHeight += getPaddingTop() + getPaddingBottom();
			if(specMode == MeasureSpec.AT_MOST) measuredHeight = Math.min(measuredHeight, specSize); //至多 与屏幕高度相比
		}

		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float mainY = getPaddingTop() - namePaint.ascent();
		float descY = mainY + 3 * (namePaint.descent() - namePaint.ascent()) / 3;
		canvas.drawText(nameText, getPaddingLeft(), mainY, namePaint);
		canvas.drawText(infoText, getPaddingLeft(), descY, infoPaint);
	}
	
	public void setNameText(String nameText) {
		this.nameText = nameText;
	}

	public void setInfoText(String infoText) {
		this.infoText = infoText;
	}

	private void initData(AttributeSet attrs, int defStyleAttr) {
		if(UtilBoss.ObjUtil.isNotNull(getContext()) && UtilBoss.ObjUtil.isNotNull(attrs)) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DescButton);
			nameText = (String) ResUtil.FromAttrs.text(getContext(), typedArray, R.styleable.DescButton_nameText);
			infoText = (String) ResUtil.FromAttrs.text(getContext(), typedArray, R.styleable.DescButton_infoText);
			nameColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.DescButton_nameColor);
			infoColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.DescButton_infoColor);
			nameSize = ResUtil.FromAttrs.dimension(getContext(), typedArray, R.styleable.DescButton_nameSize);
			infoSize = ResUtil.FromAttrs.dimension(getContext(), typedArray, R.styleable.DescButton_infoSize);
			normalColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.DescButton_normalColor);
			onColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.DescButton_onColor);
			typedArray.recycle();
		}

		if(UtilBoss.StrUtil.isEmpty(nameText)) nameText = ConstantsUtil.NULL_STRING;
		if(UtilBoss.StrUtil.isEmpty(infoText)) infoText = ConstantsUtil.NULL_STRING;
		if(nameColor == ResUtil.FromAttrs.DEFAULT_COLOR) nameColor = ResUtil.getColor(getContext(), R.color.defaultFontNormal);
		if(infoColor == ResUtil.FromAttrs.DEFAULT_COLOR) infoColor = ResUtil.getColor(getContext(), R.color.defaultFontNormal);
		if(normalColor == ResUtil.FromAttrs.DEFAULT_COLOR) normalColor = ResUtil.getColor(getContext(), R.color.white);
		if(onColor == ResUtil.FromAttrs.DEFAULT_COLOR) onColor = ResUtil.getColor(getContext(), R.color.lightgray);
		namePaint.setTextSize(nameSize = (nameSize == 0 ? DensityUtil.sp2px(getContext(), 13.0f) : nameSize)); //px
		namePaint.setTypeface(SystemStatus.typeface);
		namePaint.setColor(nameColor);
		infoPaint.setTextSize(infoSize = (infoSize == 0 ? DensityUtil.sp2px(getContext(), 12.0f) : infoSize)); //px
		infoPaint.setTypeface(SystemStatus.typeface);
		infoPaint.setColor(infoColor);
		
		setOnTouchListener(onTouchListener);
	}

	private String nameText; //主显示文字
	private String infoText; //描述性文字
	private int nameColor; //主显示文字颜色
	private int infoColor; //描述性文字颜色
	private float nameSize; //主显示文字大小
	private float infoSize; //描述性文字大小
	private int normalColor; //正常背景色
	private int onColor; //操作时背景色

	private Paint namePaint;
	private Paint infoPaint;
	
	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN: view.setBackgroundColor(onColor); break;
				case MotionEvent.ACTION_UP: view.setBackgroundColor(normalColor); break;
			}
			return false;
		}
	};
	
	{
		this.namePaint = new Paint();
		this.infoPaint = new Paint();
	}
}
