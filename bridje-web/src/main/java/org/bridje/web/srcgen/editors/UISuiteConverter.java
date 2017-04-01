/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.srcgen.editors;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bridje.web.srcgen.models.AssetModel;
import org.bridje.web.srcgen.models.ChildFieldModel;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.ResourceModel;
import org.bridje.web.srcgen.models.ResourceRefModel;
import org.bridje.web.srcgen.models.StandaloneDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.uisuite.AssetBase;
import org.bridje.web.srcgen.uisuite.AttrFlield;
import org.bridje.web.srcgen.uisuite.ChildField;
import org.bridje.web.srcgen.uisuite.ChildrenFlield;
import org.bridje.web.srcgen.uisuite.ControlDef;
import org.bridje.web.srcgen.uisuite.ElementField;
import org.bridje.web.srcgen.uisuite.EventAttrFlield;
import org.bridje.web.srcgen.uisuite.EventElementFlield;
import org.bridje.web.srcgen.uisuite.EventValueFlield;
import org.bridje.web.srcgen.uisuite.FieldDef;
import org.bridje.web.srcgen.uisuite.InAttrFlield;
import org.bridje.web.srcgen.uisuite.InElementFlield;
import org.bridje.web.srcgen.uisuite.InValueFlield;
import org.bridje.web.srcgen.uisuite.Link;
import org.bridje.web.srcgen.uisuite.OutAttrField;
import org.bridje.web.srcgen.uisuite.OutElementField;
import org.bridje.web.srcgen.uisuite.OutValueField;
import org.bridje.web.srcgen.uisuite.Resource;
import org.bridje.web.srcgen.uisuite.ResourceRef;
import org.bridje.web.srcgen.uisuite.Script;
import org.bridje.web.srcgen.uisuite.StandaloneDef;
import org.bridje.web.srcgen.uisuite.UISuite;
import org.bridje.web.srcgen.uisuite.ValueFlield;

public class UISuiteConverter
{
    public UISuiteModel toModel(UISuite suite)
    {
        UISuiteModel result = new UISuiteModel();
        result.setControls(controlsToModel(suite.getControls()));
        result.setControlsTemplates(controlsToModel(suite.getControlsTemplates()));
        result.setDefaultResources(resourceToModel(suite.getDefaultResources()));
        result.setDefines(standaloneToModel(suite.getDefines()));
        result.setFtlIncludes(stringListToModel(suite.getFtlIncludes()));
        result.setIncludes(stringListToModel(suite.getIncludes()));
        result.setName(suite.getName());
        result.setNamespace(suite.getNamespace());
        result.setRenderBody(suite.getRenderBody());
        result.setRenderHead(suite.getRenderHead());
        result.setRenderViewContainer(suite.getRenderViewContainer());
        result.setResources(resourcesToModel(suite.getResources()));
        result.setStandalone(standaloneToModel(suite.getStandalone()));
        return result;
    }

    public UISuite fromModel(UISuiteModel suiteModel)
    {
        UISuite result = new UISuite();
        result.setControls(controlsFromModel(suiteModel.getControls()));
        result.setControlsTemplates(controlsFromModel(suiteModel.getControlsTemplates()));
        result.setDefaultResources(resourceFromModel(suiteModel.getDefaultResources()));
        result.setDefines(standaloneFromModel(suiteModel.getDefines()));
        result.setFtlIncludes(stringListFromModel(suiteModel.getFtlIncludes()));
        result.setIncludes(stringListFromModel(suiteModel.getIncludes()));
        result.setName(suiteModel.getName());
        result.setNamespace(suiteModel.getNamespace());
        result.setRenderBody(suiteModel.getRenderBody());
        result.setRenderHead(suiteModel.getRenderHead());
        result.setRenderViewContainer(suiteModel.getRenderViewContainer());
        result.setResources(resourcesFromModel(suiteModel.getResources()));
        result.setStandalone(standaloneFromModel(suiteModel.getStandalone()));
        return result;
    }

    private ObservableList<ControlDefModel> controlsToModel(List<ControlDef> controls)
    {
        ObservableList<ControlDefModel> result = FXCollections.observableArrayList();
        controls.forEach(c -> result.add(controlToModel(c)));
        return result;
    }
    
