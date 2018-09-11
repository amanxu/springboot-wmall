package com.wmall.service.impl;

import com.wmall.commons.Page;
import com.wmall.dao.impl.BaseDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

public class BaseServiceImpl<T> extends BaseDaoImpl<T> implements com.wmall.service.BaseService<T> {

    Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);


    public Page<T> findPage(Page<T> page, String hql, List<Object> paramList) {
        Assert.notNull(page, "page不能为空");
        try {
            Query query = createHqlQuery(hql, paramList);
            if (page.isAutoCount()) {
                long totalCount = countHqlResult(hql, paramList);
                page.setTotalCount(totalCount);
            }
            // 设置参数
            setPageParameterToQuery(query, page);
            page.setResult(query.list());
        } catch (HibernateException hibExce) {
            hibExce.printStackTrace();
            // throw new DataBaseException("数据库错误", hibExce);
        } catch (Exception dbExce) {
            dbExce.printStackTrace();
        }
        return page;
    }

    /**
     * 执行count查询获得本次Hql查询所能获得的对象总数.
     * <p>
     * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
     */
    protected Long countHqlResult(final String hql, final List<Object> paramList)
            throws Exception {
        String countHql = prepareCountHql(hql);
        try {
            return countHql(countHql, paramList);
        } catch (Exception hibExce) {
            throw new Exception("数据库执行错误:" + countHql, hibExce);
        }
    }

    private String prepareCountHql(String orgHql) {
        String fromHql = orgHql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");
        return "select count(*) " + fromHql;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    protected Query setPageParameterToQuery(final Query query, final Page<T> page) {
        Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
        // hibernate的firstResult的序号从0开始
        query.setFirstResult(page.getFirst() - 1);
        query.setMaxResults(page.getPageSize());
        return query;
    }
}
