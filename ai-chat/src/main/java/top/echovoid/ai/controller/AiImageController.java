package top.echovoid.ai.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author Penn Collins
 * @since 2025/3/15
 */
@AllArgsConstructor
@RestController
public class AiImageController {
    private final OpenAiImageModel model;
    private final RestTemplate restTemplate;

    @RequestMapping("/ai/image")
    public String imageGenaApi(@RequestParam("desc") String prompt) {
        ImageResponse response = this.model.call(new ImagePrompt(prompt));
        return response.getResult().getOutput().getUrl();
    }

    @RequestMapping("/ai/image/resource")
    public ResponseEntity<byte[]> imageGenaWithBase64Api(@RequestParam("desc") String prompt) {
        ImageResponse response = this.model.call(new ImagePrompt(prompt));
        String imageUrl = response.getResult().getOutput().getUrl();
        ResponseEntity<byte[]> remoteResponse = restTemplate.exchange(
                URI.create(imageUrl),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                byte[].class
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(
                remoteResponse.getBody(),
                headers,
                HttpStatus.OK
        );
    }
}
