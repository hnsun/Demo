package com.hnsun.myaccount.util.platform;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 动画帮助类
 * @author hnsun
 * @date 2016/09/24
 */
public class AnimUtil {

    private AnimUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }
    
    /**
     * 一秒晃动多少次
     * @param count
     * @return
     */
    public static Animation shake(int count) {
    	Animation ret = new TranslateAnimation(0, 10, 0, 0);
    	ret.setInterpolator(new CycleInterpolator(count));
    	ret.setDuration(1 * 1000);
    	return ret;
    }
    
    /**
     * 上下移动
     * @return
     */
    public static Animation updown() {
    	Animation ret = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
    	ret.setRepeatCount(-1);
    	ret.setRepeatMode(Animation.RESTART);
    	ret.setDuration(5 * 1000);
    	return ret;
    }
}
