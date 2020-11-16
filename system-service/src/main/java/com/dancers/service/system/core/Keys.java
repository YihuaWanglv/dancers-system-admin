package com.dancers.service.system.core;

import java.util.HashMap;
import java.util.Map;

public class Keys {

    public interface User {
        interface Password {
            String SALT = "Dancers";
        }
    }

    interface DictionaryEnum {
        String getAlias();

        boolean isTypeOf(String value);
    }

    /**
     * 菜单类型
     */
    public enum MenuType implements DictionaryEnum {
        MODULE("模块"),
        MENU("菜单"),
        BUTTON("按钮");
        private String alias;

        MenuType(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }

        @Override
        public boolean isTypeOf(String value) {
            return this.name().equals(value);
        }

        public static boolean contains(String value) {
            if (value == null || "".equals(value)) return false;
            for (MenuType v : MenuType.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public static Map<String, String> toNameAliasMap() {
            Map<String, String> map = new HashMap<>();
            for (MenuType v : MenuType.values()) {
                map.put(v.name(), v.getAlias());
            }
            return map;
        }
    }

    /**
     * 机构类型
     */
    public enum OrgType implements DictionaryEnum {
        COMP("公司"),
        DEPT("部门"),
        GROUP("小组"),
        SCHOOL("学校");
        private String alias;

        OrgType(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }

        @Override
        public boolean isTypeOf(String value) {
            return this.name().equals(value);
        }

        public static boolean contains(String value) {
            if (value == null || "".equals(value)) return false;
            for (OrgType v : OrgType.values()) {
                if (v.name().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public static Map<String, String> toNameAliasMap() {
            Map<String, String> map = new HashMap<>();
            for (OrgType v : OrgType.values()) {
                map.put(v.name(), v.getAlias());
            }
            return map;
        }
    }
}
