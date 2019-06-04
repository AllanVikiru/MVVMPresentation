package com.example.mvvm.data.network.retrofit.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Results<D> {

    private List<D> results;

    @NonNull
    public List<D> getResults() {
        return results;
    }

    public void setResults(@NonNull List<D> results) {
        this.results = results;
    }
}
