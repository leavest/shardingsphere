/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.mode.manager;

import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.infra.config.database.DatabaseConfiguration;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ContextManagerBuilderParameterTest {

    @Test
    public void assertIsEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(true, true);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(new Properties())
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertTrue(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenAllFieldIsEmpty() {
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(new Properties())
                .databaseConfigs(Collections.emptyMap())
                .build();
        assertTrue(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertIsEmptyWhenAllFieldIsNotEmpty() {
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.isEmpty()).thenReturn(false);
        Collection<RuleConfiguration> mockGlobalRuleConfigs = mock(Collection.class);
        lenient().when(mockGlobalRuleConfigs.isEmpty()).thenReturn(false);
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(false, false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(mockGlobalRuleConfigs)
                .props(mockProperties)
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenPropsIsNotEmpty() {
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.isEmpty()).thenReturn(false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(mockProperties)
                .databaseConfigs(Collections.emptyMap())
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertIsEmptyWhenGlobalRuleConfigsIsNotEmpty() {
        Collection<RuleConfiguration> mockGlobalRuleConfigs = mock(Collection.class);
        when(mockGlobalRuleConfigs.isEmpty()).thenReturn(false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(mockGlobalRuleConfigs)
                .props(new Properties())
                .databaseConfigs(Collections.emptyMap())
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenSchemaConfigsIsNotEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(false, false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(new Properties())
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenDataSourcesIsNotEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(false, true);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(new Properties())
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenRuleConfigurationsIsNotEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(true, false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(new Properties())
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertIsEmptyWhenOnlyPropsIsEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(true, true);
        Collection<RuleConfiguration> mockGlobalRuleConfigs = mock(Collection.class);
        lenient().when(mockGlobalRuleConfigs.isEmpty()).thenReturn(false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(mockGlobalRuleConfigs)
                .props(new Properties())
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    public void assertIsEmptyWhenOnlyGlobalRuleConfigsIsEmpty() {
        Map<String, DatabaseConfiguration> mockDatabaseConfigs = getMockSchemaConfiguration(true, true);
        Properties mockProperties = mock(Properties.class);
        lenient().when(mockProperties.isEmpty()).thenReturn(false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(Collections.emptyList())
                .props(mockProperties)
                .databaseConfigs(mockDatabaseConfigs)
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void assertIsEmptyWhenOnlySchemaConfigsIsEmpty() {
        Properties mockProperties = mock(Properties.class);
        Collection<RuleConfiguration> mockGlobalRuleConfigs = mock(Collection.class);
        lenient().when(mockGlobalRuleConfigs.isEmpty()).thenReturn(false);
        lenient().when(mockProperties.isEmpty()).thenReturn(false);
        ContextManagerBuilderParameter contextManagerBuilderParameter = ContextManagerBuilderParameter.builder()
                .globalRuleConfigs(mockGlobalRuleConfigs)
                .props(mockProperties)
                .databaseConfigs(Collections.emptyMap())
                .build();
        assertFalse(contextManagerBuilderParameter.isEmpty());
    }

    private Map<String, DatabaseConfiguration> getMockSchemaConfiguration(final boolean dataSourcesIsEmpty, final boolean ruleConfigurationsIsEmpty) {
        DatabaseConfiguration mockSchemaConfiguration = Mockito.mock(DatabaseConfiguration.class, RETURNS_DEEP_STUBS);
        lenient().when(mockSchemaConfiguration.getDataSources().isEmpty()).thenReturn(dataSourcesIsEmpty);
        lenient().when(mockSchemaConfiguration.getRuleConfigurations().isEmpty()).thenReturn(ruleConfigurationsIsEmpty);
        Map<String, DatabaseConfiguration> result = new HashMap<>();
        result.put("logic", mockSchemaConfiguration);
        return result;
    }
}
