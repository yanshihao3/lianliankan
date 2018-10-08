package com.mh.view;

public interface OnToolsChangeListener {

	 static final int TOOL_SUCCESS = 0;
	 static final int TOOL_FAIL = 1;
	 static final int TOOL_USED_UP = 2;

	/**
	 * 打乱水果顺序时调用的方法
	 * @param leftNum 剩余次数
	 * @param state 状态
	 */
	 void onDisruptChange(int leftNum, int state);
	/**
	 * 炸弹
	 * @param leftNum
	 * @param state 0表示成功查找，1表示查找失败，2表示次数已用完
	 */
	 void onTipChange(int leftNum, int state);

	/**
	 * 帮助
	 * @param leftNum
	 * @param state 0表示成功查找，1表示查找失败，2表示次数已用完
	 */
	 void onChange(int leftNum, int state);/**

	 * 智能提示冻结效果
	 * @param leftNum
	 * @param state 0表示成功，1表示失败，2表示次数已用完
	 */
	 void onFreeze(int leftNum, int state);
}
