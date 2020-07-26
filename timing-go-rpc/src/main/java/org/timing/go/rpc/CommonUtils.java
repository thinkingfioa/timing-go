package org.timing.go.rpc;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronExpression;

/**
 * @author mengchenli
 * @version time: 2018年5月3日下午7:03:29 no body fields
 */
public class CommonUtils {

  private static final Logger LOGGER = LogManager.getLogger(CommonUtils.class);
  
  private static final byte HEADER_FLAGS_UNZIP_FLAG = 0x8; // 0b1000

  public static long currentTimeFormat() {
    LocalDateTime dt = LocalDateTime.now();
    return timeFormatLong(dt);
  }

  public static long timeFormatLong(LocalDateTime dt) {
    long ts = dt.getYear();
    ts = ts * 100 + dt.getMonthValue();
    ts = ts * 100 + dt.getDayOfMonth();
    ts = ts * 100 + dt.getHour();
    ts = ts * 100 + dt.getMinute();
    ts = ts * 100 + dt.getSecond();
    ts = ts * 1000 + (dt.getNano() / 1000 / 1000);
    return ts;
  }

  /**
   * 将timeDateText格式化成{@code LocalDateTime}
   */
  public static Date parseDate(String timeDateText, String format) throws ParseException {
    // TODO:: 使用threadLocal来避免重复创建
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.parse(timeDateText);
  }

  /**
   * 将DateTime格式化成时间字符串
   */
  public static String formatDate(Date dateTime, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(dateTime);
  }

  /**
   * 格式化当前时间
   */
  public static String formatNowTime(String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(new Date());
  }

  /**
   * 对数值进行格式化<br> 1> value=1000, decimalFormat=0.000。 输出: 1000.000<br> 2> value=1000.0,
   * decimalFormat=0.000。输出: 1000.000
   */
  public static String formatDecimal(String value, String decimalFormat) {
    if (null == decimalFormat || decimalFormat.isEmpty()) {
      return value;
    }
    BigDecimal valBD = new BigDecimal(value);
    DecimalFormat df = new DecimalFormat(decimalFormat);
    return df.format(valBD);
  }


  public static int calcCheckSum(byte[] bytes) {
    if (null == bytes) {
      LOGGER.error("checkSum error. bytes is null");
      return 0;
    }
    byte checksum = 0;
    for (int i = 0; i < bytes.length; i++) {
      checksum += bytes[i];
    }

    return 0xff & checksum;
  }

  public static int addUpCheckSum(int checkSum, byte[] bytes) {
    byte bCheckSum = (byte) ((byte) checkSum + (byte) calcCheckSum(bytes));
    return 0xff & bCheckSum;
  }

  public static boolean headerFlagNeedUnzip(byte flags) {
    return (flags & HEADER_FLAGS_UNZIP_FLAG) > 0;
  }

  public static boolean emptyCheck(String str) {
    return null == str || str.trim().isEmpty();
  }

  public static boolean emptyCheck(String... strList) {
    for (String str : strList) {
      if (emptyCheck(str)) {
        return true;
      }
    }
    return false;
  }

  public static boolean emptyCheck(List<?> list) {
    return null == list || list.isEmpty();
  }

  public static boolean legalCronExpress(String cronExpress) {
    if (emptyCheck(cronExpress)) {
      return false;
    }
    return CronExpression.isValidExpression(cronExpress);
  }

  public static List<String> getLocalIpAddress() {
    List<String> localIpList = new ArrayList<>();
    try {
      for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
          .hasMoreElements(); ) {
        NetworkInterface iface = ifaces.nextElement();
        for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs
            .hasMoreElements(); ) {
          InetAddress inetAddr = inetAddrs.nextElement();
          if (null != inetAddr && !inetAddr.isLoopbackAddress()
              && inetAddr instanceof Inet4Address) {
            localIpList.add(inetAddr.getHostAddress());
          }
        }
      }
      LOGGER.info("IPv4 localIp List {}", localIpList);
      return localIpList;
    } catch (SocketException cause) {
      LOGGER.error("fetch IPv4 wrong", cause);
      return localIpList;
    } catch (Throwable cause) {
      LOGGER.error("fetch IPv4 exception caught", cause);
      return localIpList;
    }
  }

  public static String getRecordKey(Number securityType, Number applSeqNum) {
    return String.valueOf(securityType) + "-" + String.valueOf(applSeqNum);
  }

  public static String mustEndOfChar(String str, char c) {
    if (null == str || str.trim().isEmpty()) {
      return String.valueOf(c);
    }
    str = str.trim();
    if (c != str.charAt(str.length() - 1)) {
      return str + c;
    }
    return str;
  }
}
