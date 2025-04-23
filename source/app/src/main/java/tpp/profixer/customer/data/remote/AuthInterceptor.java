package tpp.profixer.customer.data.remote;

import android.app.Application;
import android.content.Intent;
import android.util.Base64;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import okhttp3.HttpUrl;
import tpp.profixer.customer.BuildConfig;
import tpp.profixer.customer.constant.Constants;
import tpp.profixer.customer.data.local.prefs.PreferencesService;
import tpp.profixer.customer.utils.LogService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final PreferencesService appPreferences;
    private final Application application;

    public AuthInterceptor(PreferencesService appPreferences, Application application) {
        this.appPreferences = appPreferences;
        this.application = application;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        String isIgnore = chain.request().header("IgnoreAuth");
        if (isIgnore != null && isIgnore.equals("1")) {
            Request.Builder newRequest = chain.request().newBuilder();
            newRequest.removeHeader("IgnoreAuth");
            return chain.proceed(newRequest.build());
        }

        Request.Builder newRequest = chain.request().newBuilder();

        String isBasic = chain.request().header("BasicAuth");
        if (isBasic != null && isBasic.equals("1")) {
            String username = "abc_client";
            String password = "abc123";
            String base = username + ":" + password;
            newRequest = chain.request().newBuilder();
            newRequest.removeHeader("BasicAuth");
            newRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP));

            return chain.proceed(newRequest.build());
        }

        String isSearchLocation = chain.request().header("isSearchLocation");
        if(isSearchLocation != null && isSearchLocation.equals("1")){
            HttpUrl url = chain.request().url();
            String queryNames = url.encodedQuery();
            StringBuilder builder = new StringBuilder(BuildConfig.GOOGLE_MAP_URL);
            builder.append('/');
            for (int i = 0; i < chain.request().url().pathSegments().size(); i++ ) {
                if(i == chain.request().url().pathSegments().size() -1){
                    builder.append(chain.request().url().pathSegments().get(i)).append("?").append(queryNames);
                }else{
                    builder.append(chain.request().url().pathSegments().get(i)).append('/');
                }
            }
            newRequest.removeHeader("isSearchLocation");
            return chain.proceed(newRequest.url(builder.toString()).build());
        }

        String isBank = chain.request().header("isBank");
        if(isBank!= null && isBank.equals("1")){
            HttpUrl url = chain.request().url();
            String queryNames = url.encodedQuery();
            StringBuilder builder = new StringBuilder(BuildConfig.BANK_URL);
            builder.append('/');
            for (int i = 0; i < chain.request().url().pathSegments().size(); i++ ) {
                if(i == chain.request().url().pathSegments().size() -1){
                    builder.append(chain.request().url().pathSegments().get(i)).append("?").append(queryNames);
                }else{
                    builder.append(chain.request().url().pathSegments().get(i)).append('/');
                }
            }
            newRequest.removeHeader("isBank");
            return chain.proceed(newRequest.url(builder.toString()).build());
        }

        //Add Authentication
        String token = appPreferences.getToken();
        if (token != null && !token.equals("")) {
            newRequest.addHeader("Authorization", "Bearer " + token);
        }

        String isMediaKind = chain.request().header("isMedia");
        if(isMediaKind != null && isMediaKind.equals("1")){
            StringBuilder builder = new StringBuilder(BuildConfig.MEDIA_URL);
            builder.append('/');
            for (String seg: chain.request().url().pathSegments()) {
                builder.append(seg).append('/');
            }
            newRequest.removeHeader("isMedia");
            return chain.proceed(newRequest.url(builder.toString()).build());
        }

        Response origResponse = chain.proceed(newRequest.build());
        if (origResponse.code() == 403 || origResponse.code() == 401) {
            LogService.i("Error http =====================> code: " + origResponse.code());
            appPreferences.removeKey(PreferencesService.KEY_BEARER_TOKEN);
            Intent intent = new Intent();
            intent.setAction(Constants.ACTION_EXPIRED_TOKEN);
            LocalBroadcastManager.getInstance(application.getApplicationContext()).sendBroadcast(intent);
        }
        return origResponse;
    }
}
