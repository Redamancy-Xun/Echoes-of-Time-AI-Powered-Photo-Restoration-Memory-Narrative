package fun.redamancy.echo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fun.redamancy.echo.backend.common.CommonConstants;
import fun.redamancy.echo.backend.controller.photostory.response.PhotoStoryResponse;
import fun.redamancy.echo.backend.entity.PhotoStory;
import fun.redamancy.echo.backend.entity.PhotoStoryImage;
import fun.redamancy.echo.backend.exception.EnumExceptionType;
import fun.redamancy.echo.backend.exception.MyException;
import fun.redamancy.echo.backend.mapper.PhotoStoryImageMapper;
import fun.redamancy.echo.backend.mapper.PhotoStoryMapper;
import fun.redamancy.echo.backend.service.PhotoStoryService;
import fun.redamancy.echo.backend.service.impl.imagegeneralize.ImageGeneralize;
import fun.redamancy.echo.backend.service.impl.imagerecognize.ImageRecognize;
import fun.redamancy.echo.backend.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 建议操作相关接口
 * @author Redamancy
 * @description 建议行为相关接口
 * @createDate 2024-11-9 22:39:04
 */
@Service
public class PhotoStoryImpl implements PhotoStoryService {

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private PhotoStoryMapper photoStoryMapper;

    @Autowired
    private PhotoStoryImageMapper photoStoryImageMapper;

    private static final String API_URL = "https://api.replicate.com/v1/predictions";
    private static final String API_TOKEN = "r8_06IJwKm5QlsqZOCLMUO6dbvldOIR3pY3qd28M";
    private static final String DOWNLOAD_DIR = "/var/www/html/echo/image/";

