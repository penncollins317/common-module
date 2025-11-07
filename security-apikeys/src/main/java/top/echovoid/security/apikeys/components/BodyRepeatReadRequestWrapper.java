package top.echovoid.security.apikeys.components;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Penn Collins
 * @since 2024/10/24
 */
public class BodyRepeatReadRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public BodyRepeatReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException { // 方法覆写
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            private int temp;

            @Override
            public boolean isFinished() {
                return temp != -1;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() throws IOException {
                temp = inputStream.read();
                return temp;
            }
        };
    }
}
