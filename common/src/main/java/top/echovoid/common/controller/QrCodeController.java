package top.echovoid.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.echovoid.common.utils.QRCodeUtil;

/**
 * @author Penn Collins
 * @since 2024/12/7
 */
@RestController
public class QrCodeController {
    @RequestMapping("/public/qrcode")
    public ResponseEntity<byte[]> generateQRCode(
            @RequestParam String data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
        return new ResponseEntity<>(QRCodeUtil.generateQRCode(data), headers, HttpStatus.OK);
    }
}
