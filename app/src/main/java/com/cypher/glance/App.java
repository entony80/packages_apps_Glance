/*
 * Copyright (C) 2016 CypherOS
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.cypher.glance;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.Html;

import com.cypher.glance.blacklist.Blacklist;
import com.cypher.glance.notifications.NotificationHelper;
import com.cypher.glance.notifications.NotificationPresenter;
import com.cypher.glance.permissions.AccessManager;
import com.cypher.glance.services.KeyguardService;
import com.cypher.glance.services.SensorsDumpService;
import com.cypher.glance.services.activemode.ActiveModeService;
import com.cypher.base.AppHeap;
import com.cypher.base.content.ConfigBase;
import com.cypher.base.interfaces.IConfiguration;
import com.cypher.base.permissions.Permission;
import com.cypher.base.permissions.PermissionGroup;
import com.cypher.base.utils.RawReader;
import com.cypher.base.utils.smiley.SmileyParser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Artem on 22.02.14.
 */
public class App extends Application {

    private static final String TAG = "App";

    public static final int ACCENT_COLOR = 0xFF607D8B;

    public static final int ID_NOTIFY_INIT = 30;
    public static final int ID_NOTIFY_TEST = 40;
    public static final int ID_NOTIFY_BATH = 50;
    public static final int ID_NOTIFY_APP_AUTO_DISABLED = 60;

    public static final String ACTION_BIND_MEDIA_CONTROL_SERVICE = "com.cypher.glance.BIND_MEDIA_CONTROL_SERVICE";

    public static final String ACTION_ENABLE = "com.cypher.glance.ENABLE";
    public static final String ACTION_DISABLE = "com.cypher.glance.DISABLE";
    public static final String ACTION_TOGGLE = "com.cypher.glance.TOGGLE";
    public static final String ACTION_ACTIVE_MODE_ENABLE = "com.cypher.glance.ACTIVE_MODE_ENABLE";
    public static final String ACTION_ACTIVE_MODE_DISABLE = "com.cypher.glance.ACTIVE_MODE_DISABLE";
    public static final String ACTION_ACTIVE_MODE_TOGGLE = "com.cypher.glance.ACTIVE_MODE_TOGGLE";

    public static final String ACTION_STATE_START = "com.cypher.glance.STATE_START";
    public static final String ACTION_STATE_RESUME = "com.cypher.glance.STATE_RESUME";
    public static final String ACTION_STATE_PAUSE = "com.cypher.glance.STATE_PAUSE";
    public static final String ACTION_STATE_STOP = "com.cypher.glance.STATE_STOP";

    public static final String ACTION_EAT_HOME_PRESS_START = "com.cypher.glance.EAT_HOME_PRESS_START";
    public static final String ACTION_EAT_HOME_PRESS_STOP = "com.cypher.glance.EAT_HOME_PRESS_STOP";

    public static final String ACTION_INTERNAL_TIMEOUT = "TIMEOUT";
    public static final String ACTION_INTERNAL_PING_SENSORS = "PING_SENSORS";

    private AccessManager mAccessManager;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private static App instance;

    public App() {
        instance = this;
    }

    @Override
    public void onCreate() {
        mAccessManager = new AccessManager(this);

        AppHeap.getInstance().init(this, new IConfiguration() {

            @NonNull
            private final IPermissions permissions = new IPermissions() {
                @Override
                public void onBuildPermissions(@NonNull Set<String> list) {
                }
            };

            @NonNull
            private final IBilling billing = new IBilling() {

                @NonNull
                @Override
                public List<String> getProducts() {
                    return Arrays.asList(
                            "donation_1",
                            "donation_4",
                            "donation_10",
                            "donation_20",
                            "donation_50",
                            "donation_99");
                }

                @Override
                public boolean hasAlternativePaymentMethods() {
                    if (Config.getInstance().getTriggers().getLaunchCount() > 500) {
                        // Always show the addition options to an active
                        // user. Hope to not get ban for this... :/
                        return true;
                    }

                    final Resources res = AppHeap.getContext().getResources();
                    return res.getBoolean(R.bool.config_alternative_payments);
                }
            };

            @NonNull
            private final IHelp help = new IHelp() {
                @NonNull
                @Override
                public CharSequence getText(@NonNull Context context) {
                    final String source = RawReader.readText(context, R.raw.faq);
                    return Html.fromHtml(source);
                }

                @Override
                public void onUserReadHelp() {
                    final Context context = instance;
                    Config.getInstance().getTriggers().setHelpRead(context, true, null);
                }
            };

            @NonNull
            @Override
            public IBilling getBilling() {
                return billing;
            }

            @NonNull
            @Override
            public IHelp getHelp() {
                return help;
            }

            @NonNull
            @Override
            public IPermissions getPermissions() {
                return permissions;
            }
        });
        Config.getInstance().init(this);
        Blacklist.getInstance().init(this);
        SmileyParser.init(this);

        // Init the main notification listener.
        NotificationPresenter.getInstance().setOnNotificationPostedListener(
                Config.getInstance().isEnabled()
                        ? Presenter.getInstance()
                        : null);

        super.onCreate();

        // Check the main switch.
        String divider = getString(R.string.settings_multi_list_divider);
        Config config = Config.getInstance();
        if (config.isEnabled()) {
            StringBuilder sb = new StringBuilder();
            boolean foundAny = false;

            PermissionGroup pg = getAccessManager().getMasterPermissions();
            for (Permission permission : pg.permissions) {
                if (!permission.isGranted()) {
                    if (foundAny) {
                        sb.append(divider);
                    } else foundAny = true;
                    sb.append(getString(permission.getTitleResource()));
                }
            }

            if (foundAny) {
                String list = sb.toString();
                list = list.charAt(0) + list.substring(1).toLowerCase();

                ConfigBase.Option option = config.getOption(Config.KEY_ENABLED);
                option.write(config, this, false, null);

                NotificationHelper.sendNotification(this, App.ID_NOTIFY_APP_AUTO_DISABLED, list);
            }
        }

        // Launch keyguard and (or) active mode on
        // app launch.
        KeyguardService.handleState(this);
        ActiveModeService.handleState(this);
        SensorsDumpService.handleState(this);
    }

    @Override
    public void onLowMemory() {
        Config.getInstance().onLowMemory();
        Blacklist.getInstance().onLowMemory();
        NotificationPresenter.getInstance().onLowMemory();
        mAccessManager.onLowMemory();
        super.onLowMemory();
    }

    @NonNull
    public static AccessManager getAccessManager() {
        assert instance.mAccessManager != null;
        return instance.mAccessManager;
    }

}
