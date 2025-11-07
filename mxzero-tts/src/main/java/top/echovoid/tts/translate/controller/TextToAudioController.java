package top.echovoid.tts.translate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.utils.DateUtil;
import top.echovoid.tts.translate.service.TTSService;

import java.util.Date;

/**
 * @author Peng
 * @since 2025/4/23
 */
@RestController
@AllArgsConstructor
public class TextToAudioController {
    private final TTSService ttsService;

    @RequestMapping("/tts")
    public ResponseEntity<byte[]> textToAudioApi(@RequestParam("text") String text) {
        byte[] bytes = this.ttsService.translateByBytes(text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(bytes.length);
        headers.set("Content-Disposition", "inline; filename=\"" + DateUtil.formatNumber(new Date()) + ".mp3\"");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
