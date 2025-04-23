package tpp.profixer.customer.data.remote;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tpp.profixer.customer.data.model.api.ResponseListObj;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.ReviewStar;

public interface ApiService {
    @GET("/v1/category-home/client-list")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<CategoryCourse>>> getCategories(@Query("kind") Integer kind, @Query("status") Integer status);

    @GET("/v1/course/course-detail/{course_id}")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<Course>> getCourseDetails(@Path("course_id") Long courseId);

    @GET("/v1/review/star/{course_id}")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ReviewStar>> getReviewStar(@Path("course_id") Long courseId);
}
