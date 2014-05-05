package com.example.scheduleprojectapp;

import java.io.Serializable;

public class DiapasonOfDays implements Serializable {

    private DayOfWeek[] diapason;

    public void SetDiapason(final DayOfWeek[] _diapason){
        diapason = _diapason;
    }

    public final DayOfWeek[] GetDiapason(){
        return diapason;
    }

    public DiapasonOfDays(DayOfWeek[] _diapason){
        diapason = _diapason;
    }

    public DiapasonOfDays(){
        this(new DayOfWeek[Constants.DIAPASON_OF_DAYS]);
    }
}
