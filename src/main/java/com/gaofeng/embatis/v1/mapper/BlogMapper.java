package com.gaofeng.embatis.v1.mapper;


import com.gaofeng.embatis.v2.annotation.Entity;
import com.gaofeng.embatis.v2.annotation.Select;


public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */

    Blog selectBlogById(Integer bid);

}
