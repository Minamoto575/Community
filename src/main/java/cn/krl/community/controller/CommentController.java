package cn.krl.community.controller;

import cn.krl.community.dto.CommentCreateDTO;
import cn.krl.community.dto.CommentDTO;
import cn.krl.community.dto.ResultDTO;
import cn.krl.community.enums.CommentTypeEnum;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.mapper.CommentMapper;
import cn.krl.community.model.Comment;
import cn.krl.community.model.User;
import cn.krl.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        if(user == null){
            return new ResultDTO().errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //评论DTO为空或者评论内容为空
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment =new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        comment.setCommentCount(0);

        commentService.insert(comment,user);
        //评论成功
        return ResultDTO.okOf();
    }

    //获取二级评论列表的接口
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id) {
        List<CommentDTO> commentDTOS = commentService.listByQuestionOrCommentId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
