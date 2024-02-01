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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adobe.aem.webvitals.impl.path.PathSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see src/main/resources/OSGI-INF/com.adobe.aem.webvitals.impl.WebVitalsConfig.xml for OSGi config.
// XML is being used to be forwards and backwards compatable.
public class WebVitalsConfig {

    private volatile PathSet includeSet;

    private volatile PathSet excludeSet;

    private volatile char[] headSnippet;
 
    private volatile char[] bodySnippet;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private void logStatus() {
        logger.info("WebVitals support enabled with includedPaths={}, excludedPaths={}",
            this.includeSet, this.excludeSet);
    }

    protected List<String> toList(final String mode, final Object listProperty) {
        final List<String> result = new ArrayList<>();
        if ( listProperty instanceof String ) {
            for(final String v : ((String) listProperty).split(",")) {
                if (v != null && v.trim().startsWith("/") ) {
                    result.add(v);
                } else {
                    logger.error("Ignoring invalid {} path : {}", mode, v);
                }
            }
        } else if ( listProperty instanceof String[] ) {
            for(final String v : (String[])listProperty) {
                if (v != null && v.trim().startsWith("/") ) {
                    result.add(v.trim());
                } else {
                    logger.error("Ignoring invalid {} path : {}", mode, v);
                }
            }
        }
        if ( result.size() == 0 ) {
            return null;
        } else {
            return result;
        }
    }

    /**
     * Implicit activator for OSGi component compliant with OSGi 4.1 compendium 112.5.6
     * @param properties the configuration properties
     */
    public void activate(final Map<String,Object> properties) {
        final List<String> i = toList("include", properties.get("includedPaths"));
        final List<String> e = toList("exclude", properties.get("excludedPaths"));
        this.includeSet = i == null ? null : PathSet.fromStringCollection(i);
        this.excludeSet = e == null  ? null : PathSet.fromStringCollection(e);
        this.update((String) properties.get("rumScriptUrl"), (String) properties.get("rumAPIPath"));
        this.logStatus();
    }

    private void update(final String rumScriptUrl, final String rumAPIPath) {
        // create the snippets to be injected
        final StringBuilder head = new StringBuilder();
        head.append("window.RUM_BASE = '").append(rumAPIPath).append("';\n");
        head.append("import { sampleRUM } from '").append(rumScriptUrl).append("';\n");
        head.append("window.hlx = window.hlx || {};\n");
        head.append("window.hlx.sampleRUM = sampleRUM;\n");
        head.append("sampleRUM('top');\n");
        head.append("window.addEventListener('load', () => sampleRUM('load'));\n");
        head.append("document.addEventListener('click', () => sampleRUM('click'));\n");

        final StringBuilder body = new StringBuilder();
        body.append("window.RUM_BASE = '").append(rumAPIPath).append("';\n");
        body.append("import { sampleRUM } from '").append(rumScriptUrl).append("';\n");
        body.append("sampleRUM('lazy');\n");
        body.append("sampleRUM('cwv');\n");

        // update the snippets
        this.headSnippet = head.toString().toCharArray();
        this.bodySnippet = body.toString().toCharArray();
    }

    // Methods used for checking runtime config.
    // These must all be thread-safe as they will be called by all threads.
    /**
     *
     * @param path the path of the response being transformed.
     * @return true if WebVitals is enabled at the path provided WebVitals is enabled.
     */
    public boolean isWebVitalsEnabledAtPath(final String path) {
        final boolean included = this.includeSet == null || this.includeSet.matches(path) != null;
        final boolean excluded = this.excludeSet != null && this.excludeSet.matches(path) != null;
        final boolean result = included && !excluded;
        logger.debug("WebVitals enabled at path {} : {}", path, result);
        return result;
    }

    public char[] getHeadSnippet() {
        return this.headSnippet;
    }

    public char[] getBodySnippet() {
        return this.bodySnippet;
    }
}
