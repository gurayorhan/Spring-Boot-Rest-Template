package com.rest.template.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRolePermissions {

    public final static Map<String, List<String>> USER_TO_ROLES_MAP = new HashMap<String, List<String>>(){{

        put("ADMIN",new ArrayList<String>(){{
            add("*");
        }});

        put("USER",new ArrayList<String>(){{
            add("/api/auth/logout");
        }});

    }};

}
