package kr.co.scheduler.scheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class ProjectReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

        private String title;
        private String description;
        private String goal;
        private LocalDate startPRJ;
        private LocalDate endPRJ;
        private List<TaskReqDTO.CREATE> tasks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

        private String title;
        private String description;
        private String goal;
        private LocalDate startPRJ;
        private LocalDate endPRJ;
        private List<TaskReqDTO.UPDATE> updatedTasks;
        private List<TaskReqDTO.CREATE> addedTasks;
        private List<TaskReqDTO.DELETE> deletedTasks;
    }
}


