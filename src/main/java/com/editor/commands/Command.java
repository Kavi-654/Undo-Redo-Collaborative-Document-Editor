package com.editor.commands;

public interface Command {

   public void execute();
    public  void undo();
    public  String getId();
    public String getType();
    public String getUserId();
    public long getTimestamp();
    public String getDescription();
}
