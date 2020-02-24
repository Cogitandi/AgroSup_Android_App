package com.example.fieldsList;

import java.util.ArrayList;
import java.util.List;

public class FieldsGroup {
    public String string;
    public final List<String> children = new ArrayList<String>();

    public FieldsGroup(String string) {
        this.string = string;
    }
}
