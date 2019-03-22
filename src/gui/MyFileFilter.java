package gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MyFileFilter extends FileFilter {
    private String filter;

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            return f.getName().endsWith(".apk");
        }
    }

    @Override
    public String getDescription() {
        return filter;
    }

    public MyFileFilter(String filter) {
        this.filter = filter;
    }
}
