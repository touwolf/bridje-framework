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

def toSqlName = { name ->
    def result = name;
    result = result.replace("A", "_a");
    result = result.replace("B", "_b");
    result = result.replace("C", "_c");
    result = result.replace("D", "_d");
    result = result.replace("E", "_e");
    result = result.replace("F", "_f");
    result = result.replace("G", "_g");
    result = result.replace("H", "_h");
    result = result.replace("I", "_i");
    result = result.replace("J", "_j");
    result = result.replace("K", "_k");
    result = result.replace("L", "_l");
    result = result.replace("M", "_m");
    result = result.replace("N", "_n");
    result = result.replace("O", "_o");
    result = result.replace("P", "_p");
    result = result.replace("Q", "_q");
    result = result.replace("R", "_r");
    result = result.replace("S", "_s");
    result = result.replace("T", "_t");
    result = result.replace("U", "_u");
    result = result.replace("V", "_v");
    result = result.replace("W", "_w");
    result = result.replace("X", "_x");
    result = result.replace("Y", "_y");
    result = result.replace("Z", "_z");
    if(result.startsWith("_"))
    {
        result = result.substring(1);
    }
    result;
};

def loadCustomTypes = { ->
    def dtList = tools.loadXmlResources("BRIDJE-INF/orm/datatypes.xml");
    def dataTypes = [:];
    dtList.each{ dtData ->
        dtData.'*'.each{ dt ->
            def dataType = [:];
            dataType['name'] = dt.name();
            dataType['class'] = dt.'@class'.text();
            dataType['adapter'] = dt.'@adapter'.text();
            dataType['sqlType'] = dt.'@sqlType'.text();
            dataType['columnType'] = dt.'@columnType'.text();
            dataTypes[dataType['name']] = dataType;
        };
    };
    dataTypes;
};

def customTypes = loadCustomTypes();
def numberTypes = ['integer', 'byte', 'short', 'long', 'float', 'double'];
def stringTypes = ['string'];

def findJavaType = { field ->
    def result = field['fieldType'].capitalize();
    if(field["isRelation"])
    {
        result = field['with'];
    }
    else if(field['isEnum'])
    {
        result = field['type'];
    }
    else if(field['isCustom'])
    {
        result = field['customType']['class'];
    }
    result;
};

def findTableColumn = { field ->
    def result = "TableColumn";
    if(field["isRelation"])
    {
        result =  "TableRelationColumn";
    }
    else if(field['isNumber'])
    {
        result =  "TableNumberColumn";
    }
    else if(field['isString'])
    {
        result =  "TableStringColumn";
    }
    else if(field['isCustom'])
    {
        if(field['customType']['columnType'] != null)
        {
            result =  field['customType']['columnType'];
        }
    }
    result;
};

def findTableName = { entity, model ->
    if(entity['table'] == '')
    {
        entity['table'] = toSqlName(entity['name'])
    }
    model['tablePrefix'] + "_" + entity['table'];
};

def findColumnName = { field ->
    if(field['column'] == '')
    {
        field['column'] = toSqlName(field['name'])
    }
    field['column'];
};

def findEntityDescription = { entity, model ->
    if(entity['description'] == '')
    {
        entity['description'] = model['defEntityDesc'];
    }
    entity['description'];
};

def findFieldDescription = { field, model ->
    if(field['description'] == '')
    {
        field['description'] = model['defFieldDesc'];
    }
    field['description'];
};

