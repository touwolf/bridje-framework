
package org.bridje.web.srcgen.uisuite;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * This is the base class for all the resources assets, (script, link, style,
 * etc...).
 */
public class AssetBase
{
    @XmlAttribute
    private String href;

    /**
     * The href element for this asset.
     *
     * @return An string representing the href url for this asset.
     */
    public String getHref()
    {
        return href;
    }

    /**
     * The href element for this asset.
     * 
     * @param href The url to be set as the href elemente for this asset.
     */
    public void setHref(String href)
    {
        this.href = href;
    }

}
