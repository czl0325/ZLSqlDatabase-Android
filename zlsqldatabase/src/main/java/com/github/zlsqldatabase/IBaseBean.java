package com.github.zlsqldatabase;

import java.util.List;

public interface IBaseBean<T> {
    /**
     * 插入数据
     * @param t
     * @return
     */
    public Long insert(T t);

    public int delete(T t);

    public int update(T entity, T where);

    public List<T> query(T where);

    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

    public void closeDB();

    public boolean checkUpdateTable(T t);

    public void exeute(String sql);
}
