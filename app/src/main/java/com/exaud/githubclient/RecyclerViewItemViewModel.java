package com.exaud.githubclient;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

public class RecyclerViewItemViewModel extends ViewModel {
    private ObservableField<String> name;
    private ObservableField<String> description;

    public RecyclerViewItemViewModel(String name, String description) {
        this.name = new ObservableField<>(name);
        this.description = new ObservableField<>(description);
    }

    public ObservableField<String> getName() {
        return name;
    }

    public ObservableField<String> getDescription() {
        return description;
    }
}
