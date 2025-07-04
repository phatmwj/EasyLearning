package tpp.easy.learning.data.model.api;

import java.util.List;

import lombok.Data;

@Data
public class ResponseListObj<T> {
    private List<T> content;
    private List<T> apps;
    private Integer page;
    private Integer totalPages;
    private Integer totalElements;
    private Long totalUnread;
}
