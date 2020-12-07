package cn.krl.community.service;

import cn.krl.community.dto.PaginationDTO;
import cn.krl.community.dto.QuestionDTO;
import cn.krl.community.dto.QuestionQueryDTO;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.exception.CustomizeException;
import cn.krl.community.mapper.QuestionExtMapper;
import cn.krl.community.mapper.QuestionMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.Question;
import cn.krl.community.model.QuestionExample;
import cn.krl.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 首页问题或者搜索问题的分页获取
     * @param page  当前页码
     * @param size  页面大小
     * @return
     */
    public PaginationDTO list(String search,Integer page, Integer size){
        //搜索表达式
        if (StringUtils.isNotBlank(search)) {
            String regex = StringUtils.replace(search, " ", "|");
            search = regex;
        } else {
            search = null;
        }

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setSize(size);

        //问题记录数
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法性判断
        if(page>totalPage) page=totalPage;
        if(page<1)  page = 1;

        //起始地址 即第几个记录
        Integer offset =size*(page-1);
        questionQueryDTO.setOffset(offset);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        QuestionExample example = new QuestionExample();
        //创建时间倒序排放
        example.setOrderByClause("gmt_create desc");
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        PaginationDTO pagination = new PaginationDTO();

        //一次封装成questionDTO
        for(Question question:questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制
            BeanUtils.copyProperties(question,questionDTO);
            //添加User
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        //二次封装成paginationDTO
        pagination.setData(questionDTOList);
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
        example.createCriteria().andCreatorEqualTo(userId);

        //获取问题总数
        Integer totalCount = (int) questionMapper.countByExample(example);
        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法性判断
        if(page>totalPage) page=totalPage;
        if(page<1)  page = 1;
        Integer offset =size*(page-1);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //获取某个用户所有问题
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
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
        pagination.setData(questionDTOList);
        pagination.setPagination(totalPage,page,size);

        return pagination;
    }

    //问题详情信息获取
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

    //创建或者更新问题
    public void creatOrUpdate(Question question) {

        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setGmtModified(System.currentTimeMillis());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            //这一步更新问题时，有可能更新问题在数据库中不存在，会抛出异常
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

//            //更新
//            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
//            //问题不存在
//            if (dbQuestion==null)
//                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
//            //更新问题的内容以及修改时间
//            dbQuestion.setTag(question.getTag());
//            dbQuestion.setDescription(question.getDescription());
//            dbQuestion.setTitle(question.getTitle());
//            dbQuestion.setGmtModified(System.currentTimeMillis());
//            questionMapper.updateByPrimaryKey(dbQuestion);
        }
    }

    //阅读数+1
    public void incView(Integer id){
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //获取相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }
        //正则表达式
        String regexTag = StringUtils.replace(questionDTO.getTag(), ",", "|");
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexTag);
        List<Question> questions = questionExtMapper.selectRelated(question);

        List<QuestionDTO> questionDTOs = questions.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOs;
    }
}
