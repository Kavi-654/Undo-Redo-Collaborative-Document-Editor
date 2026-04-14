package com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class CareTaker {
    private  final List<Memento> history=new ArrayList<>();
    private int currentIndex=-1;

    public void save(Memento m)
    {
        history.add(m);
        currentIndex=history.size()-1;
    }

    public Memento getLatest()
    {
        if(history.isEmpty())
        {
            return null;
        }
        return history.get(history.size()-1);
    }
    public Memento getByIndex(int i)
    {
        if(i>=0 && i<history.size()) {
            return history.get(i);
        }
        return null;
    }
    public List<Memento> getAll()
    {
        return new ArrayList<>(history);
    }
    public int size()
    {
        return history.size();
    }
}
