package com.tehapo;

import java.util.Random;

import javax.servlet.annotation.WebServlet;

import com.tehapo.Reel.RollCompletedEvent;
import com.tehapo.Reel.RollCompletedListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("grid-slots")
@SuppressWarnings("serial")
public class GridSlotsUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GridSlotsUI.class, widgetset = "com.tehapo.GridSlotsWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        Label coinsRemaining = new Label("COINS 404");
        coinsRemaining.addStyleName("display");
        layout.addComponent(coinsRemaining);

        Label status = new Label("WINNER");
        status.addStyleName("display");
        status.addStyleName("blink");
        layout.addComponent(new HorizontalLayout(coinsRemaining, status));

        HorizontalLayout reels = new HorizontalLayout();
        reels.setSpacing(true);
        final Reel grid = new Reel();
        final Reel grid2 = new Reel();
        final Reel grid3 = new Reel();
        reels.addComponents(grid, grid2, grid3);

        RollCompletedListener rollCompletedListener = new RollCompletedListener() {

            @Override
            public void rollCompleted(RollCompletedEvent event) {
                Reel reel = ((Reel) event.getComponent());
                System.out.println("COMPLETED! "
                        + reel.getContainerDataSource().getItem(
                                reel.getSelectedRow()));
            }
        };
        grid.addRollCompletedListener(rollCompletedListener);
        grid2.addRollCompletedListener(rollCompletedListener);
        grid3.addRollCompletedListener(rollCompletedListener);

        final Random r = new Random();
        Button start = new Button("Roll", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                int row = r.nextInt(grid.getContainerDataSource().size());
                int row2 = r.nextInt(grid2.getContainerDataSource().size());
                int row3 = r.nextInt(grid3.getContainerDataSource().size());

                grid.roll(row, 2000 + r.nextInt(1000));
                grid2.roll(row2, 3000 + r.nextInt(1000));
                grid3.roll(row3, 4000 + r.nextInt(1000));
            }

        });
        layout.addComponents(reels, start);

    }

}
