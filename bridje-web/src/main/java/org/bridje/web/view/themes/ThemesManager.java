/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.web.view.themes;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.bridje.el.ElEnvironment;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.vfs.VfsService;
import org.bridje.web.view.widgets.Widget;
import org.bridje.web.view.WebView;
import org.bridje.http.HttpBridletResponse;
import org.bridje.vfs.VFile;

@Component
public class ThemesManager
{
    private static final Logger LOG = Logger.getLogger(ThemesManager.class.getName());

    private Configuration ftlCfg;
    
    @Inject
    private VfsService vfs;
    
    @PostConstruct
    public void init()
    {
        ftlCfg = new Configuration(Configuration.VERSION_2_3_23);
        ftlCfg.setTemplateLoader(Ioc.context().find(ThemesTplLoader.class));
        ftlCfg.setDefaultEncoding("UTF-8");
        ftlCfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        ftlCfg.setLogTemplateExceptions(false);
    }

    public void render(WebView view, OutputStream os, Map<String, String> state)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            String themeName = findThemeName();
            String templatePath = themeName + "/view.ftl";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map data = new HashMap();
            data.put("view", view);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("state", state);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void render(Widget widget, WebView view, OutputStream os, Object result, Map<String, String> state)
    {
        try(Writer w = new OutputStreamWriter(os, Charset.forName("UTF-8")))
        {
            String themeName = findThemeName();
            String templatePath = themeName + "/view.ftl";
            Template tpl = ftlCfg.getTemplate(templatePath);
            Map data = new HashMap();
            data.put("view", view);
            data.put("widget", widget);
            data.put("result", result);
            data.put("env", Thls.get(ElEnvironment.class));
            data.put("state", state);
            tpl.process(data, w);
            w.flush();
        }
        catch (TemplateException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public boolean serveResource(String themeName, String resPath, HttpBridletResponse resp) throws IOException
    {
        VFile f = vfs.findFile("/web/themes/" + themeName + "/resources/" + resPath);
        if(f != null)
        {
            String contentType = findContentType(f);
            resp.setContentType(contentType);
            try(InputStream is = f.openForRead())
            {
                OutputStream os = resp.getOutputStream();
                int ch = is.read();
                while(ch != -1)
                {
                    os.write(ch);
                    ch = is.read();
                }
                os.flush();
                return true;
            }
        }
        return false;
    }

    @Inject
    private ThemeResolver resolv;

    private String findThemeName()
    {
        if(resolv != null)
        {
            String themeName = resolv.findThemeName();
            if(themeName != null && !themeName.trim().isEmpty())
            {
                return themeName;
            }
        }
        return "default";
    }

    private Map<String, String> mimeTypes;
    
    private String findContentType(VFile f)
    {
        if(mimeTypes == null)
        {
            initMimeTypes();
        }
        String mimeType =  mimeTypes.get(f.getExtension());
        if(mimeType != null)
        {
            return mimeType;
        }
        return "text/html";
    }

    private synchronized void initMimeTypes()
    {
        Map<String,String> map = new HashMap<>();
        
        map.put("3dm","x-world/x-3dmf");
        map.put("3dmf","x-world/x-3dmf");
        map.put("a","application/octet-stream");
        map.put("aab","application/x-authorware-bin");
        map.put("aam","application/x-authorware-map");
        map.put("aas","application/x-authorware-seg");
        map.put("abc","text/vnd.abc");
        map.put("acgi","text/html");
        map.put("afl","video/animaflex");
        map.put("ai","application/postscript");
        map.put("aif","audio/aiff");
        map.put("aif","audio/x-aiff");
        map.put("aifc","audio/aiff");
        map.put("aifc","audio/x-aiff");
        map.put("aiff","audio/aiff");
        map.put("aiff","audio/x-aiff");
        map.put("aim","application/x-aim");
        map.put("aip","text/x-audiosoft-intra");
        map.put("ani","application/x-navi-animation");
        map.put("aos","application/x-nokia-9000-communicator-add-on-software");
        map.put("aps","application/mime");
        map.put("arc","application/octet-stream");
        map.put("arj","application/arj");
        map.put("arj","application/octet-stream");
        map.put("art","image/x-jg");
        map.put("asf","video/x-ms-asf");
        map.put("asm","text/x-asm");
        map.put("asp","text/asp");
        map.put("asx","application/x-mplayer2");
        map.put("asx","video/x-ms-asf");
        map.put("asx","video/x-ms-asf-plugin");
        map.put("au","audio/basic");
        map.put("au","audio/x-au");
        map.put("avi","application/x-troff-msvideo");
        map.put("avi","video/avi");
        map.put("avi","video/msvideo");
        map.put("avi","video/x-msvideo");
        map.put("avs","video/avs-video");
        map.put("bcpio","application/x-bcpio");
        map.put("bin","application/mac-binary");
        map.put("bin","application/macbinary");
        map.put("bin","application/octet-stream");
        map.put("bin","application/x-binary");
        map.put("bin","application/x-macbinary");
        map.put("bm","image/bmp");
        map.put("bmp","image/bmp");
        map.put("bmp","image/x-windows-bmp");
        map.put("boo","application/book");
        map.put("book","application/book");
        map.put("boz","application/x-bzip2");
        map.put("bsh","application/x-bsh");
        map.put("bz","application/x-bzip");
        map.put("bz2","application/x-bzip2");
        map.put("c","text/plain");
        map.put("c","text/x-c");
        map.put("c++","text/plain");
        map.put("cat","application/vnd.ms-pki.seccat");
        map.put("cc","text/plain");
        map.put("cc","text/x-c");
        map.put("ccad","application/clariscad");
        map.put("cco","application/x-cocoa");
        map.put("cdf","application/cdf");
        map.put("cdf","application/x-cdf");
        map.put("cdf","application/x-netcdf");
        map.put("cer","application/pkix-cert");
        map.put("cer","application/x-x509-ca-cert");
        map.put("cha","application/x-chat");
        map.put("chat","application/x-chat");
        map.put("class","application/java");
        map.put("class","application/java-byte-code");
        map.put("class","application/x-java-class");
        map.put("com","application/octet-stream");
        map.put("com","text/plain");
        map.put("conf","text/plain");
        map.put("cpio","application/x-cpio");
        map.put("cpp","text/x-c");
        map.put("cpt","application/mac-compactpro");
        map.put("cpt","application/x-compactpro");
        map.put("cpt","application/x-cpt");
        map.put("crl","application/pkcs-crl");
        map.put("crl","application/pkix-crl");
        map.put("crt","application/pkix-cert");
        map.put("crt","application/x-x509-ca-cert");
        map.put("crt","application/x-x509-user-cert");
        map.put("csh","application/x-csh");
        map.put("csh","text/x-script.csh");
        map.put("css","application/x-pointplus");
        map.put("css","text/css");
        map.put("cxx","text/plain");
        map.put("dcr","application/x-director");
        map.put("deepv","application/x-deepv");
        map.put("def","text/plain");
        map.put("der","application/x-x509-ca-cert");
        map.put("dif","video/x-dv");
        map.put("dir","application/x-director");
        map.put("dl","video/dl");
        map.put("dl","video/x-dl");
        map.put("doc","application/msword");
        map.put("dot","application/msword");
        map.put("dp","application/commonground");
        map.put("drw","application/drafting");
        map.put("dump","application/octet-stream");
        map.put("dv","video/x-dv");
        map.put("dvi","application/x-dvi");
        map.put("dwf","drawing/x-dwf (old)");
        map.put("dwf","model/vnd.dwf");
        map.put("dwg","application/acad");
        map.put("dwg","image/vnd.dwg");
        map.put("dwg","image/x-dwg");
        map.put("dxf","application/dxf");
        map.put("dxf","image/vnd.dwg");
        map.put("dxf","image/x-dwg");
        map.put("dxr","application/x-director");
        map.put("el","text/x-script.elisp");
        map.put("elc","application/x-bytecode.elisp (compiled elisp)");
        map.put("elc","application/x-elc");
        map.put("env","application/x-envoy");
        map.put("eps","application/postscript");
        map.put("es","application/x-esrehber");
        map.put("etx","text/x-setext");
        map.put("evy","application/envoy");
        map.put("evy","application/x-envoy");
        map.put("exe","application/octet-stream");
        map.put("f","text/plain");
        map.put("f","text/x-fortran");
        map.put("f77","text/x-fortran");
        map.put("f90","text/plain");
        map.put("f90","text/x-fortran");
        map.put("fdf","application/vnd.fdf");
        map.put("fif","application/fractals");
        map.put("fif","image/fif");
        map.put("fli","video/fli");
        map.put("fli","video/x-fli");
        map.put("flo","image/florian");
        map.put("flx","text/vnd.fmi.flexstor");
        map.put("fmf","video/x-atomic3d-feature");
        map.put("for","text/plain");
        map.put("for","text/x-fortran");
        map.put("fpx","image/vnd.fpx");
        map.put("fpx","image/vnd.net-fpx");
        map.put("frl","application/freeloader");
        map.put("funk","audio/make");
        map.put("g","text/plain");
        map.put("g3","image/g3fax");
        map.put("gif","image/gif");
        map.put("gl","video/gl");
        map.put("gl","video/x-gl");
        map.put("gsd","audio/x-gsm");
        map.put("gsm","audio/x-gsm");
        map.put("gsp","application/x-gsp");
        map.put("gss","application/x-gss");
        map.put("gtar","application/x-gtar");
        map.put("gz","application/x-compressed");
        map.put("gz","application/x-gzip");
        map.put("gzip","application/x-gzip");
        map.put("gzip","multipart/x-gzip");
        map.put("h","text/plain");
        map.put("h","text/x-h");
        map.put("hdf","application/x-hdf");
        map.put("help","application/x-helpfile");
        map.put("hgl","application/vnd.hp-hpgl");
        map.put("hh","text/plain");
        map.put("hh","text/x-h");
        map.put("hlb","text/x-script");
        map.put("hlp","application/hlp");
        map.put("hlp","application/x-helpfile");
        map.put("hlp","application/x-winhelp");
        map.put("hpg","application/vnd.hp-hpgl");
        map.put("hpgl","application/vnd.hp-hpgl");
        map.put("hqx","application/binhex");
        map.put("hqx","application/binhex4");
        map.put("hqx","application/mac-binhex");
        map.put("hqx","application/mac-binhex40");
        map.put("hqx","application/x-binhex40");
        map.put("hqx","application/x-mac-binhex40");
        map.put("hta","application/hta");
        map.put("htc","text/x-component");
        map.put("htm","text/html");
        map.put("html","text/html");
        map.put("htmls","text/html");
        map.put("htt","text/webviewhtml");
        map.put("htx","text/html");
        map.put("ice","x-conference/x-cooltalk");
        map.put("ico","image/x-icon");
        map.put("idc","text/plain");
        map.put("ief","image/ief");
        map.put("iefs","image/ief");
        map.put("iges","application/iges");
        map.put("iges","model/iges");
        map.put("igs","application/iges");
        map.put("igs","model/iges");
        map.put("ima","application/x-ima");
        map.put("imap","application/x-httpd-imap");
        map.put("inf","application/inf");
        map.put("ins","application/x-internett-signup");
        map.put("ip","application/x-ip2");
        map.put("isu","video/x-isvideo");
        map.put("it","audio/it");
        map.put("iv","application/x-inventor");
        map.put("ivr","i-world/i-vrml");
        map.put("ivy","application/x-livescreen");
        map.put("jam","audio/x-jam");
        map.put("jav","text/plain");
        map.put("jav","text/x-java-source");
        map.put("java","text/plain");
        map.put("java","text/x-java-source");
        map.put("jcm","application/x-java-commerce");
        map.put("jfif","image/jpeg");
        map.put("jfif","image/pjpeg");
        map.put("jfif-tbnl","image/jpeg");
        map.put("jpe","image/jpeg");
        map.put("jpe","image/pjpeg");
        map.put("jpeg","image/jpeg");
        map.put("jpeg","image/pjpeg");
        map.put("jpg","image/jpeg");
        map.put("jpg","image/pjpeg");
        map.put("jps","image/x-jps");
        map.put("js","application/x-javascript");
        map.put("js","application/javascript");
        map.put("js","application/ecmascript");
        map.put("js","text/javascript");
        map.put("js","text/ecmascript");
        map.put("jut","image/jutvision");
        map.put("kar","audio/midi");
        map.put("kar","music/x-karaoke");
        map.put("ksh","application/x-ksh");
        map.put("ksh","text/x-script.ksh");
        map.put("la","audio/nspaudio");
        map.put("la","audio/x-nspaudio");
        map.put("lam","audio/x-liveaudio");
        map.put("latex","application/x-latex");
        map.put("lha","application/lha");
        map.put("lha","application/octet-stream");
        map.put("lha","application/x-lha");
        map.put("lhx","application/octet-stream");
        map.put("list","text/plain");
        map.put("lma","audio/nspaudio");
        map.put("lma","audio/x-nspaudio");
        map.put("log","text/plain");
        map.put("lsp","application/x-lisp");
        map.put("lsp","text/x-script.lisp");
        map.put("lst","text/plain");
        map.put("lsx","text/x-la-asf");
        map.put("ltx","application/x-latex");
        map.put("lzh","application/octet-stream");
        map.put("lzh","application/x-lzh");
        map.put("lzx","application/lzx");
        map.put("lzx","application/octet-stream");
        map.put("lzx","application/x-lzx");
        map.put("m","text/plain");
        map.put("m","text/x-m");
        map.put("m1v","video/mpeg");
        map.put("m2a","audio/mpeg");
        map.put("m2v","video/mpeg");
        map.put("m3u","audio/x-mpequrl");
        map.put("man","application/x-troff-man");
        map.put("map","application/x-navimap");
        map.put("mar","text/plain");
        map.put("mbd","application/mbedlet");
        map.put("mc$","application/x-magic-cap-package-1.0");
        map.put("mcd","application/mcad");
        map.put("mcd","application/x-mathcad");
        map.put("mcf","image/vasa");
        map.put("mcf","text/mcf");
        map.put("mcp","application/netmc");
        map.put("me","application/x-troff-me");
        map.put("mht","message/rfc822");
        map.put("mhtml","message/rfc822");
        map.put("mid","application/x-midi");
        map.put("mid","audio/midi");
        map.put("mid","audio/x-mid");
        map.put("mid","audio/x-midi");
        map.put("mid","music/crescendo");
        map.put("mid","x-music/x-midi");
        map.put("midi","application/x-midi");
        map.put("midi","audio/midi");
        map.put("midi","audio/x-mid");
        map.put("midi","audio/x-midi");
        map.put("midi","music/crescendo");
        map.put("midi","x-music/x-midi");
        map.put("mif","application/x-frame");
        map.put("mif","application/x-mif");
        map.put("mime","message/rfc822");
        map.put("mime","www/mime");
        map.put("mjf","audio/x-vnd.audioexplosion.mjuicemediafile");
        map.put("mjpg","video/x-motion-jpeg");
        map.put("mm","application/base64");
        map.put("mm","application/x-meme");
        map.put("mme","application/base64");
        map.put("mod","audio/mod");
        map.put("mod","audio/x-mod");
        map.put("moov","video/quicktime");
        map.put("mov","video/quicktime");
        map.put("movie","video/x-sgi-movie");
        map.put("mp2","audio/mpeg");
        map.put("mp2","audio/x-mpeg");
        map.put("mp2","video/mpeg");
        map.put("mp2","video/x-mpeg");
        map.put("mp2","video/x-mpeq2a");
        map.put("mp3","audio/mpeg3");
        map.put("mp3","audio/x-mpeg-3");
        map.put("mp3","video/mpeg");
        map.put("mp3","video/x-mpeg");
        map.put("mpa","audio/mpeg");
        map.put("mpa","video/mpeg");
        map.put("mpc","application/x-project");
        map.put("mpe","video/mpeg");
        map.put("mpeg","video/mpeg");
        map.put("mpg","audio/mpeg");
        map.put("mpg","video/mpeg");
        map.put("mpga","audio/mpeg");
        map.put("mpp","application/vnd.ms-project");
        map.put("mpt","application/x-project");
        map.put("mpv","application/x-project");
        map.put("mpx","application/x-project");
        map.put("mrc","application/marc");
        map.put("ms","application/x-troff-ms");
        map.put("mv","video/x-sgi-movie");
        map.put("my","audio/make");
        map.put("mzz","application/x-vnd.audioexplosion.mzz");
        map.put("nap","image/naplps");
        map.put("naplps","image/naplps");
        map.put("nc","application/x-netcdf");
        map.put("ncm","application/vnd.nokia.configuration-message");
        map.put("nif","image/x-niff");
        map.put("niff","image/x-niff");
        map.put("nix","application/x-mix-transfer");
        map.put("nsc","application/x-conference");
        map.put("nvd","application/x-navidoc");
        map.put("o","application/octet-stream");
        map.put("oda","application/oda");
        map.put("omc","application/x-omc");
        map.put("omcd","application/x-omcdatamaker");
        map.put("omcr","application/x-omcregerator");
        map.put("p","text/x-pascal");
        map.put("p10","application/pkcs10");
        map.put("p10","application/x-pkcs10");
        map.put("p12","application/pkcs-12");
        map.put("p12","application/x-pkcs12");
        map.put("p7a","application/x-pkcs7-signature");
        map.put("p7c","application/pkcs7-mime");
        map.put("p7c","application/x-pkcs7-mime");
        map.put("p7m","application/pkcs7-mime");
        map.put("p7m","application/x-pkcs7-mime");
        map.put("p7r","application/x-pkcs7-certreqresp");
        map.put("p7s","application/pkcs7-signature");
        map.put("part","application/pro_eng");
        map.put("pas","text/pascal");
        map.put("pbm","image/x-portable-bitmap");
        map.put("pcl","application/vnd.hp-pcl");
        map.put("pcl","application/x-pcl");
        map.put("pct","image/x-pict");
        map.put("pcx","image/x-pcx");
        map.put("pdb","chemical/x-pdb");
        map.put("pdf","application/pdf");
        map.put("pfunk","audio/make");
        map.put("pfunk","audio/make.my.funk");
        map.put("pgm","image/x-portable-graymap");
        map.put("pgm","image/x-portable-greymap");
        map.put("pic","image/pict");
        map.put("pict","image/pict");
        map.put("pkg","application/x-newton-compatible-pkg");
        map.put("pko","application/vnd.ms-pki.pko");
        map.put("pl","text/plain");
        map.put("pl","text/x-script.perl");
        map.put("plx","application/x-pixclscript");
        map.put("pm","image/x-xpixmap");
        map.put("pm","text/x-script.perl-module");
        map.put("pm4","application/x-pagemaker");
        map.put("pm5","application/x-pagemaker");
        map.put("png","image/png");
        map.put("pnm","application/x-portable-anymap");
        map.put("pnm","image/x-portable-anymap");
        map.put("pot","application/mspowerpoint");
        map.put("pot","application/vnd.ms-powerpoint");
        map.put("pov","model/x-pov");
        map.put("ppa","application/vnd.ms-powerpoint");
        map.put("ppm","image/x-portable-pixmap");
        map.put("pps","application/mspowerpoint");
        map.put("pps","application/vnd.ms-powerpoint");
        map.put("ppt","application/mspowerpoint");
        map.put("ppt","application/powerpoint");
        map.put("ppt","application/vnd.ms-powerpoint");
        map.put("ppt","application/x-mspowerpoint");
        map.put("ppz","application/mspowerpoint");
        map.put("pre","application/x-freelance");
        map.put("prt","application/pro_eng");
        map.put("ps","application/postscript");
        map.put("psd","application/octet-stream");
        map.put("pvu","paleovu/x-pv");
        map.put("pwz","application/vnd.ms-powerpoint");
        map.put("py","text/x-script.phyton");
        map.put("pyc","application/x-bytecode.python");
        map.put("qcp","audio/vnd.qcelp");
        map.put("qd3","x-world/x-3dmf");
        map.put("qd3d","x-world/x-3dmf");
        map.put("qif","image/x-quicktime");
        map.put("qt","video/quicktime");
        map.put("qtc","video/x-qtc");
        map.put("qti","image/x-quicktime");
        map.put("qtif","image/x-quicktime");
        map.put("ra","audio/x-pn-realaudio");
        map.put("ra","audio/x-pn-realaudio-plugin");
        map.put("ra","audio/x-realaudio");
        map.put("ram","audio/x-pn-realaudio");
        map.put("ras","application/x-cmu-raster");
        map.put("ras","image/cmu-raster");
        map.put("ras","image/x-cmu-raster");
        map.put("rast","image/cmu-raster");
        map.put("rexx","text/x-script.rexx");
        map.put("rf","image/vnd.rn-realflash");
        map.put("rgb","image/x-rgb");
        map.put("rm","application/vnd.rn-realmedia");
        map.put("rm","audio/x-pn-realaudio");
        map.put("rmi","audio/mid");
        map.put("rmm","audio/x-pn-realaudio");
        map.put("rmp","audio/x-pn-realaudio");
        map.put("rmp","audio/x-pn-realaudio-plugin");
        map.put("rng","application/ringing-tones");
        map.put("rng","application/vnd.nokia.ringing-tone");
        map.put("rnx","application/vnd.rn-realplayer");
        map.put("roff","application/x-troff");
        map.put("rp","image/vnd.rn-realpix");
        map.put("rpm","audio/x-pn-realaudio-plugin");
        map.put("rt","text/richtext");
        map.put("rt","text/vnd.rn-realtext");
        map.put("rtf","application/rtf");
        map.put("rtf","application/x-rtf");
        map.put("rtf","text/richtext");
        map.put("rtx","application/rtf");
        map.put("rtx","text/richtext");
        map.put("rv","video/vnd.rn-realvideo");
        map.put("s","text/x-asm");
        map.put("s3m","audio/s3m");
        map.put("saveme","application/octet-stream");
        map.put("sbk","application/x-tbook");
        map.put("scm","application/x-lotusscreencam");
        map.put("scm","text/x-script.guile");
        map.put("scm","text/x-script.scheme");
        map.put("scm","video/x-scm");
        map.put("sdml","text/plain");
        map.put("sdp","application/sdp");
        map.put("sdp","application/x-sdp");
        map.put("sdr","application/sounder");
        map.put("sea","application/sea");
        map.put("sea","application/x-sea");
        map.put("set","application/set");
        map.put("sgm","text/sgml");
        map.put("sgm","text/x-sgml");
        map.put("sgml","text/sgml");
        map.put("sgml","text/x-sgml");
        map.put("sh","application/x-bsh");
        map.put("sh","application/x-sh");
        map.put("sh","application/x-shar");
        map.put("sh","text/x-script.sh");
        map.put("shar","application/x-bsh");
        map.put("shar","application/x-shar");
        map.put("shtml","text/html");
        map.put("shtml","text/x-server-parsed-html");
        map.put("sid","audio/x-psid");
        map.put("sit","application/x-sit");
        map.put("sit","application/x-stuffit");
        map.put("skd","application/x-koan");
        map.put("skm","application/x-koan");
        map.put("skp","application/x-koan");
        map.put("skt","application/x-koan");
        map.put("sl","application/x-seelogo");
        map.put("smi","application/smil");
        map.put("smil","application/smil");
        map.put("snd","audio/basic");
        map.put("snd","audio/x-adpcm");
        map.put("sol","application/solids");
        map.put("spc","application/x-pkcs7-certificates");
        map.put("spc","text/x-speech");
        map.put("spl","application/futuresplash");
        map.put("spr","application/x-sprite");
        map.put("sprite","application/x-sprite");
        map.put("src","application/x-wais-source");
        map.put("ssi","text/x-server-parsed-html");
        map.put("ssm","application/streamingmedia");
        map.put("sst","application/vnd.ms-pki.certstore");
        map.put("step","application/step");
        map.put("stl","application/sla");
        map.put("stl","application/vnd.ms-pki.stl");
        map.put("stl","application/x-navistyle");
        map.put("stp","application/step");
        map.put("sv4cpio","application/x-sv4cpio");
        map.put("sv4crc","application/x-sv4crc");
        map.put("svf","image/vnd.dwg");
        map.put("svf","image/x-dwg");
        map.put("svr","application/x-world");
        map.put("svr","x-world/x-svr");
        map.put("swf","application/x-shockwave-flash");
        map.put("t","application/x-troff");
        map.put("talk","text/x-speech");
        map.put("tar","application/x-tar");
        map.put("tbk","application/toolbook");
        map.put("tbk","application/x-tbook");
        map.put("tcl","application/x-tcl");
        map.put("tcl","text/x-script.tcl");
        map.put("tcsh","text/x-script.tcsh");
        map.put("tex","application/x-tex");
        map.put("texi","application/x-texinfo");
        map.put("texinfo","application/x-texinfo");
        map.put("text","application/plain");
        map.put("text","text/plain");
        map.put("tgz","application/gnutar");
        map.put("tgz","application/x-compressed");
        map.put("tif","image/tiff");
        map.put("tif","image/x-tiff");
        map.put("tiff","image/tiff");
        map.put("tiff","image/x-tiff");
        map.put("tr","application/x-troff");
        map.put("tsi","audio/tsp-audio");
        map.put("tsp","application/dsptype");
        map.put("tsp","audio/tsplayer");
        map.put("tsv","text/tab-separated-values");
        map.put("turbot","image/florian");
        map.put("txt","text/plain");
        map.put("uil","text/x-uil");
        map.put("uni","text/uri-list");
        map.put("unis","text/uri-list");
        map.put("unv","application/i-deas");
        map.put("uri","text/uri-list");
        map.put("uris","text/uri-list");
        map.put("ustar","application/x-ustar");
        map.put("ustar","multipart/x-ustar");
        map.put("uu","application/octet-stream");
        map.put("uu","text/x-uuencode");
        map.put("uue","text/x-uuencode");
        map.put("vcd","application/x-cdlink");
        map.put("vcs","text/x-vcalendar");
        map.put("vda","application/vda");
        map.put("vdo","video/vdo");
        map.put("vew","application/groupwise");
        map.put("viv","video/vivo");
        map.put("viv","video/vnd.vivo");
        map.put("vivo","video/vivo");
        map.put("vivo","video/vnd.vivo");
        map.put("vmd","application/vocaltec-media-desc");
        map.put("vmf","application/vocaltec-media-file");
        map.put("voc","audio/voc");
        map.put("voc","audio/x-voc");
        map.put("vos","video/vosaic");
        map.put("vox","audio/voxware");
        map.put("vqe","audio/x-twinvq-plugin");
        map.put("vqf","audio/x-twinvq");
        map.put("vql","audio/x-twinvq-plugin");
        map.put("vrml","application/x-vrml");
        map.put("vrml","model/vrml");
        map.put("vrml","x-world/x-vrml");
        map.put("vrt","x-world/x-vrt");
        map.put("vsd","application/x-visio");
        map.put("vst","application/x-visio");
        map.put("vsw","application/x-visio");
        map.put("w60","application/wordperfect6.0");
        map.put("w61","application/wordperfect6.1");
        map.put("w6w","application/msword");
        map.put("wav","audio/wav");
        map.put("wav","audio/x-wav");
        map.put("wb1","application/x-qpro");
        map.put("wbmp","image/vnd.wap.wbmp");
        map.put("web","application/vnd.xara");
        map.put("wiz","application/msword");
        map.put("wk1","application/x-123");
        map.put("wmf","windows/metafile");
        map.put("wml","text/vnd.wap.wml");
        map.put("wmlc","application/vnd.wap.wmlc");
        map.put("wmls","text/vnd.wap.wmlscript");
        map.put("wmlsc","application/vnd.wap.wmlscriptc");
        map.put("word","application/msword");
        map.put("wp","application/wordperfect");
        map.put("wp5","application/wordperfect");
        map.put("wp5","application/wordperfect6.0");
        map.put("wp6","application/wordperfect");
        map.put("wpd","application/wordperfect");
        map.put("wpd","application/x-wpwin");
        map.put("wq1","application/x-lotus");
        map.put("wri","application/mswrite");
        map.put("wri","application/x-wri");
        map.put("wrl","application/x-world");
        map.put("wrl","model/vrml");
        map.put("wrl","x-world/x-vrml");
        map.put("wrz","model/vrml");
        map.put("wrz","x-world/x-vrml");
        map.put("wsc","text/scriplet");
        map.put("wsrc","application/x-wais-source");
        map.put("wtk","application/x-wintalk");
        map.put("xbm","image/x-xbitmap");
        map.put("xbm","image/x-xbm");
        map.put("xbm","image/xbm");
        map.put("xdr","video/x-amt-demorun");
        map.put("xgz","xgl/drawing");
        map.put("xif","image/vnd.xiff");
        map.put("xl","application/excel");
        map.put("xla","application/excel");
        map.put("xla","application/x-excel");
        map.put("xla","application/x-msexcel");
        map.put("xlb","application/excel");
        map.put("xlb","application/vnd.ms-excel");
        map.put("xlb","application/x-excel");
        map.put("xlc","application/excel");
        map.put("xlc","application/vnd.ms-excel");
        map.put("xlc","application/x-excel");
        map.put("xld","application/excel");
        map.put("xld","application/x-excel");
        map.put("xlk","application/excel");
        map.put("xlk","application/x-excel");
        map.put("xll","application/excel");
        map.put("xll","application/vnd.ms-excel");
        map.put("xll","application/x-excel");
        map.put("xlm","application/excel");
        map.put("xlm","application/vnd.ms-excel");
        map.put("xlm","application/x-excel");
        map.put("xls","application/excel");
        map.put("xls","application/vnd.ms-excel");
        map.put("xls","application/x-excel");
        map.put("xls","application/x-msexcel");
        map.put("xlt","application/excel");
        map.put("xlt","application/x-excel");
        map.put("xlv","application/excel");
        map.put("xlv","application/x-excel");
        map.put("xlw","application/excel");
        map.put("xlw","application/vnd.ms-excel");
        map.put("xlw","application/x-excel");
        map.put("xlw","application/x-msexcel");
        map.put("xm","audio/xm");
        map.put("xml","application/xml");
        map.put("xml","text/xml");
        map.put("xmz","xgl/movie");
        map.put("xpix","application/x-vnd.ls-xpix");
        map.put("xpm","image/x-xpixmap");
        map.put("xpm","image/xpm");
        map.put("x-png","image/png");
        map.put("xsr","video/x-amt-showrun");
        map.put("xwd","image/x-xwd");
        map.put("xwd","image/x-xwindowdump");
        map.put("xyz","chemical/x-pdb");
        map.put("z","application/x-compress");
        map.put("z","application/x-compressed");
        map.put("zip","application/x-compressed");
        map.put("zip","application/x-zip-compressed");
        map.put("zip","application/zip");
        map.put("zip","multipart/x-zip");
        map.put("zoo","application/octet-stream");
        map.put("zsh","text/x-script.zsh");

        mimeTypes = map;
    }
}
