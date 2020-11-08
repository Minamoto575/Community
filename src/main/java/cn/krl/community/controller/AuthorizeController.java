package cn.krl.community.controller;

import cn.krl.community.dto.AccessTokenDTO;
import cn.krl.community.dto.GithubUserDTO;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.User;
import cn.krl.community.provider.GithubProvider;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private  String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser= githubProvider.getUser(token);
        System.out.println(githubUser);

        if (githubUser != null){
            //登陆成功
            request.getSession().setAttribute("user",githubUser);

            User user =new User();
            //user.setId(1);
            user.setAccount_id(githubUser.getId());
            user.setName(githubUser.getName());
            user.setBio(githubUser.getBio());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            user.setToken(UUID.randomUUID().toString());

            userMapper.insert(user);
            System.out.println(user);
            return "redirect:/";

        }
        else {
            //登录失败
            return "redirect:/";
        }
    }
}
