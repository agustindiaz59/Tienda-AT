package com.nomEmpresa.nomProyecto.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        Set<CaffeineCache> caches = new HashSet<>();
        caches.add(cacheBuild(CacheName.ADMINISTRADOR.name(),1L, TimeUnit.DAYS));

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);

        return cacheManager;
    }

    public CaffeineCache cacheBuild(String nombre, Long duracion, TimeUnit timeUnit){
        return new CaffeineCache(
                nombre,
                Caffeine.newBuilder()
                        .expireAfterWrite(duracion,timeUnit)
                        .maximumSize(10)
                        .build()
        );
    }


}
