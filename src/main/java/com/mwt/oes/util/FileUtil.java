package com.mwt.oes.util;

import com.mwt.oes.properties.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static Map<String, String> getFilePath(String fileName) {
        //判断系统
        String os = System.getProperty("os.name");
        String sysFile = Global.getConfig("linux.project.path");
        if (os.toLowerCase().startsWith("win")) {
            sysFile = Global.getConfig("win.project.path");
        }
        String basePath = getFileFolderNew();

        String absolutePath = sysFile + File.separator + Global.getConfig("file.doc.path") + File.separator + basePath;
        //相对路径
        String relativePath = File.separator + Global.getConfig("file.doc.path") + File.separator + basePath;

        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("absolutePath", absolutePath + File.separator + fileName);
        returnMap.put("relativePath", relativePath);
        return returnMap;
    }


    //上传文件
    public static String upload(MultipartFile file) {
        //判断系统
        String os = System.getProperty("os.name");
        String sysFile = Global.getConfig("linux.project.path");
        if (os.toLowerCase().startsWith("win")) {
            sysFile = Global.getConfig("win.project.path");
        }
        if (!file.isEmpty()) {
            //绝对路径 采用日期+随机数文件夹名
            String basePath = getFileFolderNew();
            String absolutePath = sysFile + File.separator + Global.getConfig("file.doc.path") + File.separator + basePath;
            //相对路径
            String relativePath = File.separator + Global.getConfig("file.doc.path") + File.separator + basePath;

            try {
                // 获取上传的文件名称，并结合存放路径，构建新的文件名称
                String fileName = file.getOriginalFilename();
                //String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                //地址 D:/titan-file/static/时间_随机数
                File filepath = new File(absolutePath, fileName);
                // 判断路径是否存在，不存在则新创建一个
                if (!filepath.getParentFile().exists()) {
                    filepath.getParentFile().mkdirs();
                }
                file.transferTo(new File(absolutePath + File.separator + fileName));
                return relativePath + File.separator + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return "300";
            }
        }
        return "404";
    }


    public static String download(String filePath, String fileName) throws Exception {
        File file = null;
        String returnFlag = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        HttpURLConnection conn = null;
        FileOutputStream fos = null;
        try {
            //通过http请求访问文件
            String urlPath = filePath.replaceAll(fileName, URLDecoder.decode(fileName, "utf-8"));


            URL url = new URL(urlPath.replaceAll("\\\\", "/"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            InputStream in = conn.getInputStream();

            //下载重命名机制
            String fileInfo = getFileInfo(fileName);

            //建立内存到硬盘的连接
            file = new File(Global.getConfig("win.project.path")
                    + File.separator + fmt.format(new Date()) + File.separator + fileInfo);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {  //先读到内存
                fos.write(b, 0, len);
            }
            fos.flush();
            returnFlag = "200";
        } catch (Exception e) {
            e.printStackTrace();
            returnFlag = "300";
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
            return returnFlag;
        }
    }

    /**
     * 重新命名文件
     * 毫秒数+随机数
     *
     * @return
     */
    public static String getFileFolderNew() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        return fmt.format(new Date()) + "_" + UUID.randomUUID().toString();
    }


    /**
     * @param fileName fileInfo[0]: toPrefix;
     *                 fileInfo[1]:toSuffix;
     * @return
     */
    public static String getFileInfo(String fileName) {
        Calendar cal = Calendar.getInstance();
        int index = fileName.lastIndexOf(".");
        String toPrefix = "";
        String toSuffix = "";
        if (index == -1) {
            toPrefix = fileName;
        } else {
            toPrefix = fileName.substring(0, index);
            toSuffix = fileName.substring(index, fileName.length());
        }
        return toPrefix + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + toSuffix;
    }

    /*
     *
     * @Author:Rick
     * filepath 模板文件路径
     * tofilepath 要生成的文件路径
     * items word xml模板文件中的占位符
     * data  word xml文件要替换的数据
     */
    public static boolean changeFileText(String filepath, String tofilepath,
                                         List<String> items, List<String> data) {

        String line = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            ClassPathResource resource = new ClassPathResource(filepath);
            is = resource.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result = String.valueOf(sb);

            for (int i = 0; i < items.size(); i++) {
                /*
                 *
                 * 正则替换文件中的占位符
                 */
                result = result.replaceAll(items.get(i), data.get(i));
            }
            tofilepath = tofilepath;

            File out = new File(tofilepath);
            if (!out.getParentFile().exists()) {
                out.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(out);
            fos.write(result.getBytes());


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static String getImageString(String filename) throws IOException {
        InputStream in = null;
        byte[] data = null;
        if(!filename.contains("http")){
            filename=Global.getConfig("ip")+filename;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(transChineseChar(filename.replaceAll("\\\\", "/")));
            //打开链接
            conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            in = conn.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                output.write(buffer, 0, n);
            }
            data=output.toByteArray();
            } catch(IOException e){
                throw e;
            } finally{
                if (in != null) in.close();
            }
            BASE64Encoder encoder = new BASE64Encoder();
            return data != null ? encoder.encode(data) : "";
        }

        public static String addImgToDoc (String base64,int rownum){
            StringBuilder img = new StringBuilder(
                    "</w:t> </w:r></w:p><w:p wsp:rsidR=\"009B2D9C\" " +
                            "wsp:rsidRDefault=\"00EE11CF\">" +
                            "<w:r>" +
                            "<w:pict>" +
                            "<v:shapetype id=\"_x0000_t75\" coordsize=\"21600,21600\" o:spt=\"75\" o:preferrelative=\"t\" path=\"m@4@5l@4@11@9@11@9@5xe\" filled=\"f\" stroked=\"f\">" +
                            "<v:stroke joinstyle=\"miter\"/>" +
                            "<v:formulas>" +
                            "<v:f eqn=\"if lineDrawn pixelLineWidth 0\"/>" +
                            "<v:f eqn=\"sum @0 1 0\"/><v:f eqn=\"sum 0 0 @1\"/>" +
                            "<v:f eqn=\"prod @2 1 2\"/><v:f eqn=\"prod @3 21600 pixelWidth\"/>" +
                            "<v:f eqn=\"prod @3 21600 pixelHeight\"/><v:f eqn=\"sum @0 0 1\"/>" +
                            "<v:f eqn=\"prod @6 1 2\"/><v:f eqn=\"prod @7 21600 pixelWidth\"/>" +
                            "<v:f eqn=\"sum @8 21600 0\"/><v:f eqn=\"prod @7 21600 pixelHeight\"/>" +
                            "<v:f eqn=\"sum @10 21600 0\"/>" +
                            "</v:formulas>" +
                            "<v:path o:extrusionok=\"f\" gradientshapeok=\"t\" o:connecttype=\"rect\"/>" +
                            "<o:lock v:ext=\"edit\" aspectratio=\"t\"/>" +
                            "</v:shapetype>" +
                            "<w:binData w:name=\"wordml://0300000" + rownum + ".png\" " +
                            "xml:space=\"preserve\">\n" + base64.substring(base64.indexOf(",") + 1) +
                            "</w:binData>" +
                            "<v:shape id=\"_x0000_i1025\" type=\"#_x0000_t75\" style=\"width:341.25pt;height:168pt\">" +
                            "<v:imagedata src=\"wordml://0300000" + rownum + ".png\" " +
                            "o:title=\"lALPD4PvH9tAY13NAvnNAoc_647_761\"/>" +
                            "</v:shape>" +
                            "</w:pict>" +
                            "</w:r>" +
                            "</w:p> <w:p wsp:rsidR=\"009B2D9C\" " +
                            "wsp:rsidRDefault=\"00EE11CF\"> <w:r " +
                            "wsp:rsidRPr=\"00310EC0\"> <w:t>");
            return img.toString();
        }

        public static String transChineseChar (String url){
            String string = url;
            String resultURL = "";
            //遍历字符串
            for (int i = 0; i < string.length(); i++) {
                char charAt = string.charAt(i);
                //只对汉字处理
                if (isChineseChar(charAt)) {
                    String encode = null;
                    try {
                        encode = URLEncoder.encode(charAt + "", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    resultURL += encode;
                } else {
                    resultURL += charAt;
                }
            }
            return resultURL;
        }

        //判断汉字的方法,只要编码在\u4e00到\u9fa5之间的都是汉字
        public static boolean isChineseChar ( char c){
            return String.valueOf(c).matches("[\u4e00-\u9fa5]");
        }
    }
