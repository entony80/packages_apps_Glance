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
package com.cypher.glance.permissions;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.cypher.glance.R;
import com.cypher.base.Device;
import com.cypher.base.permissions.Permission;
import com.cypher.base.tests.Check;
import com.cypher.base.utils.PackageUtils;

/**
 * The Usage stats permission.
 *
 * @author Artem Chepurnoy
 * @see android.app.usage.UsageStatsManager
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public final class PermissionUsageStats extends Permission {

    @NonNull
    private final PackageManager mPackageManager;

    public PermissionUsageStats(@NonNull Context context) {
        super(context);
        Check.getInstance().isTrue(Device.hasLollipopApi());
        mPackageManager = mContext.getPackageManager();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isGranted() {
        final String packageName = PackageUtils.getName(mContext);
        final int uid;
        try {
            uid = mPackageManager.getApplicationInfo(packageName, 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        int result = getAppOpsManager().checkOp(AppOpsManager.OPSTR_GET_USAGE_STATS, uid, packageName);
        return result == AppOpsManager.MODE_ALLOWED;
    }

    @NonNull
    private AppOpsManager getAppOpsManager() {
        return (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Intent getIntentSettings() {
        return new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    }

    //-- UI -------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @DrawableRes
    public int getIconResource() {
        return R.drawable.ic_settings_apps_white;
    }

    /**
     * {@inheritDoc}
     */
    @StringRes
    public int getTitleResource() {
        return R.string.permissions_usage_stats;
    }

    /**
     * {@inheritDoc}
     */
    @StringRes
    public int getSummaryResource() {
        return R.string.permissions_usage_stats_description;
    }

    /**
     * {@inheritDoc}
     */
    @StringRes
    public int getErrorResource() {
        return R.string.permissions_usage_stats_error;
    }

}
