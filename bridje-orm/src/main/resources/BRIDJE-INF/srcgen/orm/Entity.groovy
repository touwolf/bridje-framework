
import groovy.util.*;
import groovy.util.slurpersupport.*;

def result = [:];
def entities = [:];
def templates = [:];

def doc = new XmlSlurper().parse(docInput);

def findJavaType = { f->
    def javaType;
    if(f['type'] == 'relation')
    {
        javaType = f['with'];
    }
    else if(f['type'] == 'enum')
    {
        javaType = f['enumType'];
    }
    else if(custTypes[f['type']] != null)
    {
        javaType = custTypes[f['type']]['class'];
    }
    else
    {
        javaType = f['type'];
    }
};
        
def applyBase = { e->
    def base = templates[e['base']];
    if(base != null)
    {
        join(e, base);
    }
};

def join = { e,b->
    applyBase(b);
    b['fields'].each{ k,v->
        if(e['fields'][k] == null)
        {
            e['fields'][k] = v;
        }
    };
};

def parseField = { f->
    def newField = [:];
    newField['name'] = f.@name;
    newField['sqlType'] = f.@sqlType;
    newField['fieldType'] = f.name();
    newField['description'] = f.@description;
    newField['javaType'] = findJavaType(f);
    newField;
};

def parseEntity = { e->
    def newEnt = [:];
    newEnt['name'] = e.@name;
    newEnt['base'] = e.@base;
    newEnt['description'] = e.@description;
    newEnt['table'] = e.@table;
    newEnt['fields'] = [:];
    e.'*'.each{ f-> 
        newEnt['fields'][f.@name] = parseField(f)
    };
    newEnt;
};

doc.'*'
    .findAll{ n-> n.name() == 'template' }
    .each{ n->
        def ent = parseEntity(n);
        templates[ent['name']] = ent;
    };

doc.'*'
    .findAll{ n-> n.name() == 'entity' }
    .each{ n->
        def ent = parseEntity(n);
        entities[ent['name']] = ent;
    };
    
entities.each{ k,v-> 
    applyBase(v);
};

result['entities'] = entities;

return result;