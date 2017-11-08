package org.bridje.web.view.themes;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import com.yahoo.platform.yui.compressor.CssCompressor;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;

public class CompressHandler
{
    private static final Logger LOG = Logger.getLogger(CompressHandler.class.getName());

    private static final String JS_EXT = "js";

    private static final String CSS_EXT = "css";

    public static boolean canCompress(VFile file)
    {
        if (file == null)
        {
            return false;
        }
        Path filePath = file.getPath();
        if (filePath.toString().contains(".min") ||
            filePath.toString().contains("-min"))
        {
            return false;
        }
        String ext = filePath.getExtension();
        return ext.equals(JS_EXT) || ext.equals(CSS_EXT);
    }

    public static String compress(VFile file)
    {
        if (!canCompress(file))
        {
            return null;
        }

        String content = toString(file);
        if (content == null)
        {
            return null;
        }

        switch (file.getPath().getExtension())
        {
            case JS_EXT: return compressJs(file.getName(), content);
            case CSS_EXT: return compressCss(content);
        }
        return null;
    }

    private static String toString(VFile file)
    {
        try (InputStream inputStream = new VFileInputStream(file))
        {
            Reader streamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
            try (BufferedReader reader = new BufferedReader(streamReader))
            {
                return reader.lines()
                        .collect(Collectors.joining(System.lineSeparator()));
            }
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage());
            return null;
        }
    }

    private static String compressJs(String name, String js)
    {
        CompilerOptions options = new CompilerOptions();
        options.setLanguageIn(CompilerOptions.LanguageMode.ECMASCRIPT_2017);
        options.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT3);
        options.setContinueAfterErrors(true);
        options.setExternExports(true);
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        List<SourceFile> externs;
        try
        {
            externs = AbstractCommandLineRunner.getBuiltinExterns(options.getEnvironment());
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage());
            externs = Collections.emptyList();
        }

        SourceFile input = SourceFile.builder().buildFromCode(name, js);
        Compiler compiler = new Compiler();
        compiler.compile(externs, Collections.singletonList(input), options);

        if (!compiler.hasErrors())
        {
            return compiler.toSource();
        }
        else
        {
            StringBuilder msg = new StringBuilder("Error compressing " + name + ":");
            for (JSError error : compiler.getErrors())
            {
                msg.append("\n    ")
                   .append(error.toString());
            }
            LOG.log(Level.WARNING, msg.toString());
        }
        return js;
    }

    private static String compressCss(String css)
    {
        try
        {
            CssCompressor compressor = new CssCompressor(new StringReader(css));
            StringWriter writer = new StringWriter();
            compressor.compress(writer, -1);
            return writer.toString();
        }
        catch (IOException ex)
        {
            LOG.log(Level.WARNING, ex.getMessage());
        }
        return css;
    }
}
