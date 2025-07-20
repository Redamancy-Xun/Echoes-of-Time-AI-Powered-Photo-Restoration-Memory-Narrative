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

@ApiModel("photo_story_image 照片故事图片")
@TableName(value ="photo_story_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoStoryImage {

    @Id
    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("照片故事ID")
    @TableField(value = "photo_story_id")
    private Integer photoStoryId;

    @ApiModelProperty("图片URL")
    @TableField(value = "photo_url")
    private String photoUrl;
}
