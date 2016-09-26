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
            field['children'] = [:];
            field['wrapper'] = fieldNode.'@wrapper'.text();
            loadFieldChildren(field, fieldNode);
            break;
        case "child":
            field['fieldType'] = "child";
            field['javaType'] = field['resultType'];
            break;
    }
}

def generateWidgetsAndTheme = { ->
    def ormData = tools.loadXmlFile("web.xml");
    def theme = [:];
    theme['package'] = ormData.'@package'.text();
    theme['name'] = ormData.'@name'.text();
    theme['namespace'] = ormData.'@namespace'.text();
    theme['widgets'] = [];
    theme['resources'] = [];
    theme['resourcesMap'] = [:];

    ormData.'resources'.'*'.each{ resNode ->
        def resource = [:];

        resource['scripts'] = [];
        resource['styles'] = [];
        resource['fonts'] = [];
        resource['name'] = resNode.'@name'.text();
        resNode.'*'.each{ rNode ->
            if(rNode.name() == "script")
            {
                def script = [:];
                script['href'] = rNode.'@href'.text();
                resource['scripts'] << script;
            }
            else if(rNode.name() == "style")
            {
                def style = [:];
                style['href'] = rNode.'@href'.text();
                resource['styles'] << style;
            }
            else if(rNode.name() == "font")
            {
                def font = [:];
                font['href'] = rNode.'@href'.text();
                resource['fonts'] << font;
            }
        };
        
        theme['resources'] << resource;
        theme['resourcesMap'][resource['name']] = resource;
    };
    
    ormData.'widgets'.'*'.each{ widgetNode ->

        def widget = [:];
        widget['package'] = theme['package'];
        widget['name'] = widgetNode.'@name'.text();
        widget['fullName'] = widget['package'] + "." + widget['name'];
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

        theme['widgets'] << widget;
        def data = ['widget':widget];
        tools.generateClass(widget['fullName'], "web/Widget.ftl", data);
    };
    def themeFile = "BRIDJE-INF/web/themes/" + theme['name'] + "/Theme.ftl";
    def data = ['theme':theme];
    tools.generateResource(themeFile, "web/Theme.ftl", data);
    tools.generateClass(theme['package'] + ".package-info", "web/package-info.ftl", data);
};

generateWidgetsAndTheme();