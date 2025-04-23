package top.mxzero.tts.translate.service;

/**
 * 文本转音频服务
 *
 * @author Peng
 * @since 2025/4/23
 */
public interface TTSService {
    /**
     * 文本转音频，返回字节数据
     */
    byte[] translateByBytes(String text);

    /**
     * 文本转音频，返回Base64字符串
     */
    String translateByBase64(String text);

    /**
     * 文本转音频，返回URL字符串
     */
    String translateByAccessUrl(String text);
}
