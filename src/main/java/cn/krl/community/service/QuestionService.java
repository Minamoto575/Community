package cn.krl.community.service;

import cn.krl.community.dto.PaginationDTO;
import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.mapper.QuestionMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.Question;
import cn.krl.community.model.User;
import org.apache.ibatis.jdbc.Null;
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

    public PaginationDTO list(Integer page, Integer size){

        //问题记录数
        Integer totalCount = questionMapper.selectCount(null);
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法判断
        if(page<1){ page = 1;}
        if(page>totalPage){page=totalPage;}

        Integer offset =size*(page-1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questions = questionMapper.list(offset,size);
        PaginationDTO pagination = new PaginationDTO();

        //一次封装成questionDTO
        for(Question question:questions){
            User user =userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制
            BeanUtils.copyProperties(question,questionDTO);
            //添加User
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //二次封装成paginationDTO
        pagination.setQuestions(questionDTOList);
        pagination.setPagination(totalPage,page,size);

        return pagination;
    }

    public PaginationDTO list(Integer userId,Integer page, Integer size){

        //问题记录数
        Integer totalCount = questionMapper.selectCountByUserId(userId);
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法判断
        if(page>totalPage){page=totalPage;}
        if(page<1){ page = 1;}

        Integer offset =size*(page-1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        PaginationDTO pagination = new PaginationDTO();

        //一次封装成questionDTO
        for(Question question:questions){
            User user =userMapper.selectById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制
            BeanUtils.copyProperties(question,questionDTO);
            //添加User
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //二次封装成paginationDTO
        pagination.setQuestions(questionDTOList);
        pagination.setPagination(totalPage,page,size);

        return pagination;

    }

}
