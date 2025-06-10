package tpp.easy.learning.data.remote;

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
import tpp.easy.learning.data.model.api.ResponseListObj;
import tpp.easy.learning.data.model.api.ResponseWrapper;
import tpp.easy.learning.data.model.api.request.BankInfo;
import tpp.easy.learning.data.model.api.request.BookingRequest;
import tpp.easy.learning.data.model.api.request.CompleteLessonRequest;
import tpp.easy.learning.data.model.api.request.ForgetRequest;
import tpp.easy.learning.data.model.api.request.NewPassRequest;
import tpp.easy.learning.data.model.api.request.ReviewRequest;
import tpp.easy.learning.data.model.api.request.SignupRequest;
import tpp.easy.learning.data.model.api.request.Slide;
import tpp.easy.learning.data.model.api.request.UpdateProfileRequest;
import tpp.easy.learning.data.model.api.response.AvatarPathResponse;
import tpp.easy.learning.data.model.api.response.BookingResponse;
import tpp.easy.learning.data.model.api.response.Cart;
import tpp.easy.learning.data.model.api.request.LoginRequest;
import tpp.easy.learning.data.model.api.request.RequestCourse;
import tpp.easy.learning.data.model.api.response.CartInfo;
import tpp.easy.learning.data.model.api.response.CategoryCourse;
import tpp.easy.learning.data.model.api.response.Course;
import tpp.easy.learning.data.model.api.response.DeepLink;
import tpp.easy.learning.data.model.api.response.Expert;
import tpp.easy.learning.data.model.api.response.ExpertInfo;
import tpp.easy.learning.data.model.api.response.ForgetResponse;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.data.model.api.response.LoginResponse;
import tpp.easy.learning.data.model.api.response.Notification;
import tpp.easy.learning.data.model.api.response.PaymentInfo;
import tpp.easy.learning.data.model.api.response.Province;
import tpp.easy.learning.data.model.api.response.Qrcode;
import tpp.easy.learning.data.model.api.response.Review;
import tpp.easy.learning.data.model.api.response.ReviewStar;
import tpp.easy.learning.data.model.api.response.Status;

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
    @POST("/v1/completion/complete-lesson")
    Observable<ResponseWrapper> finishedLesson(@Body CompleteLessonRequest request);
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

    @GET("/v1/review/my-review")
    Observable<ResponseWrapper<ResponseListObj<Review>>> getMyReview(@Query("courseId") Long courseId);

    @POST("/v1/review/create")
    Observable<ResponseWrapper>  review(@Body ReviewRequest request);

    @POST("/v1/account/request_forget_password")
    Observable<ResponseWrapper<ForgetResponse>>  requestForgetPassword(@Body ForgetRequest request);
    @POST("/v1/account/forget_password")
    Observable<ResponseWrapper>  newPassword(@Body NewPassRequest request);

    @POST("/v1/booking/create")
    Observable<ResponseWrapper<PaymentInfo>>  createBooking(@Body BookingRequest request);

    @POST("/api/web/{paymentLinkId}/check-status/")
    @Headers({"isPayos: 1"})
    Observable<ResponseWrapper<Status>>  checkStatus(@Path("paymentLinkId") String paymentLinkId);

    @POST("/v2/generate")
    @Headers({"isBank:1","X-Api-Key:5b310ff0-2683-4cb5-a3aa-227b8179aec3","X-Client-Id:72a88234-f90e-45af-b4f1-bafdc9d7a01a"})
    Observable<ResponseWrapper<Qrcode>> generateQrcode(@Body BankInfo request);

    @GET("/v2/android-app-deeplinks")
    @Headers({"isBank:1"})
    Observable<ResponseListObj<DeepLink>> getDeepLinks();

    @GET("/v1/booking/client-get/{code}")
    Observable<ResponseWrapper<BookingResponse>>  getBooking(@Path("code") String code);
}
