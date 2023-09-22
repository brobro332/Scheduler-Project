package kr.co.scheduler.global.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TargetTokenReqDTO {

    private String targetToken;

    TargetTokenReqDTO() {

    }
}
