package com.github.zlsqldatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Condition {
    private String whereClause;
    private String[] whereArgs;

    public Condition(Map<String ,String> map) {
        ArrayList list = new ArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1=1");
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                stringBuilder.append(" and "+key+"=?");
                list.add(value);
            }
        }
        this.whereClause = stringBuilder.toString();
        this.whereArgs = (String[]) list.toArray(new String[list.size()]);
    }


    public String getWhereClause() {
        return whereClause;
    }

    public String[] getWhereArgs() {
        return whereArgs;
    }
}
