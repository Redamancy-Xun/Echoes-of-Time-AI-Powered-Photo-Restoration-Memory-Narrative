package fun.redamancy.echo.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.redamancy.echo.backend.entity.PhotoStory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description 针对表【score_action(练习与评价记录)】的数据库操作Mapper
 */
@Mapper
public interface PhotoStoryMapper extends BaseMapper<PhotoStory> {

//    /**
//     * 查询历史练习与评价记录
//     * @param userId 用户id
//     * @return List<PhotoStory>
//     */
//    @Select("SELECT * FROM score_action WHERE user_id = #{userId}")
//    List<PhotoStory> selectList(String userId);
}