def readFieldData = { entity, fieldNode, model ->
    def field = [:];
    field['name'] = fieldNode.'@name'.text();
    field['column'] = fieldNode.'@column'.text();
    field['column'] = findColumnName(field);
    field['fieldType'] = fieldNode.name();
    field['sqlType'] = fieldNode.'@sqlType'.text();
    field['adapter'] = fieldNode.'@adapter'.text();
    field['length'] = fieldNode.'@length'.text();
    field['isIndexed'] = fieldNode.'@indexed'.text() == "true";
    field['isKey'] = fieldNode.'@key'.text() == "true" ;
    field["isRelation"] = field['fieldType'] == 'relation';
    if(field["isRelation"])
        field['with'] = fieldNode.'@with'.text();
    field['isEnum'] = field['fieldType'] == 'enum';
    if(field["isEnum"])
        field['type'] = fieldNode.'@type'.text();
    field['isNumber'] = numberTypes.contains(field['fieldType']);
    field['isAutoIncrement'] = fieldNode.'@autoIncrement'.text() == "true";
    if(field['isAutoIncrement'])
        field['isKey'] = true;
    if(field['isKey'])
        entity['keyField'] = field;
    field['isString'] = stringTypes.contains(field['fieldType']);
    field['isCustom'] = customTypes[field['fieldType']] != null;
    if(field["isCustom"])
    {
        field['customType'] = customTypes[field['fieldType']];
        if(field['adapter'] == "")
            field['adapter'] = field['customType']['adapter'];
        if(field['sqlType'] == "")
            field['sqlType'] = field['customType']['sqlType'];
    }
    field['javaType'] = findJavaType(field);
    field['tableColumn'] = findTableColumn(field);
    field['description'] = fieldNode.'@description'.text();
    field['description'] = findFieldDescription(entity, model);
    field;
};

def readEntityFields = { entityNode, entity, model ->
    entity['fields'] = [];
    entity['fieldsMap'] = [:];
    entityNode.'fields'.'*'.each{ fieldNode ->
        def field = readFieldData( entity, fieldNode, model );

        entity['fields'] << field;
        entity['fieldsMap'][field['name']] = field;
    };
    entity;
};

def readEntityData = { entityNode, model ->
    def entity = [:];

    entity['package'] = model['package'];
    entity['name'] = entityNode.'@name'.text();
    entity['table'] = entityNode.'@table'.text();
    entity['table'] = findTableName(entity, model);
    entity['base'] = entityNode.'@base'.text();
    entity['fullName'] = entity['package'] + "." + entity['name'];
    entity['description'] = entityNode.'@description'.text();
    entity['description'] = findEntityDescription(entity, model);
    entity;
};

def applyBaseTemplate;
applyBaseTemplate = { entity, templates ->

    if(entity['base'] != "")
    {
        def tmpl = templates[entity['base']];
        if(tmpl != null)
        {
            applyBaseTemplate(tmpl, templates);

            if(entity['description'] == null || entity['description'].trim() == "")
            {
                entity['description'] = tmpl['description'];
            }
            def newFields = [];
            tmpl['fields'].each{ field ->
                if(entity['fieldsMap'][field['name']] == null)
                {
                    newFields << field;
                    entity['fieldsMap'][field['name']] = field;
                }
            };
            entity['fields'].each{ field -> 
                newFields << field;
            };
            entity['fields'] = newFields;
            if(entity['keyField'] == null && tmpl['keyField'] != null)
            {
                entity['keyField'] = tmpl['keyField'];
            }
        }
    }
};

def generateEntitys = { ->
    if(tools.fileExists("orm.xml"))
    {
        def ormData = tools.loadXmlFile("orm.xml");
        def model = [:];
        model['package'] = ormData.'@package'.text();
        model['defEntityDesc'] = ormData.'@defEntityDesc'.text();
        model['tablePrefix'] = ormData.'@tablePrefix'.text();
        def templates = [:];

        ormData.'templates'.'*'.each{ entityNode ->

            def entity = readEntityData(entityNode, model);
            entity = readEntityFields(entityNode, entity, model);

            templates[entity['name']] = entity;
        };

        ormData.'entities'.'*'.each{ entityNode ->

            def entity = readEntityData(entityNode, model);
            entity = readEntityFields(entityNode, entity, model);
            applyBaseTemplate(entity, templates);

            def data = ['entity':entity];
            tools.generateClass(entity['fullName'], "orm/Entity.ftl", data);
        };
    }
};

generateEntitys();