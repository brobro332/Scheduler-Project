package kr.co.scheduler.community.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReplyReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

    private String reply;

    private Long parent_reply_id;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

        private String updateReply;

        // 기본 생성자 추가
        public UPDATE() {
        }
    }
}


