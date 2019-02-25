package com.kejun.trans.fileBrowser;

import java.io.File;

public interface OnFileBrowserResultListener {
    void selectFile(File file);
    void nothing();
}
