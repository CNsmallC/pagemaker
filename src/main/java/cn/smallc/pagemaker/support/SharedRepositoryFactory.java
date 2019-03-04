package cn.smallc.pagemaker.support;

import cn.smallc.crawlercollection.repository.visited.VisitedRepository;
import cn.smallc.pagemaker.repository.IMGRepository;
import cn.smallc.pagemaker.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SharedRepositoryFactory {



    /**
     * 页面仓储
     */
    private static PageRepository pageRepository;

    /**
     * 图片仓储
     */
    private static IMGRepository imgRepository;

    public static IMGRepository getImgRepository() {
        return imgRepository;
    }

    @Autowired
    public void setImgRepository(IMGRepository imgRepository) {
        SharedRepositoryFactory.imgRepository = imgRepository;
    }

    public static PageRepository getPageRepository() {
        return pageRepository;
    }

    @Autowired
    public void setPageRepository(PageRepository pageRepository) {
        SharedRepositoryFactory.pageRepository = pageRepository;
    }


}
