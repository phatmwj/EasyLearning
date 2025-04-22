package tpp.profixer.customer.data.remote;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tpp.profixer.customer.data.model.api.ResponseListObj;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;

public interface ApiService {
    @GET("/v1/category-home/client-list")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<CategoryCourse>>> getCategories(@Query("kind") Integer kind, @Query("status") Integer status);

}
