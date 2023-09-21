package com.phazerous.phazerous.gathering.enums;

import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
public class PlayerMetaTools {
    private ObjectId _id;
    private List<ObjectId> mining;
    private List<ObjectId> digging;
    private List<ObjectId> chopping;
}
