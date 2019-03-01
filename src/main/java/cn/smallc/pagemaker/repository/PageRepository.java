package cn.smallc.pagemaker.repository;

import cn.smallc.crawlercollection.common.db.Repository;
import cn.smallc.crawlercollection.entity.PageNews;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageRepository extends Repository<PageNews,IPageRepository> {

//    @Transactional
//    public void transfer(){
//        repository.transfer(90,1);
////        int i=1/0;
//        repository.transfer(110,2);
//    }

    public List<PageNews> getByCts(Long startCts,Long endCts){
        return getByCts(startCts,endCts);
    }

}
