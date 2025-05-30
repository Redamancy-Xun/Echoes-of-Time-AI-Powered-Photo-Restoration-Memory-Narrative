package fun.redamancy.echo.backend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.redamancy.echo.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @description 针对表【user(用户信息)】的数据库操作Mapper
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE id =#{id}")
    User getUserById(String id);

    @Select("SELECT id FROM user;")
    List<String> selectAllUserId();
}




