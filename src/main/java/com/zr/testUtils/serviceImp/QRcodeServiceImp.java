package com.zr.testUtils.serviceImp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;
import com.zr.testUtils.service.IQRcodeService;
import com.zr.testUtils.utils.LogoConfig;
import com.zr.testUtils.utils.ZXingCodeUtil;
import com.zr.testUtils.utils.ZXingConfig;

/**
 * 二维码生成service 实现类
 * @author @author z_
 * @date 2018-8-27
 */
@Service
public class QRcodeServiceImp implements IQRcodeService{
	/**
	 * 生成二维码
	 * @author @author z_
	 * @date 2018-8-27
	 * @param parameter：需要封装在二维码里的参数(可自定义任意参数)
	 * @return
	 * @throws IOException
	 * @throws WriterException
	 */ 
//	@Override
	public String refreshQRcode(String parameter) throws IOException, WriterException {
		String content = "https://www.baidu.com/" + parameter; //扫面二维码访问的接口地址
		String format = "jpg";// 图像类型
		String url = null; //返回二维码的url
		String logo = "logo.png"; //如果要在二维码处设置logo,可在此处定义logo名字,logo需放在项目目录下，若是网络图片或自定义路径，根据要求自行修改
        try {
            // 生成二维码
            ZXingCodeUtil zc = new ZXingCodeUtil(); 		// 实例化二维码工具
            ZXingConfig zxingconfig = new ZXingConfig();    // 实例化二维码配置参数
            zxingconfig.setHints(zc.getDecodeHintType());   // 设置二维码的格式参数
            zxingconfig.setContent(content);				// 设置二维码生成内容
            zxingconfig.setLogoConfig(new LogoConfig());    // Logo图片参数设置   
            BufferedImage bim = zc.getQRCodeBufferedImage(zxingconfig,logo);// 调用生成二维码方法
            File file = File.createTempFile("qrcode", ".jpg");
            OutputStream out = null ; // 准备好一个输出的对象
            out = new FileOutputStream(file);
            ImageIO.write(bim, format, out);    // 图片写出
            InputStream in = new FileInputStream(file);
            /** 以下是将生成的二维码存放在本地,实际项目中若要放在 数据库或服务器，
             * 删除以下代码，自己写文件上传接口，再调用**/
            byte[] data = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = null;
            try {
            	//二维码生成存放地址与名称，
	            fileOutputStream = new FileOutputStream("D:\\test1.jpg");
	            while ((len = in.read(data)) != -1) {
	            	fileOutputStream.write(data, 0, len);
	            }
            }catch (IOException e) {
            	e.printStackTrace();
            } finally {

            	if (in != null) {
            		try {
            			in.close();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            	}
            	if (fileOutputStream != null) {
            		try {
            			fileOutputStream.close();
            		} catch (IOException e) {
            			e.printStackTrace();
            		}
            	}
            } 
            /**end**/
            
            /** 若要上传到服务器或数据库 放开以下代码，调用自己写的文件上传接口，返回地址给url就行了**/
            //若要把图片上传到第三方服务器，如阿里云，腾讯云，可自行封装上传方法，此处调用，返回url就可以了
            //url = null; ossUtil.uploadFile(in); //上传图片文件
            /**end**/
            out.close();
    		in.close();
    		file.delete();
//            Thread.sleep(500);  // 缓冲
//            zp.parseQR_CODEImage(bim);  // 解析调用
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
	}
}
