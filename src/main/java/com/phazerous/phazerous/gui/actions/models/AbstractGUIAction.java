package com.phazerous.phazerous.gui.actions.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public abstract class AbstractGUIAction {
    private ObjectId _id;

    private Integer type;
}
