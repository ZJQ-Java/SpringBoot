package com.qiu.dao.mysql.mapper;

import com.qiu.entity.mysql.Book;
import com.qiu.entity.mysql.BookExample;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    @Delete({
        "delete from book",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into book (book_name, book_counts, ",
        "detail)",
        "values (#{bookName,jdbcType=VARCHAR}, #{bookCounts,jdbcType=INTEGER}, ",
        "#{detail,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Book record);

    int insertSelective(Book record);

    List<Book> selectByExample(BookExample example);

    @Select({
        "select",
        "id, book_name, book_counts, detail",
        "from book",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.qiu.dao.mysql.mapper.BookMapper.BaseResultMap")
    Book selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Book record, @Param("example") BookExample example);

    int updateByExample(@Param("record") Book record, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book record);

    @Update({
        "update book",
        "set book_name = #{bookName,jdbcType=VARCHAR},",
          "book_counts = #{bookCounts,jdbcType=INTEGER},",
          "detail = #{detail,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Book record);

    int updateBatch(@Param("record") Book record, @Param("ids")List<Integer> ids);
}