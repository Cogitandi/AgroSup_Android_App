package com.example.apiModels;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "featureMember", strict = false)
@Namespace(prefix = "gml")
public class FeatureMember {
    @Element(name = "Layer")
    private Layer layer;

    public Layer getLayer() {
        return layer;
    }
}
