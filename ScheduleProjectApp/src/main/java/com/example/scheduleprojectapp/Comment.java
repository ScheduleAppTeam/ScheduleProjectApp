package com.example.scheduleprojectapp;

public class Comment {
    private Integer id;
    private Integer editableDay;
    private String fname;
    private String sname;
    private Integer permission;
    private Long dataComment;
    private String text;

    public void SetEditableDay(final Integer _editableDay){
        editableDay = _editableDay;
    }

    public void SetFname(final String _fname){
        fname = _fname;
    }

    public void SetSname(final String _sname){
        sname = _sname;
    }

    public void SetPermission(final Integer _permission){
        permission = _permission;
    }

    public void SetDataComment(final Long _dataComment){
        dataComment = _dataComment;
    }

    public void SetText(final String _text){
        text = _text;
    }

    public final Integer GetId(){
        return id;
    }

    public final Integer GetEditableDay(){
        return editableDay;
    }

    public final String GetFname(){
        return fname;
    }

    public final String GetSname(){
        return sname;
    }

    public final String GetText(){
        return text;
    }

    public final Integer GetPermission(){
        return editableDay;
    }

    public final Long GetDataComment(){
        return dataComment;
    }

    public Comment(Integer _editableDay, String _fname, String _sname, Integer _permission, Long _dataComment, String _text){
        editableDay = _editableDay;
        fname = _fname;
        sname = _sname;
        permission = _permission;
        dataComment = _dataComment;
        text = _text;
    }
}
