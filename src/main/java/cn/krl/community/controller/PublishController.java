package cn.krl.community.controller;

import cn.krl.community.cache.TagCache;
import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.model.Question;
import cn.krl.community.model.User;
import cn.krl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Author:Minamoto
 * Date:2020/11/9,12:19
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    //点击logo 进入发布问题界面
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish" ;
    }

    //问题编辑/问题更新
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                          Model model){
        QuestionDTO question = questionService.getQuestionById(id);
        //用于信息复原
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        //id传给前端，再传入doPublish做判断
        model.addAttribute("id",id);
        model.addAttribute("tags", TagCache.get());
        return "publish" ;
    }

    //问题发布
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam(value = "id",required = false)Integer id, //编辑页传入的问题id
                            HttpServletRequest request,
                            Model model){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //用于前端复原
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());

        //内容为空时进行提示
        if (title == null || title.equals("")) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);

        //创建或者更新问题
        questionService.creatOrUpdate(question);

        return "redirect:/";
    }


}
