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

package org.bridje.orm.impl.annotations;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 */
class ClassWriter
{
    private final Writer writer;
    
    private int currentIdent = 0;
    
    private boolean extendsPrinted = false;
    
    private boolean implementsPrinted = false;
    
    private boolean classDec = true;

    private boolean methodDec = false;

    private boolean firstParam = false;
    
    private boolean accessWrited = false;

    public ClassWriter(Writer writer)
    {
        this.writer = writer;
    }

    public ClassWriter annotate(String annotation) throws IOException
    {
        writeIdentation();
        writer.append("@");
        writer.append(annotation);
        return this;
    }

    public ClassWriter emptyLine() throws IOException
    {
        writer.append('\n');
        return this;
    }

    public ClassWriter classPackage(String packageName) throws IOException
    {
        writer.append("package ");
        writer.append(packageName);
        closeStatement();
        emptyLine();
        return this;
    }

    public ClassWriter importClass(String toImport) throws IOException
    {
        writer.append("import ");
        writer.append(toImport);
        closeStatement();
        return this;
    }

    public ClassWriter classDec(String className) throws IOException
    {
        if(!accessWrited)
        {
            emptyLine();
        }
        writer.append("class ");
        writer.append(className);
        accessWrited = false;
        return this;
    }
    
    public ClassWriter extendsFrom(String fromClass) throws IOException
    {
        if(!extendsPrinted)
        {
            writer.append(" extends ");
            writer.append(fromClass);
            extendsPrinted = true;
        }
        else
        {
            writer.append(", ");
            writer.append(fromClass);
        }
        return this;
    }
    
    public ClassWriter implementsInterf(String fromInterface) throws IOException
    {
        if(!implementsPrinted)
        {
            writer.append(" implements ");
            writer.append(fromInterface);
            implementsPrinted = true;
        }
        else
        {
            writer.append(", ");
            writer.append(fromInterface);
        }
        return this;
    }
    
    public ClassWriter begin() throws IOException
    {
        if(methodDec)
        {
            writer.append(")\n");
            methodDec = false;
        }
        if(classDec)
        {
            writer.append('\n');
            classDec = false;
        }
        writeIdentation();
        writer.append("{\n");
        currentIdent += 4;
        return this;
    }

    public ClassWriter end() throws IOException
    {
        if(currentIdent > 0)
        {
            currentIdent -= 4;
        }
        writeIdentation();
        writer.append("}\n");
        if(currentIdent > 0)
        {
            emptyLine();
        }
        return this;
    }

    public ClassWriter publicAccess() throws IOException
    {
        if(classDec)
        {
            emptyLine();
        }
        else
        {
            writeIdentation();
        }
        accessWrited = true;
        writer.append("public ");
        return this;
    }
    
    public ClassWriter privateAccess() throws IOException
    {
        if(classDec)
        {
            emptyLine();
        }
        else
        {
            writeIdentation();
        }
        accessWrited = true;
        writer.append("private ");
        return this;
    }
    
    public ClassWriter protectedAccess() throws IOException
    {
        if(classDec)
        {
            emptyLine();
        }
        else
        {
            writeIdentation();
        }
        accessWrited = true;
        writer.append("protected ");
        return this;
    }
    
    public ClassWriter finalElement() throws IOException
    {
        writer.append("final ");
        return this;
    }

    public ClassWriter staticElement() throws IOException
    {
        writer.append("static ");
        return this;
    }

    public ClassWriter fieldDec(String type, String name, String value) throws IOException
    {
        writer.append(type);
        writer.append(' ');
        writer.append(name);
        if(value != null && !value.isEmpty())
        {
            writer.append(" = ");
            writer.append(value);
        }
        writer.append(";\n");
        emptyLine();
        return this;
    }
    
    public ClassWriter contructorDec(String method) throws IOException
    {
        this.methodDec = true;
        this.firstParam = true;
        writer.append(method);
        writer.append("(");
        return this;
    }
    
    public ClassWriter methodDec(String returnType, String method) throws IOException
    {
        this.methodDec = true;
        this.firstParam = true;
        writer.append(returnType);
        writer.append(method);
        writer.append("(");
        return this;
    }
    
    public ClassWriter methodParam(String type, String param) throws IOException
    {
        if(!firstParam)
        {
            writer.append(", ");
        }
        writer.append(type);
        writer.append(' ');
        writer.append(param);
        this.firstParam = false;
        return this;
    }
    
    public ClassWriter codeLine(String code) throws IOException
    {
        writeIdentation();
        writer.append(code);
        closeStatement();
        return this;
    }

    private void closeStatement() throws IOException
    {
        writer.append(";\n");
    }

    private void writeIdentation() throws IOException
    {
        for(int i = 0; i < currentIdent; i++)
        {
            writer.append(' ');
        }
    }

    public String createGenericType(String baseType, String... parameters)
    {
        StringBuilder sb = new StringBuilder(baseType);
        sb.append('<');
        sb.append(Arrays.asList(parameters).stream().collect(Collectors.joining(", ")));
        sb.append('>');
        return sb.toString();
    }

    public String newObjStatement(String objCls, String... parameters)
    {
        StringBuilder sb = new StringBuilder("new ");
        sb.append(objCls);
        sb.append('(');
        sb.append(Arrays.asList(parameters).stream().collect(Collectors.joining(", ")));
        sb.append(')');
        return sb.toString();
    }

    public String stringLiteral(String value)
    {
        return '\"' + value + '\"';
    }

    public String dotClass(String value)
    {
        return value + ".class";
    }

    public String removeJavaLangPack(String type)
    {
        String javaLang = "java.lang.";
        if(type.startsWith(javaLang))
        {
            return type.substring(javaLang.length());
        }
        return type;
    }
}
