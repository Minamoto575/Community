package cn.krl.community.controller;

import cn.krl.community.dto.PaginationDTO;
import cn.krl.community.model.User;
import cn.krl.community.service.NotificationService;
import cn.krl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:Minamoto
 * Date:2020/11/14,20:53
 */
@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    /**
     * 用户个人中心
     * @param action  我的问题 or 通知
     * @param model
     * @param request
     * @param page   当前页码
     * @param size   页面大小
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){
        User user = (User) request.getSession().getAttribute("user");

        if(user ==null){
            System.out.println("未登录");
            return "redirect:/";
        }
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
            //获取我的问题
            PaginationDTO pagination = questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",pagination);
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName", "最新回复");
            //获取通知
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }
        return "profile";

    }
}
