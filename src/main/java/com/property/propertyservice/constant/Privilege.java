package com.property.propertyservice.constant;

import java.util.HashMap;
import java.util.List;

public class Privilege {

    public static final HashMap<String, Integer> BIT = new HashMap<String, Integer>() {{
        put("房产管理", 1 << 0);
        put("车位使用管理", 1 << 1);
        put("停车信息管理", 1 << 2);
        put("收费明细管理", 1 << 3);
        put("价格管理", 1 << 4);
        put("水费管理", 1 << 5);
        put("电费管理", 1 << 6);
        put("煤气费管理", 1 << 7);
        put("管理员列表", 1 << 8);
    }};
}
