package com.tehapo;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Random;

import com.tehapo.model.ReelItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderer.ImageRenderer;
import com.vaadin.util.ReflectTools;

@SuppressWarnings("serial")
public class Reel extends Grid {

    private ReelRoller reelRoller;

    public Reel() {
        setWidth("200px");
        setHeight("500px");
        setHeaderVisible(false);
        setSelectionMode(SelectionMode.SINGLE);
        addColumn("", ReelItem.class).setRenderer(new ImageRenderer(),
                new ReelItemConverter());

        reelRoller = new ReelRoller(this);

        final Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            addRow(ReelItem.values()[r.nextInt(ReelItem.values().length)]);

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

    public ReelItem getValue() {
        return (ReelItem) getContainerDataSource().getItem(getSelectedRow())
                .getItemProperty("").getValue();
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
                .findMethod(RollCompletedListener.class, "rollCompleted",
                        RollCompletedEvent.class);

        void rollCompleted(RollCompletedEvent event);
    }

    public static class ReelItemConverter implements
            Converter<ThemeResource, ReelItem> {

        private static final String RESOURCE_PREFIX = "img/";
        private static final String RESOURCE_SUFFIX = ".png";

        @Override
        public ReelItem convertToModel(ThemeResource value,
                Class<? extends ReelItem> targetType, Locale locale)
                throws com.vaadin.data.util.converter.Converter.ConversionException {
            String enumValue = value.getResourceId().replaceAll(
                    RESOURCE_PREFIX + "|" + RESOURCE_SUFFIX, "");
            return ReelItem.valueOf(enumValue.toUpperCase());
        }

        @Override
        public ThemeResource convertToPresentation(ReelItem value,
                Class<? extends ThemeResource> targetType, Locale locale)
                throws com.vaadin.data.util.converter.Converter.ConversionException {
            return new ThemeResource(RESOURCE_PREFIX
                    + value.toString().toLowerCase() + RESOURCE_SUFFIX);
        }

        @Override
        public Class<ReelItem> getModelType() {
            return ReelItem.class;
        }

        @Override
        public Class<ThemeResource> getPresentationType() {
            return ThemeResource.class;
        }

    }

}
