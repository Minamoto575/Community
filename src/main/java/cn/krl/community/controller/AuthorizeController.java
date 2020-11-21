package cn.krl.community.controller;

import cn.krl.community.dto.AccessTokenDTO;
import cn.krl.community.dto.GithubUserDTO;
import cn.krl.community.model.User;
import cn.krl.community.provider.GithubProvider;
import cn.krl.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private UserService userService;

    /**
     * github第三方登录
     * @param code
     * @param state
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //获取token
        String token = githubProvider.getAccessToken(accessTokenDTO);
        //获取User信息
        GithubUserDTO githubUser= githubProvider.getUser(token);
        // System.out.println(githubUser);

        if (githubUser != null){
            //登陆成功
            User user =new User();
            user.setAccountId(githubUser.getId());
            user.setName(githubUser.getName());
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatar_url());
            String token1 = UUID.randomUUID().toString();
            user.setToken(token1);
            //添加cookie
            response.addCookie(new Cookie("token",token1));
            userService.createOrUpdate(user);
            return "redirect:/";
        }
        else {
            //登录失败
            return "redirect:/";
        }
    }

    /**
     * 退出登录
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpServletRequest request){

        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        //立即传入
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
