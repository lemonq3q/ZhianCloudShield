package org.example.bean;

import org.example.enums.DBTypeEnum;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DBContextHolder {
    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();
    private static final Map<DBTypeEnum,Boolean> DBAvailableMap = DBTypeEnum.getAvailableMap();
    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void setDBAvailableMap(DBTypeEnum dbTypeEnum,Boolean available){
        DBAvailableMap.put(dbTypeEnum,available);
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        System.out.println("切换到"+DBTypeEnum.MASTER);
    }

    public static void slave() {
        //  轮询
        DBTypeEnum[] dbTypeEnums = DBTypeEnum.values();
        int index = counter.getAndIncrement() % (dbTypeEnums.length-1) + 1;  //除去一个master
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        for (int i = 0; i < dbTypeEnums.length; i++) {
            if(DBAvailableMap.get(dbTypeEnums[index]) && dbTypeEnums[index] != DBTypeEnum.MASTER){
                set(dbTypeEnums[index]);
                System.out.println("切换到"+dbTypeEnums[index]);
                return;
            }
            index = (index + 1) % dbTypeEnums.length;
        }
        //所有从库都挂了，随便给一个
        set(DBTypeEnum.SLAVE1);
        System.out.println("所有从库都宕机了");
    }


}
