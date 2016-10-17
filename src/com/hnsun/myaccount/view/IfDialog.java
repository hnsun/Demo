package com.hnsun.myaccount.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.util.UtilBoss;

/**
 * 判断弹框
 * @author hnsun
 * @date 2016/09/23
 */
public class IfDialog extends Dialog {

	public IfDialog(Context context) {
		super(context, R.style.uDialog);
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_if_icon, null);
		setContentView(view);
		initData(view);
	}
	
	public void show(String msg, IfDialog.OnConfirmClickListener listener, Type type) {
		txtvContent.setText(msg);
		this.listener = listener;
		initType(type);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(params);
		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setWindowAnimations(R.style.uDialogAnim);
		
		setCanceledOnTouchOutside(true); //以外点击事件关闭框
		show();
	}
	
	private void initData(View view) {
		txtvContent = (TextView) view.findViewById(R.id.txtvContent);
		btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
		btnCancel = (Button) view.findViewById(R.id.btnCancel);
		ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		
		btnConfirm.setOnClickListener(onClickListener);
		btnCancel.setOnClickListener(onClickListener);
	}
	
	private void initType(Type type) {
		if(UtilBoss.ObjUtil.isNull(ivIcon)) return;
		switch(type) {
			case WARNING: ;
			default: ivIcon.setImageResource(R.drawable.ic_warning); break;
		}
	}
	
	private TextView txtvContent;
	private Button btnConfirm;
	private Button btnCancel;
	private ImageView ivIcon;
	
	private IfDialog.OnConfirmClickListener listener;
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnConfirm: if(UtilBoss.ObjUtil.isNotNull(listener)) listener.onClick(view);
				case R.id.btnCancel: IfDialog.this.dismiss(); break;
			}
		}
	};
	
	public enum Type { //显示信息类型
		WARNING,
	}
	
	/**
	 * 确定按钮事件
	 * @author hnsun
	 * @date 2016/09/24
	 */
	public interface OnConfirmClickListener {
		
		public void onClick(View view);
	}
}
