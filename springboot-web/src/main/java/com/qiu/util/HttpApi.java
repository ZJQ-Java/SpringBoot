package com.qiu.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * HttpApi
 */
public class HttpApi {
    /**
     * 方法名
     */
    String name;

    /**
     * 方法描述
     */
    String desc;

    /**
     * 参数信息
     */
    List<ParamInfo> params;

    /**
     * Method
     */
    Method method;

    /**
     * 展示结果方式
     */
    String showType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public void setParams(List<ParamInfo> params) {
        this.params = params;
    }

    public void addParams(ParamInfo param) {
        if (this.params == null) {
            this.params = new ArrayList<>();
        }
        this.params.add(param);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return "HttpApi [name=" + name + ", desc=" + desc + ", params=" + params + "]";
    }

    /**
     * HttpApi 参数
     */
    public static class ParamInfo {
        /**
         * 参数类型
         */
        Class<?> clazz;

        /**
         * 参数类型，显示用
         */
        String type;

        /**
         * 参数名
         */
        String name;

        /**
         * 参数描述
         */
        String desc;

        /**
         * 必须填写
         */
        boolean isMust;

        public ParamInfo(Class<?> clazz, String name, String desc, boolean isMust) {
            super();
            this.clazz = clazz;
            this.name = name;
            this.desc = desc;
            this.isMust = isMust;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getType() {
            if (clazz == null) {
                return "";
            }
            return clazz.getSimpleName();
        }

        public void setType(String type) {
            // nothing
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public boolean isMust() {
            return isMust;
        }

        public void setMust(boolean isMust) {
            this.isMust = isMust;
        }

        @Override
        public String toString() {
            return "ParamInfo [clazz=" + clazz + ", name=" + name + ", desc=" + desc + ", isMust=" + isMust + "]";
        }

    }
}
