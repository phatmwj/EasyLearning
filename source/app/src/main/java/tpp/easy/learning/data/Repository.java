package tpp.easy.learning.data;

import tpp.easy.learning.data.local.prefs.PreferencesService;
import tpp.easy.learning.data.local.room.RoomService;
import tpp.easy.learning.data.remote.ApiService;


public interface Repository {
    PreferencesService getSharedPreferences();
    ApiService getApiService();

    RoomService getRoomService();

}
