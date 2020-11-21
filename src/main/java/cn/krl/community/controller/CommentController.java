package cn.krl.community.controller;

import cn.krl.community.dto.CommentCreateDTO;
import cn.krl.community.dto.CommentDTO;
import cn.krl.community.dto.ResultDTO;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.mapper.CommentMapper;
import cn.krl.community.model.Comment;
import cn.krl.community.model.User;
import cn.krl.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Author:Minamoto
 * Date:2020/11/19,16:54
 */
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentMapper commentMapper;

    @ResponseBody
    @RequestMapping(value="/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        //未登录
      //  if(user == null){
      //      return new ResultDTO().errorOf(CustomizeErrorCode.NO_LOGIN);
      //  }
        if (commentCreateDTO == null || commentCreateDTO.getContent() == null || "".equals(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment =new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        //comment.setLikeCount(0);
        commentService.insert(comment);
        //评论成功
        return ResultDTO.okOf();
    }
}
