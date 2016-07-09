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
package com.cypher.glance.suite;

import com.cypher.glance.FileUtilsInstTest;
import com.cypher.glance.GzipUtilsInstTest;
import com.cypher.glance.SharedListInstTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all Junit3 and Junit4 Instrumentation tests.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({FileUtilsInstTest.class, GzipUtilsInstTest.class, SharedListInstTest.class})
public class InstrumentationTestSuite {
}
