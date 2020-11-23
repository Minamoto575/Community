package cn.krl.community.service;

import cn.krl.community.dto.CommentDTO;
import cn.krl.community.enums.CommentTypeEnum;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.exception.CustomizeException;
import cn.krl.community.mapper.CommentMapper;
import cn.krl.community.mapper.QuestionExtMapper;
import cn.krl.community.mapper.QuestionMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author:Minamoto
 * Date:2020/11/19,17:05
 */
@Service
public class CommentService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;

    //事务处理
    @Transactional
    public void insert(Comment comment) {
        //评论对象不存在
        if(comment.getParentId()==null||comment.getParentId()==0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //评论类型不存在
        if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        //正常插入
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment= commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

        }else{
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion ==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            dbQuestion.setCommentCount(1);
            questionExtMapper.incComment(dbQuestion);
        }
    }

    //根据id获取评论，包括一级评论与二级评论
    public List<CommentDTO> listByQuestionOrCommentId(Integer id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        //时间倒序
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.isEmpty()) {
            return new ArrayList<>();
        }
        //获取去重的评论者
        List<Integer> userIds = comments.stream()
                .map(comment -> comment.getCommentator())
                .distinct()
                .collect(Collectors.toList());
        //根据评论人id拿到所有评论用户
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        //构造userId:User的哈希表
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //构造CommentDTO对象
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
