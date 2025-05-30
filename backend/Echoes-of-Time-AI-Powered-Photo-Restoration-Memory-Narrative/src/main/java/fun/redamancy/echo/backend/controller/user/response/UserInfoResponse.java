package fun.redamancy.echo.backend.controller.user.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import fun.redamancy.echo.backend.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@ApiModel("详细用户信息")
public class UserInfoResponse {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("SessionId")
    private String sessionId;


//    public UserInfoResponse(User user, List<ScoreAction> history) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.gender = user.getGender();
//        this.age = user.getAge();
//        this.nation = user.getNation();
//        this.telephone = user.getTelephone();
//        this.portrait = user.getPortrait();
//        this.role = user.getRole();
//        this.advice = user.getAdvice();
//
//        this.history = history;
//
//        this.sessionId = null;
//    }

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();

        this.sessionId = null;
    }

    public UserInfoResponse(User user, String sessionId) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();

        this.sessionId = sessionId;
    }
}
