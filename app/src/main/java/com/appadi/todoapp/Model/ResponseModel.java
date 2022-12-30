package com.appadi.todoapp.Model;

import java.util.List;

public class ResponseModel {
    private List<DataModel> data;

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}