package fun.redamancy.echo.backend.service;

import fun.redamancy.echo.backend.controller.photostory.response.PhotoStoryResponse;
import fun.redamancy.echo.backend.entity.PhotoStory;
import fun.redamancy.echo.backend.exception.MyException;

import java.util.List;

/**
 * 建议操作相关接口
 * @author Redamancy
 * @description 建议行为相关接口
 * @createDate 2024-11-9 22:39:04
 */
public interface PhotoStoryService {

    // 创建照片故事
    PhotoStoryResponse createPhotoStory(String photoUrl) throws MyException;

    // 更新照片故事
    PhotoStoryResponse updatePhotoStory(Integer id, String title, String description, String tags) throws Exception;

    // 查找照片故事列表
    List<PhotoStoryResponse> findPhotoStoryByUserId() throws MyException;

    // 根据id查找照片故事
    PhotoStoryResponse findPhotoStoryById(Integer id) throws MyException;

    String repairPhoto(String photoUrl) throws MyException;
}
