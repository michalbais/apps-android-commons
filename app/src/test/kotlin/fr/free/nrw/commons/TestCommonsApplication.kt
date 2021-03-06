package fr.free.nrw.commons

import android.content.ContentProviderClient
import android.content.Context
import android.support.v4.util.LruCache
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.mock
import com.squareup.leakcanary.RefWatcher
import fr.free.nrw.commons.auth.AccountUtil
import fr.free.nrw.commons.auth.SessionManager
import fr.free.nrw.commons.data.DBOpenHelper
import fr.free.nrw.commons.di.CommonsApplicationComponent
import fr.free.nrw.commons.di.CommonsApplicationModule
import fr.free.nrw.commons.di.DaggerCommonsApplicationComponent
import fr.free.nrw.commons.kvstore.BasicKvStore
import fr.free.nrw.commons.kvstore.JsonKvStore
import fr.free.nrw.commons.location.LocationServiceManager
import fr.free.nrw.commons.mwapi.MediaWikiApi
import fr.free.nrw.commons.nearby.NearbyPlaces
import fr.free.nrw.commons.upload.UploadController

class TestCommonsApplication : CommonsApplication() {
    private var mockApplicationComponent: CommonsApplicationComponent? = null

    override fun onCreate() {
        if (mockApplicationComponent == null) {
            mockApplicationComponent = DaggerCommonsApplicationComponent.builder()
                    .appModule(MockCommonsApplicationModule(this))
                    .build()
        }
        super.onCreate()
    }

    // No leakcanary in unit tests.
    override fun setupLeakCanary(): RefWatcher = RefWatcher.DISABLED
}

@Suppress("MemberVisibilityCanBePrivate")
class MockCommonsApplicationModule(appContext: Context) : CommonsApplicationModule(appContext) {
    val accountUtil: AccountUtil = mock()
    val appSharedPreferences: BasicKvStore = mock()
    val defaultSharedPreferences: BasicKvStore = mock()
    val otherSharedPreferences: BasicKvStore = mock()
    val uploadController: UploadController = mock()
    val mockSessionManager: SessionManager = mock()
    val locationServiceManager: LocationServiceManager = mock()
    val mockDbOpenHelper: DBOpenHelper = mock()
    val nearbyPlaces: NearbyPlaces = mock()
    val lruCache: LruCache<String, String> = mock()
    val gson: Gson = Gson()
    val categoryClient: ContentProviderClient = mock()
    val contributionClient: ContentProviderClient = mock()
    val modificationClient: ContentProviderClient = mock()
    val uploadPrefs: JsonKvStore = mock()

    override fun provideCategoryContentProviderClient(context: Context?): ContentProviderClient = categoryClient

    override fun provideContributionContentProviderClient(context: Context?): ContentProviderClient = contributionClient

    override fun provideModificationContentProviderClient(context: Context?): ContentProviderClient = modificationClient

    override fun providesDirectNearbyUploadKvStore(context: Context?): JsonKvStore = uploadPrefs

    override fun providesAccountUtil(context: Context): AccountUtil = accountUtil

    override fun providesApplicationKvStore(context: Context): BasicKvStore = appSharedPreferences

    override fun providesDefaultKvStore(context: Context): BasicKvStore = defaultSharedPreferences

    override fun providesOtherKvStore(context: Context): BasicKvStore = otherSharedPreferences

    override fun providesUploadController(sessionManager: SessionManager, sharedPreferences: BasicKvStore, context: Context): UploadController = uploadController

    override fun providesSessionManager(context: Context, mediaWikiApi: MediaWikiApi, sharedPreferences: BasicKvStore): SessionManager = mockSessionManager

    override fun provideLocationServiceManager(context: Context): LocationServiceManager = locationServiceManager

    override fun provideDBOpenHelper(context: Context): DBOpenHelper = mockDbOpenHelper

    override fun provideNearbyPlaces(): NearbyPlaces = nearbyPlaces

    override fun provideLruCache(): LruCache<String, String> = lruCache
}