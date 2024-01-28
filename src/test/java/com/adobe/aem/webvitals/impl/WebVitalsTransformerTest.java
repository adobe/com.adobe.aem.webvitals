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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Serializer;
import org.apache.sling.rewriter.impl.components.Html5Serializer;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WebVitalsTransformerTest {

    @Test public void testInsertScript() throws IOException, SAXException {
        final WebVitalsConfig config = new WebVitalsConfig();
        final Map<String, Object> map = new HashMap<>();
        map.put("includedPaths", new String[] {"/content/a"});
        map.put("rumScriptUrl", "/rumscript.js");
        map.put("rumAPIPath", "/rumcollect/");
        config.activate(map);

        final WebVitalsTransformer t = new WebVitalsTransformer(config);

        final ProcessingContext ctx = Mockito.mock(ProcessingContext.class);
        final SlingHttpServletRequest req = Mockito.mock(SlingHttpServletRequest.class);
        final Resource rsrc = Mockito.mock(Resource.class);
        Mockito.when(req.getResource()).thenReturn(rsrc);
        Mockito.when(rsrc.getPath()).thenReturn("/content/a");
        Mockito.when(ctx.getRequest()).thenReturn(req);
        final StringWriter writer = new StringWriter();
        Mockito.when(ctx.getWriter()).thenReturn(new PrintWriter(writer));

        t.init(ctx, null);
        final Serializer handler = new Html5Serializer();
        handler.init(ctx, null);
        t.setContentHandler(handler);

        t.startDocument();
        t.startElement("", "html", "html", new AttributesImpl());
        t.startElement("", "head", "head", new AttributesImpl());
        t.endElement("", "head", "head");
        t.startElement("", "body", "body", new AttributesImpl());
        t.endElement("", "body", "body");
        t.endElement("", "html", "html");
        t.endDocument();

        final String expected = "<!DOCTYPE html>\n" +
            "<html><head><script type=\"module\">window.RUM_BASE = '/rumcollect/';\n" +
            "import { sampleRUM } from '/rumscript.js';\n" +
            "sampleRUM('top');\n" +
            "window.addEventListener('load', () => sampleRUM('load'));\n" +
            "document.addEventListener('click', () => sampleRUM('click'));\n" +
            "</script></head><body><script type=\"module\">window.RUM_BASE = '/rumcollect/';\n" +
            "import { sampleRUM } from '/rumscript.js';\n" +
            "sampleRUM('lazy');\n" +
            "sampleRUM('cwv');\n" +
            "</script></body></html>";
        assertEquals(expected, writer.toString());
    }

    @Test public void testDoNotInsertScript() throws IOException, SAXException {
        final WebVitalsConfig config = new WebVitalsConfig();
        final Map<String, Object> map = new HashMap<>();
        map.put("includedPaths", new String[] {"/content/foo"});
        map.put("rumScriptUrl", "/rumscript.js");
        map.put("rumAPIPath", "/rumcollect/");
        config.activate(map);

        final WebVitalsTransformer t = new WebVitalsTransformer(config);

        final ProcessingContext ctx = Mockito.mock(ProcessingContext.class);
        final SlingHttpServletRequest req = Mockito.mock(SlingHttpServletRequest.class);
        final Resource rsrc = Mockito.mock(Resource.class);
        Mockito.when(req.getResource()).thenReturn(rsrc);
        Mockito.when(rsrc.getPath()).thenReturn("/content/a");
        Mockito.when(ctx.getRequest()).thenReturn(req);
        final StringWriter writer = new StringWriter();
        Mockito.when(ctx.getWriter()).thenReturn(new PrintWriter(writer));

        t.init(ctx, null);
        final Serializer handler = new Html5Serializer();
        handler.init(ctx, null);
        t.setContentHandler(handler);

        t.startDocument();
        t.startElement("", "html", "html", new AttributesImpl());
        t.startElement("", "head", "head", new AttributesImpl());
        t.endElement("", "head", "head");
        t.startElement("", "body", "body", new AttributesImpl());
        t.endElement("", "body", "body");
        t.endElement("", "html", "html");
        t.endDocument();

        final String expected = "<!DOCTYPE html>\n" +
            "<html><head></head><body></body></html>";
        assertEquals(expected, writer.toString());
    }
}