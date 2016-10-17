package com.hnsun.myaccount.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.AssetsUtil;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 商标图
 * @author hnsun
 * @date 2016/08/23
 */
public class BrandView extends View {

	public BrandView(Context context) {
		this(context, null);
	}

	public BrandView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BrandView(Context context, AttributeSet attrs, int defStyleAttr) {
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
			measuredWidth = (int) mainPaint.measureText(mainText) + getPaddingLeft() + getPaddingRight();
			if(specMode == MeasureSpec.AT_MOST) measuredWidth = Math.min(measuredWidth, specSize); //至多 与屏幕宽度对比
		}

		//measuredHeight
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if(specMode == MeasureSpec.EXACTLY) measuredHeight = specSize; //确定的
		else {
			measuredHeight = (5 * (int) (mainPaint.descent() - mainPaint.ascent()) / 4) + getPaddingTop() + getPaddingBottom();
			if(specMode == MeasureSpec.AT_MOST) measuredHeight = Math.min(measuredHeight, specSize); //至多 与屏幕高度对比
		}

		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float mainY = getPaddingTop() - mainPaint.ascent();
		float descY = mainY + (mainPaint.descent() - mainPaint.ascent()) / 3;
		canvas.drawText(mainText, getPaddingLeft(), mainY, mainPaint);
		canvas.drawText(descText, getMeasuredWidth() - getPaddingRight() - descPaint.measureText(descText), descY, descPaint);
	}
	
	private void initData(AttributeSet attrs, int defStyleAttr) {
		if(UtilBoss.ObjUtil.isNotNull(getContext()) && UtilBoss.ObjUtil.isNotNull(attrs)) {
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BrandView);
			mainText = (String) ResUtil.FromAttrs.text(getContext(), typedArray, R.styleable.BrandView_mainText);
			descText = (String) ResUtil.FromAttrs.text(getContext(), typedArray, R.styleable.BrandView_descText);
			mainColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.BrandView_mainColor);
			descColor = ResUtil.FromAttrs.color(getContext(), typedArray, R.styleable.BrandView_descColor);
			mainSize = ResUtil.FromAttrs.dimension(getContext(), typedArray, R.styleable.BrandView_mainSize);
			descSize = ResUtil.FromAttrs.dimension(getContext(), typedArray, R.styleable.BrandView_descSize);
			typedArray.recycle();
		}
		
		padding = DensityUtil.dp2px(getContext(), 5.0f);
		if(UtilBoss.StrUtil.isEmpty(mainText)) mainText = ResUtil.getText(getContext(), R.string.app_name);
		if(UtilBoss.StrUtil.isEmpty(descText)) descText = ResUtil.getText(getContext(), R.string.app_desc);
		if(mainColor == ResUtil.FromAttrs.DEFAULT_COLOR) mainColor = ResUtil.getColor(getContext(), R.color.defaultFontNormal);
		if(descColor == ResUtil.FromAttrs.DEFAULT_COLOR) descColor = ResUtil.getColor(getContext(), R.color.defaultFontNormal);
		mainPaint.setTextSize(mainSize = (mainSize == 0 ? DensityUtil.sp2px(getContext(), 40.0f) : mainSize));
		mainPaint.setTypeface(AssetsUtil.ttf(getContext(), AssetsUtil.TTF_BRAND));
		mainPaint.setColor(mainColor);
		mainPaint.getTextBounds(mainText, 0, mainText.length(), rect);
		descPaint.setTextSize(descSize = (descSize == 0 ? DensityUtil.sp2px(getContext(), 12.0f) : descSize));
		descPaint.setTypeface(AssetsUtil.ttf(getContext(), AssetsUtil.TTF_BRAND));
		descPaint.setColor(descColor);
		
		setPadding(padding, padding, padding, padding);
	}

	private int padding;
	
	private String mainText; //主显示文字
	private String descText; //描述性文字
	private int mainColor; //主显示文字颜色
	private int descColor; //描述性文字颜色
	private float mainSize; //主显示文字大小 比描述性大
	private float descSize; //描述性文字大小 比主显示小

	private Rect rect;
	private Paint mainPaint;
	private Paint descPaint;
	
	{
		this.rect = new Rect();
		this.mainPaint = new Paint();
		this.descPaint = new Paint();
	}
}
