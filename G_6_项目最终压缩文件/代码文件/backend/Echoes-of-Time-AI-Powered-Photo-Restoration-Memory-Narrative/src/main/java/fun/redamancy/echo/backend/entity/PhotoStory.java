package fun.redamancy.echo.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDateTime;

@ApiModel("photo_story 照片故事")
@TableName(value ="photo_story")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoStory {

    @Id
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField(value = "user_id")
    private String userId;

    @ApiModelProperty("照片URL")
    @TableField(value = "photo_url")
    private String photoUrl;

    @ApiModelProperty("故事标题")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty("故事描述")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty("故事标签")
    @TableField(value = "tags")
    private String tags;

    @ApiModelProperty("修复时间")
    @TableField(value = "repair_time")
    private LocalDateTime repairTime;
}
