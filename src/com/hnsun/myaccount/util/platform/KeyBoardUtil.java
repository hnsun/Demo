package com.hnsun.myaccount.util.platform;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 输入设备相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class KeyBoardUtil {

    private KeyBoardUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * 打开软键盘
     * @param editText 输入框
     * @param context  上下文
     */
    public static void openKeybord(Context context, EditText editText) {
		UtilBoss.ConditionUtil.n(context, editText);
		
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * @param editText 输入框
     * @param context  上下文
     */
    public static void closeKeybord(Context context, EditText editText) {
		UtilBoss.ConditionUtil.n(context, editText);
		
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
