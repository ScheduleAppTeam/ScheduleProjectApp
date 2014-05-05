package com.example.scheduleprojectapp;

public class Venue {
    private Integer _id;
    private Integer audience;
    private Integer housing;

    public void SetId(final Integer id){
        _id = id;
    }

    public void SetAudience(final Integer _audience){
        audience = _audience;
    }

    public void SetHousing(final Integer _housing){
        housing = _housing;
    }

    public final Integer GetId(){
        return _id;
    }

    public final Integer GetAudience(){
        return audience;
    }

    public final Integer GetHousing(){
        return housing;
    }

    public Venue(Integer id, Integer _audience, Integer _housing){
        _id = id;
        audience = _audience;
        housing = _housing;
    }

    public Venue(Integer _audience, Integer _housing){
        audience = _audience;
        housing = _housing;
    }

    public Venue(){
    }
}
