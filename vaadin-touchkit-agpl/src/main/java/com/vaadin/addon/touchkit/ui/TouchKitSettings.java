package com.vaadin.addon.touchkit.ui;

import java.io.IOException;
import java.util.Collection;

import com.vaadin.Application;
import com.vaadin.addon.touchkit.rootextensions.ApplicationIcons;
import com.vaadin.addon.touchkit.rootextensions.IosWebAppSettings;
import com.vaadin.addon.touchkit.rootextensions.OfflineModeSettings;
import com.vaadin.addon.touchkit.rootextensions.ViewPortSettings;
import com.vaadin.terminal.Extension;
import com.vaadin.terminal.RequestHandler;
import com.vaadin.terminal.WrappedRequest;
import com.vaadin.terminal.WrappedResponse;
import com.vaadin.terminal.gwt.server.BootstrapFragmentResponse;
import com.vaadin.terminal.gwt.server.BootstrapListener;
import com.vaadin.terminal.gwt.server.BootstrapPageResponse;
import com.vaadin.ui.UI;

public class TouchKitSettings implements RequestHandler, BootstrapListener {

    private ViewPortSettings viewPortSettings;
    private IosWebAppSettings iosWebAppSettings;
    private ApplicationIcons applicationIcons;
    private OfflineModeSettings offlineModeSettings;
    
    public ViewPortSettings getViewPortSettings() {
        return viewPortSettings;
    }
    
    public IosWebAppSettings getIosWebAppSettings() {
        return iosWebAppSettings;
    }
    
    public ApplicationIcons getApplicationIcons() {
        return applicationIcons;
    }
    
    public OfflineModeSettings getOfflineModeSettings() {
        return offlineModeSettings;
    }

    private TouchKitSettings(Application app) {
        viewPortSettings = new ViewPortSettings();
        iosWebAppSettings = new IosWebAppSettings();
        applicationIcons = new ApplicationIcons();
        offlineModeSettings = new OfflineModeSettings();

        app.addBootstrapListener(this);
        // no getter for bootstraplisteners so implementing requesthandler to
        // connect settings to app instance
        app.addRequestHandler(this);
    }

    public TouchKitSettings(UI root) {
        Collection<Extension> extensions = root.getExtensions();
        for (Extension extension : extensions) {
            if (extension instanceof ViewPortSettings) {
                viewPortSettings = (ViewPortSettings) extension;
                
            } else if (extension instanceof IosWebAppSettings) {
                iosWebAppSettings = (IosWebAppSettings) extension;
            } else if (extension instanceof ApplicationIcons) {
               applicationIcons = (ApplicationIcons) extension;
                
            } else if (extension instanceof OfflineModeSettings) {
                offlineModeSettings = (OfflineModeSettings) extension;
            }
        }
    }

    static TouchKitSettings init(Application app) {
        TouchKitSettings touchKitSettings = get(app);
        if (touchKitSettings == null) {
            return new TouchKitSettings(app);
        }
        return touchKitSettings;
    }
    
    public static TouchKitSettings init(UI root) {
        return null;
    }

    public static TouchKitSettings get(Application app) {
        Collection<RequestHandler> requestHandlers = app.getRequestHandlers();
        for (RequestHandler requestHandler : requestHandlers) {
            if (requestHandler instanceof TouchKitSettings) {
                return (TouchKitSettings) requestHandler;
            }
        }
        return null;
    }

    public static TouchKitSettings get() {
        return get(Application.getCurrent());
    }
    
    public static TouchKitSettings get(UI root) {
        return new TouchKitSettings(root);
    }

    @Override
    public boolean handleRequest(Application application,
            WrappedRequest request, WrappedResponse response)
            throws IOException {
        // NOP this is request handler just to attach this to application somehow
        return false;
    }

    @Override
    public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
        // NOP no support
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        UI root = response.getUI();
        if(root != null) {
            ensureInitialized(root);
            TouchKitSettings rootsettings = get(root);
            modifyBootstrap(rootsettings, response);
        } else {
            modifyBootstrap(this, response);
        }
    }
    
    private static void modifyBootstrap(TouchKitSettings settings, BootstrapPageResponse response) {
        settings.viewPortSettings.modifyBootstrapPage(response);
        settings.iosWebAppSettings.modifyBootstrapPage(response);
        settings.applicationIcons.modifyBootstrapPage(response);
        settings.offlineModeSettings.modifyBootstrapPage(response);
    }
    
    private static void ensureInitialized(UI root) {
        if(!isInitializedForTouchDevices(root)) {
            TouchKitSettings appSettings = get();
            appSettings.viewPortSettings.cloneAndExtend(root);
            appSettings.iosWebAppSettings.cloneAndExtend(root);
            appSettings.applicationIcons.cloneAndExtend(root);
            appSettings.offlineModeSettings.cloneAndExtend(root);
        }
    }

    static private boolean isInitializedForTouchDevices(UI r) {
        if(r == null) {
            return false;
        }
        for(Extension e : r.getExtensions()) {
            if(e instanceof ViewPortSettings) {
                return true;
            }
        }
        return false;
    }


}