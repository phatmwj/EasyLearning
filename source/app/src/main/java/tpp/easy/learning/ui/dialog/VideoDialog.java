package tpp.easy.learning.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import tpp.easy.learning.R;
import tpp.easy.learning.data.model.api.response.Lesson;
import tpp.easy.learning.databinding.DialogVideoBinding;

public class VideoDialog extends Dialog {
    public ObservableField<Lesson> currentLesson = new ObservableField<>();
    private ExoPlayer player;
    @Getter
    @Setter
    private ConfirmDialog.ConfirmListener listener;

    public interface ConfirmListener{
        void confirm();
    }
    public VideoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVideoBinding viewBinding = DialogVideoBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(viewBinding.getRoot());
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        player = new ExoPlayer.Builder(getContext()).build();
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
//        setupFullscreenToggle();
        currentLesson.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Lesson lesson = currentLesson.get();
                if(lesson != null && lesson.getKind() == 2){
                    setVideoPlay(lesson.getVideoUrl());
                }
            }
        });

        setVideoPlay(currentLesson.get().getVideoUrl());
        viewBinding.setD(this);
        viewBinding.executePendingBindings();

    }
    private void setVideoPlay(String url){
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    public void dismiss() {
        if (player != null) {
            player.release();
        }
        super.dismiss();
    }
}
