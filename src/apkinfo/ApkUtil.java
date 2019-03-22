package apkinfo;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApkUtil {

    public static final String APPLICATION = "application:";
    public static final String APPLICATION_ICON = "application-icon";
    public static final String APPLICATION_LABEL = "application-label";
    public static final String APPLICATION_LABEL_N = "application: label";
    public static final String DENSITIES = "densities";
    public static final String AppName = "application-label";
    public static final String LAUNCHABLE_ACTIVITY = "launchable";
    public static final String PACKAGE = "package";
    public static final String SDK_VERSION = "sdkVersion";
    public static final String SUPPORTS_ANY_DENSITY = "support-any-density";
    public static final String SUPPORTS_SCREENS = "support-screens";
    public static final String TARGET_SDK_VERSION = "targetSdkVersion";
    public static final String VERSION_CODE = "versionCode";
    public static final String VERSION_NAME = "versionName";
    public static final String USES_FEATURE = "uses-feature";
    public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
    public static final String USES_PERMISSION = "uses-permission";

    private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";

    private ProcessBuilder builder;

    public ApkUtil() {
        builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
    }


    //获取apk签名信息
    public String getApkSign(String apkPath) {
        StringBuffer sb = new StringBuffer();
        String keyTool = "keytool";
        Process process = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            //出exe才这样写。
//          File f=new File("");
//          keyTool=f.getCanonicalPath()+"\\res\\"+keyTool;

            //  keyTool="src\\tool\\"+keyTool;
            //直接用JRE中bin中的keytool
            process = builder.command(keyTool, "-printcert", "-jarfile", apkPath).start();
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                temp = temp.trim();
                if ("".equalsIgnoreCase(temp)) {
                    continue;
                }
                if (temp.startsWith("MD5")) {
                    temp = temp.toLowerCase().replace("md5", "");
                    temp = temp.replace(":", "");
                    sb.append("MD5:" + temp + "\n");
                    continue;
                }

                if (temp.startsWith("SHA1")) {
                    temp = temp.toLowerCase().replace("sha1", "");
                    temp = temp.replace(":", "");
                    sb.append("SHA1:" + temp + "\n");
                    continue;
                }

                if (temp.startsWith("SHA256")) {
                    temp = temp.toLowerCase().replace("sha256", "");
                    temp = temp.replace(":", "");
                    sb.append("SHA256:" + temp + "\n");
                    continue;
                }
                sb.append(temp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("sb==" + sb.toString());
        return sb.toString().substring(sb.indexOf("所有者"), sb.indexOf("版本"));
    }

    //获取apk信息
    public ApkInfo parseApk(String apkPath) {

        Process process = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            String aaptTool = "aapt";
            //出exe才这样写。
//             File f=new File("");
//             aaptTool=f.getCanonicalPath()+"\\res\\"+aaptTool;

            aaptTool = "src\\tool\\" + aaptTool;
            process = builder.command(aaptTool, "d", "badging", apkPath).start();
            inputStream = process.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            ApkInfo apkInfo = new ApkInfo();
            apkInfo.setSize(new File(apkPath).length());
            String temp = null;

            while ((temp = bufferedReader.readLine()) != null) {
                setApkInfoProperty(apkInfo, temp);
                // System.out.println(temp);
            }
            return apkInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setApkInfoProperty(ApkInfo apkInfo, String source) {
        //System.out.println("*******************************");
        if (source.startsWith(APPLICATION)) {
            //System.out.println(APPLICATION + " : ");
            String[] rs = source.split("( icon=')|'");
            apkInfo.setIcon(rs[rs.length - 1]);
        } else if (source.startsWith(APPLICATION_ICON)) {
            //System.out.println(APPLICATION_ICON + " : ");
            apkInfo.addToIcons(getKeyBeforeColon(source), getPropertyInQuote(source));

        } else if (source.startsWith(AppName)) {
            //System.out.println("添加名称："+getPropertyInQuote(source));
            String[] rs = getKeyBeforeColon(source).split("-");
            apkInfo.addAppNameKey(rs[rs.length - 1]);
            apkInfo.addAppName(rs[rs.length - 1], getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION_LABEL)) {
            //System.out.println(APPLICATION_LABEL + " : ");
            apkInfo.setLabel(getPropertyInQuote(source));
        } else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
            //System.out.println(LAUNCHABLE_ACTIVITY + " : ");
            apkInfo.setLaunchableActivity(getPropertyInQuote(source));
        } else if (source.startsWith(PACKAGE)) {
            //System.out.println(PACKAGE + " : ");
            String[] packageInfo = source.split(SPLIT_REGEX);
            apkInfo.setPackageName(packageInfo[2]);
            apkInfo.setVersionCode(packageInfo[4]);
            apkInfo.setVersionName(packageInfo[6]);
        } else if (source.startsWith(SDK_VERSION)) {
            //System.out.println(SDK_VERSION + " : ");
            apkInfo.setSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(TARGET_SDK_VERSION)) {
            //System.out.println(TARGET_SDK_VERSION + " : ");
            apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(USES_PERMISSION)) {
            //System.out.println(USES_PERMISSION + " : ");
            apkInfo.addToUsesPermissions(getPropertyInQuote(source));
        } else if (source.startsWith(USES_FEATURE)) {
            // System.out.println(USES_FEATURE + " : ");
            apkInfo.addToFeatures(getPropertyInQuote(source));
        } else {
            //System.out.println("Others : ");
        }
        // System.out.println(source);
    }

    private String getKeyBeforeColon(String source) {
        return source.substring(0, source.indexOf(':'));
    }

    private String getPropertyInQuote(String source) {
        int index = source.indexOf("'") + 1;
        return source.substring(index, source.indexOf('\'', index));
    }

}