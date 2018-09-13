package com.zr.testUtils.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * 二维码生成google zxing
 * @author zhurun
 * @date 2018-8-27
 */
public class ZXingCodeUtil {
    /**
     * 二维码图片添加Logo
     * @author zhurun
     * @date 2018-8-27
     * @param bim 图片流
     * @param logoConfig Logo图片设置参数
     * @param logoParameter： logo图片名
     * @throws Exception 异常上抛
     */
    private void addLogoQRCode(BufferedImage bim, LogoConfig logoConfig,String logoParameter) throws Exception {
        try {
            BufferedImage image = bim; // 对象流传输
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            BufferedImage logo = ImageIO.read(ZXingCodeUtil.class.getResource(logoParameter));
            // 设置logo的大小,本人设置为二维码图片的20%,因为过大会盖掉二维码
            int widthLogo = logo.getWidth(null) > image.getWidth() * 2 / 10 ? (image.getWidth() * 2 / 10) : logo.getWidth(null), heightLogo = logo
                    .getHeight(null) > image.getHeight() * 2 / 10 ? (image.getHeight() * 2 / 10) : logo.getWidth(null);

            // 计算图片放置位置  logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            g.setStroke(new BasicStroke(logoConfig.getBorder()));
            g.setColor(logoConfig.getBorderColor());
            g.drawRect(x, y, widthLogo, heightLogo);
            g.dispose();
            logo.flush();
            image.flush();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 二维码的解析
     * @author zhurun
     * @date 2018-8-27
     * @param image  图片文件流
     * @return 解析后的Result结果集
     * @throws Exception 错误异常上抛
     */
    @SuppressWarnings("unchecked")
    public Result parseQR_CODEImage(BufferedImage image) throws Exception {
        // 设置解析
        Result result = null;
        try {
            MultiFormatReader formatReader = new MultiFormatReader();

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            result = formatReader.decode(binaryBitmap, hints);

            System.out.println("resultFormat = " + result.getBarcodeFormat());
            System.out.println("resultText = " + result.getText());
        } catch (Exception e) {
            throw e;
        }
        return result;
    }

    /**
     * 生成二维码bufferedImage图片
     * @author zhurun
     * @date 2018-8-27
     * @param zxingconfig 二维码配置信息
     * @return 生成后的 BufferedImage
     * @throws Exception 异常上抛
     */
    public BufferedImage getQRCodeBufferedImage(ZXingConfig zxingconfig,String logo) throws Exception {
        // Google配置文件
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(zxingconfig.getContent(), zxingconfig.getBarcodeformat(), zxingconfig.getWidth(), zxingconfig.getHeight(),
                    zxingconfig.getHints());
            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            // 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            // 是否设置Logo图片
            if (logo != null && !"".equals(logo.trim())) {
            	System.out.println("123123");
                this.addLogoQRCode(image, zxingconfig.getLogoConfig(),logo);
            }
        } catch (WriterException e) {
            throw e;
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     * @author zhurun
     * @date 2018-8-27 
     * @return
     */
    @SuppressWarnings("deprecation")
	public Map<EncodeHintType, Object> getDecodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);

        return hints;
    }

}
