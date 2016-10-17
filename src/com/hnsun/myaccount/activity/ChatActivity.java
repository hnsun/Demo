package com.hnsun.myaccount.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hnsun.myaccount.R;
import com.hnsun.myaccount.activity.common.BaseActivity;
import com.hnsun.myaccount.mess.ContextHandler;
import com.hnsun.myaccount.mess.TextChangeAdapter;
import com.hnsun.myaccount.model.ChatModel;
import com.hnsun.myaccount.model.ChatModel.Type;
import com.hnsun.myaccount.model.NetModel;
import com.hnsun.myaccount.model.SystemStatus;
import com.hnsun.myaccount.model.dbo.TblUser;
import com.hnsun.myaccount.support.online.ChatOnline;
import com.hnsun.myaccount.util.CodeBoss;
import com.hnsun.myaccount.util.ConstantsUtil;
import com.hnsun.myaccount.util.I18nUtil;
import com.hnsun.myaccount.util.UtilBoss;
import com.hnsun.myaccount.util.platform.KeyBoardUtil;
import com.hnsun.myaccount.util.platform.ResUtil;
import com.hnsun.myaccount.util.view.ViewHolder;
import com.hnsun.myaccount.util.view.adapter.AdapterForList;

/**
 * 聊天界面
 * @author hnsun
 * @date 2016/09/22
 */
public class ChatActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {
		initContentView(R.layout.activity_chat);
		getActionBar().hide();
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		txtvUsername = (TextView) findViewById(R.id.txtvUsername);
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSend = (Button) findViewById(R.id.btnSend);
		etMessage = (EditText) findViewById(R.id.etMessage);
		lvChat = (ListView) findViewById(R.id.lvChat);
		
		setExit(false);
		fillChatUser();
		chatOnline = new ChatOnline(chatOnlineHandler.setContext(instance));
		txtvUsername.setText(I18nUtil.i18nTogether(instance, uUser.getUserLogname(), uUser.getUserCnname(), uUser.getUserEnname()));
		if(UtilBoss.StrUtil.isEmpty(uUser.getUserId())) dataSource.add(new ChatModel(Type.LEFT, ResUtil.getText(instance, R.string.msg_chat_robot)));
		lvChat.setAdapter(adapter = new AdapterForList<ChatModel>(instance, R.layout.listview_item_chat, dataSource) {

			@Override
			protected void dataInjected(int index, View view, ChatModel data) {
				switch(data.getType()) {
					case LEFT: 
						ViewHolder.get(view, R.id.rlLeft, RelativeLayout.class).setVisibility(View.VISIBLE);
						ViewHolder.get(view, R.id.rlRight, RelativeLayout.class).setVisibility(View.GONE);
						ViewHolder.get(view, R.id.txtvMessageLeft, TextView.class).setText(data.getMsg());
						CodeBoss.ViewCode.userIcon(ViewHolder.get(view, R.id.ivIconLeft, ImageView.class), uUser.getUserId());
						break;
					case RIGHT: 
						ViewHolder.get(view, R.id.rlLeft, RelativeLayout.class).setVisibility(View.GONE);
						ViewHolder.get(view, R.id.rlRight, RelativeLayout.class).setVisibility(View.VISIBLE);
						ViewHolder.get(view, R.id.txtvMessageRight, TextView.class).setText(data.getMsg());
						CodeBoss.ViewCode.userIcon(ViewHolder.get(view, R.id.ivIconRight, ImageView.class), mUser.getUserId());
						break;
				}
				
				if(index % 8 == 0) { //8条记录才显示一次
					ViewHolder.get(view, R.id.txtvTime, TextView.class).setVisibility(View.VISIBLE);
					ViewHolder.get(view, R.id.txtvTime, TextView.class).setText(UtilBoss.DatetimeUtil.formatDate2Str(data.getDatetime(), ConstantsUtil.FORMAT_DATE_DATETIME));
				} else ViewHolder.get(view, R.id.txtvTime, TextView.class).setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void initAction(Bundle savedInstanceState) {
		etMessage.addTextChangedListener(twMessage);
		btnBack.setOnClickListener(onClickListener);
		btnSend.setOnClickListener(onClickListener);
		etMessage.setOnFocusChangeListener(onMessageFocusChangeListener);
	}
	
	private void fillChatUser() { //设置聊天对象
		uUser = (TblUser) getIntent().getSerializableExtra(ChatActivity.FLAG_CHATTER);
		if(UtilBoss.ObjUtil.isNull(uUser)) uUser = CodeBoss.DataCode.robotUser(instance); //机器人
			
		if(CodeBoss.MessCode.accountState(instance) && UtilBoss.ObjUtil.isNotNull(SystemStatus.curAccount)) {
			mUser = new TblUser();
			mUser.setUserId("111");
			mUser.setUserCnname(SystemStatus.curAccount.name);
			mUser.setUserEnname("hnsun");
		}
	}
	
	private void updateChat(ChatModel model) { //当对话时
		dataSource.add(model);
		adapter.notifyDataSetChanged();
		lvChat.setSelection(dataSource.size() - 1);
	}

	private Context instance;
	private TblUser uUser; //对方
	private TblUser mUser; //己方
	private ChatOnline chatOnline; //聊天传送
	private List<ChatModel> dataSource; //对话内容
	private AdapterForList<ChatModel> adapter; //内容适配器
	
	private Button btnBack;
	private Button btnSend;
	private TextView txtvUsername;
	private EditText etMessage;
	private ListView lvChat;
	
	private TextWatcher twMessage = new TextChangeAdapter() {
		
		@Override
		public void afterTextChanged(Editable editable) {
			if(editable.toString().length() > 0) {
				btnSend.setEnabled(true);
				btnSend.setTextColor(ResUtil.getColor(instance, R.color.txtBtnSendOn));
			} else {
				btnSend.setEnabled(false);
				btnSend.setTextColor(ResUtil.getColor(instance, R.color.txtBtnSendNot));
			}
		}
	};
	
	private OnFocusChangeListener onMessageFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View view, boolean hasFocus) {
			if(hasFocus) lvChat.setSelection(dataSource.size() - 1); //最后一行
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
				case R.id.btnBack: ((ChatActivity) instance).finish(); break;
				case R.id.btnSend: 
					String msg = etMessage.getText().toString();
					updateChat(new ChatModel(Type.RIGHT, msg));
					etMessage.setText("");
					KeyBoardUtil.closeKeybord(instance, etMessage);
					
					if(UtilBoss.StrUtil.isEmpty(uUser.getUserId())) chatOnline.robot(msg);
					else { //具体人聊天
						
					}
					break;
			}
		}
	};
	
	private static ContextHandler chatOnlineHandler = new ContextHandler() { //聊天线上主线程
		
		@Override
		public void handleMessage(Message msg) {
			NetModel model = (NetModel) msg.obj;
			switch(msg.what) {
				case ChatOnline.FROM_ROBOT:
					if(model.isRetIs()) {
						ChatModel.RobotResult result = model.getRetObjJsonObj(ChatModel.RobotResult.class);
						((ChatActivity) getContext()).updateChat(new ChatModel(Type.LEFT, result.getText()));
					}
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	public static final String FLAG_CHATTER = "FLAG_CHATTER";
	
	{
		this.instance = ChatActivity.this;
		this.dataSource = new ArrayList<ChatModel>();
	}
}
