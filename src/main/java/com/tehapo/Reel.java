package com.tehapo;

import java.util.Random;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderer.ImageRenderer;

public class Reel extends Grid {

    private ReelRoller reelRoller;

    public Reel() {
        setWidth("200px");
        setHeight("500px");
        setHeaderVisible(false);
        setSelectionMode(SelectionMode.SINGLE);
        addColumn("", ThemeResource.class).setRenderer(new ImageRenderer());

        reelRoller = new ReelRoller(this);

        final Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            addRow(new ThemeResource("img/"
                    + (r.nextBoolean() ? "vaadin-silver-coin" : "vaadin-coin")
                    + ".png"));
        }
    }

    public void roll(int rowIndex, int duration) {
        select(getContainerDataSource().getIdByIndex(rowIndex));
        reelRoller.roll(rowIndex, duration);
    }

}
