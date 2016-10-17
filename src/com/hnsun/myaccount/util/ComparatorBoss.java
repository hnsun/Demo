package com.hnsun.myaccount.util;

import java.util.Comparator;

import com.hnsun.myaccount.usb.SortUsb;

/**
 * 比较帮助类
 * @author hnsun
 * @date 2016/09/24
 * 类型：SortComparator、
 */
public class ComparatorBoss {

	private ComparatorBoss() { UtilBoss.ExceptionUtil.throwUnsupportedOperationInit(); }

    /*************
     *****
     **        排序比较 ：SortComparator
     **
     *****
     *******************************************************/
	public static class SortComparator implements Comparator<SortUsb> {

		@Override
		public int compare(SortUsb model1, SortUsb model2) {
			if(model1.getSort() == '@' || model2.getSort() == '#') {
				return -1;
			} else if(model1.getSort() == '#' || model2.getSort() == '@') {
				return 1;
			} else {
				return model1.getSort() - model2.getSort();
			}
		}
		
	}
}
