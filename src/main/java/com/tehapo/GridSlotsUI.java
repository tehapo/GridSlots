package com.tehapo;

import java.util.Random;

import javax.servlet.annotation.WebServlet;

import com.tehapo.Reel.RollCompletedEvent;
import com.tehapo.Reel.RollCompletedListener;
import com.tehapo.model.Payouts;
import com.tehapo.model.RollResult;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
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

    private int coins = 10;
    private Label coinsRemaining;
    private Label status;
    private Button start;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        coinsRemaining = new Label("");
        coinsRemaining.addStyleName("display");
        layout.addComponent(coinsRemaining);
        updateCoins();

        status = new Label("-");
        status.addStyleName("display");
        HorizontalLayout displays = new HorizontalLayout(coinsRemaining, status);
        displays.setSpacing(true);
        displays.setWidth("100%");
        layout.addComponent(displays);

        HorizontalLayout reels = new HorizontalLayout();
        reels.setSpacing(true);
        final Reel grid = new Reel();
        final Reel grid2 = new Reel();
        final Reel grid3 = new Reel();
        reels.addComponents(grid, grid2, grid3);

        RollCompletedListener rollCompletedListener = new RollCompletedListener() {

            @Override
            public void rollCompleted(RollCompletedEvent event) {
                RollResult result = new RollResult(grid.getValue(),
                        grid2.getValue(), grid3.getValue());
                Integer payout = Payouts.getPayout(result);
                if (payout != null) {
                    coins += payout;
                    updateCoins();

                    status.setValue("WINNER, " + payout);
                    status.addStyleName("blink");
                } else {
                    if (coins == 0) {
                        start.setEnabled(false);
                        status.setValue("GAME OVER");
                    }
                }
            }
        };
        grid3.addRollCompletedListener(rollCompletedListener);

        final Random r = new Random();
        start = new Button("Roll", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                coins--;
                updateCoins();
                status.removeStyleName("blink");

                int row = r.nextInt(grid.getContainerDataSource().size());
                int row2 = r.nextInt(grid2.getContainerDataSource().size());
                int row3 = r.nextInt(grid3.getContainerDataSource().size());

                grid.roll(row, 1000 + r.nextInt(1000));
                grid2.roll(row2, 2000 + r.nextInt(1000));
                grid3.roll(row3, 3000 + r.nextInt(1000));
            }

        });
        start.addStyleName("start");
        start.setHeight("100px");
        layout.addComponents(reels, start);
        layout.setComponentAlignment(reels, Alignment.TOP_CENTER);
        start.setWidth("100%");
    }

    private void updateCoins() {
        coinsRemaining.setValue("COINS " + coins);
    }

}
