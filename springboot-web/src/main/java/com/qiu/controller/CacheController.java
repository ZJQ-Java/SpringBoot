package com.qiu.controller;

import com.google.common.cache.CacheLoader;
import com.qiu.config.MyConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/cache")
public class CacheController {
    @RequestMapping("/get")
    public String getCache(Integer key) throws ExecutionException {
        String s = null;
        try {
            s = MyConfig.KEY_NAME_CACHE.get(key);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof CacheLoader.InvalidCacheLoadException) {
                return "hha";
            }

        }
        return s;
    }
}