package org.broken.cacheremidator;

import org.hibernate.cache.cfg.spi.DomainDataRegionBuildingContext;
import org.hibernate.cache.cfg.spi.DomainDataRegionConfig;
import org.hibernate.cache.spi.CacheKeysFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cache.spi.support.DomainDataRegionTemplate;
import org.hibernate.cache.spi.support.DomainDataStorageAccess;

public class CustomDomainDataRegion extends DomainDataRegionTemplate {
    public CustomDomainDataRegion(
          DomainDataRegionConfig regionConfig,
          RegionFactory regionFactory,
          DomainDataStorageAccess storageAccess,
          CacheKeysFactory defaultKeysFactory,
          DomainDataRegionBuildingContext buildingContext) {
       super( regionConfig, regionFactory, storageAccess, defaultKeysFactory, buildingContext );
    }
}