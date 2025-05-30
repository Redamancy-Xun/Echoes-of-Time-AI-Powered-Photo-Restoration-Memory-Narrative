package fun.redamancy.echo.backend.service.impl.imagerecognize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImageRecognize extends WebSocketListener {
    
    public static final String hostUrl = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
    public static final String appid = "24d7d7a8";
    public static final String apiSecret = "MWViNWU3ZTE5OTMyMTIzZDEyMjk3NTlj";
    public static final String apiKey = "6cb900a2ac3de927a65bf27b1f1e2df2";

//    public static List<RoleContent> historyList=new ArrayList<>(); // 对话历史存储集合

    public static String totalAnswer=""; // 大模型的答案汇总

    // 环境治理的重要性  环保  人口老龄化  我爱我的祖国
    public static  String NewQuestion = "";
    public static  Boolean ImageAddFlag = false; // 判断是否添加了图片信息

    public static final Gson gson = new Gson();


    // 用户ID
    private String userId;

    // 照片URL
    private String photoUrl;

    // 用户输入的问题
    private String prompt;

    // WebSocket关闭标志
    private Boolean wsCloseFlag;

    // 总回答标志
    private static Boolean totalFlag = true;

    // 构造函数
    public ImageRecognize(String userId, String photoUrl, String prompt , Boolean wsCloseFlag) {
        this.userId = userId;
        this.photoUrl = photoUrl;
        this.prompt = prompt;
        this.wsCloseFlag = wsCloseFlag;
    }

    // 主方法，用于测试图像识别功能
    public static void main(String[] args) throws Exception {
        // 测试代码
        String userId = "testUser";
        String photoUrl = "src/main/resources/static/与大模型进行图像交互.png"; // 替换为实际的图片URL
        String prompt = "请参考下面对于照片中的故事的描述，识别这张照片中的内容（尤其识别照片的背景故事），给出这个照片故事的详细描述，要求故事的详细描述必须比用户给出的参考描述更加详细具体、优化用户文笔、可自由发挥想象、可自己杜撰一些当天可能发生的故事（类似日记）。要求字数750字左右，不得超过800字。要求输出格式为：{故事描述}：{XXX}。" +
                "接下来给你相关的照片中的故事的描述：{故事标题}：{1985年的家庭聚会}；{故事描述}：{这张照片拍摄于1985年的春节，是我们大家庭难得的一次团聚。照片中可以看到我的祖父母、父母、叔叔阿姨和兄弟姐妹。那年我只有5岁，对当时的情景记忆已经有些模糊，但听父母说，那是一个非常欢乐的时刻。\n" +
                "祖父穿着他最好的中山装，祖母脸上洋溢着幸福的笑容。父母站在后排，年轻而充满活力。叔叔抱着刚出生的小堂妹，阿姨们围坐在一起，手里还拿着包饺子的工具。\n" +
                "每当看到这张照片，我都会想起那个温暖的冬天，想起家人之间的关爱和亲情。虽然已经过去了很多年，但这些回忆永远留在我的心中。}；{故事标签}：{家庭聚会、1985、春节}";


        String result = imageRecognition(userId, photoUrl, prompt);
        System.out.println("识别结果：" + result);
    }

    // 图像识别模型调用入口
    public static String imageRecognition(String userId, String photoUrl, String prompt) throws Exception {
        totalFlag = false; // 重置总回答标志
        NewQuestion = prompt; // 用户输入的问题
        System.out.println("prompt:" + prompt);
        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        // 创建 WebSocket 连接
        // 使用 OkHttp 库创建 WebSocket 连接
        // 将鉴权 URL 从 HTTP/HTTPS 转换为 WS/WSS
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        for (int i = 0; i < 1; i++) {
            totalAnswer = ""; // 每次清空答案
            WebSocket webSocket = client.newWebSocket(request, new ImageRecognize(userId, photoUrl, prompt, false));
        }

        // 等待 WebSocket 连接关闭
        int waitTime = 0; // 等待时间计数
        while (!totalFlag) {
            waitTime += 200; // 每次等待200毫秒
            Thread.sleep(200); // 等待200毫秒
            System.out.println("imageRecognition等待大模型回答中...已经等待了" + waitTime + "毫秒");
        }
        System.out.println("大模型回答完毕，准备返回结果");
        // 返回大模型的回答
        String result = totalAnswer;
        System.out.println("大模型回答：" + result);
//        historyList.clear(); // 清空历史记录
        ImageAddFlag = false; // 重置图片添加标志
        return result; // 返回大模型的回答
    }

//    public static boolean canAddHistory() {  // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
//        int history_length = 0;
//        for (RoleContent temp : historyList) {
//            history_length = history_length + temp.content.length();
//        }
//        // System.out.println(history_length);
//        if (history_length > 1200000000) { // 这里限制了总上下文携带，图片理解注意放大 ！！！
//            historyList.remove(0);
//            return false;
//        } else {
//            return true;
//        }
//    }

    /**
     * 自定义线程类，用于处理WebSocket请求
     * 该类继承自Thread，并在run方法中构建请求参数并发送请求
     * 注意：在实际应用中，可能需要根据具体需求调整请求参数的构建方式
     * 该线程负责构建完整的请求 JSON，包括 header、parameter 和 payload 部分
     * 处理历史对话，确保先添加图片信息，再添加文本信息
     * 添加当前用户输入的图片（如果尚未添加）和问题文本
     * 发送构建好的 JSON 请求，并等待服务端响应完成后关闭连接
     */
    class MyThread extends Thread {

        private WebSocket webSocket;

        public MyThread(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        // 线程run方法
        public void run() {
            try {
                // 构建请求参数
                JSONObject requestJson = new JSONObject();

                // header参数
                JSONObject header = new JSONObject();  // header参数
                header.put("app_id",appid);
                header.put("uid",UUID.randomUUID().toString().substring(0, 10));

                // parameter参数
                JSONObject parameter = new JSONObject(); // parameter参数

                // 鉴权参数
                JSONObject chat = new JSONObject();
                chat.put("domain","imagev3");
                chat.put("temperature",0.5);
                chat.put("max_tokens",4096);
                chat.put("auditing","default");

                parameter.put("chat",chat);

                JSONObject payload = new JSONObject(); // payload参数
                JSONObject message = new JSONObject();
                JSONArray text = new JSONArray();

//                // 历史问题获取
//                if (historyList.size() > 0) { // 保证首个添加的是图片
//                    // 先添加图片类型的历史消息
//                    for (RoleContent tempRoleContent:historyList) {
//                        if (tempRoleContent.content_type.equals("image")) { // 保证首个添加的是图片
//                            text.add(JSON.toJSON(tempRoleContent));
//                            ImageAddFlag=true;
//                        }
//                    }
//                }
//                if (historyList.size() > 0) {
//                    // 再添加文本类型的历史消息
//                    for (RoleContent tempRoleContent:historyList) {
//                        if(!tempRoleContent.content_type.equals("image")){ // 添加费图片类型
//                            text.add(JSON.toJSON(tempRoleContent));
//                        }
//                    }
//                }

                RoleContent imageRoleContent = new RoleContent();
                imageRoleContent.role = "user";
                imageRoleContent.content = Base64.getEncoder().encodeToString(ImageUtil.read(photoUrl)); // 添加用户上传的图片URL
                imageRoleContent.content_type = "image"; // 设置内容类型为图片
                text.add(JSON.toJSON(imageRoleContent)); // 将图片信息添加到文本数组中

                RoleContent promptRoleContent = new RoleContent();
                promptRoleContent.role = "user";
                promptRoleContent.content = prompt; // 添加用户输入的问题
                promptRoleContent.content_type = "text"; // 设置内容类型为文本
                text.add(JSON.toJSON(promptRoleContent)); // 将问题文本添加到文本数组中

//                // 最新问题
//                RoleContent roleContent = new RoleContent();
//                // 添加图片信息
//                if (!ImageAddFlag) {
//                    roleContent.role = "user";
//                    // TODO: 这里可以添加图片的路径，注意图片的大小限制
//                    roleContent.content = Base64.getEncoder().encodeToString(ImageUtil.read("src\\main\\resources\\1.png"));
//                    roleContent.content_type = "image";
//                    text.add(JSON.toJSON(roleContent));
//                    historyList.add(roleContent);
//                }
//                // 添加对图片提出要求的信息
//                RoleContent roleContent1 = new RoleContent();
//                roleContent1.role = "user";
//                roleContent1.content = NewQuestion;
//                roleContent1.content_type = "text";
//                text.add(JSON.toJSON(roleContent1));
//                historyList.add(roleContent1);

                message.put("text",text);
                payload.put("message",message);

                requestJson.put("header",header);
                requestJson.put("parameter",parameter);
                requestJson.put("payload",payload);
//                System.err.println(requestJson); // 可以打印看每次的传参明细

                // 发送请求
                webSocket.send(requestJson.toString());

                // 等待服务端返回完毕后关闭
                int waitTime = 0; // 等待时间计数
                while (true) {
                    waitTime += 200; // 每次等待200毫秒
//                    System.err.println(wsCloseFlag + "---");
                    System.out.println("MyThread等待大模型回答中...已经等待了" + waitTime + "毫秒");
                    Thread.sleep(200);
                    if (wsCloseFlag) {
                        break;
                    }
                }
                webSocket.close(1000, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // WebSocket 连接与消息处理：连接建立后，启动一个新线程构建并发送请求消息
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        System.out.print("大模型：");
        MyThread myThread = new MyThread(webSocket);
        myThread.start();
    }

    /**
     * 处理接收到的消息，解析 JSON 响应
     * 检查状态码，如果有错误则输出错误信息并关闭连接
     * 提取并显示模型返回的内容，同时将其追加到totalAnswer中
     * 当收到状态码为 2 的消息时，表示响应结束，将回复添加到历史记录中，并设置标志允许用户输入下一个问题
     * @param webSocket
     * @param text
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("用户 {" + userId + "} 的成果块：" + text);
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);

        // 处理返回的 JSON 数据
        if (myJsonParse.header.code != 0) {
            System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
            System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
            webSocket.close(1000, "");
        }

        // 处理返回的文本内容
        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            System.out.println(temp.content);
            totalAnswer = totalAnswer + temp.content;
        }

        // 如果状态码为2，表示请求已完成
        if (myJsonParse.header.status == 2) {
            // 可以关闭连接，释放资源
            System.out.println();
            System.out.println("*************************************************************************************");
//            if (canAddHistory()) {
//                RoleContent roleContent=new RoleContent();
//                roleContent.setRole("assistant");
//                roleContent.setContent(totalAnswer);
//                roleContent.setContent_type("text");
//                historyList.add(roleContent);
//            } else {
//                historyList.remove(0);
//                RoleContent roleContent=new RoleContent();
//                roleContent.setRole("assistant");
//                roleContent.setContent(totalAnswer);
//                roleContent.setContent_type("text");
//                historyList.add(roleContent);
//            }
            wsCloseFlag = true;
            totalFlag = true;
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                System.out.println("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
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

    //返回的json结果拆解
    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }
    class RoleContent{
        String role;
        String content;

        String content_type;

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}