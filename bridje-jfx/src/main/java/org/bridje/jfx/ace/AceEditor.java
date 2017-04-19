package org.bridje.jfx.ace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

/**
 * Control to edit code using <a href="https://ace.c9.io">Ace Editor</a> version 1.2.6.
 * Example usage:
 * <pre><code>
 *     //edit FTL code
 *     AceEditor editor = new AceEditor(AceEditor.Mode.FTL);
 *
 *     //fill content on ready
 *     editor.onReady(() -> editor.setText("&lt;#ftl encoding='UTF-8'&gt;\n"));
 *
 *     //handle replacement of editor selected text
 *     editor.onReplace(text ->
 *     {
 *          TextInputDialog dialog = new TextInputDialog("");
 *          dialog.setTitle("Replace...");
 *          dialog.setHeaderText("Please enter the replacement text.");
 *          dialog.setContentText("Replacement text:");
 *
 *          Optional&lt;String&gt; result = dialog.showAndWait();
 *          String replaceValue = "";
 *          if (result.isPresent())
 *          {
 *              replaceValue = result.get();
 *          }
 *          return replaceValue;
 *      });
 *
 *      //listen for text changes
 *      editor.textProperty()
 *            .addListener((observable, oldValue, newValue) -> System.out.println(newValue));
 * </code></pre>
 */
public final class AceEditor extends VBox
{
    private final ObjectProperty<ContextMenu> contextMenuProperty = new SimpleObjectProperty<>();

    /**
     * Text content of the editor.
     */
    private final SimpleStringProperty textProperty = new SimpleStringProperty("");

    private JsGate gate;

    private final ChangeListener<String> listener;

    private ReplaceHandler replaceHandler;

    /**
     * Initialize control.
     *
     * @param mode indicates the language to edit. (FTL, JAVA, etc).
     * @see <a href="https://github.com/ajaxorg/ace/tree/master/lib/ace/mode">Ace Modes</a>
     */
    public AceEditor(Mode mode)
    {
        WebView editor = new WebView();
        loadContent(editor, mode);
        getChildren().add(editor);
        contextMenuProperty.addListener((observable, oldValue, newValue) ->
        {
            editor.setContextMenuEnabled(false);
            editor.setOnContextMenuRequested(e -> newValue.show(editor, e.getScreenX(), e.getScreenY()));
        });
        setContextMenu(createDefaultContextMenu());

        listener = (observable, oldValue, newValue) -> updateEditorContent(newValue);
        textProperty().addListener(listener);
    }

    /**
     * Called when editor has loaded.
     *
     * @param listener ready event listener.
     */
    public final void onReady(ReadyListener listener)
    {
        readyListener = listener;
    }

    /**
     * Obtains selected text on editor.
     *
     * @return selected text.
     */
    public String findSelection()
    {
        if (gate != null)
        {
            Object raw = gate.exec("editorSelected");
            if (raw != null)
            {
                return raw.toString();
            }
        }
        return "";
    }

    /**
     * Replaces selected text on editor.
     *
     * @param text replacement for selected text.
     */
    public void replaceSelection(String text)
    {
        if (gate != null)
        {
            gate.exec("editorReplaceSelected", text);
        }
    }

    /**
     * Search and replace text on editor.
     *
     * @param oldValue text to search.
     * @param newValue new text.
     */
    public void searchAndReplace(String oldValue, String newValue)
    {
        if (gate != null)
        {
            gate.exec("editorSearchAndReplace", oldValue, newValue);
        }
    }
    
    public ObjectProperty<ContextMenu> contextMenuProperty()
    {
        return contextMenuProperty;
    }

    public ContextMenu getContextMenu()
    {
        return contextMenuProperty.get();
    }

    public void setContextMenu(ContextMenu value)
    {
        contextMenuProperty.set(value);
    }

    public void onReplace(ReplaceHandler handler)
    {
        replaceHandler = handler;
    }

