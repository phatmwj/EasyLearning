package tpp.profixer.customer.ui.lesson;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.google.android.material.tabs.TabLayoutMediator;

import tpp.profixer.customer.R;
import tpp.profixer.customer.databinding.ActivityLessonBinding;
import tpp.profixer.customer.di.component.ActivityComponent;
import tpp.profixer.customer.ui.base.activity.BaseActivity;
import tpp.profixer.customer.ui.lesson.adapter.ViewPagerAdapter;

public class LessonActivity extends BaseActivity<ActivityLessonBinding, LessonViewModel> {

    private ExoPlayer player;
    private ViewPagerAdapter viewPagerAdapter;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.courseId = getIntent().getLongExtra("course_id", -1);

        viewPagerAdapter = new ViewPagerAdapter(this, viewModel.courseId);
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
        setVideoPlay("https://video.edward.io.vn/hls/media/general/7049999380021248/COURSE/8395895960731648/8395898804961280/livestream.m3u8");
        setupFullscreenToggle();
        viewModel.course.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

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
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_BUFFERING) {
                    viewBinding.playerView.findViewById(R.id.exo_play_pause).setVisibility(View.GONE);
                    viewBinding.playerView.findViewById(R.id.exo_progress_placeholder).setVisibility(View.VISIBLE);
                } else if (state == Player.STATE_READY || state == Player.STATE_ENDED) {
                    viewBinding.playerView.findViewById(R.id.exo_play_pause).setVisibility(View.VISIBLE);
                    viewBinding.playerView.findViewById(R.id.exo_progress_placeholder).setVisibility(View.GONE);
                }
            }
        });
        viewBinding.playerView.setPlayer(player);
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
}
