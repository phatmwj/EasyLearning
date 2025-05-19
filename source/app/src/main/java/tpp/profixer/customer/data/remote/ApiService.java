package tpp.profixer.customer.data.remote;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tpp.profixer.customer.data.model.api.ResponseListObj;
import tpp.profixer.customer.data.model.api.ResponseWrapper;
import tpp.profixer.customer.data.model.api.request.CompleteLessonRequest;
import tpp.profixer.customer.data.model.api.request.SignupRequest;
import tpp.profixer.customer.data.model.api.request.Slide;
import tpp.profixer.customer.data.model.api.request.UpdateProfileRequest;
import tpp.profixer.customer.data.model.api.response.AvatarPathResponse;
import tpp.profixer.customer.data.model.api.response.Cart;
import tpp.profixer.customer.data.model.api.request.LoginRequest;
import tpp.profixer.customer.data.model.api.request.RequestCourse;
import tpp.profixer.customer.data.model.api.response.CartInfo;
import tpp.profixer.customer.data.model.api.response.CategoryCourse;
import tpp.profixer.customer.data.model.api.response.Course;
import tpp.profixer.customer.data.model.api.response.Expert;
import tpp.profixer.customer.data.model.api.response.ExpertInfo;
import tpp.profixer.customer.data.model.api.response.Lesson;
import tpp.profixer.customer.data.model.api.response.LoginResponse;
import tpp.profixer.customer.data.model.api.response.Notification;
import tpp.profixer.customer.data.model.api.response.Province;
import tpp.profixer.customer.data.model.api.response.Review;
import tpp.profixer.customer.data.model.api.response.ReviewStar;

public interface ApiService {
    @POST("/api/token")
    @Headers({"BasicAuth: 1"})
    Observable<LoginResponse> login(@Body LoginRequest request);

    @POST("/v1/student/signup")
    @Headers({"BasicAuth: 1"})
    Observable<ResponseWrapper> signup(@Body SignupRequest request);
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
    Observable<ResponseWrapper<Lesson>> getLessonDetails(@Path("lesson_id") Long lessonId);

    @DELETE("/v1/cart-item/delete/{cart_item_id}")
    Observable<ResponseWrapper> deleteCartItem(@Path("cart_item_id") Long cartItemId);

    @DELETE("/v1/cart-item/delete-all")
    Observable<ResponseWrapper> deleteAllCartItem();

    @GET("/v1/student/my-course")
    Observable<ResponseWrapper<ResponseListObj<Course>>> getMyCourses(@Query("isFinished") Boolean isFinished);

    @POST("/v1/completion/create")
    Observable<ResponseWrapper> completeLesson(@Body CompleteLessonRequest request);

    @GET("/v1/course/client-list")
    Observable<ResponseWrapper<ResponseListObj<Course>>> getCoursesByCategory(@Query("categoryId") Long categoryId, @Query("page") Integer page, @Query("size") Integer size, @Query("isFree") Boolean isFree);

    @GET("/v1/slideshow/list")
    Observable<ResponseWrapper<ResponseListObj<Slide>>> getSlideShow(@Query("status") Integer status);

    @GET("/v1/course/client-list")
    Observable<ResponseWrapper<ResponseListObj<Course>>> searchCourses(@Query("query") String query, @Query("isFree") Boolean isFree, @Query("fieldId") Long fieldId, @Query("size") Integer size);
    @GET("/v1/course/client-list")
    Observable<ResponseWrapper<ResponseListObj<Course>>> getCoursesByExpert(@Query("expertId") Long expertId);

    @GET("/v1/expert/client-get/{expert_id}")
    Observable<ResponseWrapper<ExpertInfo>> getExpertInfo(@Path("expert_id") Long expertId);

    @PUT("/v1/student/update-profile")
    Observable<ResponseWrapper>  updateProfile(@Body UpdateProfileRequest request);

    @GET("/v1/nation/auto-complete")
    Observable<ResponseWrapper<ResponseListObj<Province>>>  getNation(@Query("kind") Integer kind, @Query("parentId") Long parentId);

    @Multipart
    @POST("v1/file/upload")
    @Headers({"isMedia: 1"})
    Observable<ResponseWrapper<AvatarPathResponse>> uploadFile(@Part("type") RequestBody type, @Part MultipartBody.Part file);

    @GET("/v1/notification/my-notification")
    Observable<ResponseWrapper<ResponseListObj<Notification>>>  getNotification(@Query("appKind") Integer appKind, @Query("state") Integer state);

    @PUT("/v1/notification/read-all")
    Observable<ResponseWrapper>  readAll(@Query("appKind") Integer appKind);

    @DELETE("/v1/notification/delete-all")
    Observable<ResponseWrapper>  deleteAllNotification(@Query("appKind") Integer appKind);
}
