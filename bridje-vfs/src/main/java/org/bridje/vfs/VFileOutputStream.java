
package org.bridje.vfs;

import java.io.IOException;
import java.io.OutputStream;

public class VFileOutputStream extends OutputStream
{
    private final VFile vfile;

    private final OutputStream os;

    public VFileOutputStream(VFile vfile)
    {
        this.vfile = vfile;
        this.os = this.vfile.openForWrite();
    }

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
