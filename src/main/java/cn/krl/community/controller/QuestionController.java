package cn.krl.community.controller;

import cn.krl.community.dto.CommentDTO;
import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.enums.CommentTypeEnum;
import cn.krl.community.model.Comment;
import cn.krl.community.model.Question;
import cn.krl.community.service.CommentService;
import cn.krl.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/11/15,16:32
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    //问题详细页展示
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model){

        //获取该问题
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);

        //获取该问题的评论
        List<CommentDTO> comments = commentService.listByQuestionOrCommentId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("comments",comments);

        //获取该问题的相关问题
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";

    }
}
