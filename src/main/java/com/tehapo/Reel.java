package com.tehapo;

import java.lang.reflect.Method;
import java.util.Random;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderer.ImageRenderer;
import com.vaadin.util.ReflectTools;

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

    /**
     * Start the rolling animation for the given {@code duration} (in
     * milliseconds) to target the given row. A {@link RollCompletedEvent} is
     * fired when the animation is completed.
     * 
     * @param rowIndex
     * @param duration
     */
    public void roll(int rowIndex, int duration) {
        select(getContainerDataSource().getIdByIndex(rowIndex));
        reelRoller.roll(rowIndex, duration);
    }

    public void addRollCompletedListener(RollCompletedListener listener) {
        addListener(RollCompletedEvent.class, listener,
                RollCompletedListener.ROLL_COMPLETED_METHOD);
    }

    public void removeRollCompletedListener(RollCompletedListener listener) {
        removeListener(RollCompletedEvent.class, listener);
    }

    void fireCompletedEvent() {
        fireEvent(new RollCompletedEvent(this));
    }

    public static class RollCompletedEvent extends Component.Event {

        public RollCompletedEvent(Component source) {
            super(source);
        }

    }

    public static interface RollCompletedListener {

        public static final Method ROLL_COMPLETED_METHOD = ReflectTools
                .findMethod(RollCompletedListener.class, "rollCompleted");

        void rollCompleted();
    }

}