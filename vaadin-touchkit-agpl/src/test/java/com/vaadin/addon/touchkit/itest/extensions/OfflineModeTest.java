package com.vaadin.addon.touchkit.itest.extensions;

import com.vaadin.addon.touchkit.AbstractTouchKitIntegrationTest;
import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class OfflineModeTest extends AbstractTouchKitIntegrationTest {
    public OfflineModeTest() {
    }

    @Override
    public void attach() {
        super.attach();
        final OfflineModeSettings offlineModeSettings = new OfflineModeSettings();
        offlineModeSettings.extend(getUI());

        setDescription("Test Offline mode");

        Button detectButton = new Button("Open offline app",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        offlineModeSettings.goOffline();
                    }
                });
        addComponent(detectButton);
    }

}