    public static String callGFPGANAPI(String imageUrl) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_TOKEN);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Prefer", "wait");

        connection.setDoOutput(true);

        String requestBody = "{\n" +
                "    \"version\": \"tencentarc/gfpgan:0fbacf7afc6c144e5be9767cff80f25aff23e52b0708f17e20f9879b2f21516c\",\n" +
                "    \"input\": {\n" +
                "        \"img\": \"" + imageUrl + "\",\n" +
                "        \"scale\": 2,\n" +
                "        \"version\": \"v1.4\"\n" +
                "    }\n" +
                "}";

        System.out.println("请求体: " + requestBody);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        System.out.println("请求发送完毕，等待响应...");

        int responseCode = connection.getResponseCode();
        System.out.println("响应状态码: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            System.out.println("响应成功，状态码: " + responseCode);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("响应内容: " + response.toString());
                return response.toString();
            }
        } else {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.err.println("HTTP error code: " + responseCode + "\n" + response.toString());
                throw new IOException("HTTP error code: " + responseCode + "\n" + response.toString());
            }
        }
    }

    public static String parseOutputUrl(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        System.out.println("解析JSON响应: " + jsonObject);
        return jsonObject.get("output").getAsString();
    }

    public static String downloadImage(String srcImageUrl, String imageUrl, String saveDir) throws IOException {
        URL url = new URL(imageUrl);
        String fileName = srcImageUrl.substring(srcImageUrl.lastIndexOf("/") + 1) + "-repaired";
        String savePath = saveDir + fileName;

        System.out.println("下载图片: " + savePath);

        try (InputStream in = url.openStream();
             OutputStream out = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        return CommonConstants.IMAGE_PATH + fileName;
    }

    /**
     * 调用GFPGAN接口，修复照片
     * @param photoUrl 照片地址
     * @return repairUrl 修复后的照片地址
     * @throws MyException 自定义异常
     */
    @Override
    public String repairPhoto(String photoUrl) throws MyException {

        sessionUtils.refreshData(null);

        String savedFilePath = "http://www.sei.ecnu.edu.cn/_upload/tpl/11/e5/4581/template4581/Assets/images/banner-ny.jpg";

        try {
            // 确保目录存在
//            Files.createDirectories(Paths.get(DOWNLOAD_DIR));

            // 1. 调用API获取预测结果
            String responseJson = callGFPGANAPI(photoUrl);

            System.out.println("API响应: " + responseJson);

            // 2. 解析JSON获取输出图片URL
            String outputImageUrl = parseOutputUrl(responseJson);

            System.out.println("输出图片URL: " + outputImageUrl);

            // 3. 下载图片到指定目录
            savedFilePath = downloadImage(photoUrl, outputImageUrl, DOWNLOAD_DIR);

            System.out.println("图片已成功下载到: " + savedFilePath);

//            // TODO
//            System.out.println("图片已成功下载到: " + outputImageUrl);
//            savedFilePath = outputImageUrl;
        } catch (Exception e) {
            e.getMessage();
            System.err.println("调用GFPGAN接口失败: " + e.getMessage());
            e.printStackTrace();
        }

        // 返回修复后的照片地址
        return savedFilePath;
    }


    /**
     * 创建照片故事
     * @param photoUrl 照片地址
     * @return AdviceAction
     * @throws MyException 自定义异常
     */
    @Override
    public PhotoStoryResponse createPhotoStory(String photoUrl) throws MyException {

        // 获取用户信息
        String userId = sessionUtils.getUserId();
        sessionUtils.refreshData(null);

        PhotoStory photoStory = new PhotoStory().builder()
                .userId(userId)
                .photoUrl(photoUrl)
                .repairTime(LocalDateTime.now())
                .title("")
                .description("")
                .tags("")
                .build();

        // 插入数据
        if (photoStoryMapper.insert(photoStory) == 0) {
            throw new MyException(EnumExceptionType.CREATE_PHOTO_STORY_FAILED, "创建照片故事失败");
        }

        // 返回结果
        return new PhotoStoryResponse(photoStory);
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
    @Override
    public PhotoStoryResponse updatePhotoStory(Integer id, String title, String description, String tags) throws Exception {

        // 获取用户信息
        String userId = sessionUtils.getUserId();
        sessionUtils.refreshData(null);

        QueryWrapper<PhotoStory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);

        // 如果没有数据，给空值
        if (photoStoryMapper.selectCount(queryWrapper) == 0) {
            throw new MyException(EnumExceptionType.PHOTO_STORY_NOT_FOUND, "照片故事不存在");
        }

        // 更新数据
        PhotoStory photoStory = photoStoryMapper.selectOne(queryWrapper);
        if (title != null) {
            photoStory.setTitle(title);
        }
        if (description != null) {
            photoStory.setDescription(description);
        }
        if (tags != null) {
            photoStory.setTags(tags);
        }

        if (photoStoryMapper.updateById(photoStory) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_PHOTO_STORY_FAILED, "更新照片故事失败");
        }

        // 汇总照片、照片故事信息等组成 prompt，调用讯飞的图像识别接口生成模拟的照片故事内容
        String photoUrl = CommonConstants.USER_FILE_PATH + '/' + photoStory.getPhotoUrl().substring(photoStory.getPhotoUrl().lastIndexOf("/") + 1);
        String prompt = "请参考下面对于照片中的故事的描述，识别这张照片中的内容（尤其识别照片的背景故事），给出这个照片故事的详细描述，要求故事的详细描述必须比用户给出的参考描述更加详细具体、优化用户文笔、可自由发挥想象、可自己杜撰一些当天可能发生的故事（类似日记）。要求字数750字左右，绝对不得超过800字。要求输出格式为：{故事描述}：{XXX}。" +
        "接下来给你相关的照片中的故事的描述：{故事标题}：{" + photoStory.getTitle() + "}；{故事描述}：{" + photoStory.getDescription() + "}；{故事标签}：{" + photoStory.getTags() + "}";
        String newDescription = ImageRecognize.imageRecognition(userId, photoUrl, prompt);

        // 如果newDescription中{故事描述}：后的内容被{}包裹则选取newDescription中{故事描述}：后{}内的内容；如果没有被{}包裹，则直接选取newDescription中{故事描述}：后面的内容
        System.out.println("模拟的照片故事内容: " + newDescription);
        newDescription = newDescription.substring(newDescription.indexOf("：") + 1).trim();
        // 如果newDescription中有{}，则取出{}内的内容
        int startIndex = newDescription.indexOf("{");
        int endIndex = newDescription.indexOf("}");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // 如果有{}，则取出{}内的内容
            newDescription = newDescription.substring(startIndex + 1, endIndex).trim();
        }
        // 如果newDescription中没有{}，则直接取出{故事描述}：后面的内容
        System.out.println("模拟的照片故事内容: " + newDescription);

        // 将模拟的照片故事内容更新到 photoStory 中
        photoStory.setDescription(newDescription);

        // 更新照片故事
        if (photoStoryMapper.updateById(photoStory) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_PHOTO_STORY_FAILED, "更新照片故事失败");
        }

        System.out.println("更新后的照片故事: " + photoStory);

        // 把 newDescription 中的特殊字符（如换行符）替换成空格
        newDescription = newDescription.replaceAll("[\\n\\r]", " ");
        // 用模拟的照片故事内容来调用文生图 AI 模型生成 photoStoryImageUrls
        String photoStoryImageUrl1 = ImageGeneralize.imageGeneralize(newDescription);
        String photoStoryImageUrl2 = ImageGeneralize.imageGeneralize(newDescription);
        String photoStoryImageUrl3 = ImageGeneralize.imageGeneralize(newDescription);

        System.out.println("生成的照片故事图片地址: " + photoStoryImageUrl1);
        System.out.println("生成的照片故事图片地址: " + photoStoryImageUrl2);
        System.out.println("生成的照片故事图片地址: " + photoStoryImageUrl3);

        // TODO 测试
