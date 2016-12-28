
package org.bridje.vfs;

import java.io.IOException;
import java.io.InputStream;

public class VFileInputStream extends InputStream
{
    private final VFile vfile;

    private final InputStream os;
    
    public VFileInputStream(VFile vfile)
    {
        this.vfile = vfile;
        this.os = this.vfile.openForRead();
    }

    public VFile getVFile()
    {
        return vfile;
    }

    @Override
    public void close() throws IOException
    {
        this.os.close();
    }

    @Override
    public int available() throws IOException
    {
        return this.os.available();
    }

    @Override
    public synchronized void mark(int readlimit)
    {
        this.os.mark(readlimit);
    }

    @Override
    public boolean markSupported()
    {
        return this.os.markSupported();
    }

    @Override
    public int read() throws IOException
    {
        return this.os.read();
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        return this.os.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        return this.os.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException
    {
        this.os.reset();
    }

    @Override
    public long skip(long n) throws IOException
    {
        return this.os.skip(n);
    }
}
