package top.mxzero.httpclient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import top.mxzero.common.dto.RestData;
import top.mxzero.httpclient.dto.request.LoginRequestDTO;
import top.mxzero.httpclient.dto.response.LoginResponseDTO;
import top.mxzero.httpclient.dto.response.UserinfoDTO;

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
