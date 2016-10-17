package com.hnsun.myaccount.util.view.click;

import java.util.Calendar;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 防止重复提交
 * @author hnsun
 * @date 2016/09/07
 */
public abstract class OnNoDoubleClickListener implements OnClickListener {

	public OnNoDoubleClickListener() {}
	
	public OnNoDoubleClickListener(boolean canClick) {
		this.canClick = canClick;
	}
	
	@Override
	public void onClick(View view) {
		if(canClick) { //某段时间内点击不执行事件
			long currentTime = Calendar.getInstance().getTimeInMillis();
			if((currentTime - lastClickTime) > OnNoDoubleClickListener.DELAY_TIME) { //超过时间可以执行
				lastClickTime = currentTime;
				onNoDoubleClick(view);
			}
		} else {
//			isUsing(view, true); //运行中
//			onNoDoubleClick(view);
//			isUsing(view, false); //运行完毕
			new UsingTask(view).execute(); //异步操作
		}
	}

	public abstract void onNoDoubleClick(View view); //防止二次提交
	
	private void isUsing(View view, boolean isUsing) { //是否正在执行，若执行 则禁止按钮否则可以按钮
		view.setClickable(!isUsing);
		view.setEnabled(!isUsing);
	}
	
	private boolean canClick; //是否还可以点击按钮,
	private long lastClickTime; //上次点击时间
	
	private static final int DELAY_TIME = 2 * 1000; //两次点击的时间间隔
	
	{
		this.canClick = true;
		this.lastClickTime = 0;
	}
	
	private class UsingTask extends AsyncTask<Boolean, Void, Boolean> { //1传入参数2加载3返回

        public UsingTask(View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() { //一开始
            super.onPreExecute();
            isUsing(view, true);
        }
        
		@Override
		protected Boolean doInBackground(Boolean... params) {
            onNoDoubleClick(view);
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) { //结束后
            super.onPostExecute(result);
            isUsing(view, false);
        }

        private View view;
	}
}
