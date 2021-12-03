package com.univalle.javiermurguia.proyectoteleferico.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MarkerViewModel extends ViewModel {
    private final MutableLiveData<Marcador> selectedItem = new MutableLiveData<Marcador>();

    public void setData(Marcador marker){
        selectedItem.setValue(marker);
    }

    public LiveData<Marcador> getSelectedItem(){
        return selectedItem;
    }
}
