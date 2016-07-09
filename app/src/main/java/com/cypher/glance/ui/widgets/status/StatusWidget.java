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
package com.cypher.glance.ui.widgets.status;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.cypher.glance.Config;
import com.cypher.glance.R;
import com.cypher.base.content.ConfigBase;
import com.cypher.base.utils.Operator;
import com.cypher.base.utils.ViewUtils;

public class StatusWidget extends LinearLayout implements
        ConfigBase.OnConfigChangedListener,
        BatteryMeterView.OnBatteryChangedListener {

    private BatteryMeterView mBatteryMeterView;

    public StatusWidget(Context context) {
        super(context);
    }

    public StatusWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mBatteryMeterView = (BatteryMeterView) findViewById(R.id.battery);
        mBatteryMeterView.setOnBatteryChangedListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isInEditMode()) {
            Config config = Config.getInstance();
            config.registerListener(this);
        }
        updateBatteryVisibility();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (!isInEditMode()) {
            Config config = Config.getInstance();
            config.unregisterListener(this);
        }
    }

    @Override
    public void onBatteryChanged(BatteryMeterView view, int event) {
        if (Operator.bitAnd(event, BatteryMeterView.EVENT_CHARGING)
                || Operator.bitAnd(event, BatteryMeterView.EVENT_LEVEL)) {
            updateBatteryVisibility();
        }
    }

    @Override
    public void onConfigChanged(@NonNull ConfigBase config,
                                @NonNull String key,
                                @NonNull Object value) {
        switch (key) {
            case Config.KEY_UI_STATUS_BATTERY_STICKY:
                updateBatteryVisibility();
                break;
        }
    }

    private void updateBatteryVisibility() {
        boolean visible = Config.getInstance().isStatusBatterySticky()
                || mBatteryMeterView.getBatteryCharging()
                || mBatteryMeterView.getBatteryLevel() < 15;
        ViewUtils.setVisible(mBatteryMeterView, visible);
    }
}
