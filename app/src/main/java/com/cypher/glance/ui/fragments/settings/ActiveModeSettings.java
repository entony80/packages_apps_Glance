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
package com.cypher.glance.ui.fragments.settings;

import android.os.Bundle;

import com.cypher.glance.Config;
import com.cypher.glance.R;

/**
 * Created by Artem on 09.02.14.
 */
public class ActiveModeSettings extends BaseSettings {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMasterSwitch(Config.KEY_ACTIVE_MODE);
        addPreferencesFromResource(R.xml.settings_active_fragment);
        syncPreference(Config.KEY_ACTIVE_MODE_RESPECT_INACTIVE_TIME);
        syncPreference(Config.KEY_ACTIVE_MODE_WITHOUT_NOTIFICATIONS);
        syncPreference(Config.KEY_ACTIVE_MODE_ACTIVE_CHARGING);
        syncPreference(Config.KEY_ACTIVE_MODE_DISABLE_ON_LOW_BATTERY);
        syncPreference(Config.KEY_ACTIVE_MODE_WAVE_TO_WAKE);
    }

}
