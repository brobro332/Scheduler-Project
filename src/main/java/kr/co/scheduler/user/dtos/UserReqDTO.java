package kr.co.scheduler.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.co.scheduler.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CREATE {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=2, message = "이름이 너무 짧습니다.")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = "10~11자리의 숫자만 입력가능합니다")
    private String phone;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8자리 이상으로 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;
    
    private String checkedPassword;

    private Role role;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UPDATE {

            @NotBlank(message = "이름을 입력해주세요.")
            @Size(min=2, message = "이름이 너무 짧습니다.")
            private String name;

            @NotBlank(message = "전화번호를 입력해주세요")
            @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = "10~11자리의 숫자만 입력가능합니다")
            private String phone;

            @NotNull(message = "비밀번호를 입력해주세요.")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
                    message = "비밀번호는 8자리 이상으로 알파벳, 숫자, 특수문자를 포함해야합니다.")
            private String password;

            private String checkedPassword;
    }
}


