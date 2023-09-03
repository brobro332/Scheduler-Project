package kr.co.scheduler.community.dtos;

import kr.co.scheduler.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CommentReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

    private User user;
    private String comment;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

        private String comment;
    }
}


