package top.echovoid.httpclient;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.echovoid.common.dto.RestData;
import top.echovoid.httpclient.dto.request.LoginRequestDTO;
import top.echovoid.httpclient.dto.response.LoginResponseDTO;
import top.echovoid.httpclient.dto.response.UserinfoDTO;

import java.io.IOException;

/**
 * @author Peng
 * @since 2025/5/9
 */
public class LoginCallTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginCallTest.class);

    @Test
    public void loginTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        LoginServiceApi service = retrofit.create(LoginServiceApi.class);

        Call<RestData<LoginResponseDTO>> call = service.login(new LoginRequestDTO("admin", "admin"));

        Response<RestData<LoginResponseDTO>> response = call.execute(); // 同步调用
        if (response.isSuccessful()) {
            LoginResponseDTO data = response.body().getData();
            LOGGER.info("data:{}", data);
            Call<RestData<UserinfoDTO>> userinfo = service.getUserinfo("Bearer " + response.body().getData().getAccessToken());
            Response<RestData<UserinfoDTO>> userinfoRes = userinfo.execute();
            if (userinfoRes.isSuccessful()) {
                LOGGER.info("userinfo:{}", userinfoRes.body().getData());
            }
        } else {
            LOGGER.error("请求失败: {}", response.code());
        }
    }
}

