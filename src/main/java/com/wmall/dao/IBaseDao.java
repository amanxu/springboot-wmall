package com.wmall.dao;

import java.util.List;

public interface IBaseDao<T> {

    /**
     * 新增对象
     *
     * @param t 要保存的实体
     */
    public void add(T t);

    /**
     * 新增对象，并返回对象
     *
     * @param t 要保存的实体
     * @return 返回持久化后的实体
     */
    public T save(T t);

    /**
     * 新增或修改对象
     * <p>当实体已存在时修改实体，不存在时添加实体</p>
     *
     * @param t
     */
    public void saveOrUpdate(T t);

    /**
     * 修改对象
     *
     * @param t 要修改的实体
     */
    public void update(T t);

    /**
     * 删除对象
     *
     * @param t 要删除的实体
     */
    public void delete(T t);

    /**
     * 通过键值对删除
     */
    public void deleteByKey(String key, Object value);

    /**
     * 通过键值对查询
     */
    public T fetch(String key, Object value);

    /**
     * 查询所有对象
     */
    public List<T> findAll();

    /**
     * hql 查询List
     *
     * @param hql 查询语句
     * @return 返回符合条件的class对象集合
     */
    public List<T> findByHql(String hql, Object... values);

    /**
     * SQL查询List,SQL预处理
     *
     * @param hql       sql查询语句
     * @param paramList 参数对象集合
     * @return 返回符合条件的class对象集合<br/>
     */
    public List<T> findByHql(String hql, List<Object> paramList);

    /**
     * hql查询对象
     *
     * @param hql 查询语句
     * @return 返回符合条件的class对象
     */
    public T findUniqueByHql(String hql, Object... values);

    /**
     * hql查询对象,hql预处理
     *
     * @param hql       查询语句
     * @param paramList 参数组成的Map集合
     * @return 返回符合条件的class对象<br/>
     */
    public T findUniqueByHql(String hql, List<Object> paramList);


    /**
     * sql 查询List
     *
     * @param sqlString 查询语句
     * @return 返回符合条件的class对象集合
     */
    public List<T> findBySql(String sqlString, Object... values);

    /**
     * SQL查询List,SQL预处理
     *
     * @param sqlString sql查询语句
     * @param paramList 参数对象集合
     * @return 返回符合条件的class对象集合<br/>
     * eg:<br/>
     * select * from tableName where userName = ? and password = ? <br/>
     * List<Object> paramList = new ArrayList<Object> ();<br/>
     * paramList.add(admin);<br/>
     * paramList.add(123456);<br/>
     * 参数的添加顺序要跟Sql中顺序对应<br/>
     */
    public List<T> findBySql(String sqlString, List<Object> paramList);

    /**
     * SQL查詢List
     *
     * @param sqlString sql查询语句
     * @return 符合条件的Map对象集合 <br/>
     * eg:返回值 List<Map<Object,Object>>
     */
    public List<T> findMapBySql(String sqlString, Object... values);

    /**
     * SQL查詢
     *
     * @param sqlString
     * @return 返回符合条件的Object数组
     */
    public List<T> findArrBySql(String sqlString, Object... values); //Object[]

    /**
     * sql查询对象
     *
     * @param sqlString 查询语句
     * @return 返回符合条件的class对象
     */
    public T findUniqueBySql(String sqlString, Object... values);

    /**
     * SQL查询对象,SQL预处理
     *
     * @param sqlString 查询语句
     * @param paramList 参数组成的Map集合
     * @return 返回符合条件的class对象<br/>
     * eg:<br/>
     * select * from tableName where userName = ? and password = ? <br/>
     * List<Object> paramList = new ArrayList<Object> ();<br/>
     * paramList.add(admin);<br/>
     * paramList.add(123456);<br/>
     * 参数的添加顺序要跟Sql中顺序对应<br/>
     */
    public T findUniqueBySql(String sqlString, List<Object> paramList);

    /**
     * 查询记录的总条数
     *
     * @param hql
     * @param values 对象数组或者对象List
     * @return
     */
    public Long countHql(String hql, List<Object> paramList);
}
