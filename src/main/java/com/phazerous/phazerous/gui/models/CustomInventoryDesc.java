package com.phazerous.phazerous.gui.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomInventoryDesc {
    private String title;
    private Integer size;
    private List<CustomInventoryItemDesc> contents;
}
