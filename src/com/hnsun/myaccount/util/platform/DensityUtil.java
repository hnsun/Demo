package com.hnsun.myaccount.util.platform;

import android.content.Context;
import android.util.TypedValue;

import com.hnsun.myaccount.util.UtilBoss;

/**
 * 单位转换相关（摘抄）
 * @author hnsun
 * @date 2016/08/19
 */
public class DensityUtil {

    private DensityUtil() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
		UtilBoss.ConditionUtil.n(context);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
		UtilBoss.ConditionUtil.n(context);
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param context
     * @param px
     * @return
     */
    public static float px2dp(Context context, float px) {
		UtilBoss.ConditionUtil.n(context);
        return (px / context.getResources().getDisplayMetrics().density);
    }

    /**
     * px转sp
     * @param context
     * @param px
     * @return
     */
    public static float px2sp(Context context, float px) {
		UtilBoss.ConditionUtil.n(context);
        return (px / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
