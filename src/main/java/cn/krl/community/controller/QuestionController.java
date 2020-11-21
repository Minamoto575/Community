package cn.krl.community.controller;

import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Author:Minamoto
 * Date:2020/11/15,16:32
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    //问题详细页展示
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model){

        QuestionDTO questionDTO = questionService.getQuestionById(id);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";

    }
}
