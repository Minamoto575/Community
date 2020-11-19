package cn.krl.community.service;

import cn.krl.community.dto.PaginationDTO;
import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.exception.CustomizeException;
import cn.krl.community.mapper.QuestionMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.Question;
import cn.krl.community.model.QuestionExample;
import cn.krl.community.model.User;
import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.RowBounds;
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

    /**
     * 首页问题获取
     * @param page  当前页码
     * @param size  页面大小
     * @return
     */
    public PaginationDTO list(Integer page, Integer size){

        //问题记录数
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法性判断
        if(page>totalPage) page=totalPage;
        if(page<1)  page = 1;
        //起始地址 即第几个记录
        Integer offset =size*(page-1);

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        PaginationDTO pagination = new PaginationDTO();

        //一次封装成questionDTO
        for(Question question:questions){
            User user =userMapper.selectByPrimaryKey(question.getCreator());
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

    /**
     * 个人中心问题获取
     * @param userId
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Integer userId,Integer page, Integer size){

        //问题记录数
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(userId);

        Integer totalCount = (int) questionMapper.countByExample(example);
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法性判断
        if(page>totalPage) page=totalPage;
        if(page<1)  page = 1;

        Integer offset =size*(page-1);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        PaginationDTO pagination = new PaginationDTO();

        //一次封装成questionDTO
        for(Question question:questions){
            User user =userMapper.selectByPrimaryKey(question.getCreator());
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

    /**
     * 问题详情页获取
     * @param id
     * @return
     */
    public QuestionDTO getQuestionById(Integer id) {
        QuestionDTO questionDTO = new QuestionDTO();

        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question,questionDTO);
        //添加User
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void creatOrUpdate(Question question) {

        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int result=questionMapper.updateByExampleSelective(updateQuestion, example);
            if (result==0)
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }
}
