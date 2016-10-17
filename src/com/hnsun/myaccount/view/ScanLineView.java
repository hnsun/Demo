package com.hnsun.myaccount.view;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.platform.ResUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 二维码扫描时扫描线（摘抄）
 * @author hnsun
 * @date 2016/10/10
 */
public class ScanLineView extends View {

	public ScanLineView(Context context) {
		this(context, null);
	}

	public ScanLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScanLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData(attrs, defStyleAttr);
	}
	
	private void initData(AttributeSet attrs, int defStyleAttr) {
		paint.setAntiAlias(true);
		paint.setColor(ResUtil.getColor(getContext(), R.color.defaultTheme));
	}
	
	@Override
	@SuppressLint("DrawAllocation")
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) width = MeasureSpec.getSize(widthMeasureSpec);
		else {
			width = DensityUtil.dp2px(getContext(), 250);
		}
		
		if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) height = MeasureSpec.getSize(heightMeasureSpec);
		else {
			height = DensityUtil.dp2px(getContext(), 250);
		}
		
		setMeasuredDimension(width, height);
		Shader shader = new LinearGradient(width / 2.0f, height, width / 2.0f, 0, 
				new int[] { ResUtil.getColor(getContext(), R.color.defaultTheme), ResUtil.getColor(getContext(), R.color.transparent), }, null, Shader.TileMode.CLAMP);
		paint.setShader(shader); //颜色渐变
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(0, 0, width, height, paint);
	}
	
	private int width, height; //控件高度宽度
	private Paint paint;
	
	{
		this.paint = new Paint();
	}
}
