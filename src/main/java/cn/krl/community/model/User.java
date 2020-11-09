package cn.krl.community.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Author:Minamoto
 * Date:2020/11/8,10:56
 */
@Data
public class User {
    //mybatisPlus 需要额外注释主键自增
    @TableId(type= IdType.AUTO)
    private Integer id;
    private Long account_id;
    private String name;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String  bio;
    private String avatar_url;
}
