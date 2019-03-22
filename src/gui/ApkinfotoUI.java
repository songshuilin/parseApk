package gui;

import toolUtil.*;
import apkinfo.ApkInfo;
import apkinfo.ApkUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkinfotoUI {
    private JTextField Edit_FilePath;
    private JTextField Edit_AppName;
    private JTextField Edit_PackageName;
    private JTextField Edit_VersionName;
    private JTextField Edit_Version;
    private JTextField Edit_MD5;
    private JTextField Edit_ApkSize;
    private JComboBox jComboBox_lang;
    private Map AppName;
    private List AppNamekey;
    private ItemListener itemListener;
    private JLabel IcoBox;
    private JTable jTable_Permissions;
    private JTextArea jTextArea;//显示签名信息
    private JScrollPane jPanel_Sign;//签名信息的panel


    public ApkinfotoUI(JTextField Edit_FilePath, JTextField Edit_AppName, JTextField Edit_PackageName, JTextField Edit_VersionName,
                       JTextField Edit_Version, JTextField Edit_MD5, JTextField Edit_Apksieze, JComboBox jComboBox_lang, JLabel IcoBox, JTable jTable_Permissions
            , JScrollPane jPanel_Sign
    ) {
        this.Edit_FilePath = Edit_FilePath;
        this.Edit_AppName = Edit_AppName;
        this.Edit_MD5 = Edit_MD5;
        this.Edit_ApkSize = Edit_Apksieze;
        this.Edit_PackageName = Edit_PackageName;
        this.Edit_Version = Edit_Version;
        this.Edit_VersionName = Edit_VersionName;
        this.jComboBox_lang = jComboBox_lang;
        this.IcoBox = IcoBox;
        this.jTable_Permissions = jTable_Permissions;
        this.jPanel_Sign = jPanel_Sign;


        jTextArea = new JTextArea();//需要在换行的地方加入\n
        jTextArea.setSize(600, 1500);

        itemListener = getItemListener();
        jComboBox_lang.removeItemListener(itemListener);
        jComboBox_lang.addItemListener(itemListener);
    }

    public void OpenApkFile(String filePath) {
        ApkUtil apkUtil = new ApkUtil();
        ApkInfo apkInfo = apkUtil.parseApk(filePath);
        String signInfo = apkUtil.getApkSign(filePath);

        jTextArea.setText(signInfo);
        jPanel_Sign.add(jTextArea);
        jPanel_Sign.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel_Sign.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        Edit_FilePath.setText(filePath);
        Edit_PackageName.setText(apkInfo.getPackageName());
        Edit_AppName.setText(apkInfo.getLabel());
        String size = String.format("%.2f", apkInfo.getSize());
        Edit_ApkSize.setText(size + " M");//取小数点两位
        Edit_VersionName.setText(apkInfo.getVersionName());
        Edit_Version.setText(apkInfo.getVersionCode());
        AppName = apkInfo.getAppName();
        AppNamekey = apkInfo.getAppNameKey();

        if (AppNamekey.size() != 0) {
            jComboBox_lang.removeAllItems();
            for (Object key : AppNamekey) {
                if (key.equals("label")) {
                    jComboBox_lang.addItem("默认");
                } else {
                    jComboBox_lang.addItem(key);
                }
            }
            Edit_AppName.setText((String) AppName.get("label"));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Edit_MD5.setText("正在读取MD5");
                File file = new File(filePath);
                String md5 = Md5CaculateUtil.getMD5(file);
                Edit_MD5.setText(md5.toUpperCase());
            }
        }).start();

        //图标读取

        String icopath = apkInfo.getIcon();
    /*    Map map=apkInfo.getIcons();
        for (Object key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }*/

        //遍历ICON列表
        PermissionsUtil permissionsUtil = new PermissionsUtil();
        List<String> list1 = permissionsUtil.getList_PermissionsKey();
        List<String> list2 = permissionsUtil.getList_PermissionsName();
        List<String> list3 = permissionsUtil.getList_PermissionsNotes();
        Map<String, Object> map = new HashMap<>();

        List list = apkInfo.getUsesPermissions();

        for (Object key : list) {
            boolean isput = false;
            for (int i = 0; i < list1.size(); i++) {
                if (key.equals(list1.get(i))) {
                    PermissionsObj permissionsObj = new PermissionsObj((String) key, list2.get(i), list3.get(i));
                    map.put((String) key, permissionsObj);
                    isput = true;
                    break;
                }
                if (isput == false) {
                    PermissionsObj permissionsObj = new PermissionsObj((String) key, "未知权限", "");
                    map.put((String) key, permissionsObj);
                }
            }
        }

        DefaultTableModel jTable_PermissionsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int i = 0;
        //权限转数组
        String DataArray[][] = new String[map.size()][4];
        for (Object key : map.keySet()) {
            PermissionsObj obj = (PermissionsObj) map.get(key);
            DataArray[i][0] = String.valueOf(i + 1);
            DataArray[i][1] = obj.getPermissionskey();
            DataArray[i][2] = obj.getPermissionsName();
            DataArray[i][3] = obj.getPermissionsExplan();
            i++;
        }

        //构建表格模型
        String[] jTable_Permissions_colname = {"编号", "英文名", "权限名称", "权限注释"};
        DefaultTableModel model = new DefaultTableModel(DataArray, jTable_Permissions_colname) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Permissions.setModel(model);
        jTable_Permissions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable_Permissions.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable_Permissions.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(3).setPreferredWidth(400);

        //end
        File file = new File(filePath);
        try {
            ZipFile zf = new ZipFile(file);
            ZipEntry zipEntry = zf.getEntry(icopath);
            InputStream inputStream = zf.getInputStream(zipEntry);
            byte[] bite = readStream(inputStream);
            ImageIcon icon = new ImageIcon(bite);
            Image img = icon.getImage();
            img = img.getScaledInstance(96, 96, Image.SCALE_SMOOTH);//图片平滑优先，大小默认96*96
            icon.setImage(img);
            IcoBox.setIcon(icon);
            /*=null;
            inputStream.read(bite);
            ImageIcon icon=new ImageIcon(bite);
            IcoBox.setIcon(icon);*/
            zf.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ItemListener getItemListener() {
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem().equals("默认")) {
                    Edit_AppName.setText(String.valueOf(AppName.get("label")));

                } else {
                    Edit_AppName.setText(String.valueOf(AppName.get(e.getItem())));
                }
            }
        };
        return itemListener;
    }

    public byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

}