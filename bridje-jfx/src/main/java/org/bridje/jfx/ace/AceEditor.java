package org.bridje.jfx.ace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *     editor.onReady(() -{@literal &gt;} editor.setText("{@literal <}#ftl encoding='UTF-8'{@literal >}\n"));
 *
 *     //handle replacement of editor selected text
 *     editor.onReplace(text -{@literal &gt;}
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
 *            .addListener((observable, oldValue, newValue) -{@literal &gt;} System.out.println(newValue));
 * </code></pre>
 */
public final class AceEditor extends VBox
{
    private static final Logger LOG = Logger.getLogger(AceEditor.class.getName());

    private final ObjectProperty<ContextMenu> contextMenuProperty = new SimpleObjectProperty<>();

    private final SimpleStringProperty textProperty = new SimpleStringProperty("");

    private AceJsGate gate;
    
    private AceReadyListener readyListener;

    private final ChangeListener<String> listener;

    private AceReplaceHandler replaceHandler;

    /**
     * Initialize control.
     *
     * @param mode indicates the language to edit. (FTL, JAVA, etc).
     * @see <a href="https://github.com/ajaxorg/ace/tree/master/lib/ace/mode">Ace Modes</a>
     */
    public AceEditor(AceMode mode)
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
    public final void onReady(AceReadyListener listener)
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
    
    /**
     * The property for the context menu of this component.
     * 
     * @return The property for the context menu of this component.
     */
    public ObjectProperty<ContextMenu> contextMenuProperty()
    {
        return contextMenuProperty;
    }

    /**
     * The context menu of this component.
     * 
     * @return The context menu of this component.
     */
    public ContextMenu getContextMenu()
    {
        return contextMenuProperty.get();
    }

    /**
     * The context menu of this component.
     * 
     * @param value The context menu of this component.
     */
    public void setContextMenu(ContextMenu value)
    {
        contextMenuProperty.set(value);
    }

    /**
     * Called after a replace has happend.
     * 
     * @param handler The replace handler.
     */
    public void onReplace(AceReplaceHandler handler)
    {
        replaceHandler = handler;
    }

    /**
     * The property for the content of the editor.
     * 
     * @return The property for the content of the editor.
     */
    public SimpleStringProperty textProperty()
    {
        return textProperty;
    }

    /**
     * The content of the editor.
     * 
     * @return The content of the editor.
     */
    public String getText()
    {
        return textProperty.get();
    }

    /**
     * The content of the editor.
     * 
     * @param text The content of the editor.
     */
    public void setText(String text)
    {
        this.textProperty.set(text);
    }

    private ContextMenu createDefaultContextMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(
                gateExecMenuItem("Cut", "editorCut"), 
                gateExecMenuItem("Copy", "editorCopy"), 
                gateExecMenuItem("Paste", "editorPaste"), 
                gateExecMenuItem("Replace...", "editorReplace"));
        return contextMenu;
    }

    private MenuItem gateExecMenuItem(String title, String command)
    {
        MenuItem mi = new MenuItem(title);
        mi.setOnAction(event -> gateExec(command));
        return mi;
    }
    
    private void gateExec(String command)
    {
        if (gate != null) gate.exec(command);
    }

    private void updateEditorContent(String text)
    {
        String realText = text;
        if(realText == null) realText = "";
        gate.exec("editorContent", realText);
    }
    
    protected void setTextFromJs(String text)
    {
        textProperty().removeListener(listener);
        setText(text);
        textProperty().addListener(listener);
    }

    private void loadContent(WebView editor, AceMode aceMode)
    {
        AceMode mode = aceMode;
        WebEngine engine = editor.getEngine();
        if (mode == null) mode = AceMode.JAVA;
        String strMode = "var mode = 'VALUE';\n{{mode-VALUE.js}}"
                .replaceAll("VALUE", mode.name().toLowerCase());
        engine.loadContent(readContent("ace.html", true, strMode));
        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) ->
        {
            switch (newState)
            {
                case SUCCEEDED:
                    JSObject js = (JSObject) engine.executeScript("window");
                    gate = new AceJsGate(AceEditor.this, js);
                    if (readyListener != null) readyListener.ready();
                    break;
                default:
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
                    if (mode != null) line = line.replace("[[MODE]]", mode);
                    if (replace) line = doReplace(line);
                    builder.append(line).append("\n");
                    line = buf.readLine();
                }
            }
            catch (IOException ignored)
            {
                LOG.log(Level.SEVERE, ignored.getMessage(), ignored);
            }
            return builder.toString();
        }
        return "";
    }

    private static String doReplace(String line)
    {
        int startIndex = line.indexOf("{{");
        if (startIndex < 0) return line;
        int endIndex = line.indexOf("}}", startIndex);
        if (endIndex < 0) return line;
        String name = line.substring(startIndex + 2, endIndex);
        String replacement = readContent(name, false, null);
        endIndex += 3;
        if (endIndex > line.length()) endIndex = line.length();
        return line.substring(0, startIndex) + replacement + line.substring(endIndex);
    }

    /**
     * Return the replace handler.
     * 
     * @return The replace handler.
     */
    public AceReplaceHandler getReplaceHandler()
    {
        return replaceHandler;
    }
}
