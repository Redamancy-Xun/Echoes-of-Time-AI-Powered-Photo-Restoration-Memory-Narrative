package fun.redamancy.echo.backend.controller.user;

import fun.redamancy.echo.backend.common.Result;
import fun.redamancy.echo.backend.controller.user.response.UserInfoResponse;
import fun.redamancy.echo.backend.exception.MyException;
import fun.redamancy.echo.backend.service.UserService;
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

@Api(tags = "用户操作相关接口")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param email 邮箱
     * @param password 密码
     * @return LoginInfo
     * @throws Exception 异常
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ApiOperation(value = "登录", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public Result login(@NotNull @RequestParam("email") String email,
                        @NotNull @RequestParam("password") String password) {
        try {
            return Result.success(userService.login(email, password));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 检测登录状态
     * @return Integer
     */
    @GetMapping(value = "/checkLogin", produces = "application/json")
    public Result checkLogin() throws MyException {
        try {
            return Result.success(userService.checkLogin());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

//    /**
//     * 登出
//     * @return UserInfoResponse
//     */
//    @PostMapping(value = "/logout", produces = "application/json")
//    @ApiOperation(value = "登出", response = UserInfoResponse.class)
//    public Result logout() {
//        try {
//            return Result.success(userService.logout());
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.getMessage());
//        }
//    }

    /**
     * 注册
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     */
    @PostMapping(value = "/signup", produces = "application/json")
    @ApiOperation(value = "注册", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public Result signup(@NotNull @RequestParam("username") String username,
                         @NotNull @RequestParam("email") String email,
                         @NotNull @RequestParam("password") String password) {
        try {
            return Result.success(userService.signup(username, email, password));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }
//
//    /**
//     * 获取用户信息
//     * @return UserInfoResponse
//     */
//    @GetMapping(value = "/getUserInfo", produces = "application/json")
//    @ApiOperation(value = "获取用户信息", response = UserInfoResponse.class)
//    public Result getUserInfo() {
//        try {
//            return Result.success(userService.getUserInfo());
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            e.printStackTrace();
//            return Result.fail(e.getMessage());
//        }
//    }
//
//    /**
//     * 更新用户信息
//     * @param updateUserInfoRequest
//     * @return UserInfoResponse
//     */
//    @PostMapping(value = "/updateUserInfo", produces = "application/json")
//    @ApiOperation(value = "更新用户信息", response = UserInfoResponse.class)
//    public Result updateUserInfo(@NotNull @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
//        try {
//            return Result.success(userService.updateUserInfo(updateUserInfoRequest));
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.getMessage());
//        }
//    }
//
//    /**
//     * 更新用户的建议信息
//     * @return UserInfoResponse
//     */
//    @PostMapping(value = "/updateSuggestion", produces = "application/json")
//    @ApiOperation(value = "更新用户的建议信息")
//    public Result updateSuggestion() {
//        try {
//            return Result.success(userService.updateOneAdvice());
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.getMessage());
//        }
//    }
//
//    /**
//     * 修改用户密码
//     * @param oldPassword
//     * @param newPassword
//     * @return UserInfoResponse
//     */
//    @PostMapping(value = "/changePassword", produces = "application/json")
//    @ApiOperation(value = "修改用户密码", response = UserInfoResponse.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, paramType = "query", dataType = "String")
//    })
//    public Result changePassword(@NotNull @RequestParam("oldPassword") String oldPassword,
//                                 @NotNull @RequestParam("newPassword") String newPassword) {
//        try {
//            return Result.success(userService.changePassword(oldPassword, newPassword));
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.getMessage());
//        }
//    }
//
//    /**
//     * 上传用户头像
//     * @param file
//     * @return String
//     */
//    @PostMapping(value = "/uploadPortrait", produces = "application/json")
//    @ApiOperation(value = "上传用户头像", response = String.class)
//    @ApiImplicitParam(name = "file", value = "图片文件", required = true, paramType = "formData", dataType = "file")
//    public Result uploadPortrait(@RequestParam("file") MultipartFile file) {
//        try {
//            return Result.success(userService.uploadPortrait(file));
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.toString());
//        }
//    }
}
