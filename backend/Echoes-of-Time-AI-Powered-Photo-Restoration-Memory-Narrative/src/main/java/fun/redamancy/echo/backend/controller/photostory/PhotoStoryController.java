package fun.redamancy.echo.backend.controller.photostory;

import fun.redamancy.echo.backend.common.Result;
import fun.redamancy.echo.backend.controller.photostory.response.PhotoStoryResponse;
import fun.redamancy.echo.backend.controller.user.response.UserInfoResponse;
import fun.redamancy.echo.backend.entity.PhotoStory;
import fun.redamancy.echo.backend.exception.MyException;
import fun.redamancy.echo.backend.service.PhotoStoryService;
import fun.redamancy.echo.backend.util.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Api(tags = "照片故事相关接口")
@RestController
@RequestMapping("/photoStory")
@Slf4j
public class PhotoStoryController {

    @Autowired
    private PhotoStoryService photoStoryService;

    /**
     * 调用GFPGAN接口，修复照片
     * @param photoUrl 照片地址
     * @return repairUrl 修复后的照片地址
     * @throws MyException 自定义异常
     */
    @PostMapping(value = "/repairPhoto")
    @ApiOperation(value = "修复照片", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoUrl", value = "照片地址", required = true, paramType = "query", dataType = "String")})
    public Result repairPhoto(@RequestParam("photoUrl") @NotNull String photoUrl) {
        try {
            String repairUrl = photoStoryService.repairPhoto(photoUrl);
            return Result.success(repairUrl);
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 创建照片故事
     * @param photoUrl 照片地址
     * @return AdviceAction
     * @throws MyException 自定义异常
     */
    @PostMapping(value = "/createPhotoStory")
    @ApiOperation(value = "创建照片故事", response = PhotoStory.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoUrl", value = "照片地址", required = true, paramType = "query", dataType = "String")})
    public Result sore(@RequestParam("photoUrl") String photoUrl) {
        try {
            PhotoStoryResponse photoStoryResponse = photoStoryService.createPhotoStory(photoUrl);
            return Result.success(photoStoryResponse);
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新照片故事
     * @param id 照片故事id
     * @param title 照片故事标题
     * @param description 照片故事描述
     * @param tags 照片故事标签
     * @return PhotoStory
     * @throws MyException 自定义异常
     */
    @PostMapping(value = "/updatePhotoStory")
    @ApiOperation(value = "更新照片故事", response = PhotoStory.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "照片故事id", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "title", value = "照片故事标题", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "照片故事描述", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "tags", value = "照片故事标签", required = true, paramType = "query", dataType = "String")
    })
    public Result updatePhotoStory(@RequestParam("id") Integer id,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("tags") String tags) {
        try {
            PhotoStoryResponse photoStoryResponse = photoStoryService.updatePhotoStory(id, title, description, tags);
            return Result.success(photoStoryResponse);
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查找照片故事列表
     * @return List<PhotoStory>
     * @throws MyException 自定义异常
     */
    @GetMapping(value = "/findPhotoStoryByUserId")
    @ApiOperation(value = "查找照片故事列表", response = PhotoStory.class)
    public Result findPhotoStoryByUserId() {
        try {
            return Result.success(photoStoryService.findPhotoStoryByUserId());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据id查找照片故事
     * @param id 照片故事id
     * @return PhotoStory
     * @throws MyException 自定义异常
     */
    @GetMapping(value = "/findPhotoStoryById")
    @ApiOperation(value = "根据id查找照片故事", response = PhotoStory.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "照片故事id", required = true, paramType = "query", dataType = "Integer")
    })
    public Result findPhotoStoryById(@RequestParam("id") Integer id) {
        try {
            return Result.success(photoStoryService.findPhotoStoryById(id));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

}
