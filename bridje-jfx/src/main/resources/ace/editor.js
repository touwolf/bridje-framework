var editor = ace.edit('editor');
editor.getSession().setTabSize(4);
editor.setHighlightActiveLine(true);
editor.setShowInvisibles(true);
editor.setDisplayIndentGuides(true);
editor.getSession().setMode('ace/mode/' + mode);

var editorCut = function()
{
    var range = editor.getSelectionRange();
    var selected = editor.getSession().getTextRange(range);
    if (selected)
    {
        java.copyToClipboard(selected);
        editor.getSession().replace(range, '');
    }
};

var editorCopy = function()
{
    var range = editor.getSelectionRange();
    var selected = editor.getSession().getTextRange(range);
    if (selected)
    {
        java.copyToClipboard(selected);
    }
};

var editorPaste = function()
{
    var range = editor.getSelectionRange();
    var replace = java.getClipboardContent();
    editor.getSession().replace(range, replace);
};

var editorReplace = function()
{
    doReplace(editor);
};

var editorContent = function(text)
{
    editor.setValue(text);
    editor.gotoLine(1);
};

var doReplace = function(editor)
{
    var range = editor.getSelectionRange();
    var selected = editor.getSession().getTextRange(range);
    if (selected)
    {
        var replace = java.findReplace(selected);
        editor.getSession().replace(range, replace);
    }
};

editor.getSession().on('change', function(e)
{
    java.textChanged(editor.getValue());
});

editor.commands.addCommand({
    name: 'replace',
    bindKey: {
        win: 'Ctrl-H',
        mac: 'Command-H'
    },
    exec: doReplace
});
