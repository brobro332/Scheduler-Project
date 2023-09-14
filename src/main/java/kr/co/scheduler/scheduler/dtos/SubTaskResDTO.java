package kr.co.scheduler.scheduler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SubTaskResDTO {

    public Long id;
    public String name;

    public SubTaskResDTO() {

    }
}
