package com.wmall.dao.impl;

import com.wmall.dao.IBaseDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BaseDaoImpl<T> implements IBaseDao<T> {
    Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    //@Qualifier("mySessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    protected Class<T> entityClass;

    public BaseDaoImpl() {
    }

    /**
     * 得到泛型的类型
     *
     * @return
     */
    protected Class getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return entityClass;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void add(T t) {
        this.getSession().save(t);
    }

    @Override
    public T save(T t) {
        this.getSession().save(t);
        return t;
    }

    @Override
    public void saveOrUpdate(T t) {
        this.getSession().saveOrUpdate(t);
    }

    @Override
    public void update(T t) {
        this.getSession().update(t);
    }

    @Override
    public void delete(T t) {
        this.getSession().delete(t);
    }

    @Override
    public void deleteByKey(String key, Object value) {
        T t = fetch(key, value);
        if (null != t) {
            delete(t);
        }
    }

    @Override
    public T fetch(String key, Object value) {
        StringBuilder hql = new StringBuilder();
        hql.append("from " + getEntityClass().getSimpleName());
        hql.append(" t where 1=1 and t.").append(key).append("=?");
        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter(0, value);
        return (T) query.uniqueResult();
    }

    @Override
    public List<T> findAll() {
        String hql = "from " + getEntityClass().getSimpleName() + " where 1=1";
        Query query = this.getSession().createQuery(hql);
        return query.list();
    }

    @Override
    public List<T> findByHql(String hql, Object... values) {
        Assert.hasText(hql, "hql不能为空");
        Query query = this.getSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    @Override
    public List<T> findByHql(String hql, List<Object> paramList) {
        Assert.hasText(hql, "sqlString不能为空");
        Query query = this.getSession().createQuery(hql);
        if (null != paramList) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return query.list();
    }

    @Override
    public T findUniqueByHql(String hql, Object... values) {
        Assert.hasText(hql, "sqlString不能为空");
        Query query = this.getSession().createQuery(hql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public T findUniqueByHql(String hql, List<Object> paramList) {
        Assert.hasText(hql, "sqlString不能为空");
        Query query = this.getSession().createQuery(hql);
        if (null != paramList) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public List findBySql(String sql, Object... values) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql).addEntity(getEntityClass()).setCacheable(true);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    @Override
    public List findBySql(String sql, List paramList) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql).addEntity(getEntityClass()).setCacheable(true);
        if (null != paramList) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return query.list();
    }

    @Override
    public List findMapBySql(String sql, Object... values) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql);
        query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);// 转为MAP
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        List<T> list = query.list();
        return list;
    }

    @Override
    public List findArrBySql(String sql, Object... values) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        List<T> list = query.list();
        return list;
    }

    @Override
    public T findUniqueBySql(String sql, Object... values) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql).addEntity(getEntityClass());
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public T findUniqueBySql(String sql, List paramList) {
        Assert.hasText(sql, "sqlString不能为空");
        Query query = this.getSession().createSQLQuery(sql).addEntity(getEntityClass());
        if (null != paramList) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return (T) query.uniqueResult();
    }

    @Override
    public Long countHql(String hql, List paramList) {
        Query query = this.getSession().createQuery(hql);
        //List<Object> paramList = convertList(values);
        if (null != paramList) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return (Long) query.uniqueResult();
    }

    private List<Object> convertList(Object values) {
        if (values != null) {
            if (values instanceof Object[]) {
                return Arrays.asList(values);
            } else if (values instanceof Collection) {
                return (List<Object>) values;
            } else {
                try {
                    throw new Exception("CountHql 参数类型不匹配");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     *
     * @param paramList 数量可变的参数,按顺序绑定.
     */
    public Query createHqlQuery(final String hqlString, final List<Object> paramList) {
        Assert.hasText(hqlString, "hqlString不能为空");
        Query query = this.getSession().createQuery(hqlString);
        if (paramList != null) {
            for (int i = 0; i < paramList.size(); i++) {
                query.setParameter(i, paramList.get(i));
            }
        }
        return query;
    }
}
