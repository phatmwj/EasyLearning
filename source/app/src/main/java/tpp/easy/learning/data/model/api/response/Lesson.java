package tpp.easy.learning.data.model.api.response;

import android.annotation.SuppressLint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private Long id;
    private Integer status;
    private String name;
    private Integer kind;
    private Integer ordering;
    private Boolean isDone;
    private Integer videoDuration;
    private Integer state;
    private Boolean isPreview;
    private String thumbnail;
    private String videoUrl;
    private Integer secondProgress;
    private Course course;
    private String content;

    @SuppressLint("DefaultLocale")
    public String getTimeVideo() {
        if (videoDuration == null || videoDuration == 0) {
            return "00:00";
        }
        int hours = videoDuration / 3600;
        int minutes = videoDuration / 60 - hours * 60;
        int seconds = videoDuration % 3600 - minutes * 60;
        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
