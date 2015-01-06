package com.tehapo;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.Grid;

@JavaScript("reelroller.js")
public class ReelRoller extends AbstractJavaScriptExtension {

    public ReelRoller(Grid reelGrid) {
        extend(reelGrid);
    }

    public void roll(int rowIndex, int duration) {
        callFunction("roll", rowIndex, duration);
    }
}
