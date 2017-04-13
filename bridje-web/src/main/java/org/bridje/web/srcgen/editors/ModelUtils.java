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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.JAXBException;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;
import org.bridje.vfs.VFileOutputStream;
import org.bridje.web.srcgen.models.AssetModel;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.PartialUISuiteModel;
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
import org.bridje.web.srcgen.uisuite.PartialUISuite;
import org.bridje.web.srcgen.uisuite.Resource;
import org.bridje.web.srcgen.uisuite.ResourceRef;
import org.bridje.web.srcgen.uisuite.Script;
import org.bridje.web.srcgen.uisuite.StandaloneDef;
import org.bridje.web.srcgen.uisuite.Style;
import org.bridje.web.srcgen.uisuite.UISuite;
import org.bridje.web.srcgen.uisuite.ValueFlield;

public class ModelUtils
{
    private static final Logger LOG = Logger.getLogger(ModelUtils.class.getName());

    public static void saveUISuite(UISuiteModel uiSuite)
    {
        if(uiSuite.getName() != null)
        {
            if(uiSuite.getFile() == null)
            {
                uiSuite.setFile(new VFile(SrcGenService.DATA_PATH.join(uiSuite.getName() + ".xml")));
                uiSuite.getFile().createNewFile();
            }
            UISuite data = ModelUtils.fromModel(uiSuite);
            try(OutputStream os = new VFileOutputStream(uiSuite.getFile()))
            {
                UISuite.save(os, data);
            }
            catch(JAXBException | IOException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public static UISuiteModel toModel(UISuite suite, VFile file)
    {
        UISuiteModel result = new UISuiteModel();
        result.setFile(file);

        result.setName(suite.getName());
        result.setNamespace(suite.getNamespace());
        result.setPackageName(suite.getPackage());
        result.setRenderBody(suite.getRenderBody());
        if(isBlank(result.getRenderBody())) result.setRenderBody("<body>\n\t<#nested />\n</body>");
        result.setRenderHead(suite.getRenderHead());
        if(isBlank(result.getRenderHead())) result.setRenderHead("<head>\n\t<title>${view.title!}</title>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n\t<#nested />\n</head>");
        result.setRenderViewContainer(suite.getRenderViewContainer());
        if(isBlank(result.getRenderViewContainer())) result.setRenderViewContainer("<div id=\"view-form\">\n\t<#nested />\n</div>");

        result.setControls(controlsToModel(suite.getControls()));
        result.getControls().forEach(c -> c.setParent(result));
        result.setControlsTemplates(controlsToModel(suite.getControlsTemplates()));
        result.getControlsTemplates().forEach(c -> c.setParent(result));
        if(suite.getDefaultResources() == null) suite.setDefaultResources(new Resource());
        result.setDefaultResources(resourceToModel(suite.getDefaultResources()));
        result.getDefaultResources().setParent(result);
        result.setDefines(standaloneToModel(suite.getDefines()));
        result.getDefines().setParent(result);
        result.setFtlIncludes(stringListToModel(suite.getFtlIncludes()));
        result.setIncludes(includesToModel(suite.getIncludes(), file));
        result.getIncludes().forEach(c -> c.setParent(result));
        result.setResources(resourcesToModel(suite.getResources()));
        result.getResources().forEach(c -> c.setParent(result));
        result.setStandalone(standaloneToModel(suite.getStandalone()));
        result.getStandalone().setParent(result);

        return result;
    }

    public static UISuite fromModel(UISuiteModel suiteModel)
    {
        UISuite result = new UISuite();
        result.setControls(controlsFromModel(suiteModel.getControls()));
        result.setControlsTemplates(controlsFromModel(suiteModel.getControlsTemplates()));
        result.setDefaultResources(resourceFromModel(suiteModel.getDefaultResources()));
        result.setDefines(standaloneFromModel(suiteModel.getDefines()));
        result.setFtlIncludes(stringListFromModel(suiteModel.getFtlIncludes()));
        //result.setIncludes(includesFromModel(suiteModel.getIncludes()));
        result.setName(suiteModel.getName());
        result.setNamespace(suiteModel.getNamespace());
        result.setPackage(suiteModel.getPackageName());
        result.setRenderBody(suiteModel.getRenderBody());
        result.setRenderHead(suiteModel.getRenderHead());
        result.setRenderViewContainer(suiteModel.getRenderViewContainer());
        result.setResources(resourcesFromModel(suiteModel.getResources()));
        result.setStandalone(standaloneFromModel(suiteModel.getStandalone()));
        return result;
    }

    private static ObservableList<ControlDefModel> controlsToModel(List<ControlDef> controls)
    {
        ObservableList<ControlDefModel> result = FXCollections.observableArrayList();
        if(controls != null) controls.forEach(c -> result.add(controlToModel(c)));
        return result;
    }
    
    private static ControlDefModel controlToModel(ControlDef control)
    {
        ControlDefModel result = new ControlDefModel();

        result.setName(control.getName());
        result.setBase(control.getBase());
        if(control.getBaseTemplate() != null)
            result.setBaseTemplate(control.getBaseTemplate().getName());
        result.setFields(fieldsToModel(control.getFields()));
        result.getFields().forEach(f -> f.setParent(result));
        result.setRender(control.getRender());
        result.setResources(resourcesRefToModel(control.getResources()));
        result.getResources().forEach(f -> f.setParent(result));

        return result;
    }

    private static ResourceModel resourceToModel(Resource resource)
    {
        if(resource == null) return null;
        ResourceModel result = new ResourceModel();
        result.setName(resource.getName());
        result.setContent(assetsToModel(resource.getContent()));
        result.getContent().forEach(f -> f.setParent(result));

        return result;
    }

    private static StandaloneDefModel standaloneToModel(StandaloneDef standalone)
    {
        StandaloneDefModel result = new StandaloneDefModel();
        if(standalone != null) result.setContent(childsToModel(standalone.getContent()));
        return result;
    }

    private static ObservableList<PartialUISuiteModel> includesToModel(List<String> includes, VFile file)
    {
        ObservableList<PartialUISuiteModel> result = FXCollections.observableArrayList();
        if(includes != null) includes.forEach(i -> result.add(loadPartialUISuite(i, file)));
        return result;
    }

    private static PartialUISuiteModel loadPartialUISuite(String include, VFile file)
    {
        Path incPath = file.getPath().getParent().join(include);
        VFile incFile = new VFile(incPath);
        try(VFileInputStream is = new VFileInputStream(incFile))
        {
            PartialUISuite load = PartialUISuite.load(is);
            return partialUISuiteToModel(load, incFile);
        }
        catch(IOException | JAXBException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private static PartialUISuiteModel partialUISuiteToModel(PartialUISuite suite, VFile incFile)
    {
        if(suite == null) return null;
        PartialUISuiteModel result = new PartialUISuiteModel();
        result.setFile(incFile);
        result.setControls(controlsToModel(suite.getControls()));
        result.getControls().forEach(c -> c.setPartialSuite(result));
        result.setControlsTemplates(controlsToModel(suite.getControlsTemplates()));
        result.getControlsTemplates().forEach(c -> c.setPartialSuite(result));
        result.setFtlIncludes(stringListToModel(suite.getFtlIncludes()));
        result.setResources(resourcesToModel(suite.getResources()));
        result.getResources().forEach(c -> c.setPartialSuite(result));
        return result;
    }
    
    private static ObservableList<String> stringListToModel(List<String> stringList)
    {
        ObservableList<String> result = FXCollections.observableArrayList();
        if(stringList != null) result.addAll(stringList);
        return result;
    }

    private static ObservableList<ResourceModel> resourcesToModel(List<Resource> resources)
    {
        ObservableList<ResourceModel> result = FXCollections.observableArrayList();
        if(resources != null) resources.forEach(c -> result.add(resourceToModel(c)));
        return result;
    }

    private static ObservableList<FieldDefModel> fieldsToModel(List<FieldDef> fields)
    {
        ObservableList<FieldDefModel> result = FXCollections.observableArrayList();
        fields.forEach(c -> result.add(fieldToModel(c)));
        return result;
    }

    private static ObservableList<FieldDefModel> childsToModel(List<ChildField> fields)
    {
        ObservableList<FieldDefModel> result = FXCollections.observableArrayList();
        if(fields != null) fields.forEach(c -> result.add(fieldToModel(c)));
        return result;
    }

    private static ObservableList<ResourceRefModel> resourcesRefToModel(List<ResourceRef> resources)
    {
        ObservableList<ResourceRefModel> result = FXCollections.observableArrayList();
        if(resources != null) resources.forEach(c -> result.add(resourceRefToModel(c)));
        return result;
    }

    private static ResourceRefModel resourceRefToModel(ResourceRef resource)
    {
        ResourceRefModel result = new ResourceRefModel();
        result.setName(resource.getName());
        return result;
    }

    private static ObservableList<AssetModel> assetsToModel(List<AssetBase> content)
    {
        ObservableList<AssetModel> result = FXCollections.observableArrayList();
        if(content != null) content.forEach(c -> result.add(assetToModel(c)));
        return result;
    }
    
    private static AssetModel assetToModel(AssetBase asset)
    {
        AssetModel result = new AssetModel();
        result.setHref(asset.getHref());
        if(asset instanceof Link)
        {
            result.setType("link");
            result.setRel(((Link)asset).getRel());
        }
        else if(asset instanceof Script)
        {
            result.setType("script");
        }
        else if(asset instanceof Style)
        {
            result.setType("style");
        }
        return result;
    }

    private static List<ControlDef> controlsFromModel(ObservableList<ControlDefModel> controls)
    {
        List<ControlDef> result = new ArrayList<>();
        if(controls != null) controls.forEach(c -> result.add(controlFromModel(c)));
        return result;
    }

    private static StandaloneDef standaloneFromModel(StandaloneDefModel defines)
    {
        StandaloneDef result = new StandaloneDef();
        if(defines != null) result.setContent(childsFromModel(defines.getContent()));
        return result;
    }

    private static List<String> stringListFromModel(ObservableList<String> stringList)
    {
        List<String> result = new ArrayList<>();
        if(stringList != null) result.addAll(stringList);
        return result;
    }

    private static List<Resource> resourcesFromModel(ObservableList<ResourceModel> resources)
    {
        List<Resource> result = new ArrayList<>();
        if(resources != null) resources.forEach(r -> result.add(resourceFromModel(r)));
        return result;
    }

    private static Resource resourceFromModel(ResourceModel resource)
    {
        Resource result = new Resource();
        result.setName(resource.getName());
        result.setContent(assetsFromModel(resource.getContent()));
        return result;
    }

    private static ControlDef controlFromModel(ControlDefModel control)
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

    private static List<AssetBase> assetsFromModel(ObservableList<AssetModel> content)
    {
        List<AssetBase> result = new ArrayList<>();
        if(content != null) content.forEach(c -> result.add(assetFromModel(c)));
        return result;
    }

    private static List<ResourceRef> resourcesRefFromModel(ObservableList<ResourceRefModel> resources)
    {
        List<ResourceRef> result = new ArrayList<>();
        if(resources != null) resources.forEach(r -> result.add(resourceRefFromModel(r)));
        return result;
    }

    private static AssetBase assetFromModel(AssetModel c)
    {
        switch(c.getType())
        {
            case "link":
                Link link = new Link();
                link.setHref(c.getHref());
                link.setRel(c.getRel());
                return link;
            case "script":
                Script script = new Script();
                script.setHref(c.getHref());
                return script;
            default:
                Style style = new Style();
                style.setHref(c.getHref());
                return style;
        }
    }

    private static ResourceRef resourceRefFromModel(ResourceRefModel r)
    {
        ResourceRef result = new ResourceRef();
        result.setName(r.getName());
        return result;
    }

    private static List<FieldDef> fieldsFromModel(ObservableList<FieldDefModel> fields)
    {
        List<FieldDef> result = new ArrayList<>();
        if(fields != null) fields.forEach(f -> result.add(fieldFromModel(f)));
        return result;
    }
    

    private static List<ChildField> childsFromModel(ObservableList<FieldDefModel> fields)
    {
        List<ChildField> result = new ArrayList<>();
        if(fields != null) fields.forEach(f -> result.add((ChildField)fieldFromModel(f)));
        return result;
    }

    private static FieldDefModel fieldToModel(FieldDef fieldDef)
    {
        FieldDefModel result = new FieldDefModel();
        Class<?> cls = fieldDef.getClass();
        result.setName(fieldDef.getName());
        if(cls == OutAttrField.class)
        {
            result.setField("outAttr");
            OutAttrField outAttr = (OutAttrField)fieldDef;
            result.setDefaultValue(outAttr.getDefaultValue());
            result.setType(outAttr.getType());
        }
        if(cls == InAttrFlield.class)
        {
            result.setField("inAttr");
            InAttrFlield inAttr = (InAttrFlield)fieldDef;
            result.setDefaultValue(inAttr.getDefaultValue());
            result.setType(inAttr.getType());
        }
        if(cls == EventAttrFlield.class)
        {
            result.setField("eventAttr");
            EventAttrFlield eventAttr = (EventAttrFlield)fieldDef;
        }
        if(cls == AttrFlield.class)
        {
            result.setField("attr");
            AttrFlield attr = (AttrFlield)fieldDef;
            result.setDefaultValue(attr.getDefaultValue());
            result.setType(attr.getType());
        }
        if(cls == OutElementField.class)
        {
            result.setField("outEl");
            OutElementField outEl = (OutElementField)fieldDef;
            result.setDefaultValue(outEl.getDefaultValue());
            result.setType(outEl.getType());
        }
        if(cls == InElementFlield.class)
        {
            result.setField("inEl");
            InElementFlield inEl = (InElementFlield)fieldDef;
            result.setDefaultValue(inEl.getDefaultValue());
            result.setType(inEl.getType());
        }
        if(cls == EventElementFlield.class)
        {
            result.setField("eventEl");
            EventElementFlield eventEl = (EventElementFlield)fieldDef;
        }
        if(cls == ElementField.class)
        {
            result.setField("el");
            ElementField el = (ElementField)fieldDef;
            result.setDefaultValue(el.getDefaultValue());
            result.setType(el.getType());
        }
        if(cls == OutValueField.class)
        {
            result.setField("outValue");
            OutValueField outValue = (OutValueField)fieldDef;
            result.setDefaultValue(outValue.getDefaultValue());
            result.setType(outValue.getType());
        }
        if(cls == InValueFlield.class)
        {
            result.setField("inValue");
            InValueFlield inValue = (InValueFlield)fieldDef;
            result.setDefaultValue(inValue.getDefaultValue());
            result.setType(inValue.getType());
        }
        if(cls == EventValueFlield.class)
        {
            result.setField("eventValue");
            EventValueFlield eventValue = (EventValueFlield)fieldDef;
        }
        if(cls == ValueFlield.class)
        {
            result.setField("value");
            ValueFlield value = (ValueFlield)fieldDef;
            result.setDefaultValue(value.getDefaultValue());
            result.setType(value.getType());
        }
        if(cls == ChildField.class)
        {
            result.setField("child");
            ChildField child = (ChildField)fieldDef;
            result.setType(child.getType());
            result.setAllowPlaceHolder(child.getAllowPlaceHolder());
        }
        if(cls == ChildrenFlield.class)
        {
            result.setField("children");
            ChildrenFlield children = (ChildrenFlield)fieldDef;
            result.setAllowPlaceHolder(children.getAllowPlaceHolder());
            result.setWrapper(children.getWrapper());
            result.setSingle(children.getIsSingle());
            result.setChilds(childsToModel(children.getContent()));
        }
        return result;
    }

    private static FieldDef fieldFromModel(FieldDefModel model)
    {
        switch(model.getField())
        {
            case "outAttr":
                OutAttrField outAttr = new OutAttrField();
                outAttr.setType(model.getType());
                outAttr.setName(model.getName());
                outAttr.setDefaultValue(model.getDefaultValue());
                return outAttr;
            case "inAttr":
                InAttrFlield inAttr = new InAttrFlield();
                inAttr.setType(model.getType());
                inAttr.setName(model.getName());
                inAttr.setDefaultValue(model.getDefaultValue());
                return inAttr;
            case "eventAttr":
                EventAttrFlield eventAttr = new EventAttrFlield();
                eventAttr.setName(model.getName());
                return eventAttr;
            case "attr":
                AttrFlield attr = new AttrFlield();
                attr.setType(model.getType());
                attr.setName(model.getName());
                attr.setDefaultValue(model.getDefaultValue());
                return attr;
            case "outEl":
                OutElementField outEl = new OutElementField();
                outEl.setType(model.getType());
                outEl.setName(model.getName());
                outEl.setDefaultValue(model.getDefaultValue());
                return outEl;
            case "inEl":
                InElementFlield inEl = new InElementFlield();
                inEl.setType(model.getType());
                inEl.setName(model.getName());
                inEl.setDefaultValue(model.getDefaultValue());
                return inEl;
            case "eventEl":
                EventElementFlield eventEl = new EventElementFlield();
                eventEl.setName(model.getName());
                return eventEl;
            case "el":
                ElementField el = new ElementField();
                el.setType(model.getType());
                el.setName(model.getName());
                el.setDefaultValue(model.getDefaultValue());
                return el;
            case "outValue":
                OutValueField outValue = new OutValueField();
                outValue.setType(model.getType());
                outValue.setName(model.getName());
                outValue.setDefaultValue(model.getDefaultValue());
                return outValue;
            case "inValue":
                InValueFlield inValue = new InValueFlield();
                inValue.setType(model.getType());
                inValue.setName(model.getName());
                inValue.setDefaultValue(model.getDefaultValue());
                return inValue;
            case "eventValue":
                EventValueFlield eventValue = new EventValueFlield();
                eventValue.setName(model.getName());
                return eventValue;
            case "value":
                ValueFlield value = new ValueFlield();
                value.setType(model.getType());
                value.setName(model.getName());
                value.setDefaultValue(model.getDefaultValue());
                return value;
            case "child":
                ChildField child = new ChildField();
                child.setType(model.getType());
                child.setName(model.getName());
                child.setAllowPlaceHolder(model.getAllowPlaceHolder());
                return child;
            case "children":
                ChildrenFlield children = new ChildrenFlield();
                children.setSingle(model.getSingle());
                children.setWrapper(model.getWrapper());
                children.setName(model.getName());
                children.setAllowPlaceHolder(model.getAllowPlaceHolder());
                children.setContent(childsFromModel(model.getChilds()));
                return children;
        }
        return null;
    }

    private static boolean isBlank(String str)
    {
        return str == null || str.trim().isEmpty();
    }
}
