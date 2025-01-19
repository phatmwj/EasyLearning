package tpp.profixer.customer.data;

import tpp.profixer.customer.data.local.prefs.PreferencesService;
import tpp.profixer.customer.data.local.room.RoomService;
import tpp.profixer.customer.data.remote.ApiService;


public interface Repository {
    PreferencesService getSharedPreferences();
    ApiService getApiService();

    RoomService getRoomService();

}
