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

import java.io.IOException;

import org.apache.sling.rewriter.DefaultTransformer;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WebVitalsTransformer extends DefaultTransformer {

    public static final String ELEMENT_BODY = "body";
    public static final String ELEMENT_HEAD = "head";
    public static final String ELEMENT_SCRIPT = "script";

    private static final AttributesImpl SCRIPT_ATTRIBUTES = new AttributesImpl();
    static {
        SCRIPT_ATTRIBUTES.addAttribute("","type", "", "string", "module");
    }
    
    private int inBody = 0;
    private int inHead = 0;

    // can be null.
    private final WebVitalsConfig config;

    private boolean webVitalsEnabled = true;

    public WebVitalsTransformer(final WebVitalsConfig config) {
        this.config = config;
    }

    public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
        super.init(context, config);
        this.webVitalsEnabled = (this.config != null)
                && this.config.isWebVitalsEnabledAtPath(context.getRequest().getResource().getPath());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ( webVitalsEnabled ) {
            if (ELEMENT_HEAD.equals(localName)) {
                inHead++;
            }
            if (ELEMENT_BODY.equals(localName)) {
                inBody++;
            }
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ( webVitalsEnabled ) {
            if (ELEMENT_HEAD.equals(localName)) {
                inHead--;
                if (inHead == 0) {
                    addStartRUMMonitor();
                }

            }
            if (ELEMENT_BODY.equals(localName)) {
                inBody--;
                if (inBody == 0) {
                    addEndRUMMonitor();
                }
            }
        }
        super.endElement(uri, localName, qName);
    }

    public void addStartRUMMonitor() throws SAXException {
        if ( this.config == null ) { // to be safe.
            return;
        }
        // could convert to the inline script, but these are problematic if the customer 
        // has CSP nonces enabled. Nonces could be captured from earlier inline.
        // hashes are more of a problem.
        super.startElement("", ELEMENT_SCRIPT, "", SCRIPT_ATTRIBUTES);
        final char[] output = config.getHeadSnippet();
        super.characters(output, 0, output.length );
        super.endElement("", ELEMENT_SCRIPT, "");
    }

    public void addEndRUMMonitor() throws SAXException {
        if ( this.config == null ) { // to be safe.
            return;
        }
        super.startElement("", ELEMENT_SCRIPT, "", SCRIPT_ATTRIBUTES);
        final char[] output = config.getBodySnippet();
        super.characters(output, 0, output.length );
        super.endElement("", ELEMENT_SCRIPT, "");
    }
}
