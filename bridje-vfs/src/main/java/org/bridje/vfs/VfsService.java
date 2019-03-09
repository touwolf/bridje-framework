
package org.bridje.vfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface represents the virtual file system for Bridje, it provides
 * methods to mount and file files and folder into the system.
 */
public interface VfsService
{

    /**
     * Mounts a new source into the given path.
     *
     * @param path   The path to mount the source.
     * @param source The virtual file system source object to be mounted.
     * @throws java.io.FileNotFoundException If the underlying file does not exists.
     */
    void mount(Path path, VfsSource source) throws FileNotFoundException;

    /**
     * Unmounts the last source mounted into the given path.
     *
     * @param path   The path to mount the source.
     * @throws java.io.FileNotFoundException If the underlying file does not exists.
     */
    void unmount(Path path) throws FileNotFoundException;

    /**
     * Check if the node is a directory
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the node is a directory and false otherwise.
     */
    boolean isDirectory(Path path);

    /**
     * Check if the file exist really.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the file exist and false otherwise.
     */
    boolean exists(Path path);

    /**
     * Check if the node is a file.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the node is a file and false otherwise.
     */
    boolean isFile(Path path);

    /**
     * Check if we can write on this node.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if we can write and false otherwise.
     */
    boolean canWrite(Path path);

    /**
     * Check if we can read on this node.
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
     * @return VFile[], return VFile Array that result of the quest.
     */
    VFile[] search(GlobExpr globExpr, Path path);

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
     * Delete a file in the VFS tree.
     * <p>
     * @param path The path attribute
     *
     * @return Boolean, true if the file is deleted without problem and false
     *         otherwise.
     */
    boolean delete(Path path);

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
     * Return String object with the name of the file type.
     * <p>
     * @param extension The extension of the file.
     *
     * @return String object, return String object with name of the file type.
     */
    String getMimeType(String extension);

    /**
     * Gets the raw file that match the given virtual file path if any. 
     * or null if no raw file is attached to this virtual file.
     * 
     * @param path The path of the virtual file.
     * @return The raw file for the given path.
     */
    File getRawFile(Path path);
}
