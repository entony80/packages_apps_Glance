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
package com.cypher.glance.ui.activities.settings;

import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;

import com.cypher.glance.R;
import com.cypher.glance.services.activemode.ActiveModeService;
import com.cypher.glance.ui.fragments.settings.ActiveModeSettings;
import com.cypher.glance.ui.fragments.settings.InterfaceSettings;
import com.cypher.glance.ui.fragments.settings.KeyguardSettings;
import com.cypher.glance.ui.fragments.settings.MoreSettings;
import com.cypher.glance.ui.fragments.settings.NotificationSettings;
import com.cypher.base.dashboard.DashboardTile;
import com.cypher.base.ui.activities.SettingsActivity;

import static com.cypher.base.Build.DEBUG;

/**
 * Created by Artem Chepurnoy on 02.01.2015.
 */
public class Settings2 extends SettingsActivity {

    private static final String[] ENTRY_FRAGMENTS = {
            KeyguardSettings.class.getName(),
            ActiveModeSettings.class.getName(),
            NotificationSettings.class.getName(),
            InterfaceSettings.class.getName(),
            MoreSettings.class.getName()
    };

    @Override
    protected boolean isValidFragment(@NonNull String fragmentName) {
        // Almost all fragments are wrapped in this,
        // except for a few that have their own activities.
        for (String fragment : ENTRY_FRAGMENTS)
            if (fragment.equals(fragmentName))
                return true;
        return false;
    }

    @XmlRes
    @Override
    public int getDashboardResource() {
        return R.xml.settings_dashboard;
    }

    @Override
    protected boolean isTileSupported(@NonNull DashboardTile tile) {
        switch ((int) tile.id) {
            case R.id.dev_settings:
                return DEBUG;
            case R.id.active_settings:
                return ActiveModeService.isSupported(this);
        }
        return true;
    }
}
