package top.echovoid.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Penn Collins
 * @since 2024/12/7
 */
public class QRCodeUtil {

    /**
     * 生成二维码并返回字节数组
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return 二维码的字节数组
     */
    public static byte[] generateQRCode(String content, int width, int height) {
        try {
            // 配置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // 转换为字节数组

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    public static byte[] generateQRCode(String content) {
        return generateQRCode(content, 300, 300);
    }
}