package com.example.apiModels;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "FeatureCollection", strict = false)
@Namespace(reference = "http://www.intergraph.com/geomedia/gml")
public class FeatureCollection {

    @Namespace(reference = "http://www.opengis.net/gml", prefix = "gml")
    @Element(name = "featureMember", required = false)
    FeatureMember featureMember;


    public FeatureMember getFeatureMember() {
        return featureMember;
    }
}