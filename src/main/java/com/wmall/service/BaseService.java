package com.wmall.service;

import com.wmall.commons.Page;
import com.wmall.dao.IBaseDao;

import java.util.List;

public interface BaseService<T> extends IBaseDao<T> {
    public Page<T> findPage(Page<T> page, String hql, List<Object> paramList);
}
