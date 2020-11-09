package cn.krl.community.service;

import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.mapper.QuestionMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.Question;
import cn.krl.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/11/9,16:09
 */
@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> list(){

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questions;
        questions = questionMapper.selectList(null);
        for(Question question:questions){
            User user =userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制
            BeanUtils.copyProperties(question,questionDTO);
            //添加User
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }

}
