package ru.mipt.bit.platformer.logic.observer;

public interface Observer {
    void onObjectAdded(Object obj);
    void onObjectRemoved(Object obj);
}
