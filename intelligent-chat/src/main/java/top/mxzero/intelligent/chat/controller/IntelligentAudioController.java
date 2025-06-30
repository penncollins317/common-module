package top.mxzero.intelligent.chat.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.mxzero.intelligent.chat.service.SpeechService;

/**
 * AI音频
 *
 * @author Peng
 * @since 2025/6/27
 */
@Slf4j
@Controller
@CrossOrigin("*")
@AllArgsConstructor
public class IntelligentAudioController {
    private final SpeechService speechService;
    private final OpenAiAudioSpeechModel audioModel;

    /**
     * 音频合成
     *
     * @param text 文字
     * @return 音频流
     */
    @GetMapping(value = "/intelligent/speech", produces = "audio/mpeg")
    public ResponseEntity<byte[]> speechApi(
            @RequestParam("text") String text
    ) {
        byte[] audioBytes = this.speechService.speech(text);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audioBytes);
    }


}
