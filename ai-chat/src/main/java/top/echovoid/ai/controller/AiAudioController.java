package top.echovoid.ai.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Peng
 * @since 2025/3/15
 */
@RestController
@AllArgsConstructor
public class AiAudioController {
    private final OpenAiAudioSpeechModel speechModel;

    @RequestMapping("/ai/audio/speech")
    public ResponseEntity<byte[]> speechApi(@RequestParam("content") String content) {
        byte[] audioBytes = this.speechModel.call(content);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audioBytes);
    }
}

