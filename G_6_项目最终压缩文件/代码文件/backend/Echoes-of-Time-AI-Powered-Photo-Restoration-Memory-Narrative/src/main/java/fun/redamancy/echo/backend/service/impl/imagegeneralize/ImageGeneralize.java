package fun.redamancy.echo.backend.service.impl.imagegeneralize;


import com.google.gson.Gson;
import fun.redamancy.echo.backend.common.CommonConstants;
import okhttp3.HttpUrl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

// 主函数入口
public class ImageGeneralize {

    public static final String hostUrl = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";
    public static final String appid = "24d7d7a8";
    public static final String apiSecret = "MWViNWU3ZTE5OTMyMTIzZDEyMjk3NTlj";
    public static final String apiKey = "6cb900a2ac3de927a65bf27b1f1e2df2";
    public static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            String prompt = "这张照片拍摄于1985年的春节，是我们大家庭难得的一次团聚。照片中可以看到我的祖父母、父母、叔叔阿姨和兄弟姐妹。那年我只有5岁，对当时的情景记忆已经有些模糊，但听父母说，那是一个非常欢乐的时刻。" +
                    "祖父穿着他最好的中山装，祖母脸上洋溢着幸福的笑容。父母站在后排，年轻而充满活力。叔叔抱着刚出生的小堂妹，阿姨们围坐在一起，手里还拿着包饺子的工具。" +
                    "每当看到这张照片，我都会想起那个温暖的冬天，想起家人之间的关爱和亲情。虽然已经过去了很多年，但这些回忆永远留在我的心中。";
            String imageUrl = imageGeneralize(prompt);
            System.out.println("生成的图片URL: " + imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String imageGeneralize(String prompt) throws Exception {
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        // URL地址正确
        //System.err.println(authUrl);

        prompt = "请根据下面对于图片的描述，帮我绘制图片：{" + prompt + "}";
        String json = "{\n" +
                "  \"header\": {\n" +
                "    \"app_id\": \"" + appid + "\",\n" +
                "    \"uid\": \"" + UUID.randomUUID().toString().substring(0, 15) + "\"\n" + "  },\n" +
                "  \"parameter\": {\n" +
                "    \"chat\": {\n" +
                "      \"domain\": \"s291394db\",\n" +
                "      \"temperature\": 0.5,\n" +
                "      \"max_tokens\": 4096,\n" +
                "      \"width\": 1024,\n" +
                "      \"height\": 1024\n" + "    }\n" + "  },\n" +
                "  \"payload\": {\n" +
                "    \"message\": {\n" +
                "      \"text\": [\n" + "        {\n" +
                "          \"role\": \"user\",\n" +
                "          \"content\": \"" + prompt + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        // 发起 Post 请求
        // System.err.println(json);
        String res = MyUtil.doPostJson(authUrl, null, json);
//        System.out.println(res);

        JsonParse jsonParse = gson.fromJson(res, JsonParse.class);
        byte[] imageBytes = Base64.getDecoder().decode(jsonParse.payload.choices.text.get(0).content);

        // 指定输出图片的文件路径
        String outputFileName = "image_generalize_" + System.currentTimeMillis() + ".png";
        String outputPath = CommonConstants.USER_FILE_PATH + "/" + outputFileName;
//        String outputPath = "src/main/resources/static/" + outputFileName;
        try (FileOutputStream imageOutFile = new FileOutputStream(outputPath)) {
            // 将字节数组写入文件
            imageOutFile.write(imageBytes);
            System.out.println("图片已成功保存到: " + outputPath);
        } catch (IOException e) {
            System.err.println("保存图片时出错: " + e.getMessage());
        }

        // 返回图片的 URL
        return CommonConstants.IMAGE_PATH + outputFileName;
    }

    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // date="Thu, 12 Oct 2023 03:05:28 GMT";
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" + "date: " + date + "\n" + "POST " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }

    class JsonParse {
        Payload payload;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String content;
    }
}