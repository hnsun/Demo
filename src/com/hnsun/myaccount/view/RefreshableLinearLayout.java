package com.hnsun.myaccount.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.platform.SharedPreferencesUtil;

/**
 * 可下拉刷新布局（摘抄）
 * @author hnsun
 * @date 2016/10/12
 */
public class RefreshableLinearLayout extends LinearLayout {

	public RefreshableLinearLayout(Context context) {
		this(context, null);
	}

	public RefreshableLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RefreshableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) { //进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
		super.onLayout(changed, l, t, r, b);
		
		if(changed && !loadOnce) {
			hideHeaderHeight = - lHeader.getHeight();
			headerLayoutParams = (MarginLayoutParams) lHeader.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			lvContent = (ListView) getChildAt(1);
			lvContent.setOnTouchListener(onTouchListener);
			loadOnce = true;
		}
	}

	/**
	 * 给下拉刷新控件注册一个监听器
	 * @param listener 监听器的实现。
	 * @param id 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		pullToRefreshListener = listener;
		refreshId = id;
	}

	public void setOnTouchListViewListener(OnTouchListViewListener onTouchListViewListener) {
		this.onTouchListViewListener = onTouchListViewListener;
	}

	public void setAble(boolean isAble) {
		this.isAble = isAble;
	}

	private void finishRefreshing() { //当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
		currentStatus = RefreshableLinearLayout.STATUS_REFRESH_FINISHED;
		sharedPreferencesUtil.put(SharedPreferencesUtil.KEY_VIEW_REFRESH +refreshId , System.currentTimeMillis());
		new HideHeaderTask().execute();
	}

	private void initData(AttributeSet attrs, int defStyle) {
		lHeader = LayoutInflater.from(getContext()).inflate(R.layout.layout_refreshable_1, null, true);
		txtvDesc = (TextView) lHeader.findViewById(R.id.txtvDesc);
		txtvUpdatedAt = (TextView) lHeader.findViewById(R.id.txtvUpdatedAt);
		ivArrow = (ImageView) lHeader.findViewById(R.id.ivArrow);
		pbLoading = (ProgressBar) lHeader.findViewById(R.id.pbLoading);
		
		touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		sharedPreferencesUtil = new SharedPreferencesUtil(getContext(), SharedPreferencesUtil.FILENAME_VIEW_CONFIG);
		refreshUpdatedAt();
		setOrientation(VERTICAL);
		addView(lHeader, 0);
	}
	
	/**
	 * 根据当前ListView的滚动状态来设定 {@link #ableToPull}
	 * 的值，每次都需要在onTouch中第一个执行，这样可以判断出当前应该是滚动ListView，还是应该进行下拉。
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		View firstChild = lvContent.getChildAt(0);
		if(!isAble) {
			ableToPull = false;
			return ;
		}
		
		if(UtilBoss.ObjUtil.isNotNull(firstChild)) {
			int firstVisiblePos = lvContent.getFirstVisiblePosition();
			if(firstVisiblePos == 0 && firstChild.getTop() == 0) { //如果首个元素的上边缘，距离父布局值为0，就说明ListView滚动到了最顶部，此时应该允许下拉刷新
				if(!ableToPull) yDown = event.getRawY();
				ableToPull = true;
			} else {
				if(headerLayoutParams.topMargin != hideHeaderHeight) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					lHeader.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} else { //如果ListView中没有元素，也应该允许下拉刷新
			ableToPull = true;
		}
	}

	private void updateHeaderView() { //更新下拉头中的信息
		if(lastStatus == currentStatus) return;
		
		switch(currentStatus) {
			case RefreshableLinearLayout.STATUS_PULL_TO_REFRESH: 
				txtvDesc.setText(ResUtil.getText(getContext(), R.string.txt_refreshable_refresh_pull));
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.GONE);
				rotateArrow();
				break;
			case RefreshableLinearLayout.STATUS_RELEASE_TO_REFRESH: 
				txtvDesc.setText(ResUtil.getText(getContext(), R.string.txt_refreshable_refresh_release));
				ivArrow.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.GONE);
				rotateArrow();
				break;
			case RefreshableLinearLayout.STATUS_REFRESHING: 
				txtvDesc.setText(ResUtil.getText(getContext(), R.string.txt_refreshable_refreshing));
				ivArrow.clearAnimation();
				ivArrow.setVisibility(View.GONE);
				pbLoading.setVisibility(View.VISIBLE);
				break;
		}
		refreshUpdatedAt();
	}

	private void refreshUpdatedAt() { //刷新下拉头中上次更新时间的文字描述
		lastUpdateTime = sharedPreferencesUtil.loadLong(SharedPreferencesUtil.KEY_VIEW_REFRESH + refreshId);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String content;
		
		if(lastUpdateTime == 0) {
			content = ResUtil.getText(getContext(), R.string.txt_refreshable_not_updated);
		} else if(timePassed < 0) {
			content = ResUtil.getText(getContext(), R.string.txt_refreshable_time_error);
		} else if (timePassed < 1 * 60 * 1000) {
			content = ResUtil.getText(getContext(), R.string.txt_refreshable_updated_just_now);
		} else if (timePassed < 1 * 60 * 60 * 1000) {
			timeIntoFormat = timePassed / (1 * 60 * 1000);
			content = String.format(ResUtil.getText(getContext(), R.string.txt_refreshable_updated_at), timeIntoFormat);
		} else {
			content = ResUtil.getText(getContext(), R.string.txt_refreshable_not_updated);
		}
		txtvUpdatedAt.setText(content);
	}

	private void rotateArrow() { //根据当前的状态来旋转箭头
		float fromDegrees = 0f;
		float toDegrees = 0f;
		float pivotX = ivArrow.getWidth() / 2f;
		float pivotY = ivArrow.getHeight() / 2f;
		
		switch(currentStatus) {
			case RefreshableLinearLayout.STATUS_PULL_TO_REFRESH: 
				fromDegrees = 180f;
				toDegrees = 360f;
				break;
			case RefreshableLinearLayout.STATUS_RELEASE_TO_REFRESH: 
				fromDegrees = 0f;
				toDegrees = 180f;
				break;
		}

		Animation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		ivArrow.startAnimation(animation);
	}

	private int refreshId; //为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	private int currentStatus; //当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH, STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
	private int lastStatus; //记录上一次的状态是什么，避免进行重复操作
	private int touchSlop; //在被判定为滚动之前用户手指可以移动的最大值。
	private int hideHeaderHeight; //下拉头的高度
	private long lastUpdateTime; //上次更新时间的毫秒值
	private float yDown; //手指按下时的屏幕纵坐标
	private boolean loadOnce; //是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	private boolean ableToPull; //当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
	private boolean isAble; //可调
	private SharedPreferencesUtil sharedPreferencesUtil; //用于存储上次更新时间
	private MarginLayoutParams headerLayoutParams; //下拉头的布局参数
	private PullToRefreshListener pullToRefreshListener; //下拉刷新的回调接口
	private OnTouchListViewListener onTouchListViewListener; //ListView普通触碰事件

	private TextView txtvDesc; //指示下拉和释放的文字描述
	private TextView txtvUpdatedAt; //上次更新时间的文字描述
	private ImageView ivArrow; //指示下拉和释放的箭头
	private ProgressBar pbLoading; //刷新时显示的进度条
	private View lHeader; //下拉头的View
	private ListView lvContent; //需要去下拉刷新的ListView
	
	private OnTouchListener onTouchListener = new OnTouchListener() { //当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			setIsAbleToPull(event);
			
			if(ableToPull) {
				switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN: yDown = event.getRawY(); break;
					case MotionEvent.ACTION_MOVE:
						float yMove = event.getRawY();
						int distance = (int) (yMove - yDown);
						
						//如果手指是下滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
						if(distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) return false;
						if(distance < touchSlop) return false;
						if(currentStatus != RefreshableLinearLayout.STATUS_REFRESHING) {
							currentStatus = (headerLayoutParams.topMargin > 0) ? RefreshableLinearLayout.STATUS_RELEASE_TO_REFRESH : RefreshableLinearLayout.STATUS_PULL_TO_REFRESH;
							//通过偏移下拉头的topMargin值，来实现下拉效果
							headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
							lHeader.setLayoutParams(headerLayoutParams);
						}
						break;
					case MotionEvent.ACTION_UP:
					default:
						if(currentStatus == RefreshableLinearLayout.STATUS_RELEASE_TO_REFRESH) {
							//松手时如果是释放立即刷新状态，就去调用正在刷新的任务
							new RefreshingTask().execute();
						} else if(currentStatus == RefreshableLinearLayout.STATUS_PULL_TO_REFRESH) {
							//松手时如果是下拉状态，就去调用隐藏下拉头的任务
							new HideHeaderTask().execute();
						}
						break;
				}
				
				//时刻记得更新下拉头中的信息
				if (currentStatus == RefreshableLinearLayout.STATUS_PULL_TO_REFRESH
						|| currentStatus == RefreshableLinearLayout.STATUS_RELEASE_TO_REFRESH) {
					updateHeaderView();
					//当前正处于下拉或释放状态，要让ListView失去焦点，否则被点击的那一项会一直处于选中状态
					lvContent.setPressed(false);
					lvContent.setFocusable(false);
					lvContent.setFocusableInTouchMode(false);
					lastStatus = currentStatus;
					return true; //当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
				}
			}
			
			if(UtilBoss.ObjUtil.isNotNull(onTouchListViewListener)) return onTouchListViewListener.onTouch(view, event);
			return false;
		}
	};

	public static final int SCROLL_SPEED = -20; //下拉头部回滚的速度
	
	public static final int STATUS_PULL_TO_REFRESH = 0x1301; //下拉状态
	public static final int STATUS_RELEASE_TO_REFRESH = 0x1302; //释放立即刷新状态
	public static final int STATUS_REFRESHING = 0x1303; //正在刷新状态
	public static final int STATUS_REFRESH_FINISHED = 0x1304; //刷新完成或未刷新状态

	{
		this.refreshId = -1;
		this.currentStatus = RefreshableLinearLayout.STATUS_REFRESH_FINISHED;
		this.lastStatus = RefreshableLinearLayout.STATUS_REFRESH_FINISHED;
		this.isAble = true;
	}
	
	public interface PullToRefreshListener { //下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。

		public void onRefresh(); //刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
	}
	
	public interface OnTouchListViewListener {
		
		public boolean onTouch(View view, MotionEvent event);
	}

	/**
	 * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器
	 * @author hnsun
	 * @date 2016/10/12
	 */
	private class RefreshingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while(true) {
				topMargin = topMargin + RefreshableLinearLayout.SCROLL_SPEED;
				if(topMargin <= 0) {
					topMargin = 0;
					break;
				}
				publishProgress(topMargin);
				UtilBoss.ThreadUtil.sleepBy(10);
			}
			currentStatus = RefreshableLinearLayout.STATUS_REFRESHING;
			publishProgress(0);
			if(UtilBoss.ObjUtil.isNotNull(pullToRefreshListener)) pullToRefreshListener.onRefresh();
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			updateHeaderView();
			headerLayoutParams.topMargin = topMargin[0];
			lHeader.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			finishRefreshing();
		}
	}

	/**
	 * 隐藏下拉头的任务，当未进行下拉刷新或下拉刷新完成后，此任务将会使下拉头重新隐藏
	 * @author hnsun
	 * @date 2016/10/12
	 */
	private class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while(true) {
				topMargin = topMargin + RefreshableLinearLayout.SCROLL_SPEED;
				if(topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
				UtilBoss.ThreadUtil.sleepBy(10);
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			lHeader.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			lHeader.setLayoutParams(headerLayoutParams);
			currentStatus = RefreshableLinearLayout.STATUS_REFRESH_FINISHED;
		}
	}
}
