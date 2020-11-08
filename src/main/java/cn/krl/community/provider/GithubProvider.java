package cn.krl.community.provider;

import cn.krl.community.dto.AccessTokenDTO;
import cn.krl.community.dto.GithubUserDTO;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;


@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://github.com/login/oauth/access_token";

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public GithubUserDTO getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        System.out.println(request);
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //string转换为java对象
            GithubUserDTO githubUserDTO =JSON.parseObject(string,GithubUserDTO.class);
            return githubUserDTO;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
