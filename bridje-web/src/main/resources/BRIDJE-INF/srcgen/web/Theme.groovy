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

def loadFieldChildren = { field, fieldNode ->
    def result = [:];
    fieldNode.'child'.each{ node ->
        result[node.'@name'.text()] = node.'@type'.text();
    };
    field['children'] = result;
};

def loadFieldTypes = { field, fieldNode ->
    switch(fieldNode.name())
    {
        case "attr":
            field['fieldType'] = "attribute";
            field['javaType'] = field['resultType'];
            break;
        case "inAttr":
            field['fieldType'] = "attribute";
            field['javaType'] = "UIInputExpression";
            break;
        case "outAttr":
            field['fieldType'] = "attribute";
            field['javaType'] = "UIExpression";
            break;
        case "eventAttr":
            field['fieldType'] = "attribute";
            field['javaType'] = "UIEvent";
            break;
        case "el":
            field['fieldType'] = "element";
            field['javaType'] = field['resultType'];
            break;
        case "inEl":
            field['fieldType'] = "element";
            field['javaType'] = "UIInputExpression";
            break;
        case "outEl":
            field['fieldType'] = "element";
            field['javaType'] = "UIExpression";
            break;
        case "eventEl":
            field['fieldType'] = "element";
            field['javaType'] = "UIEvent";
            break;
        case "value":
            field['fieldType'] = "value";
            field['javaType'] = field['resultType'];
            break;
        case "inValue":
            field['fieldType'] = "value";
            field['javaType'] = "UIInputExpression";
            break;
        case "outValue":
            field['fieldType'] = "value";
            field['javaType'] = "UIExpression";
            break;
        case "eventValue":
            field['fieldType'] = "value";
            field['javaType'] = "UIEvent";
            break;
        case "children":
            field['fieldType'] = "children";
            field['javaType'] = "List<Widget>";
            field['isSingle'] = fieldNode.'@single'.text() == "true";
            field['allowPlaceHolder'] = fieldNode.'@allowPlaceHolder'.text() == "true";
            if(field['isSingle'])
            {
                field['javaType'] = "Widget";
            }
            field['children'] = [:];
            field['wrapper'] = fieldNode.'@wrapper'.text();
            loadFieldChildren(field, fieldNode);
            break;
        case "child":
            field['fieldType'] = "child";
            field['javaType'] = field['resultType'];
            field['allowPlaceHolder'] = fieldNode.'@allowPlaceHolder'.text() == "true";
            break;
    }
}

def removeSlash = { str ->
    if(str != null)
    {
        if(str.startsWith("/"))
        {
            return str.substring(1);
        }
    }
    return str;
};

def findBaseWidget = { theme, widget ->
    if(widget['baseTemplate'] != null && widget['baseTemplate'] != '')
    {
        return theme['widgetsTemplates'][widget['baseTemplate']];
    }
    return null;
};

def findValue = { first, second ->
    if(first == null || first == "")
    {
        return second;
    }
    return first;
};

def applyBaseTemplateWidget;
applyBaseTemplateWidget = { theme, widget ->

    def baseWidget = findBaseWidget(theme, widget);
    if(baseWidget != null)
    {
        applyBaseTemplateWidget(theme, baseWidget);
        widget['package'] = findValue(widget['package'], baseWidget['package']);
        widget['base'] = findValue(widget['base'], baseWidget['base']);
        widget['render'] = findValue(widget['render'], baseWidget['render']);
        if(widget['base'] == "")
        {
            widget['base'] = "Widget";
        }

        widget['hasInputs'] = widget['hasInputs'] || baseWidget['hasInputs'];
        widget['hasChildren'] = widget['hasChildren'] || baseWidget['hasChildren'];
        widget['hasEvents'] = widget['hasEvents'] || baseWidget['hasEvents'];
        widget['hasResources'] = widget['hasResources'] || baseWidget['hasResources'];

        baseWidget['fields'].each{ field ->
            widget['fields'] << field;
            widget['fieldsMap'][field['name']] = field;
        };

        baseWidget['resources'].each{ resNode ->
            widget['resources'] << resNode;
        };

        widget;
    }
};

