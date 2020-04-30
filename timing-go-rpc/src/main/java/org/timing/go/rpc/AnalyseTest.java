package org.timing.go.rpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author thinking_fioa 2020/4/30
 */
public class AnalyseTest {

  public static final String P_COMM = ".*(\\d{2})\\:(\\d{2})\\:(\\d{2})\\.(\\d{3}) \\[.*$";

  public static Pattern pattern = Pattern.compile(P_COMM);


  public static void main(String[] args) throws IOException {
    long minValue = Long.MAX_VALUE;
    long maxValue = Long.MIN_VALUE;
    long sum = 0;
    int count = 0;
    File file = new File("/Users/thinking/Documents/workspace/timing-go/logs/lu.txt");
    InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
    BufferedReader bf = new BufferedReader(inputReader);
    // 按行读取字符串
    String str;

    while ((str = bf.readLine()) != null) {
      Matcher matcher = pattern.matcher(str);
      matcher.find();
      int second = Integer.parseInt(matcher.group(3));
      int ms = Integer.parseInt(matcher.group(4));
//      arrayList.add(str);
      int tt = calMs(second, ms);
      sum += tt;
      minValue = Math.min(minValue, tt);
      maxValue = Math.max(maxValue, tt);
      count++;
    }

    System.out.printf("avg=%dms, max=%dms, min=%dms\n", (sum / count), maxValue, minValue);
    bf.close();
    inputReader.close();
  }

  private static int calMs(int second, int ms) {
    return second * 1000 + ms;
  }

}
