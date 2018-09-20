package com.github.zlsqldatabase;

public interface IBaseBean<T> {
    /**
     * 插入数据
     * @param t
     * @return
     */
    public Long insert(T t);

    public int delete(T t);

    public int update(T entity, T where);

    public int getTotalCount();
}
