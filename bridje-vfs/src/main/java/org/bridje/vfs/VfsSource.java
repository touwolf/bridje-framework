
package org.bridje.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface VfsSource
{
    boolean isDirectory(Path path);

    boolean isFile(Path path);
    
    boolean exists(Path path);

    boolean canWrite(Path path);

    boolean canRead(Path path);

    String[] list(Path path);

    InputStream openForRead(Path path);

    OutputStream openForWrite(Path path);

    List<Path> search(GlobExpr globExpr, Path path);

    boolean createNewFile(Path path);

    boolean mkdir(Path path);
}
