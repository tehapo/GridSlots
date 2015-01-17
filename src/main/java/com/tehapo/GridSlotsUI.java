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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("grid-slots")
@SuppressWarnings("serial")
public class GridSlotsUI extends UI {

    private SoundFx soundFx;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GridSlotsUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private Label statusDisplay;
    private Button start;

    // "Model"
    private String status;
    private int coins = 10;

    @Override
    protected void init(VaadinRequest request) {
        soundFx = new SoundFx(this);

        final VerticalLayout layout = new VerticalLayout();
        layout.setWidth("900px");
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addStyleName("main");
        setContent(layout);

        layout.addComponent(new Logo());

        statusDisplay = new Label("-", ContentMode.HTML);
        statusDisplay.addStyleName("display");
        updateStatus();
        layout.addComponent(statusDisplay);

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
                    status = "WINNER, " + payout;
                    statusDisplay.addStyleName("blink");
                    soundFx.play("winner.wav");

                } else {
                    if (coins == 0) {
                        status = "GAME OVER";
                    }
                }
                updateStatus();
                start.setEnabled(coins > 0);
            }
        };
        grid3.addRollCompletedListener(rollCompletedListener);

        final Random r = new Random();
        start = new Button("Roll", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                coins--;
                updateStatus();
                statusDisplay.removeStyleName("blink");

                int row = r.nextInt(grid.getContainerDataSource().size());
                int row2 = r.nextInt(grid2.getContainerDataSource().size());
                int row3 = r.nextInt(grid3.getContainerDataSource().size());

                grid.roll(row, 1000 + r.nextInt(1000));
                grid2.roll(row2, 2000 + r.nextInt(1000));
                grid3.roll(row3, 3000 + r.nextInt(1000));
            }

        });
        start.addStyleName(ValoTheme.BUTTON_HUGE);
        start.addStyleName(ValoTheme.BUTTON_PRIMARY);
        start.addStyleName("start");
        start.setDisableOnClick(true);
        layout.addComponents(reels, start);
        layout.setComponentAlignment(reels, Alignment.TOP_CENTER);
        start.setWidth("100%");
    }

    private void updateStatus() {
        statusDisplay.setValue("COINS " + coins + "<span>"
                + (status != null ? status : "") + "</span>");
    }

}
