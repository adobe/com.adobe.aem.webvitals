/*
 * Copyright 2024 Adobe Systems Incorporated
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adobe.aem.webvitals.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class WebVitalsConfigTest {

    public Map<String, Object> newDefaultConfig() {
        Map<String, Object> c = new HashMap<>();
        c.put("includedPaths", new String[] {"/content"});
        c.put("excludedPaths", new String[] {});
        c.put("rumScriptUrl", "https://rum.hlx.page/.rum/@adobe/helix-rum-js@^1/src/index.js");
        c.put("rumAPIPath", "https://rum.hlx.page/");
        return c;
    }

    @Test public void testConfigEnable() {
        WebVitalsConfig config = new WebVitalsConfig();
        config.activate(newDefaultConfig());
        assertTrue(config.isWebVitalsEnabledAtPath("/content/a"));
    }

    @Test public void testConfigEmptyExclude() {
        Map<String, Object> c = newDefaultConfig();
        c.put("excludedPaths", new String[] {""});
        WebVitalsConfig config = new WebVitalsConfig();
        config.activate(c);
        assertTrue(config.isWebVitalsEnabledAtPath("/content/a"));
    }

    @Test public void testToList() {
        Map<String, Object> c = newDefaultConfig();
        WebVitalsConfig config = new WebVitalsConfig();
        config.activate(c);

        assertNull(config.toList("x", null));
        assertNull(config.toList("x", new String[] {}));
        assertNull(config.toList("x", new String[] {""}));
        assertNull(config.toList("x", new String[] {"", " "}));

        assertEquals(Arrays.asList("/a", "/b"), config.toList("x", new String[] {"/a", "/b"}));
        assertEquals(Arrays.asList("/b"), config.toList("x", new String[] {"a", "/b"}));
    }
}
