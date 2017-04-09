package org.bridje.jfx.ace;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import netscape.javascript.JSObject;

/**
 * Manage the js editor communication.
 */
public class JsGate
{
    private final AceEditor editor;

    private final JSObject js;

    JsGate(AceEditor editor, JSObject js)
    {
        this.editor = editor;
        js.setMember("java", this);
        this.js = js;
    }

    public void textChanged(String text)
    {
        editor.setText(text, false);
    }

    public String findReplace(String text)
    {
        String replace = text;
        if (editor.getReplaceHandler() != null)
        {
            replace = editor.getReplaceHandler().replace(text);
        }
        return replace;
    }

    public void copyToClipboard(String text)
    {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(content);
    }

    public String getClipboardContent()
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return String.valueOf(clipboard.getContent(DataFormat.PLAIN_TEXT));
    }

    void exec(String method, Object... args)
    {
        if (js != null)
        {
            js.call(method, args);
        }
    }
}
