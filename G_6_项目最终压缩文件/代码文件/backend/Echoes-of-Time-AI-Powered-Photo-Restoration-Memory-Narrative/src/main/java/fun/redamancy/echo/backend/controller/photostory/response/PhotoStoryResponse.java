package fun.redamancy.echo.backend.controller.photostory.response;

import com.baomidou.mybatisplus.annotation.IdType;
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
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@ApiModel("PhotoStoryResponse 照片故事响应对象")
public class PhotoStoryResponse {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("照片URL")
    private String photoUrl;

    @ApiModelProperty("故事标题")
    private String title;

    @ApiModelProperty("故事描述")
    private String description;

    @ApiModelProperty("故事标签")
    private String tags;

    @ApiModelProperty("修复时间")
    private LocalDateTime repairTime;

    @ApiModelProperty("照片故事图片")
    private List<String> photoStoryImageUrls;

    public PhotoStoryResponse(Integer id, String userId, String photoUrl, String title, String description, String tags, LocalDateTime repairTime) {
        this.id = id;
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.repairTime = repairTime;

        this.photoStoryImageUrls = null; // 初始化为空列表或null
    }

    public PhotoStoryResponse(PhotoStory photoStory) {
        this.id = photoStory.getId();
        this.userId = photoStory.getUserId();
        this.photoUrl = photoStory.getPhotoUrl();
        this.title = photoStory.getTitle();
        this.description = photoStory.getDescription();
        this.tags = photoStory.getTags();
        this.repairTime = photoStory.getRepairTime();

        this.photoStoryImageUrls = null; // 初始化为空列表或null
    }

    public PhotoStoryResponse(PhotoStory photoStory, List<String> photoStoryImageUrls) {
        this.id = photoStory.getId();
        this.userId = photoStory.getUserId();
        this.photoUrl = photoStory.getPhotoUrl();
        this.title = photoStory.getTitle();
        this.description = photoStory.getDescription();
        this.tags = photoStory.getTags();
        this.repairTime = photoStory.getRepairTime();

        this.photoStoryImageUrls = photoStoryImageUrls;
    }


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

//    public PhotoStoryResponse(User user) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//
//        this.sessionId = null;
//    }
//
//    public PhotoStoryResponse(User user, String sessionId) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//
//        this.sessionId = sessionId;
//    }
}
