
package org.bridje.vfs;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public interface VfsService
{
    void mount(Path path, VfsSource source) throws FileNotFoundException;

    boolean isDirectory(Path path);

    boolean exists(Path path);

    boolean isFile(Path path);

    boolean canWrite(Path path);

    boolean canRead(Path path);

    String[] list(Path path);

    InputStream openForRead(Path path);

    OutputStream openForWrite(Path path);

    VFile[] search(GlobExpr globExpr, Path path);

    boolean createNewFile(Path path);

    boolean mkdir(Path path);
    
    String getMimeType(String extension);
}