    private ControlDefModel controlToModel(ControlDef control)
    {
        ControlDefModel result = new ControlDefModel();

        result.setBase(control.getBase());
        if(control.getBaseTemplate() != null)
            result.setBaseTemplate(control.getBaseTemplate().getName());
        result.setFields(fieldsToModel(control.getFields()));
        result.setName(control.getName());
        result.setRender(control.getRender());
        result.setResources(resourcesRefToModel(control.getResources()));

        return result;
    }

    private ResourceModel resourceToModel(Resource resource)
    {
        ResourceModel result = new ResourceModel();

        result.setName(resource.getName());
        result.setContent(assetsToModel(resource.getContent()));

        return result;
    }

    private StandaloneDefModel standaloneToModel(StandaloneDef standalone)
    {
        StandaloneDefModel result = new StandaloneDefModel();
        result.setContent(childFieldsToModel(standalone.getContent()));
        return result;
    }

    private ObservableList<String> stringListToModel(List<String> stringList)
    {
        ObservableList<String> result = FXCollections.observableArrayList();
        result.addAll(stringList);
        return result;
    }

    private ObservableList<ResourceModel> resourcesToModel(List<Resource> resources)
    {
        ObservableList<ResourceModel> result = FXCollections.observableArrayList();
        resources.forEach(c -> result.add(resourceToModel(c)));
        return result;
    }

    private ObservableList<FieldDefModel> fieldsToModel(List<FieldDef> fields)
    {
        ObservableList<FieldDefModel> result = FXCollections.observableArrayList();
        fields.forEach(c -> result.add(fieldToModel(c)));
        return result;
    }

    private ObservableList<ResourceRefModel> resourcesRefToModel(List<ResourceRef> resources)
    {
        ObservableList<ResourceRefModel> result = FXCollections.observableArrayList();
        resources.forEach(c -> result.add(resourceRefToModel(c)));
        return result;
    }

    private ResourceRefModel resourceRefToModel(ResourceRef resource)
    {
        ResourceRefModel result = new ResourceRefModel();
        result.setName(resource.getName());
        return result;
    }

    private ObservableList<AssetModel> assetsToModel(List<AssetBase> content)
    {
        ObservableList<AssetModel> result = FXCollections.observableArrayList();
        content.forEach(c -> result.add(assetToModel(c)));
        return result;
    }
    
    private AssetModel assetToModel(AssetBase asset)
    {
        AssetModel result = new AssetModel();
        result.setHref(asset.getHref());
        if(asset instanceof Link)
        {
            result.setRel(((Link)asset).getRel());
        }
        return result;
    }

    private ObservableList<ChildFieldModel> childFieldsToModel(List<ChildField> content)
    {
        ObservableList<ChildFieldModel> result = FXCollections.observableArrayList();
        content.forEach(c -> result.add(childFieldToModel(c)));
        return result;
    }

    private ChildFieldModel childFieldToModel(ChildField child)
    {
        ChildFieldModel result = new ChildFieldModel();
        
        result.setName(child.getName());
        result.setType(child.getType());
        
        return result;
    }

    private List<ControlDef> controlsFromModel(ObservableList<ControlDefModel> controls)
    {
        List<ControlDef> result = new ArrayList<>();
        controls.forEach(c -> result.add(controlFromModel(c)));
        return result;
    }

    private StandaloneDef standaloneFromModel(StandaloneDefModel defines)
    {
        StandaloneDef result = new StandaloneDef();
        result.setContent(childFieldsFromModel(defines.getContent()));
        return result;
    }

    private List<String> stringListFromModel(ObservableList<String> stringList)
    {
        List<String> result = new ArrayList<>();
        result.addAll(stringList);
        return result;
    }

    private List<Resource> resourcesFromModel(ObservableList<ResourceModel> resources)
    {
        List<Resource> result = new ArrayList<>();
        resources.forEach(r -> result.add(resourceFromModel(r)));
        return result;
    }

    private Resource resourceFromModel(ResourceModel resource)
    {
        Resource result = new Resource();
        result.setName(resource.getName());
        result.setContent(assetsFromModel(resource.getContent()));
        return result;
    }

    private ControlDef controlFromModel(ControlDefModel control)
    {
        ControlDef result = new ControlDef();
        result.setBase(control.getBase());
        result.setBaseTemplate(null);
        result.setDeclaredResources(resourcesRefFromModel(control.getResources()));
        result.setDeclaredFields(fieldsFromModel(control.getFields()));
        result.setName(control.getName());
        result.setRender(control.getRender());
        return result;
    }

