package kr.co.scheduler.community.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

    @NotNull
    private String title;
    @NotNull
    private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {
        @NotNull
        private String title;
        @NotNull
        private String content;
    }
}


