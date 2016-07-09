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

import android.os.Bundle;

import com.cypher.glance.R;
import com.cypher.glance.ui.activities.base.BaseActivity;

/**
 * An activity for tweaking the
 * {@link com.cypher.glance.services.activemode.sensors.ProximitySensor proximity sensor}.
 *
 * @author Artem Chepurnoy
 */
public class SettingsProximitySensorActivity extends BaseActivity {
    private static final String TAG = "SettingsProximitySensorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_proximity_sensor_programs);
    }

}
