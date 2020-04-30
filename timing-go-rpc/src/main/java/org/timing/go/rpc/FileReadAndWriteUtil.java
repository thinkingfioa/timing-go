package org.timing.go.rpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * File读写操作工具类，作为静态类使用
 */
public class FileReadAndWriteUtil {

  private static final Logger LOGGER = LogManager.getLogger(FileReadAndWriteUtil.class);

  private final static char SLASH = '/';

  // 最大读取文件大小: 10 * 1024 * 1024 = 10M
  private final static long MAX_READ_FILE_LEN = 10 * 1024 * 1024;

  private FileReadAndWriteUtil() {
    throw new IllegalAccessError("can not use constructor.");
  }

  public static String read(String dirAddr, String fileName, final String format)
      throws IOException {
    if (null == fileName || fileName.isEmpty()) {
      throw new NullPointerException("fileName is null");
    }

    byte[] fileContent = read(dirAddr, fileName);
    return new String(fileContent, format);
  }

  /**
   * 通过ClassPath读取资源文件内容
   *
   * @return byte[]
   */
  public static byte[] readResource(String resourcePath) throws IOException {
    BufferedInputStream inputStream = null;
    ByteArrayOutputStream bos = null;
    try {
      InputStream input =
          FileReadAndWriteUtil.class.getClassLoader().getResourceAsStream(resourcePath);
      inputStream = new BufferedInputStream(input);
      bos = new ByteArrayOutputStream();
      byte[] readBytes = new byte[512];
      int len = 0;
      while ((len = inputStream.read(readBytes, 0, readBytes.length)) != -1) {
        bos.write(readBytes, 0, len);
        if (bos.size() >= MAX_READ_FILE_LEN) {
          throw new UnsupportedOperationException("refuse to read file size > 1M");
        }
      }
      return bos.toByteArray();
    } catch (IOException e) {
      LOGGER.error("read fail.", e);
      throw e;
    } finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        } catch (IOException e) {
          LOGGER.error("read fail.", e);
        }
      }
      if (null != bos) {
        try {
          bos.close();
        } catch (IOException e) {
          LOGGER.error("read fail.", e);
        }
      }
    }
  }

  /**
   * 读文件内容。最大可读取10M内容大小的文件 <br>
   *
   * @return byte[]
   */
  public static byte[] read(String dirAddr, final String fileName) throws IOException {
    if (null == fileName || fileName.isEmpty()) {
      throw new NullPointerException("fileName is null");
    }
    if (null == dirAddr) {
      dirAddr = "./";
    }
    dirAddr = CommonUtils.mustEndOfChar(dirAddr.trim(), SLASH);
    final String fileAddr = dirAddr.trim() + fileName.trim();
    BufferedInputStream inputStream = null;
    ByteArrayOutputStream bos = null;
    try {
      File file = new File(fileAddr);
      if (!file.exists() || !file.isFile()) {
        throw new IllegalArgumentException("file is not exist. fileAddr:" + fileAddr);
      }
      if (file.length() > MAX_READ_FILE_LEN) {
        throw new UnsupportedOperationException("refuse to read file size > 10M");
      }
      inputStream = new BufferedInputStream(new FileInputStream(file));
      bos = new ByteArrayOutputStream();
      byte[] readBytes = new byte[512];
      int len = 0;
      while ((len = inputStream.read(readBytes, 0, readBytes.length)) != -1) {
        bos.write(readBytes, 0, len);
        if (bos.size() >= MAX_READ_FILE_LEN) {
          throw new UnsupportedOperationException("refuse to read file size > 1M");
        }
      }
      return bos.toByteArray();
    } catch (IOException e) {
      LOGGER.error("fileAddr: {}", fileAddr, e);
      throw e;
    } finally {
      if (null != inputStream) {
        try {
          inputStream.close();
        } catch (IOException e) {
          LOGGER.error("fileAddr: {}", fileAddr, e);
        }
      }
      if (null != bos) {
        try {
          bos.close();
        } catch (IOException e) {
          LOGGER.error("fileAddr: {}", fileAddr, e);
        }
      }
    }
  }


  public static void write(String dirAddr, final String fileName, String contentStr,
      final Charset format, boolean append) {
    if (null == fileName || fileName.isEmpty()) {
      throw new NullPointerException("fileName or contentStr is null");
    }
    if (null == contentStr) {
      contentStr = "";
    }
    byte[] content = contentStr.getBytes(format);
    write(dirAddr, fileName, content, append);
  }

  /**
   * 将内容写入文件
   */
  public static void write(String dirAddr, final String fileName, byte[] content, boolean append) {
    if (null == fileName || fileName.isEmpty() || null == content) {
      throw new NullPointerException("fileName or content is null");
    }

    if (null == dirAddr) {
      dirAddr = "./";
    }
    dirAddr = CommonUtils.mustEndOfChar(dirAddr.trim(), SLASH);
    BufferedOutputStream outputStream = null;
    final String fileAddr = dirAddr.trim() + fileName.trim();
    try {
      File dirFile = new File(dirAddr.trim());
      if (!dirFile.exists() && !dirFile.isDirectory()) {
        // 创建目录
        dirFile.mkdirs();
      }
      File file = new File(fileAddr);
      if (!file.exists()) {
        file.createNewFile();
      }
      outputStream = new BufferedOutputStream(new FileOutputStream(file, append));
      outputStream.write(content);
      outputStream.flush();
    } catch (IOException e) {
      LOGGER.error("fileAddr: {}", fileAddr, e);
    } finally {
      try {
        if (null != outputStream) {
          outputStream.close();
        }
      } catch (IOException e) {
        LOGGER.error("fileAddr: {}", fileAddr, e);
      }
    }
  }


}
