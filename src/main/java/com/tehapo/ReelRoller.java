package com.tehapo;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

@JavaScript("reelroller.js")
public class ReelRoller extends AbstractJavaScriptExtension {

    public ReelRoller(final Reel reel) {
        extend(reel);
        addFunction("onAnimationComplete", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {
                reel.fireCompletedEvent();
            }

        });
    }

    public void roll(int rowIndex, int duration) {
        callFunction("roll", rowIndex, duration);
    }

}
