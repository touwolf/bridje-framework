
package org.bridje.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class VFileInputStream extends InputStream
{
    private final VFile vfile;

    private final InputStream is;
    
    public VFileInputStream(VFile vfile) throws FileNotFoundException
    {
        this.vfile = vfile;
        this.is = this.vfile.openForRead();
        if(is == null) throw new FileNotFoundException("Cannot open the file for read.");
    }

    public VFile getVFile()
    {
        return vfile;
    }

    @Override
    public void close() throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        this.is.close();
    }

    @Override
    public int available() throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        return this.is.available();
    }

    @Override
    public synchronized void mark(int readlimit)
    {
        if(is == null) return;
        this.is.mark(readlimit);
    }

    @Override
    public boolean markSupported()
    {
        if(is == null) return false;
        return this.is.markSupported();
    }

    @Override
    public int read() throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        return this.is.read();
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        return this.is.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        return this.is.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        this.is.reset();
    }

    @Override
    public long skip(long n) throws IOException
    {
        if(is == null) throw new IOException("Cannot open the file for read.");
        return this.is.skip(n);
    }
}
