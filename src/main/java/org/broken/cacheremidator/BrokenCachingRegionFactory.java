package org.broken.cacheremidator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.cfg.spi.DomainDataRegionBuildingContext;
import org.hibernate.cache.cfg.spi.DomainDataRegionConfig;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.DomainDataRegion;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;
import org.hibernate.cache.spi.support.RegionFactoryTemplate;
import org.hibernate.cache.spi.support.StorageAccess;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

public class BrokenCachingRegionFactory extends RegionFactoryTemplate {

    private final CacheKeysFactory cacheKeysFactory;

    public BrokenCachingRegionFactory() {
       this.cacheKeysFactory = DefaultCacheKeysFactory.INSTANCE;
    }

    @Override
    protected void prepareForUse(SessionFactoryOptions settings, Map configValues) {
    }

    @Override
    public DomainDataRegion buildDomainDataRegion(
          DomainDataRegionConfig regionConfig, DomainDataRegionBuildingContext buildingContext) {
       return new CustomDomainDataRegion(
             regionConfig,
             this,
             new MapStorageAccessImpl(),
             cacheKeysFactory,
             buildingContext
       );
    }

    @Override
    protected StorageAccess createQueryResultsRegionStorageAccess(
          String regionName,
          SessionFactoryImplementor sessionFactory) {
       return new MapStorageAccessImpl();
    }

    @Override
    protected StorageAccess createTimestampsRegionStorageAccess(
          String regionName,
          SessionFactoryImplementor sessionFactory) {
       return new MapStorageAccessImpl();
    }

    @Override
    protected void releaseFromUse() {
    }

    public static class MapStorageAccessImpl implements DomainDataStorageAccess {
       private ConcurrentMap data;

       @Override
       public boolean contains(Object key) {
          return data != null && data.containsKey( key );
       }

       @Override
       public Object getFromCache(Object key, SharedSessionContractImplementor session) {
          if ( data == null ) {
             return null;
          }
          return data.get( key );
       }

       @Override
       public void putIntoCache(Object key, Object value, SharedSessionContractImplementor session) {
          getOrMakeDataMap().put( key, value );
       }

       protected ConcurrentMap getOrMakeDataMap() {
          if ( data == null ) {
             data = new ConcurrentHashMap();
          }
          return data;
       }

       @Override
       public void removeFromCache(Object key, SharedSessionContractImplementor session) {
          if ( data == null ) {
             return;
          }

          data.remove( key );
       }

       @Override
       public void clearCache(SharedSessionContractImplementor session) {
          if ( data == null ) {
             return;
          }

          data.clear();
       }

       @Override
       public void evictData() {
          if ( data != null ) {
             data.clear();
          }
       }

       @Override
       public void evictData(Object key) {
          if ( data != null ) {
             data.remove( key );
          }
       }

       @Override
       public void release() {
          if ( data != null ) {
             data.clear();
             data = null;
          }
       }
    }
} 