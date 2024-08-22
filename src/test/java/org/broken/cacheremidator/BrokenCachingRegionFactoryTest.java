package org.broken.cacheremidator;

import static org.junit.jupiter.api.Assertions.*;
import org.hibernate.cache.cfg.spi.DomainDataRegionBuildingContext;
import org.hibernate.cache.cfg.spi.DomainDataRegionConfig;
import org.hibernate.cache.spi.DomainDataRegion;
import org.hibernate.cache.spi.support.StorageAccess;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BrokenCachingRegionFactoryTest {

    private BrokenCachingRegionFactory regionFactory;

    @Mock
    private DomainDataRegionConfig mockRegionConfig;

    @Mock
    private DomainDataRegionBuildingContext mockBuildingContext;

    @Mock
    private SessionFactoryImplementor mockSessionFactory;

    @Mock
    private SharedSessionContractImplementor mockSession;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        regionFactory = new BrokenCachingRegionFactory();
    }

    @Test
    public void testBuildDomainDataRegion() {
        DomainDataRegion region = regionFactory.buildDomainDataRegion(mockRegionConfig, mockBuildingContext);
        assertNotNull(region, "DomainDataRegion should not be null");
        assertTrue(region instanceof CustomDomainDataRegion, "Region should be an instance of CustomDomainDataRegion");
    }

    @Test
    public void testCreateQueryResultsRegionStorageAccess() {
        StorageAccess storageAccess = regionFactory.createQueryResultsRegionStorageAccess("queryRegion", mockSessionFactory);
        assertNotNull(storageAccess, "StorageAccess should not be null");
        assertTrue(storageAccess instanceof BrokenCachingRegionFactory.MapStorageAccessImpl, "StorageAccess should be an instance of MapStorageAccessImpl");
    }

    @Test
    public void testCreateTimestampsRegionStorageAccess() {
        StorageAccess storageAccess = regionFactory.createTimestampsRegionStorageAccess("timestampRegion", mockSessionFactory);
        assertNotNull(storageAccess, "StorageAccess should not be null");
        assertTrue(storageAccess instanceof BrokenCachingRegionFactory.MapStorageAccessImpl, "StorageAccess should be an instance of MapStorageAccessImpl");
    }

    @Test
    public void testMapStorageAccessImpl() {
        BrokenCachingRegionFactory.MapStorageAccessImpl storageAccess = new BrokenCachingRegionFactory.MapStorageAccessImpl();

        // Test putIntoCache
        storageAccess.putIntoCache("key1", "value1", mockSession);
        assertEquals("value1", storageAccess.getFromCache("key1", mockSession), "Value should be stored and retrievable");

        // Test contains
        assertTrue(storageAccess.contains("key1"), "Cache should contain the key");

        // Test removeFromCache
        storageAccess.removeFromCache("key1", mockSession);
        assertFalse(storageAccess.contains("key1"), "Cache should not contain the key after removal");

        // Test clearCache
        storageAccess.putIntoCache("key2", "value2", mockSession);
        storageAccess.clearCache(mockSession);
        assertNull(storageAccess.getFromCache("key2", mockSession), "Cache should be empty after clearing");

        // Test evictData
        storageAccess.putIntoCache("key3", "value3", mockSession);
        storageAccess.evictData();
        assertNull(storageAccess.getFromCache("key3", mockSession), "Cache should be empty after eviction");

        // Test release
        storageAccess.putIntoCache("key4", "value4", mockSession);
        storageAccess.release();
        assertNull(storageAccess.getFromCache("key4", mockSession), "Cache should be empty after release");
    }
}
