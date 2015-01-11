package com.tehapo;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@JavaScript("soundfx.js")
public class SoundFx extends AbstractJavaScriptExtension {

    public SoundFx(final UI ui) {
        extend(ui);
    }

    public void play(String soundId) {
        callFunction("playSound", soundId);
    }

}
