package kr.co.scheduler.scheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TaskLogReqDTO {

    private String title;
    private String content;
    private String taskCategory;
    private String subTaskCategory;
}
