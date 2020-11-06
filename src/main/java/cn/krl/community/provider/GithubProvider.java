package cn.krl.community.provider;

import cn.krl.community.dto.AccessTokenDTO;
import cn.krl.community.dto.GithubUserDTO;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;


@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO acccessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(acccessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public GithubUserDTO getUser(String accessToken){
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //string转换为java对象
            GithubUserDTO githubUserDTO =JSON.parseObject(string,GithubUserDTO.class);
            System.out.println(githubUserDTO.getBio());
            return githubUserDTO;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
