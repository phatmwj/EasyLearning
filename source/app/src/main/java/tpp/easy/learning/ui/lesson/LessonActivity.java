package tpp.easy.learning.ui.lesson;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.webkit.WebSettings;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.CustomLesson;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.databinding.ActivityLessonBinding;
import tpp.easy.learning.di.component.ActivityComponent;
import tpp.easy.learning.ui.base.activity.BaseActivity;
import tpp.easy.learning.ui.lesson.adapter.ViewPagerAdapter;

public class LessonActivity extends BaseActivity<ActivityLessonBinding, LessonViewModel> {

    private ExoPlayer player;
    private ViewPagerAdapter viewPagerAdapter;
    private List<CustomLesson> customLessons = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_lesson;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.courseId = getIntent().getLongExtra("course_id", -1);
        viewModel.expertId = getIntent().getLongExtra("expert_id", -1);

        WebSettings webSettings = viewBinding.webview.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webSettings.setJavaScriptEnabled(true);

        viewBinding.webview.setHorizontalScrollBarEnabled(false);

        viewBinding.webview.setOnTouchListener(new View.OnTouchListener() {
            float m_downX;
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        m_downX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        event.setLocation(m_downX, event.getY());
                        break;
                    }

                }
                return false;
            }
        });

        viewPagerAdapter = new ViewPagerAdapter(this, viewModel.courseId, viewModel.expertId);
        viewBinding.viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager,
                (tab, position) -> {
                    if (position == 0){
                        tab.setText("Giới thiệu");
                    }else if (position == 1){
                        tab.setText("Nội dung");
                    }else if (position == 2){
                        tab.setText("Đánh giá");
                    }
                }
        ).attach();

        player = new ExoPlayer.Builder(this).build();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_BUFFERING) {
                    viewBinding.playerView.findViewById(R.id.exo_play_pause).setVisibility(View.GONE);
                    viewBinding.playerView.findViewById(R.id.exo_progress_placeholder).setVisibility(View.VISIBLE);
                } else if (state == Player.STATE_READY || state == Player.STATE_ENDED) {
                    viewBinding.playerView.findViewById(R.id.exo_play_pause).setVisibility(View.VISIBLE);
                    viewBinding.playerView.findViewById(R.id.exo_progress_placeholder).setVisibility(View.GONE);
                    if(state == Player.STATE_ENDED){
                        Integer a = (int) (player.getCurrentPosition()/1000);
                        viewModel.completeLesson(a);
                        handler.removeCallbacks(updateRunnable);
                    }else {
                        handler.post(updateRunnable);
                    }
                }
            }
        });
        viewBinding.playerView.setPlayer(player);
        setupFullscreenToggle();

        viewModel.currentLesson.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Lesson lesson = viewModel.currentLesson.get();
                if(lesson != null && lesson.getKind() == 1){
                    String htmlContentWithCss = addCssToHtmlContent(lesson.getContent());
                    viewBinding.webview.loadData(htmlContentWithCss, "text/html", "UTF-8");
                    viewModel.finishedLesson();
                }
                if(lesson != null && lesson.getKind() == 2){
                    setVideoPlay(lesson.getVideoUrl());
                }
            }
        });
        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                List<Lesson> lessons = viewModel.course.get().getLessons();
                customLessons.clear();
                if(lessons != null){
                    lessons.sort(Comparator.comparingInt(Lesson::getOrdering));
                    for(int i = 0; i < lessons.size(); i++){
                        Lesson lesson = lessons.get(i);
                        if(lesson.getKind() == 3){
                            CustomLesson customLesson = new CustomLesson();
                            customLesson.setLesson(lesson);
                            customLesson.setLessons(new ArrayList<>());
                            customLessons.add(customLesson);
                        }else {
                            if(customLessons.isEmpty()) return;
                            CustomLesson customLesson = customLessons.get(customLessons.size() - 1);
                            customLesson.getLessons().add(lesson);
                        }
                    }
                }
                if(!customLessons.isEmpty() && !customLessons.get(0).getLessons().isEmpty()){
                    viewModel.getLessonDetails(customLessons.get(0).getLessons().get(0).getId());
                }
            }
        });
        viewModel.getCourseDetails();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isChangingConfigurations() && player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Ví dụ: full màn hình thì ẩn action bar
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            getSupportActionBar().hide();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            getSupportActionBar().show();
//        }
    }

    private void setVideoPlay(String url){
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    private void setupFullscreenToggle() {
        ImageView fullscreenButton = viewBinding.playerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(v -> toggleFullscreen(fullscreenButton));
    }

    private void toggleFullscreen(ImageView fullscreenButton) {
        if (viewModel.isFullscreen.get()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            fullscreenButton.setImageResource(R.drawable.exo_icon_fullscreen_enter);
            viewModel.isFullscreen.set(false);
            // Hiện lại status bar và navigation bar
            // Hiện status bar lại
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(true);
                getWindow().getInsetsController().show(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fullscreenButton.setImageResource(R.drawable.exo_icon_fullscreen_exit);
            viewModel.isFullscreen.set(true);
            // Ẩn status bar và navigation bar
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
                WindowInsetsController controller = getWindow().getInsetsController();
                if (controller != null) {
                    controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            }
        }
    }

    public void getLessonDetails(Long lessonId){
        viewModel.getLessonDetails(lessonId);
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                int currentPosition = (int) player.getCurrentPosition()/1000;

                if (currentPosition > 0 && currentPosition % 5 == 0) {
                    viewModel.completeLesson(currentPosition);
                }
            }
            handler.postDelayed(this, 1000);
        }
    };

    private String addCssToHtmlContent(String htmlContent) {
        String css = "<style>" +
                "img { max-width: 100%; height: auto; display: block; margin: 10px auto; }" +
                "body { padding: 0 10px; font-family: sans-serif; font-size: 16px; }" +
                "</style>";

        String headTag = "<head>";
        int headIndex = htmlContent.indexOf(headTag);

        if (headIndex != -1) {
            // Nếu có <head>, chèn sau nó
            headIndex += headTag.length();
            return htmlContent.substring(0, headIndex) + css + htmlContent.substring(headIndex);
        } else {
            // Nếu không có <head>, tự thêm cả <head> + CSS
            return "<head>" + css + "</head>" + htmlContent;
        }
    }

}
