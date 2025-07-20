package fun.redamancy.echo.backend.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import fun.redamancy.echo.backend.entity.PhotoStory;
import fun.redamancy.echo.backend.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * session缓存实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("SessionData 会话实体")
public class SessionData  implements Serializable {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    public SessionData(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}
