package cn.krl.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:Minamoto
 * Date:2020/11/14,16:00
 */
@Data
//分页DTO，包括主页问题展示，个人问题展示，通知展示，都要用到分页
public class PaginationDTO <T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirst;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;   //当前页码
    private List<Integer> pages = new ArrayList<>();    //底部展示页号的数组
    private Integer totalPage;  //页面总数

    public void setPagination(Integer totalPage, Integer page, Integer size) {

        this.totalPage = totalPage;

        this.page = page;

        //确定pages中的页号
        //逻辑：最多向前和向后展示三个数
        //先将当前页加入
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //判断是否有上一页和下一页的标识
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        //判断是否有第一页和最后一页的标识
        if (pages.contains(1)) {
            showFirst = false;
        } else {
            showFirst = true;
        }

        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
