package com.hnsun.myaccount.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.DensityUtil;
import com.hnsun.myaccount.util.platform.ResUtil;

/**
 * 单词字母栏
 * @author hnsun
 * @date 2016/09/24
 */
public class LetterBarView extends View {

	public LetterBarView(Context context) {
		this(context, null);
	}

	public LetterBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LetterBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		fillChars();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = getWidth(); //对应宽度
		int height = getHeight(); //对应高度
		int singleHeight = height / chars.length; //单个字母高度
		
		String text = null;
		for(int i = 0; i < chars.length; i++) { //足个画上
			paint.setColor(ResUtil.getColor(getContext(), R.color.defaultFontNormal)); //android.graphics.Color.rgb(22, 22, 22)
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true); //抗锯齿
			paint.setTextSize(DensityUtil.sp2px(getContext(), 11.0f));
			
			if(i == choose) { //选中
				paint.setColor(ResUtil.getColor(getContext(), R.color.defaultFontOn)); //android.graphics.Color.parseColor("#009900")
				paint.setFakeBoldText(true);
			}
			
			text = String.valueOf(chars[i]);
			float x = width / 2 - paint.measureText(text) / 2;
			float y = singleHeight * i + singleHeight;
			canvas.drawText(text, x, y, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final float y = event.getY();
		final int action = event.getAction();
		final int oldChoose = choose;
		final int pos = (int) (y / getHeight() * chars.length); //当前位置
		final OnTouchLetterListener listener = onTouchLetterListener;
		
		switch(action) {
			case MotionEvent.ACTION_UP:
				setBackgroundColor(0x00000000); //setBackgroundDrawable(new ColorDrawable(0x00000000));
				choose = -1;
				invalidate();
				if(UtilBoss.ObjUtil.isNotNull(txtvBigLetter)) txtvBigLetter.setVisibility(View.GONE);
				break;
			default :
				setBackgroundColor(ResUtil.getColor(getContext(), R.color.lightgray));
				if(oldChoose != pos) {
					if(pos >= 0 && pos < chars.length) {
						if(UtilBoss.ObjUtil.isNotNull(listener)) listener.onTouch(chars[pos]);
						if(UtilBoss.ObjUtil.isNotNull(txtvBigLetter)) {
							txtvBigLetter.setText(String.valueOf(chars[pos]));
							txtvBigLetter.setVisibility(View.VISIBLE);
						}
						choose = pos;
						invalidate();
					}
				}
				break;
		}
		return true;
	}

	public void setTxtvBigLetter(TextView txtvBigLetter) {
		this.txtvBigLetter = txtvBigLetter;
	}

	public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
		this.onTouchLetterListener = onTouchLetterListener;
	}
	
	private void fillChars() {
		System.arraycopy(ConstantsUtil.LETTER_EN, 0, chars, 0, ConstantsUtil.LETTER_EN.length); //数组复制
		chars[chars.length - 1] = '#';
	}
	

	private int choose; //选中
	private Paint paint; //画笔
	private char[] chars; //字母组
	
	private TextView txtvBigLetter; //放大显示字母
	private OnTouchLetterListener onTouchLetterListener; //字母触摸事件
	
	{
		this.choose = -1;
		this.paint = new Paint();
		this.chars = new char[ConstantsUtil.LETTER_EN.length + 1];
	}
	
	/**
	 * 当触摸到字母时事件
	 * @author hnsun
	 * @date 2016/09/24
	 */
	public interface OnTouchLetterListener {
		
		public void onTouch(char letter);
	}
}
