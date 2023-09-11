package kr.co.scheduler.scheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TaskReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

        public String idx;
        public String task;
        public List<String> subTasks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

        public String idx;
        public String task;
        public List<String> subTasks;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DELETE {

        public String idx;

        public DELETE() {
        }
    }
}
