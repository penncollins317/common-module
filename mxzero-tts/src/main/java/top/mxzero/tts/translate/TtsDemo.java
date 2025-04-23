package top.mxzero.tts.translate;

import top.mxzero.tts.translate.utils.AuthV3Util;
import top.mxzero.tts.translate.utils.FileUtil;
import top.mxzero.tts.translate.utils.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云语音合成服务api调用demo
 * api接口: https://openapi.youdao.com/ttsapi
 */
public class TtsDemo {

    private static final String APP_KEY = "496600955eb048d1";     // 您的应用ID
    private static final String APP_SECRET = "NkaR8fhbGy4063s9BYk3n4CZgAU651Hi";  // 您的应用密钥
    private static final String PATH = System.getProperty("user.dir") + File.separator + "temp" + File.separator + "tts.mp3";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ttsapi", null, params, "audio");
        // 打印返回结果
        if (result != null) {
            String path = FileUtil.saveFile(PATH, result, false);
            System.out.println("save path:" + path);
        }
    }

    private static Map<String, String[]> createRequestParams() {
        /*
         * note: 将下列变量替换为需要请求的参数
         */
        String q = """
你好！我是DeepSeek Chat，一个智能AI助手，可以帮你处理各种任务，比如：
知识问答：解答历史、科学、技术、文化等各种问题。
写作助手：帮你写文章、改写、润色、生成创意文案等。
办公效率：处理Excel、Word、PDF文件，总结、翻译、提取关键信息。
编程帮助：写代码、调试、优化、解释技术概念（支持Python、C++、Java等）。
语言翻译：多语言互译，并提供语法修正和地道表达建议。
学习辅导：解题思路、论文大纲、考试备考建议等。
创意灵感：起名字、写故事、策划活动、提供商业点子。
生活助手：旅行规划、健康小贴士、时间管理等。
                """;
        String voiceName = "youxiaoxun";
        String format = "mp3";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("voiceName", new String[]{voiceName});
            put("format", new String[]{format});
            put("volume", new String[]{"5.00"});
        }};
    }
}
