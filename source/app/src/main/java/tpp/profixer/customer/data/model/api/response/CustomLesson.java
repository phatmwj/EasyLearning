package tpp.profixer.customer.data.model.api.response;

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
}
