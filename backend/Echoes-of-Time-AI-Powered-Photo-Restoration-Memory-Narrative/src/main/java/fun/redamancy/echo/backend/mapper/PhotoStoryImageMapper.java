package fun.redamancy.echo.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.redamancy.echo.backend.entity.PhotoStory;
import fun.redamancy.echo.backend.entity.PhotoStoryImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description 照片故事图片Mapper
 */
@Mapper
public interface PhotoStoryImageMapper extends BaseMapper<PhotoStoryImage> {

//    /**
//     * 查询历史练习与评价记录
//     * @param userId 用户id
//     * @return List<PhotoStory>
//     */
//    @Select("SELECT * FROM score_action WHERE user_id = #{userId}")
//    List<PhotoStory> selectList(String userId);
}
