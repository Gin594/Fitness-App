package com.example.cse110project.Cloud;

import com.example.cse110project.Models.RouteForm;

public interface ICloudCollection extends IFireStoreListener {

    void get();

    void add(Object o);

    void update(Object o, Object data, String field);

}
