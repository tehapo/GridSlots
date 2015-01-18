package com.tehapo;

import java.util.Random;

import javax.servlet.annotation.WebServlet;

import com.tehapo.Reel.RollCompletedEvent;
import com.tehapo.Reel.RollCompletedListener;
import com.tehapo.model.Payouts;
import com.tehapo.model.RollResult;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction;
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
public class GridSlotsUI extends UI implements RollCompletedListener {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GridSlotsUI.class)
    public static class Servlet extends VaadinServlet {
    }

    // UI
    private SoundFx soundFx;
    private Label statusDisplay;
    private Button start;
    private Reel reel1;
    private Reel reel2;
    private Reel reel3;

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
        reel1 = new Reel();
        reel2 = new Reel();
        reel3 = new Reel();
        reel3.addRollCompletedListener(this);
        reels.addComponents(reel1, reel2, reel3);

        start = new Button("Roll", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                start.setEnabled(false);
                roll();
            }

        });
        start.setClickShortcut(ShortcutAction.KeyCode.SPACEBAR);
        start.addStyleName(ValoTheme.BUTTON_HUGE);
        start.addStyleName(ValoTheme.BUTTON_PRIMARY);
        start.addStyleName("start");
        start.setDisableOnClick(true);
        layout.addComponents(reels, start);
        layout.setComponentAlignment(reels, Alignment.TOP_CENTER);
        start.setWidth("100%");
    }

    @Override
    public void rollCompleted(RollCompletedEvent event) {
        RollResult result = new RollResult(reel1.getValue(), reel2.getValue(),
                reel3.getValue());
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

    private void roll() {
        coins--;
        updateStatus();
        statusDisplay.removeStyleName("blink");

        final Random r = new Random();
        int row = r.nextInt(reel1.getContainerDataSource().size());
        int row2 = r.nextInt(reel2.getContainerDataSource().size());
        int row3 = r.nextInt(reel3.getContainerDataSource().size());

        reel1.roll(row, 1000 + r.nextInt(1000));
        reel2.roll(row2, 2000 + r.nextInt(1000));
        reel3.roll(row3, 3000 + r.nextInt(1000));
    }

    private void updateStatus() {
        statusDisplay.setValue("COINS " + coins + "<span>"
                + (status != null ? status : "") + "</span>");
    }

}
