package top.echovoid.intelligent.chat.service;

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

/**
 * @author Penn Collins
 * @since 2025/6/27
 */
@Service
public class SpeechService {
    private static final String API_KEY = "sk-fdeb5cb9b141473ca196615ed8537df0";
    private static final String voice = "longfeifei_v2";
    private static final String ttsModel = "cosyvoice-v2";

    public byte[] speech(String text) {
        SpeechSynthesisParam param =
                SpeechSynthesisParam.builder()
                        .apiKey(API_KEY)
                        .model(ttsModel)
                        .voice(voice)
                        .build();
        SpeechSynthesizer synthesizer = new SpeechSynthesizer(param, null);
        ByteBuffer audio = synthesizer.call(text);
        return audio.array();
    }
}
