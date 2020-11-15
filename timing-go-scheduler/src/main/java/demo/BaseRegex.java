package demo;

//import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author thinking_fioa 2020/11/15
 */
public class BaseRegex {

  private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  private static final String PHONE_REGEX = "\\+86\\d{11}";
  private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

  public static void main(String[] args) {
    String email = "thinking_fioa@163.com";
    String email1 = "ppp@123.cn";
    String phone = "+8618918509525";
    String phone1 = "12334567890";
    System.out.println("email=" + email + ", " + validEmail(email));
    System.out.println("email1=" + email1 + ", " + validEmail(email1));
    System.out.println("phone=" + phone + ", " + validPhone(phone));
    System.out.println("phone1=" + phone1 + ", " + validPhone(phone1));

    // JSONObject
//    JSONObject jsonObject = JSONObject.parseObject(jsonStr());
//    Map<String, String> validsMap = jsonObject.getObject("valids", Map.class);
//    System.out.println(validsMap);
  }

  public static boolean validEmail(String email) {
    Matcher portMatcher = EMAIL_PATTERN.matcher(String.valueOf(email));
    System.out.println("1111111111");
    return portMatcher.find();
  }

  public static boolean validPhone(String phone) {
    Matcher portMatcher = PHONE_PATTERN.matcher(String.valueOf(phone));
    System.out.println("222222222");
    return portMatcher.find();
  }

  public static void addPhone() {
    System.out.println("addPhone");
  }

  public static String jsonStr() {
    return "{"
        + "\"valids\":{"
        + "\"用户名\":\"thinking_fioa\","
        + "\"密码\":\"123456\""
        + "}"
        + "}";
  }
}
