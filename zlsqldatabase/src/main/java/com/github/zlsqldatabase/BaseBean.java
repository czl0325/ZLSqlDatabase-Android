package com.github.zlsqldatabase;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.zlsqldatabase.Annotation.dbField;
import com.github.zlsqldatabase.Annotation.dbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseBean<T> implements IBaseBean<T> {
    private SQLiteDatabase sqLiteDatabase;      //数据库类的引用
    private boolean isInit = false;             //只实例化一次
    private Class<T> entityClass;               //数据库表所对应的JAVA类型
    private HashMap<String , Field> cacheMap;   //用于缓存这张表的HashMap
    private String tableName;                   //表名

    protected synchronized boolean init(Class<T> entityClass, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.entityClass = entityClass;
            this.sqLiteDatabase = sqLiteDatabase;
            if (entityClass.getAnnotation(dbTable.class) == null) {
                tableName = entityClass.getSimpleName();
            } else {
                tableName = entityClass.getAnnotation(dbTable.class).value();
            }
            if (!sqLiteDatabase.isOpen()) {
                return false;
            }
            String createTable = "create table if not exists "+tableName+"(";
            Field []fields = entityClass.getFields();
            for (int i=0; i<fields.length; i++ ) {
                Field field = fields[i];
                field.setAccessible(true);
                String str = null;
                if (!(field.getName().equals("$change")
                        || field.getName().equals("serialVersionUID"))) {
                    if (field.getAnnotation(dbField.class)!=null) {
                        str = field.getAnnotation(dbField.class).fieldName();
                        createTable += str;
                    } else {
                        str = field.getName();
                        createTable += str;
                    }
                    if (field.getAnnotation(dbField.class)!=null) {
                        createTable += " varchar("+field.getAnnotation(dbField.class).fieldLength()+")";
                    } else {
                        createTable+=" varchar(100)";
                    }
                }
                if (i==fields.length-1) {
                    if (createTable.endsWith(",")) {
                        createTable = createTable.substring(0, createTable.length()-1);
                    }
                    createTable+=")";
                } else {
                    if (str != null) {
                        createTable+=",";
                    }
                }
            }

            sqLiteDatabase.execSQL(createTable);
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {
        String sql = "select * from "+tableName+" limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql,null);
            //表的列名数组
            String[] columnNames = cursor.getColumnNames();
            //拿到Filed数组
            Field[] columnFields = entityClass.getFields();
            for (Field field:columnFields) {
                field.setAccessible(true);
            }
            //开始找对应关系
            for (String columnName : columnNames) {
                Field columnField = null;
                for (Field field:columnFields) {
                    String fieldName = null;
                    if (field.getAnnotation(dbField.class)!=null) {
                        fieldName = field.getAnnotation(dbField.class).fieldName();
                    } else {
                        fieldName = field.getName();
                    }
                    //如果表的列名 等于了  成员变量的注解名字
                    if (columnName.equals(fieldName)) {
                        columnField = field;
                        break;
                    }
                }
                if (columnField!=null) {
                    cacheMap.put(columnName, columnField);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    @Override
    public Long insert(T t) {
        Map<String, String> hashMap = getValue(t);
        ContentValues contentValues = getContentValue(hashMap);
        return sqLiteDatabase.insert(tableName, null, contentValues);
    }

    @Override
    public int delete(T t) {
        Map<String, String> hashMap = getValue(t);
        Condition condition = new Condition(hashMap);
        return sqLiteDatabase.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
    }

    @Override
    public int update(T entity, T where) {
        Map value = getValue(entity);
        Map whereClause = getValue(where);
        Condition condition = new Condition(whereClause);
        ContentValues contentValues = getContentValue(value);
        return sqLiteDatabase.update(tableName, contentValues, condition.getWhereClause(),
                condition.getWhereArgs());
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValue(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex+" , "+limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.getWhereClause(),
                condition.getWhereArgs(), null, null, orderBy, limitString);
        List<T> result = getResult(cursor, where);
        cursor.close();
        return result;
    }


    private Map<String, String> getValue(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field columnToField = fieldIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (columnToField.getAnnotation(dbField.class) != null) {
                cacheKey = columnToField.getAnnotation(dbField.class).fieldName();
            } else {
                cacheKey = columnToField.getName();
            }
            try {
                if(null==columnToField.get(entity)) {
                    continue;
                }
                cacheValue=columnToField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey,cacheValue);
        }
        return result;
    }

    private ContentValues getContentValue(Map<String ,String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key,value);
            }
        }
        return contentValues;
    }

    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list = new ArrayList();
        Object item = null;
        if (cursor.moveToFirst()) {
            do {
                try {
                    item = where.getClass().newInstance();
                    Iterator iterator = cacheMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        //得到列名
                        String columnName = (String) entry.getKey();
                        //然后以列名拿到  列名在游标的位子
                        Integer columnIndex = cursor.getColumnIndex(columnName);
                        Field field = (Field) entry.getValue();
                        Class type = field.getType();
                        if (columnIndex != -1) {
                            if (type == String.class) {
                                field.set(item, cursor.getString(columnIndex));
                            } else if (type == Integer.class) {
                                field.set(item, cursor.getInt(columnIndex));
                            } else if (type == Long.class) {
                                field.set(item, cursor.getLong(columnIndex));
                            } else if (type == Double.class) {
                                field.set(item, cursor.getDouble(columnIndex));
                            } else if (type == byte[].class) {
                                field.set(item, cursor.getBlob(columnIndex));
                            } else if (type == float.class) {
                                field.set(item, cursor.getFloat(columnIndex));
                            } else {
                                Log.e("SQL","不支持该类型!");
                            }
                        }
                    }
                    list.add(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        return list;
    }
}
