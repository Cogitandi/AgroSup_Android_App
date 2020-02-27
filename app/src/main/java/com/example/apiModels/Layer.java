package com.example.apiModels;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Map;

@Root(name = "Layer", strict = false)
public class Layer {
    @ElementMap(entry = "Attribute", key = "Name", attribute = true, inline = true)
    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

}
