package com.gaofeng.embatis.v1;

import com.gaofeng.embatis.v1.mapper.Blog;
import com.gaofeng.embatis.v1.mapper.BlogMapper;

public class MeBatisMain {
    public static void main(String[] args) {
        GFSqlSession sqlSession = new GFSqlSession(new GFConfiguration(),new GFExecutor());
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById(1);
        System.out.println(blog.toString());
    }
}
