package gui;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class WinMain {
    private JFrame win;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private Label label6;
    private Label label7;

    private JTextField Edit_FilePath;
    private JTextField Edit_AppName;
    private JTextField Edit_PackageName;
    private JTextField Edit_VersionName;
    private JTextField Edit_Version;
    private JTextField Edit_MD5;
    private JTextField Edit_apkSize;

    private JButton Btn_ll;
    private JButton Btn_AppName;
    private JButton Btn_PackageName;
    private JButton Btn_VersionName;
    private JButton Btn_Version;
    private JButton Btn_MD5;
    private JButton Btn_Size;

    private JComboBox jComboBox_lang;
    private Thread ThreadMain;
    private ApkinfotoUI apkinfotoUI;
    private JTabbedPane jTabbedPane;
    private JPanel jPanel_Main;
    private JPanel jPanel_Permissions;
    private JScrollPane jPanel_Sign;
    private JTable jTable_Permissions;
    private final Color WRITE = new Color(16777215);

    //显示签名信息
    private JTextArea jTextArea;

    public void CreatWin() {
        setUI();
        win = new JFrame("APK Messenger v1.0");
        win.setSize(586, 277);
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        win.setResizable(false);
        win.setLayout(null);
        win.getContentPane().setBackground(WRITE);
        win.setSize(586, 535);

        //创建菜单
        MenuBar menuBar = new MenuBar();
        Menu menu_4 = new Menu("帮助");

        //帮助菜单
        MenuItem menuItem_4_1 = new MenuItem("关于软件");
        MenuItem menuItem_4_3 = new MenuItem("常见问题");
        menu_4.add(menuItem_4_1);
        menu_4.add(menuItem_4_3);
        //设置菜单
        menuBar.add(menu_4);
        win.setMenuBar(menuBar);
        //图标图片框
        JLabel IcoBox = new JLabel();

        IcoBox.setBounds(8, 24, 152, 152);
        IcoBox.setBorder(BorderFactory.createLineBorder(Color.black));
        ImageIcon andico = new ImageIcon(getClass().getResource("andico.png"));
        IcoBox.setHorizontalAlignment(JLabel.CENTER);//图片居中
        IcoBox.setIcon(andico);
        win.add(IcoBox);
        label1 = DrowLable("文件路径：", 168, 16, 56, 24);
        label2 = DrowLable("应用名：", 168, 48, 56, 24);
        label3 = DrowLable("包名：", 168, 75, 56, 24);
        label4 = DrowLable("版本名：", 168, 104, 56, 24);
        label5 = DrowLable("文件MD5：", 168, 136, 56, 24);
        label6 = DrowLable("APK大小：", 168, 168, 56, 24);
        label7 = DrowLable("版本号：", 377, 104, 45, 24);
        //语言选择下拉列表
        jComboBox_lang = new JComboBox();
        jComboBox_lang.setBounds(232, 49, 72, 20);
        jComboBox_lang.addItem("默认");
        //编辑框绘制
        Edit_FilePath = DrowEdit("", 232, 16, 256, 24);
        Edit_AppName = DrowEdit("", 312, 48, 176, 24);
        Edit_PackageName = DrowEdit("", 232, 75, 256, 24);
        Edit_VersionName = DrowEdit("", 232, 104, 64, 24);
        Edit_Version = DrowEdit("", 424, 104, 64, 24);
        Edit_MD5 = DrowEdit("", 232, 136, 256, 24);
        Edit_apkSize = DrowEdit("", 232, 168, 256, 24);
        //绘制按钮
        Btn_ll = DrowBtn("浏览", 497, 16, 72, 24);
        Btn_AppName = DrowBtn("复制", 497, 46, 72, 24);
        Btn_PackageName = DrowBtn("复制", 497, 74, 72, 24);
        Btn_VersionName = DrowBtn("复制", 301, 104, 72, 24);
        Btn_Version = DrowBtn("复制", 497, 104, 72, 24);
        Btn_MD5 = DrowBtn("复制", 497, 136, 72, 24);
        Btn_Size = DrowBtn("复制", 497, 168, 72, 24);

        //事件绑定
        Btn_ll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("安卓apk文件", "apk");
                fileChooser.addChoosableFileFilter(fileNameExtensionFilter);
                fileChooser.setFileFilter(fileNameExtensionFilter);
                int status = fileChooser.showOpenDialog(win.getContentPane());
                if (status != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "你没有选中文件！ 请重新尝试。", "出错啦！", JOptionPane.WARNING_MESSAGE);
                } else {
                    File file = fileChooser.getSelectedFile();
                    new Thread() {
                        @Override
                        public void run() {
                            apkinfotoUI.OpenApkFile(file.getAbsolutePath().toString().trim());
                        }
                    }.start();
                }
            }
        });

        Btn_AppName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_AppName.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });
        Btn_PackageName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_PackageName.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });

        Btn_Size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_apkSize.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });

        Btn_MD5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_MD5.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });

        Btn_Version.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_Version.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });

        Btn_VersionName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(Edit_VersionName.getText().toString().trim());
                clip.setContents(tText, null);
            }
        });

        //绘制详细信息的面板
        jPanel_Main = new JPanel();
        jPanel_Permissions = new JPanel();
        jPanel_Permissions.setOpaque(false);

        jPanel_Sign = new JScrollPane();
        jPanel_Sign.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jPanel_Sign.setLayout(null);
        jPanel_Sign.setBackground(WRITE);
        jPanel_Sign.setBounds(400, 200, 100, 200);

        jTabbedPane = new JTabbedPane();
        jTabbedPane.setBackground(WRITE);
        jPanel_Main.setBounds(8, 232, 560, 248);
        jPanel_Main.setBackground(WRITE);
        jTabbedPane.setBounds(8, 0, 550, 240);
        jPanel_Main.setLayout(null);
        jPanel_Main.add(jTabbedPane);
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        //权限表
        String[] jTable_Permissions_colname = {"编号", "英文名", "权限名称", "权限注释"};
        jTable_Permissions = new JTable(null, jTable_Permissions_colname);
        //禁止表格编辑
        DefaultTableModel jTable_PermissionsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable_Permissions.setGridColor(WRITE);

        JScrollPane jScrollPane_Permissions = new JScrollPane(jTable_Permissions);//拖动面板
        jScrollPane_Permissions.setBorder(BorderFactory.createLineBorder(WRITE));
        jTable_Permissions.getTableHeader().setReorderingAllowed(false);//禁止拖动
        jPanel_Permissions.setLayout(new BorderLayout());
        // jScrollPane_Permissions.setOpaque(false);
        jScrollPane_Permissions.setBackground(WRITE);
        jPanel_Permissions.add(jScrollPane_Permissions, BorderLayout.CENTER);
        jTable_PermissionsTableModel.setDataVector(null, jTable_Permissions_colname);
        jTable_Permissions.setModel(jTable_PermissionsTableModel);
        jTable_Permissions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable_Permissions.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable_Permissions.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(2).setPreferredWidth(120);
        jTable_Permissions.getColumnModel().getColumn(3).setPreferredWidth(400);


        jTabbedPane.addTab("权限信息", jPanel_Permissions);
        jTabbedPane.addTab("签名信息", jPanel_Sign);

        //添加组件
        addobj(Edit_AppName, Edit_FilePath, Edit_MD5, Edit_apkSize, Edit_PackageName, Edit_Version, Edit_VersionName);
        addobj(label1, label2, label3, label4, label5, label6, label7);
        addobj(Btn_AppName, Btn_ll, Btn_MD5, Btn_PackageName, Btn_Size, Btn_Version, Btn_VersionName, jComboBox_lang);
        addobj(jPanel_Main);
        addDropTarget(win.getContentPane());
        addDropTarget(Edit_AppName);
        addDropTarget(Edit_FilePath);
        addDropTarget(Edit_MD5);
        addDropTarget(Edit_apkSize);
        addDropTarget(Edit_PackageName);
        addDropTarget(Edit_Version);
        addDropTarget(Edit_VersionName);

        addDropTarget(IcoBox);
        win.setVisible(true);
        apkinfotoUI = new ApkinfotoUI(Edit_FilePath, Edit_AppName, Edit_PackageName, Edit_VersionName, Edit_Version, Edit_MD5
                , Edit_apkSize, jComboBox_lang, IcoBox, jTable_Permissions, jPanel_Sign);
    }

    private void addDropTarget(Component obj) {
        DropTarget dropTarget = CreatdropTarget();
        obj.setDropTarget(dropTarget);
    }

    private DropTarget CreatdropTarget() {
        DropTargetAdapter dropTargetAdapter = new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        //只能单一拖放
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        //获取文件后缀名
                        String FileName = list.get(0).getAbsolutePath();
                        String prefix = FileName.substring(FileName.lastIndexOf(".") + 1).toLowerCase();
                        if (prefix.equals("apk")) {

                            ThreadMain = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    apkinfotoUI.OpenApkFile(FileName);
                                }
                            });
                            ThreadMain.start();
                        } else {
                            JOptionPane.showMessageDialog(null, "你这个不是apk！ 请重新尝试。", "出错啦！", JOptionPane.ERROR_MESSAGE);
                        }
                        dtde.dropComplete(true);//指示拖拽操作已完成
                    } else {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DropTarget dropTarget = new DropTarget(win, dropTargetAdapter);
        return dropTarget;
    }


    private void addobj(Object... objs) {
        for (Object obj : objs) {
            win.add((Component) obj);
        }
    }

    private Label DrowLable(String str, int x, int y, int w, int h) {
        Label lab = new Label(str);
        lab.setBounds(x, y, w, h);
        return lab;
    }

    private JTextField DrowEdit(String str, int x, int y, int w, int h) {
        JTextField Edit = new JTextField(str);
        Edit.setBounds(x, y, w, h);
        return Edit;
    }

    private JButton DrowBtn(String str, int x, int y, int w, int h) {
        JButton Btn = new JButton(str);
        Btn.setBounds(x, y, w, h);
        return Btn;
    }


    private void setUI() {
        String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        try {
            UIManager.setLookAndFeel(windows);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
