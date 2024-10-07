package org.example.enums;

import java.util.HashMap;
import java.util.Map;

public enum DBTypeEnum {
    MASTER, SLAVE1, SLAVE2;

    public static Map<DBTypeEnum,Boolean> getAvailableMap(){
        Map<DBTypeEnum,Boolean> availableMap = new HashMap<>();
        availableMap.put(MASTER,true);
        availableMap.put(SLAVE1,true);
        availableMap.put(SLAVE2,true);
        return availableMap;
    }
}
