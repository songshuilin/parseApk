package toolUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PermissionsUtil {
    private List<String> List_PermissionsKey = new ArrayList();
    private List<String> List_PermissionsName = new ArrayList();
    private List<String> List_PermissionsNotes = new ArrayList();

    public List<String> getList_PermissionsKey() {
        return List_PermissionsKey;
    }

    public List<String> getList_PermissionsName() {
        return List_PermissionsName;
    }

    public List<String> getList_PermissionsNotes() {
        return List_PermissionsNotes;
    }

    public PermissionsUtil() {
        try {

            //用exe4j 工具出exe才这样写。
//            File f=new File("");
//            String filepath=f.getCanonicalPath()+"\\res\\Permissionsinfo.dat";
             String filepath=this.getClass().getResource("/Permissionsinfo.dat").getPath();

                InputStreamReader read = new InputStreamReader(new FileInputStream(filepath), "utf-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] str = line.split("/");
                    for (int i = 0; i < str.length; i++) {
                        List_PermissionsKey.add(str[0]);
                        List_PermissionsName.add(str[1]);
                        List_PermissionsNotes.add(str[2]);
                    }
                }
                read.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"出错啦！",JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }
}
