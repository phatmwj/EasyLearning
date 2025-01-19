package tpp.profixer.customer.data;

import tpp.profixer.customer.data.local.prefs.PreferencesService;
import tpp.profixer.customer.data.local.room.RoomService;
import tpp.profixer.customer.data.remote.ApiService;

import javax.inject.Inject;

public class AppRepository implements Repository {

    private final ApiService mApiService;
    private final PreferencesService mPreferencesHelper;
    private final RoomService roomService;

    @Inject
    public AppRepository(PreferencesService preferencesHelper, ApiService apiService, RoomService roomService) {
        this.mPreferencesHelper = preferencesHelper;
        this.mApiService = apiService;
        this.roomService = roomService;
    }

    @Override
    public PreferencesService getSharedPreferences() {
        return mPreferencesHelper;
    }


    @Override
    public ApiService getApiService() {
        return mApiService;
    }


    @Override
    public RoomService getRoomService() {
        return roomService;
    }
}
