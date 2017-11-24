
package org.bridje.vfs;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This object represents an output stream for a virtual file.
 */
public class VFileOutputStream extends OutputStream
{
    private final VFile vfile;

    private final OutputStream os;

    /**
     * Defaults constructor for this object.
     * 
     * @param vfile The file to write to.
     */
    public VFileOutputStream(VFile vfile)
    {
        this.vfile = vfile;
        this.os = this.vfile.openForWrite();
    }

    /**
     * Gets the VFile that this stream writes to.
     * 
     * @return The VFile that this stream writes to.
     */
    public VFile getVFile()
    {
        return vfile;
    }

    @Override
    public void write(int b) throws IOException
    {
        this.os.write(b);
    }

    @Override
    public void flush() throws IOException
    {
        this.os.flush();
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        this.os.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        this.os.write(b, off, len);
    }

    @Override
    public void close() throws IOException
    {
        this.os.close();
    }

}
