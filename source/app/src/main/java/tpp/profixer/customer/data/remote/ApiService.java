package tpp.profixer.customer.data.remote;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tpp.profixer.customer.data.model.api.ResponseListObj;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.response.Cart;
import tpp.profixer.customer.data.model.api.request.LoginRequest;
import tpp.profixer.customer.data.model.api.request.RequestCourse;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.Expert;
import tpp.profixer.customer.data.model.api.response.Lesson;
import tpp.profixer.customer.data.model.api.response.LoginResponse;
import tpp.profixer.customer.data.model.api.response.Review;
import tpp.profixer.customer.data.model.api.response.ReviewStar;

public interface ApiService {
    @POST("/api/token")
    @Headers({"BasicAuth: 1"})
    Observable<LoginResponse> login(@Body LoginRequest request);
    @GET("/v1/category-home/client-list")
    Observable<ResponseWrapper<ResponseListObj<CategoryCourse>>> getCategories(@Query("kind") Integer kind, @Query("status") Integer status);

    @GET("/v1/course/course-detail/{course_id}")
    Observable<ResponseWrapper<Course>> getCourseDetails(@Path("course_id") Long courseId);

    @GET("/v1/review/star/{course_id}")
    Observable<ResponseWrapper<ReviewStar>> getReviewStar(@Path("course_id") Long courseId);
    @GET("/v1/review/list-reviews/{course_id}")
    Observable<ResponseWrapper<ResponseListObj<Review>>> getReviewList(@Path("course_id") Long courseId, @Query("page") Integer page, @Query("size") Integer size);
    @GET("/v1/course/client-list")
    Observable<ResponseWrapper<ResponseListObj<Course>>> getRelatedCourses(@Query("categoryIds") List<Long> categoryId, @Query("ignoreId") List<Long> ignoreId, @Query("page") Integer page, @Query("size") Integer size);

    @POST("/v1/cart-item/create")
    Observable<ResponseWrapper> addToCart(@Body RequestCourse request);

    @GET("/v1/cart-item/list")
    Observable<ResponseWrapper<CartInfo>> getCart(@Query("studentId") Long studentId);

    @GET("/v1/student/profile")
    Observable<ResponseWrapper<Expert>> getProfile(@Query("studentId") Long studentId);

    @GET("/v1/lesson/lesson-detail/{lesson_id}")
    Observable<ResponseWrapper<Lesson>> getLessonDetails(@Path("studentId") Long lessonId);
}
