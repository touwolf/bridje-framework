package org.bridje.jfx.ace;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import netscape.javascript.JSObject;

/**
 * Manage the js editor communication.
 */
public final class AceJsGate
{
    private final AceEditor editor;

    private final JSObject js;

    @SuppressWarnings("LeakingThisInConstructor")
    public AceJsGate(AceEditor editor, JSObject js)
    {
        this.editor = editor;
        this.js = js;
        this.js.setMember("java", this);
    }

    /**
     * Called on text changed.
     * 
     * @param text The new text.
     */
    public void textChanged(String text)
    {
        editor.setTextFromJs(text);
    }

    /**
     * Called to replace the selected text.
     * 
     * @param text The selected text.
     * @return The text for wich the selected text should be replaced.
     */
    public String findReplace(String text)
    {
        if (editor.getReplaceHandler() != null) return text;
        String replace = editor.getReplaceHandler().replace(text);
        return replace;
    }

    /**
     * Copy the given text to the clipboard.
     * 
     * @param text The text to copy.
     */
    public void copyToClipboard(String text)
    {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(content);
    }

    /**
     * Gets the content of the clipboard.
     * 
     * @return The content of the clipboard.
     */
    public String getClipboardContent()
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return String.valueOf(clipboard.getContent(DataFormat.PLAIN_TEXT));
    }

    /**
     * Executes a js method.
     * 
     * @param method The method to executed.
     * @param args The arguments of the method.
     * @return The result of the method.
     */
    public Object exec(String method, Object... args)
    {
        if (js != null) return js.call(method, args);
        return null;
    }
}
