package tpp.profixer.customer.di.module;

import android.app.Application;
import android.content.Context;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tpp.profixer.customer.BuildConfig;
import tpp.profixer.customer.constant.Constants;
import tpp.profixer.customer.data.AppRepository;
import tpp.profixer.customer.data.Repository;
import tpp.profixer.customer.data.local.prefs.AppPreferencesService;
import tpp.profixer.customer.data.local.prefs.PreferencesService;
import tpp.profixer.customer.data.local.room.AppDatabase;
import tpp.profixer.customer.data.local.room.AppDbService;
import tpp.profixer.customer.data.local.room.RoomService;
import tpp.profixer.customer.data.remote.ApiService;
import tpp.profixer.customer.data.remote.AuthInterceptor;
import tpp.profixer.customer.di.qualifier.ApiInfo;
import tpp.profixer.customer.di.qualifier.DatabaseInfo;
import tpp.profixer.customer.di.qualifier.PreferenceInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    @ApiInfo
    @Singleton
    String provideBaseUrl() {
        return BuildConfig.BASE_URL;
    }


    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }


    @Provides
    @PreferenceInfo
    @Singleton
    String providePreferenceName() {
        return Constants.PREF_NAME;
    }


    @Provides
    @Singleton
    PreferencesService providePreferencesService(AppPreferencesService appPreferencesService) {
        return appPreferencesService;
    }

    @Provides
    @Singleton
    AuthInterceptor proviceAuthInterceptor(PreferencesService appPreferencesService, Application application) {
        return new AuthInterceptor(appPreferencesService, application);
    }

    @Provides
    @Singleton
    public OkHttpClient getClient(AuthInterceptor authInterceptor) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit retrofitBuild(OkHttpClient client, @ApiInfo String url) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }


    @Provides
    @Singleton
    public ApiService apiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }


    @Provides
    @Singleton
    public Repository provideDataManager(AppRepository appRepository) {
        return appRepository;
    }

    @Provides
    @DatabaseInfo
    @Singleton
    String provideDatabaseName() {
        return Constants.DB_NAME;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName)
                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationOnDowngrade()
                .build();
    }

    @Provides
    @Singleton
    RoomService provideDbService(AppDbService roomService) {
        return roomService;
    }

}
