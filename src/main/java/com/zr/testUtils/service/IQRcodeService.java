package com.zr.testUtils.service;

import java.io.IOException;

import com.google.zxing.WriterException;
/**
 * 二维码生成service接口
 * @author zhurun
 * @date 2018-8-27
 */
public interface IQRcodeService {
	/**
	 * 生成二维码
	 * @author z_r
	 * @date 2018-8-27
	 * @param parameter : 需要封装在二维码里的内容(可自定义任意参数)
	 * @return
	 * @throws IOException
	 * @throws WriterException
	 */
	String refreshQRcode(String parameter) throws IOException, WriterException;
	
}
