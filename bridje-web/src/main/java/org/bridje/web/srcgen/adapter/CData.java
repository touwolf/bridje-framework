package org.bridje.web.srcgen.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CData extends XmlAdapter<String, String>
{
    private static final String BEGIN_TAG = "<![CDATA[";

    private static final String END_TAG = "]]>";

    @Override
    public String unmarshal(String value) throws Exception
    {
        if (value != null)
        {
            String umValue = value;
            if (umValue.startsWith(BEGIN_TAG))
            {
                umValue = umValue.substring(BEGIN_TAG.length());
            }
            if (umValue.endsWith(END_TAG))
            {
                umValue = umValue.substring(0, umValue.length() - END_TAG.length());
            }
            return umValue;
        }
        return null;
    }

    @Override
    public String marshal(String value) throws Exception
    {
        if (value != null)
        {
            String mValue = value;
            if (!mValue.startsWith(BEGIN_TAG))
            {
                mValue = BEGIN_TAG + mValue;
            }
            if (!mValue.endsWith(END_TAG))
            {
                mValue += END_TAG;
            }
            return mValue;
        }
        return null;
    }
}
