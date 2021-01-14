package com.rest.template.util;

import java.util.ArrayList;
import java.util.List;

public class AllUserPermissions {

    public final static List<String> All_PERMISSIONS = new ArrayList<String>(){{
       add("/api/auth/login");
    }};

}
