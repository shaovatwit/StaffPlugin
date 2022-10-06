package com.ventedmc.staff.interfaces;

public interface Module {
    void enableModule();
    void disableModule();

    boolean isEnabled();
    String getName();
}