//        String photoStoryImageUrl1 = "https://chinese.redamancyxun.fun/images/72759687ad1e4c0fac6b5bfc701d4964-image.png";
//        String photoStoryImageUrl2 = "https://chinese.redamancyxun.fun/images/72759687ad1e4c0fac6b5bfc701d4964-image.png";
//        String photoStoryImageUrl3 = "https://chinese.redamancyxun.fun/images/72759687ad1e4c0fac6b5bfc701d4964-image.png";


        List<String> photoStoryImageUrls = new ArrayList<>();
        photoStoryImageUrls.add(photoStoryImageUrl1);
        photoStoryImageUrls.add(photoStoryImageUrl2);
        photoStoryImageUrls.add(photoStoryImageUrl3);

        // 将生成的图片地址设置到 photoStoryImage 中
        PhotoStoryImage photoStoryImage1 = new PhotoStoryImage().builder()
                .photoStoryId(photoStory.getId())
                .photoUrl(photoStoryImageUrl1)
                .build();
        PhotoStoryImage photoStoryImage2 = new PhotoStoryImage().builder()
                .photoStoryId(photoStory.getId())
                .photoUrl(photoStoryImageUrl2)
                .build();
        PhotoStoryImage photoStoryImage3 = new PhotoStoryImage().builder()
                .photoStoryId(photoStory.getId())
                .photoUrl(photoStoryImageUrl3)
                .build();

        System.out.println("生成的照片故事图片: " + photoStoryImage1);
        System.out.println("生成的照片故事图片: " + photoStoryImage2);
        System.out.println("生成的照片故事图片: " + photoStoryImage3);

        // 将原本的照片故事图片替换掉
        QueryWrapper<PhotoStoryImage> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.eq("photo_story_id", photoStory.getId());
        List<PhotoStoryImage> existingImages = photoStoryImageMapper.selectList(imageQueryWrapper);
        if (existingImages != null && !existingImages.isEmpty()) {
            for (PhotoStoryImage existingImage : existingImages) {
                photoStoryImageMapper.deleteById(existingImage.getId());
            }
        }

        System.out.println("删除原有的照片故事图片");

        // 插入新的照片故事图片
        if (photoStoryImageMapper.insert(photoStoryImage1) == 0 ||
            photoStoryImageMapper.insert(photoStoryImage2) == 0 ||
            photoStoryImageMapper.insert(photoStoryImage3) == 0) {
            throw new MyException(EnumExceptionType.INSERT_FAILED, "创建照片故事图片失败");
        }

        PhotoStoryResponse photoStoryResponse = new PhotoStoryResponse(photoStory, photoStoryImageUrls);
        System.out.println("更新后的照片故事: " + photoStoryResponse);

        // 返回结果
        return photoStoryResponse;
    }

    /**
     * 查找照片故事列表
     * @return List<PhotoStory>
     * @throws MyException 自定义异常
     */
    @Override
    public List<PhotoStoryResponse> findPhotoStoryByUserId() throws MyException {

        // 获取用户信息
        String userId = sessionUtils.getUserId();
        sessionUtils.refreshData(null);

        QueryWrapper<PhotoStory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("repair_time");

        // 如果没有数据，给空值
        if (photoStoryMapper.selectCount(queryWrapper) == 0) {
            return new ArrayList<>();
        }
        List<PhotoStory> photoStoryList = photoStoryMapper.selectList(queryWrapper);

        // 将查询到的照片故事转换为响应对象
        List<PhotoStoryResponse> photoStoryResponses = new ArrayList<>();
        for (PhotoStory photoStory : photoStoryList) {
            // 查询照片故事对应的图片
            QueryWrapper<PhotoStoryImage> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.eq("photo_story_id", photoStory.getId());
            List<PhotoStoryImage> photoStoryImages = photoStoryImageMapper.selectList(imageQueryWrapper);
            List<String> photoUrls = new ArrayList<>();
            for (PhotoStoryImage image : photoStoryImages) {
                photoUrls.add(image.getPhotoUrl());
            }
            // 将照片故事和图片地址封装到响应对象中
            PhotoStoryResponse response = new PhotoStoryResponse(photoStory, photoUrls);
            photoStoryResponses.add(response);
        }

        // 返回照片故事列表
        return photoStoryResponses;
    }

    /**
     * 根据id查找照片故事
     * @param id 照片故事id
     * @return PhotoStory
     * @throws MyException 自定义异常
     */
    @Override
    public PhotoStoryResponse findPhotoStoryById(Integer id) throws MyException {

        // 获取用户信息
        String userId = sessionUtils.getUserId();
        sessionUtils.refreshData(null);

        QueryWrapper<PhotoStory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);

        // 如果没有数据，给空值
        if (photoStoryMapper.selectCount(queryWrapper) == 0) {
            throw new MyException(EnumExceptionType.PHOTO_STORY_NOT_FOUND, "照片故事不存在");
        }

        PhotoStory photoStory = photoStoryMapper.selectOne(queryWrapper);

        System.out.println("查询到的照片故事: " + photoStory);

        // 查询照片故事对应的图片
        QueryWrapper<PhotoStoryImage> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.eq("photo_story_id", photoStory.getId());
        List<PhotoStoryImage> photoStoryImages = photoStoryImageMapper.selectList(imageQueryWrapper);
        List<String> photoUrls = new ArrayList<>();
        for (PhotoStoryImage image : photoStoryImages) {
            photoUrls.add(image.getPhotoUrl());
        }

        // 将照片故事和图片地址封装到响应对象中
        PhotoStoryResponse response = new PhotoStoryResponse(photoStory, photoUrls);

        // 返回照片故事
        return response;
    }
}