    private List<ChildField> childFieldsFromModel(ObservableList<ChildFieldModel> content)
    {
        List<ChildField> result = new ArrayList<>();
        content.forEach(r -> result.add(childFieldFromModel(r)));
        return result;
    }

    private List<AssetBase> assetsFromModel(ObservableList<AssetModel> content)
    {
        List<AssetBase> result = new ArrayList<>();
        content.forEach(c -> result.add(assetFromModel(c)));
        return result;
    }

    private List<ResourceRef> resourcesRefFromModel(ObservableList<ResourceRefModel> resources)
    {
        List<ResourceRef> result = new ArrayList<>();
        resources.forEach(r -> result.add(resourceRefFromModel(r)));
        return result;
    }

    private ChildField childFieldFromModel(ChildFieldModel r)
    {
        ChildField result = new ChildField();
        result.setName(r.getName());
        result.setType(r.getType());
        return result;
    }

    private AssetBase assetFromModel(AssetModel c)
    {
        AssetBase result = new Script();
        result.setHref(c.getHref());
        return result;
    }

    private ResourceRef resourceRefFromModel(ResourceRefModel r)
    {
        ResourceRef result = new ResourceRef();
        result.setName(r.getName());
        return result;
    }

    private List<FieldDef> fieldsFromModel(ObservableList<FieldDefModel> fields)
    {
        List<FieldDef> result = new ArrayList<>();
        fields.forEach(f -> result.add(fieldFromModel(f)));
        return result;
    }

    private FieldDefModel fieldToModel(FieldDef fieldDef)
    {
        FieldDefModel result = new FieldDefModel();
        Class<?> cls = fieldDef.getClass();
        result.setName(fieldDef.getName());
        if(cls == OutAttrField.class)
        {
            result.setField("outAttr");
        }
        if(cls == InAttrFlield.class)
        {
            result.setField("inAttr");
        }
        if(cls == EventAttrFlield.class)
        {
            result.setField("eventAttr");
        }
        if(cls == AttrFlield.class)
        {
            result.setField("attr");
        }
        if(cls == OutElementField.class)
        {
            result.setField("outEl");
        }
        if(cls == InElementFlield.class)
        {
            result.setField("inEl");
        }
        if(cls == EventElementFlield.class)
        {
            result.setField("eventEl");
        }
        if(cls == ElementField.class)
        {
            result.setField("el");
        }
        if(cls == OutValueField.class)
        {
            result.setField("outValue");
        }
        if(cls == InValueFlield.class)
        {
            result.setField("inValue");
        }
        if(cls == EventValueFlield.class)
        {
            result.setField("eventValue");
        }
        if(cls == ValueFlield.class)
        {
            result.setField("value");
        }
        if(cls == ChildField.class)
        {
            result.setField("child");
        }
        if(cls == ChildrenFlield.class)
        {
            result.setField("children");
        }
        return result;
    }

    private FieldDef fieldFromModel(FieldDefModel model)
    {
        switch(model.getField())
        {
            case "outAttr":
                OutAttrField outAttr = new OutAttrField();
                return outAttr;
            case "inAttr":
                InAttrFlield inAttr = new InAttrFlield();
                return inAttr;
            case "eventAttr":
                EventAttrFlield eventAttr = new EventAttrFlield();
                return eventAttr;
            case "attr":
                AttrFlield attr = new AttrFlield();
                return attr;
            case "outEl":
                OutElementField outEl = new OutElementField();
                return outEl;
            case "inEl":
                InElementFlield inEl = new InElementFlield();
                return inEl;
            case "eventEl":
                EventElementFlield eventEl = new EventElementFlield();
                return eventEl;
            case "el":
                ElementField el = new ElementField();
                return el;
            case "outValue":
                OutValueField outValue = new OutValueField();
                return outValue;
            case "inValue":
                InValueFlield inValue = new InValueFlield();
                return inValue;
            case "eventValue":
                EventValueFlield eventValue = new EventValueFlield();
                return eventValue;
            case "value":
                ValueFlield value = new ValueFlield();
                return value;
            case "child":
                ChildField child = new ChildField();
                return child;
            case "children":
                ChildrenFlield children = new ChildrenFlield();
                return children;
        }
        return null;
    }
}
