package com.example.apiModels;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Attribute", strict = false)
public class Attribut {
    @Attribute(name="Name")
    private String name;
    @Element
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
