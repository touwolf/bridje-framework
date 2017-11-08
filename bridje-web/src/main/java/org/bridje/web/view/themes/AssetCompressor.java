package org.bridje.web.view.themes;

import org.bridje.vfs.VFile;

public interface AssetCompressor
{
    boolean canCompress(VFile file);

    String compress(VFile file);
}