def readWidget = { theme, widgetNode ->

    def widget = [:];
    widget['package'] = theme['package'];
    widget['name'] = widgetNode.'@name'.text();
    widget['fullName'] = widget['package'] + "." + widget['name'];
    widget['baseTemplate'] = widgetNode.'@baseTemplate'.text();
    widget['base'] = widgetNode.'@base'.text();
    widget['render'] = widgetNode.'render'.text();
    widget['rootElement'] = widgetNode.'@rootElement'.text();
    if(widget['base'] == "")
    {
        widget['base'] = "Widget";
    }

    widget['hasInputs'] = false;
    widget['hasChildren'] = false;
    widget['hasEvents'] = false;
    widget['hasResources'] = false;

    widget['fields'] = [];
    widget['fieldsMap'] = [:];
    widgetNode.'fields'.'*'.each{ fieldNode ->
        def field = [:];
        field['name'] = fieldNode.'@name'.text();
        field['resultType'] = fieldNode.'@type'.text();
        field['defaultValue'] = fieldNode.'@def'.text();
        if(field['defaultValue'] == "")
        {
            field['defaultValue'] = "null";
        }
        loadFieldTypes(field, fieldNode);
        if(field['javaType'] == 'UIInputExpression')
            widget['hasInputs'] = true;
        if(field['fieldType'] == 'children' || field['fieldType'] == 'child')
            widget['hasChildren'] = true;
        if(field['javaType'] == 'UIEvent')
            widget['hasEvents'] = true;

        widget['fields'] << field;
        widget['fieldsMap'][field['name']] = field;
    };

    widget['resources'] = [];
    widgetNode.'resources'.'*'.each{ resNode ->
        widget['resources'] << resNode.'@name'.text();
        widget['hasResources'] = true;
    };

    widget;
};

def readResource = { theme, resNode ->
    def resource = [:];

    resource['scripts'] = [];
    resource['styles'] = [];
    resource['fonts'] = [];
    resource['name'] = resNode.'@name'.text();
    resNode.'*'.each{ rNode ->
        if(rNode.name() == "script")
        {
            def script = [:];
            script['href'] = removeSlash(rNode.'@href'.text());
            resource['scripts'] << script;
        }
        else if(rNode.name() == "style")
        {
            def style = [:];
            style['href'] = removeSlash(rNode.'@href'.text());
            resource['styles'] << style;
        }
        else if(rNode.name() == "font")
        {
            def font = [:];
            font['href'] = removeSlash(rNode.'@href'.text());
            resource['fonts'] << font;
        }
    };
    resource;
};

def readDefResources = { theme, rNode ->
    if(rNode.name() == "script")
    {
        def script = [:];
        script['href'] = removeSlash(rNode.'@href'.text());
        theme['defaultResources']['scripts'] << script;
    }
    else if(rNode.name() == "style")
    {
        def style = [:];
        style['href'] = removeSlash(rNode.'@href'.text());
        theme['defaultResources']['styles'] << style;
    }
    else if(rNode.name() == "link")
    {
        def link = [:];
        link['rel'] = rNode.'@rel'.text();
        link['href'] = removeSlash(rNode.'@href'.text());
        theme['defaultResources']['links'] << link;
    }
    else if(rNode.name() == "font")
    {
        def font = [:];
        font['href'] = removeSlash(rNode.'@href'.text());
        theme['defaultResources']['fonts'] << font;
    }
};

def generateWidgetsAndTheme = { themeData ->
    def theme = [:];
    theme['package'] = themeData.'@package'.text();
    theme['renderBody'] = themeData.'renderBody'.text();
    theme['renderHead'] = themeData.'renderHead'.text();
    theme['renderViewContainer'] = themeData.'renderViewContainer'.text();
    theme['name'] = themeData.'@name'.text();
    theme['namespace'] = themeData.'@namespace'.text();
    theme['widgets'] = [];
    theme['widgetsTemplates'] = [:];
    theme['macros'] = [];
    theme['defaultResources'] = [:];
    theme['resources'] = [];
    theme['resourcesMap'] = [:];

    themeData.'resources'.'*'.each{ resNode ->
        def resource = readResource(theme, resNode);

        theme['resources'] << resource;
        theme['resourcesMap'][resource['name']] = resource;
    };

    theme['defaultResources']['scripts'] = [];
    theme['defaultResources']['styles'] = [];
    theme['defaultResources']['links'] = [];
    theme['defaultResources']['fonts'] = [];

    themeData.'defaultResources'.'*'.each{ resNode ->
        readDefResources(theme, resNode);
    };

    themeData.'macros'.'*'.each{ macroNode ->
        def macro = [:];

        macro['name'] = macroNode.'@name'.text();
        macro['parameters'] = macroNode.'@parameters'.text();
        macro['content'] = macroNode.'content'.text();

        theme['macros'] << macro;
    };

    themeData.'widgetsTemplates'.'*'.each{ widgetNode ->
        def widget = readWidget(theme, widgetNode);

        theme['widgetsTemplates'][widget['name']] = widget;
    };

    themeData.'widgets'.'*'.each{ widgetNode ->
        def widget = readWidget(theme, widgetNode);

        applyBaseTemplateWidget(theme, widget);

        theme['widgets'] << widget;
        def data = ['widget':widget];
        data['theme'] = theme;
        tools.generateClass(widget['fullName'], "web/Widget.ftl", data);
    };
    def themeFile = "BRIDJE-INF/web/themes/" + theme['name'] + "/Theme.ftl";
    def data = ['theme':theme];
    tools.generateResource(themeFile, "web/Theme.ftl", data);
    tools.generateClass(theme['package'] + ".package-info", "web/package-info.ftl", data);
};

def generateAll = { ->
    def themes = tools.loadXmlFiles("web", "*.xml");
    themes.each{ themeData -> generateWidgetsAndTheme(themeData); };
};

generateAll();