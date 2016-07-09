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
package com.cypher.glance.notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import static com.cypher.glance.graphics.BackgroundFactory.generate;

/**
 * Simple single-thread background factory.
 *
 * @author Artem Chepurnoy
 * @see com.cypher.glance.graphics.BackgroundFactory
 * @see com.cypher.glance.notifications.IconFactory
 */
class BackgroundFactory extends IconFactory {

    public interface BackgroundAsyncListener extends IconAsyncListener {
        // Identical
    }

    @NonNull
    @Override
    protected Bitmap onGenerate(@NonNull Context context, @NonNull OpenNotification notification) {
        // The context is always null here!
        return generate(notification.getNotification().largeIcon);
    }

}
