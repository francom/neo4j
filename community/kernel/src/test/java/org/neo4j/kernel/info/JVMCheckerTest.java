/**
 * Copyright (c) 2002-2013 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.info;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.neo4j.kernel.info.JvmChecker.INCOMPATIBLE_JVM_VERSION_WARNING;
import static org.neo4j.kernel.info.JvmChecker.INCOMPATIBLE_JVM_WARNING;

import org.junit.Test;
import org.neo4j.kernel.logging.BufferingLogger;

public class JVMCheckerTest
{
    @Test
    public void shouldNotIssueWarningWhenUsingHotspotServerVmVersion6() throws Exception
    {
        BufferingLogger bufferingLogger = new BufferingLogger();

        new JvmChecker( bufferingLogger, new CannedJvmMetadataRepository( "Java HotSpot(TM) 64-Bit Server VM",
                "1.6.0_37" ) ).checkJvmCompatibilityAndIssueWarning();

        assertTrue( bufferingLogger.toString().isEmpty() );
    }

    @Test
    public void shouldNotIssueWarningWhenUsingHotspotClientVmVersion6() throws Exception
    {
        BufferingLogger bufferingLogger = new BufferingLogger();

        new JvmChecker( bufferingLogger, new CannedJvmMetadataRepository( "Java HotSpot(TM) Client VM",
                "1.6.42_87" ) ).checkJvmCompatibilityAndIssueWarning();

        assertTrue( bufferingLogger.toString().isEmpty() );
    }

    @Test
    public void shouldIssueWarningWhenUsingUnsupportedJvm() throws Exception
    {
        BufferingLogger bufferingLogger = new BufferingLogger();

        new JvmChecker( bufferingLogger, new CannedJvmMetadataRepository( "OpenJDK 64-Bit Server VM",
                "1.6.0_24" ) ).checkJvmCompatibilityAndIssueWarning();

        assertThat( bufferingLogger.toString().trim(), is( INCOMPATIBLE_JVM_WARNING ) );
    }

    @Test
    public void shouldIssueWarningWhenUsingUnsupportedJvmVersion() throws Exception
    {
        BufferingLogger bufferingLogger = new BufferingLogger();

        new JvmChecker( bufferingLogger, new CannedJvmMetadataRepository( "Java HotSpot(TM) 64-Bit Server VM",
                "1.7.0_05" ) ).checkJvmCompatibilityAndIssueWarning();

        assertThat( bufferingLogger.toString().trim(), is( INCOMPATIBLE_JVM_VERSION_WARNING ) );
    }
}
