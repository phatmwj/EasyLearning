package tpp.profixer.customer.data.model.api.response;

import android.annotation.SuppressLint;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomLesson {
    private Lesson lesson;
    private List<Lesson> lessons;
    @SuppressLint("DefaultLocale")
    public String getTimeVideo() {
        int time = 0;
        for(Lesson lesson1 : lessons){
            time += lesson1.getVideoDuration();
        }
        if (time == 0) {
            return "00:00";
        }
        int hours = time / 3600;
        int minutes = time / 60 - hours * 60;
        int seconds = time % 3600 - minutes * 60;
        if (hours == 0) {
            return String.format("%02d:%02d", minutes, seconds);
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
