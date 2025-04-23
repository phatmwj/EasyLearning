package tpp.profixer.customer.data.remote;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tpp.profixer.customer.data.model.api.ResponseListObj;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.request.LoginRequest;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.LoginResponse;
import tpp.profixer.customer.data.model.api.response.Review;
import tpp.profixer.customer.data.model.api.response.ReviewStar;

public interface ApiService {
    @POST("/api/token")
    @Headers({"BasicAuth: 1"})
    Observable<LoginResponse> login(@Body LoginRequest request);
    @GET("/v1/category-home/client-list")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<CategoryCourse>>> getCategories(@Query("kind") Integer kind, @Query("status") Integer status);

    @GET("/v1/course/course-detail/{course_id}")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<Course>> getCourseDetails(@Path("course_id") Long courseId);

    @GET("/v1/review/star/{course_id}")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ReviewStar>> getReviewStar(@Path("course_id") Long courseId);
    @GET("/v1/review/list-reviews/{course_id}")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<Review>>> getReviewList(@Path("course_id") Long courseId, @Query("page") Integer page, @Query("size") Integer size);
    @GET("/v1/course/client-list")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<Course>>> getRelatedCourses(@Query("categoryIds") Long categoryId, @Query("ignoreId") Long ignoreId, @Query("page") Integer page, @Query("size") Integer size);
}
