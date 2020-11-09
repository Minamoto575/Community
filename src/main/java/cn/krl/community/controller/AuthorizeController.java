package cn.krl.community.controller;

import cn.krl.community.dto.AccessTokenDTO;
import cn.krl.community.dto.GithubUserDTO;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.User;
import cn.krl.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

@Controller
/**
 * 登录认证控制器
 */
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
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClientId(clientId);
        accessTokenDTO.setClientSecret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirectUri(redirectUri);
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser= githubProvider.getUser(token);
        System.out.println(githubUser);

        if (githubUser != null){
            //登陆成功
            User user =new User();
            user.setAccount_id(githubUser.getId());
            user.setName(githubUser.getName());
            user.setBio(githubUser.getBio());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            user.setAvatar_url(githubUser.getAvatar_url());
            String token1 = UUID.randomUUID().toString();
            user.setToken(token1);
            //添加cookie
            response.addCookie(new Cookie("token",token1));
            userMapper.insert(user);
            return "redirect:/";
        }
        else {
            //登录失败
            return "redirect:/";
        }
    }
}
