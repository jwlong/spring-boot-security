package org.thinkingingis.utils;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

public class QRCodeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeUtils.class);

    //编码格式
    private static final String CHARSET = "utf-8";
    //生成的qrCode的图片格式
    private static final String FORMAT_NAME = "png";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 350;
    // LOGO高度
    private static final int HEIGHT = 350;

    // 条形码宽度
    private static final int BAR_WIDTH = 240;

    // 条形码高度
    private static final int BAR_HEIGHT = 80;

    private static BufferedImage createImage(String content, String imgPath,
                                             boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        // 容错级别最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 二维码空白区域最小为0也有白边， 只是很小，最小是6px左右
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.QR_VERSION, 2);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        // 删除白边
        BufferedImage image = deleteWhite(bitMatrix);

        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }

        // 插入图片
        QRCodeUtils.insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     * 去掉二维码白边.
     *
     * @param matrix BitMatrix
     * @return BitMatrix
     */
    private static BufferedImage deleteWhite(BitMatrix matrix) {
        // 获取二维码实际尺寸(去掉二维码两边空白)
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                resMatrix.set(i, j);
            }
        }

        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }


    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath,
                                    boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static String encode(String content, String imgPath, String destPath,
                                boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999) + ".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
        return file;
    }

    /**
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
     *
     * @param destPath 存放目录
     * @date 2013-12-11 上午10:16:36
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content  内容
     * @param imgPath  LOGO地址
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String imgPath, String destPath)
            throws Exception {
        QRCodeUtils.encode(content, imgPath, destPath, false);
    }

    public static void createEncode(String qrCode, OutputStream output) throws Exception {
        Hashtable hints = new Hashtable();

        //容错级别最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //二维码空白区域,最小为0也有白边,只是很小,最小是6像素左右
        hints.put(EncodeHintType.MARGIN, 1);
        // 1、读取文件转换为字节数组
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCode, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        //转换成png格式的IO流
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content      内容
     * @param destPath     存储地址
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String destPath,
                              boolean needCompress) throws Exception {
        QRCodeUtils.encode(content, null, destPath, needCompress);
    }

    /**
     * 生成二维码
     *
     * @param content  内容
     * @param destPath 存储地址
     * @throws Exception
     */
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtils.encode(content, null, destPath, false);
    }

    /**
     * 生成二维码(内嵌LOGO)
     *
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param output       输出流
     * @param needCompress 是否压缩LOGO
     * @throws Exception
     */
    public static void encode(String content, String imgPath,
                              OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtils.createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param output  输出流
     * @throws Exception
     */
    public static void encode(String content, OutputStream output)
            throws Exception {
        QRCodeUtils.encode(content, null, output, false);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtils.decode(new File(path));
    }

    public static void cratreBarCode(String content, OutputStream outputStream) throws Exception {
        LOGGER.info("createBarCode");
        try {
            BufferedImage image = getCode128(content);
            ImageIO.write(image, FORMAT_NAME, outputStream);
            LOGGER.info("createBarCode ==> successfuly.");
        } catch (Exception e) {
            LOGGER.info("createBarCode ==> faild.");
            throw new Exception(e);
        }
    }

    /**
     *  生成二维码.
     *
     * @param content content
     * @return BufferedImage
     * @throws Exception
     */
    public static BufferedImage getCode128(String content) throws Exception {
        Hashtable hints = new Hashtable();
        // 容错级别最高
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        // 二维码空白区域最小为0也有白边， 只是很小，最小是6px左右
        hints.put(EncodeHintType.MARGIN, 0);

        // 为了无边距，需设置宽度为条码自动生成规则的宽度
        int width = new Code128Writer().encode(content).length;
        // 条码放大倍数
        int codeMultiples = 2;
        //获取条码内容的宽，不含两边距，当EncodeHintType.MARGIN为0时即为条码宽度
        int codeWidth = width * codeMultiples;

        // 图像数据转换，使用了矩阵转换 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
        //  Write Barcode
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, codeWidth, BAR_HEIGHT, hints);

        return  MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static void main(String[] args) throws Exception {
        String text = "202019094913201911151175";  //这里设置自定义网站url
        String logoPath = "C:\\test\\test.jpg";
        String destPath = "C:\\test\\";
        System.out.println(QRCodeUtils.encode(text, logoPath, destPath, true));
    }
}