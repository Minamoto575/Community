package cn.krl.community.service;

import cn.krl.community.dto.NotificationDTO;
import cn.krl.community.dto.PaginationDTO;
import cn.krl.community.enums.NotificationStatusEnum;
import cn.krl.community.enums.NotificationTypeEnum;
import cn.krl.community.exception.CustomizeErrorCode;
import cn.krl.community.exception.CustomizeException;
import cn.krl.community.mapper.NotificationMapper;
import cn.krl.community.mapper.UserMapper;
import cn.krl.community.model.Notification;
import cn.krl.community.model.NotificationExample;
import cn.krl.community.model.QuestionExample;
import cn.krl.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/12/3,14:35
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;

    //列出通知
    public PaginationDTO list(Integer userId, Integer page, Integer size) {

        //拿到某用户收到通知的总数
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(example);

        //页面总数
        int totalPage = (int) Math.ceil((double)totalCount/size);
        //合法性判断
        if(page>totalPage) page=totalPage;
        if(page<1)  page = 1;
        int offset = size * (page - 1);

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        //将notification封装为notificationDTO
        if (notifications.isEmpty()) {
            return paginationDTO;
        }
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        //将该页的通知列表放入页信息对象中，二次封装
        paginationDTO.setData(notificationDTOS);

        //在PaginationDTO类的逻辑中设置一些其他的信息
        paginationDTO.setPagination(totalPage, page, size);

        return paginationDTO;
    }

    //未读通知计数
    public Long unreadCount(Integer userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    //标记通知已读
    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //异常处理
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver() != user.getId()) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //设置为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        //设置通知类型
        notificationDTO.setTypeName(NotificationTypeEnum.nameOf(notification.getType()));
        return notificationDTO;
    }

}
