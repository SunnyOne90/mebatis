package com.gaofeng.embatis.v2.mapper;


import com.gaofeng.embatis.v2.annotation.Entity;
import com.gaofeng.embatis.v2.annotation.Select;

@Entity(Blog.class)
public interface BlogMapper {
    /**
     * 根据主键查询文章
     * @param bid
     * @return
     */
    @Select("select * from blog where bid = ?")
    Blog selectBlogById(Integer bid);

}
