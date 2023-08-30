package kr.co.scheduler.community.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class PostReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

    private String title;

    private String content;
    }

/*    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

    }*/
}


