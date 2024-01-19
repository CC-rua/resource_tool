package com.test;

public class TestNum {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(isNum("6.01500089E8"));
	}

	/**
	 * 是否为数字
	 * @param str
	 * @return
	 */
	private static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
}
