package top.echovoid.httpclient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import top.echovoid.common.dto.RestData;
import top.echovoid.httpclient.dto.request.LoginRequestDTO;
import top.echovoid.httpclient.dto.response.LoginResponseDTO;
import top.echovoid.httpclient.dto.response.UserinfoDTO;

/**
 * @author Peng
 * @since 2025/5/9
 */
public interface LoginServiceApi {
    @POST("/token/create")
    Call<RestData<LoginResponseDTO>> login(@Body LoginRequestDTO requestDTO);

    @GET("/api/userinfo")
    Call<RestData<UserinfoDTO>> getUserinfo(@Header("Authorization") String authToken);
}
