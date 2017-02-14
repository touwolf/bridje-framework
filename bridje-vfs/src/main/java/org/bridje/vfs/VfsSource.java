
package org.bridje.vfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * This interface represents the virtual file system source(VfsSource).
 */
public interface VfsSource
{
    /**
     * Check if the VfsSource is a directory
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the VfsSource is a directory and false
     *         otherwise.
     */
    boolean isDirectory(Path path);

    /**
     * Check if the VfsSource is a file.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the VfsSource is a file and false otherwise.
     */
    boolean isFile(Path path);

    /**
     * Check if the VfsSource exist really.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the VfsSource exist and false otherwise.
     */
    boolean exists(Path path);

    /**
     * Check if we can write on this VfsSource.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if we can write and false otherwise.
     */
    boolean canWrite(Path path);

    /**
     * Check if we can read on this VfsSource.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if we can read and false otherwise.
     */
    boolean canRead(Path path);

    /**
     * Return String Array with the names of files and folder that they exist in
     * this path attribute.
     * <p>
     * @param path The path attribute
     *
     * @return String[] ,String Array with the names of files and folder that
     *         they exist in this path attribute.
     */
    String[] list(Path path);

    /**
     * Return InputStream object, then we can read the files. This method we
     * utilized it when we want to read a file
     * <p>
     * @param path The path attribute
     *
     * @return InputStream object.
     */
    InputStream openForRead(Path path);

    /**
     * Return OutputStream object, then we can write the file. This method we
     * utilized it when we want to write a file
     * <p>
     * @param path The path attribute
     *
     * @return OutputStream object.
     */
    OutputStream openForWrite(Path path);

    /**
     * This method we utilized it when we want to search any file inside of the
     * path
     * <p>
     * @param globExpr object, that represent the regular expression for make
     *                 the quest.
     * @param path     The path attribute
     *
     * @return List of Path, return List of Path object that result of the
     *         quest.
     */
    List<Path> search(GlobExpr globExpr, Path path);

    /**
     * Create a new file.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the file is created without problem and false
     *         otherwise.
     */
    boolean createNewFile(Path path);

    /**
     * Create a new directory.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the directory is created without problem and
     *         false otherwise.
     */
    boolean mkdir(Path path);

    /**
     * Delete a file.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the file is deleted without problem and false
     *         otherwise.
     */
    boolean delete(Path path);

}