    public String getText()
    {
        return textProperty.get();
    }

    public SimpleStringProperty textProperty()
    {
        return textProperty;
    }

    public void setText(String text)
    {
        this.textProperty.set(text);
    }

    private ContextMenu createDefaultContextMenu()
    {
        MenuItem cut = new MenuItem("Cut");//todo: i18n
        cut.setOnAction(event ->
        {
            if (gate != null)
            {
                gate.exec("editorCut");
            }
        });

        MenuItem copy = new MenuItem("Copy");//todo: i18n
        copy.setOnAction(event ->
        {
            if (gate != null)
            {
                gate.exec("editorCopy");
            }
        });

        MenuItem paste = new MenuItem("Paste");//todo: i18n
        paste.setOnAction(event ->
        {
            if (gate != null)
            {
                gate.exec("editorPaste");
            }
        });

        MenuItem replace = new MenuItem("Replace...");//todo: i18n
        replace.setOnAction(event ->
        {
            if (gate != null)
            {
                gate.exec("editorReplace");
            }
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(cut, copy, paste, replace);
        return contextMenu;
    }

    private void updateEditorContent(String text)
    {
        String realText = text;
        if(realText == null) realText = "";
        gate.exec("editorContent", realText);
    }
    
    void setTextFromJs(String text)
    {
        textProperty().removeListener(listener);
        setText(text);
        textProperty().addListener(listener);
    }

    private ReadyListener readyListener;

    private void loadContent(WebView editor, Mode mode)
    {
        WebEngine engine = editor.getEngine();
        if (mode == null)
        {
            mode = Mode.JAVA;
        }
        String strMode = "var mode = 'VALUE';\n{{mode-VALUE.js}}"
                .replaceAll("VALUE", mode.name().toLowerCase());
        engine.loadContent(readContent("ace.html", true, strMode));
        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) ->
        {
            switch (newState)
            {
                case SUCCEEDED:
                    JSObject js = (JSObject) engine.executeScript("window");
                    gate = new JsGate(AceEditor.this, js);
                    if (readyListener != null)
                    {
                        readyListener.ready();
                    }
                    break;
            }
        });
    }

    private static String readContent(String resource, boolean replace, String mode)
    {
        InputStream stream = AceEditor.class.getResourceAsStream(resource);
        if (stream != null)
        {
            BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            try
            {
                String line = buf.readLine();
                while (line != null)
                {
                    if (mode != null)
                    {
                        line = line.replace("[[MODE]]", mode);
                    }
                    if (replace)
                    {
                        line = doReplace(line);
                    }
                    builder.append(line).append("\n");
                    line = buf.readLine();
                }
            }
            catch (IOException ignored)
            {
            }
            return builder.toString();
        }
        return "";
    }

    private static String doReplace(String line)
    {
        int startIndex = line.indexOf("{{");
        if (startIndex < 0)
        {
            return line;
        }
        int endIndex = line.indexOf("}}", startIndex);
        if (endIndex < 0)
        {
            return line;
        }
        String name = line.substring(startIndex + 2, endIndex);
        String replacement = readContent(name, false, null);
        endIndex += 3;
        if (endIndex > line.length())
        {
            endIndex = line.length();
        }
        return line.substring(0, startIndex) + replacement + line.substring(endIndex);
    }

    ReplaceHandler getReplaceHandler()
    {
        return replaceHandler;
    }

    /**
     * Supported modes.
     */
    public enum Mode
    {
        JAVA, JAVASCRIPT, FTL
    }

    /**
     * Listener for ready event of editor.
     */
    public interface ReadyListener
    {
        /**
         * Called when editor has load and it's ready.
         */
        void ready();
    }

    /**
     * Handler for replace operation on editor.
     */
    public interface ReplaceHandler
    {
        /**
         * Called when replace action was called on editor.
         *
         * @param text the selected text in editor to replace
         * @return the value that was gathered to replace.
         */
        String replace(String text);
    }
}
