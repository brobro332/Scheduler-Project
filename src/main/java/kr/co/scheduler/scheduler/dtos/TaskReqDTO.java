package kr.co.scheduler.scheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class TaskReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

        public String idx;
        public String task;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

        public String idx;
        public String task;
    }
}
