package cn.smallc.pagemaker.repository;

import cn.smallc.crawlercollection.common.db.IRepository;
import cn.smallc.crawlercollection.entity.PageNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IPageRepository extends IRepository<PageNews>{

//    int transfer(@Param("money") double money, @Param("id") int id);
    List<PageNews> getByCts(@Param("startCts") Long startCts,@Param("endCts") Long endCts);



}
