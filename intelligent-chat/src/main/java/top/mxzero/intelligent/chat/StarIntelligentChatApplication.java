package top.mxzero.intelligent.chat;

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import com.alibaba.dashscope.utils.JsonUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Peng
 * @since 2025/6/27
 */
@SpringBootApplication
public class StarIntelligentChatApplication {
    private static final String API_KEY = "sk-fdeb5cb9b141473ca196615ed8537df0";
    private static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    private static String voice = "longwan_v2";
    private static String ttsModel = "cosyvoice-v2";

    public static void call() throws Exception {
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .model("qwen-tts")
                .apiKey(API_KEY)
                .text("Today is a wonderful day to build something people love!")
                .voice(AudioParameters.Voice.CHERRY)
                .build();
        MultiModalConversationResult result = conv.call(param);
        System.out.println(JsonUtils.toJson(result));
    }

    private static void spead() {
        // 请求参数
        SpeechSynthesisParam param =
                SpeechSynthesisParam.builder()
                        .apiKey(API_KEY)
                        .model(ttsModel) // 模型
                        .voice(voice) // 音色
                        .build();

        // 同步模式：禁用回调（第二个参数为null）
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
        // 阻塞直至音频返回
        ByteBuffer audio = synthesizer.call("你好，小智");
        // 将音频数据保存到本地文件“output.mp3”中
        File file = new File("D:" + File.separator + "output.mp3");
        System.out.println(
                "[Metric] requestId为："
                        + synthesizer.getLastRequestId()
                        + "首包延迟（毫秒）为："
                        + synthesizer.getFirstPackageDelay());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StarIntelligentChatApplication.class, args);
    }
}
