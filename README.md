# WebVitals Support for AEM

This bundle implements a Sling Rewriter Transformer that provides support for best practice web development
as detailed on Google Core WebVitals documentation. The bundle applies optimisations to html emitted by an AEM instance
and adds RUM to enable a customer to both optimise performance and monitor the impact. 

The aim is to ensure Google Core WebVitals scores are acceptable and aligned with current Adobe thinking and best practice, 
as far as that is possible without customers rewriting their code bases in AEM.

* WebVitals is owned by Google, see https://web.dev/vitals/ for more details.
* RUM is a generic term, see https://en.wikipedia.org/wiki/Real_user_monitoring

# OSGi config

see src/main/java/com/adobe/aem/webvitals/WebVitalsConfig.java WebVitalsConfig.Config for details.


# Testing locally

1. Download AEM-CS SDK and unpack
2. startup an author (eg java -jar author-4502.jar)
5. Load the bundle after building `mvn clean install sling:install`
7. Create a site from a template e.g. from here https://github.com/adobe/aem-site-template-standard
8. edit the home page http://localhost:4502/editor.html/content/testsite/en/home.html
9. preview it http://localhost:4502/content/testsite/en/home.html
10. Inspect the page for rum scripts and the JS console for errors.

Rum probably won't record as the host will be localhost. For fuller testing add the bundle to running AEM instance.


# ToDo  (remove before Opensourcing below here)

* [x] Fix checkstyle to allow A2 license headers.
* [x] Decide what to do about OSGi style, currently will only work AEM 6.6 IIUC. Ideally should work back to AEM 6.0 with minimal code changes.
* [x] Test locally in a OOTB AEM 6.6
* [x] Identify Granite parent poms for previous AEM versions and create a version for each significant version.
* [ ] Confirm everything is good for moving to github, eg package name, dependencies.


https://github.com/apache/sling-org-apache-sling-rewriter/blame/master/src/main/java/org/apache/sling/rewriter/package-info.java#L20 indicates that
the rewriter first appeared 8 years ago.

## In AEM

                
                Symbolic Name	com.adobe.aem.webvitals
                Version	1.0.0.SNAPSHOT
                Bundle Location	inputstream:com.adobe.aem.webvitals-1.0.0-SNAPSHOT.jar
                Last Modification	Mon Jan 08 12:48:56 GMT 2024
                Bundle Documentation	https://www.adobe.com
                Vendor	Adobe Systems Incorporated
                Description	Injects WebVitals into the output.
                Start Level	20
                Exported Packages	---
                Imported Packages	org.apache.sling.api,version=2.3.4 from org.apache.sling.api (95)
                org.apache.sling.api.resource,version=2.13.0 from org.apache.sling.api (95)
                org.apache.sling.api.resource.path,version=1.2.2 from org.apache.sling.api (95)
                org.apache.sling.rewriter,version=1.0.0 from org.apache.sling.rewriter (576)
                org.slf4j,version=1.7.32 from slf4j.api (11)
                org.xml.sax,version=0.0.0.JavaSE_011 from org.apache.felix.framework (0)
                org.xml.sax.helpers,version=0.0.0.JavaSE_011 from org.apache.felix.framework (0)
                Service ID 6314	Types: com.adobe.aem.webvitals.impl.WebVitalsConfig
                Component Name: com.adobe.aem.webvitals.impl.WebVitalsConfig
                Component ID: 3686
                Service ID 6315	Types: org.apache.sling.rewriter.TransformerFactory
                Component Name: com.adobe.aem.webvitals.impl.WebVitalsTransformerFactory
                Component ID: 3687
                Manifest Headers	Build-Jdk-Spec: 11
                Bundle-Category: aem
                Bundle-Description: Injects WebVitals into the output.
                Bundle-Developers: AEM; name="Adobe Experience Manager"
                Bundle-DocURL: https://www.adobe.com
                Bundle-License: "Apache License, Version 2.0"; link="https://www.apache.org/licenses/LICENSE-2.0.txt"
                Bundle-ManifestVersion: 2
                Bundle-Name: Adobe WebVitals Script Injector
                Bundle-SCM: url="https://github.com/adobe/aem-webvitals/tree/main/", connection="scm:git:https://github.com/adobe/aem-webvitals.git", developer-connection="scm:git:git@github.com/adobe/aem-webvitals.git", tag=HEAD
                Bundle-SymbolicName: com.adobe.aem.webvitals
                Bundle-Vendor: Adobe Systems Incorporated
                Bundle-Version: 1.0.0.SNAPSHOT
                Created-By: Maven JAR Plugin 3.2.2
                Implementation-Title: Adobe WebVitals Script Injector
                Implementation-Vendor: The Apache Software Foundation
                Implementation-Version: 1.0.0-SNAPSHOT
                Import-Package: org.apache.sling.api; version="[2.3, 3)", org.apache.sling.api.resource; version="[2.11, 3)", org.apache.sling.api.resource.path; version="[1.2, 2)", org.apache.sling.rewriter; version="[1.0, 2)", org.slf4j; version="[1.7, 2)", org.xml.sax, org.xml.sax.helpers
                Manifest-Version: 1.0
                Private-Package: com.adobe.aem.webvitals.impl
                Provide-Capability: osgi.service; objectClass:List<String>="com.adobe.aem.webvitals.impl.WebVitalsConfig"; uses:="com.adobe.aem.webvitals.impl", osgi.service; objectClass:List<String>="org.apache.sling.rewriter.TransformerFactory"; uses:="org.apache.sling.rewriter"
                Require-Capability: osgi.service; filter:="(objectClass=com.adobe.aem.webvitals.impl.WebVitalsConfig)"; effective:=active, osgi.extender; filter:="(&(osgi.extender=osgi.component)(version>=1.4.0)(!(version>=2.0.0)))", osgi.ee; filter:="(&(osgi.ee=JavaSE)(version=1.8))"
                Service-Component: OSGI-INF/com.adobe.aem.webvitals.impl.WebVitalsConfig.xml, OSGI-INF/com.adobe.aem.webvitals.impl.WebVitalsTransformerFactory.xml
                Specification-Title: Adobe WebVitals Script Injector
                Specification-Vendor: The Apache Software Foundation
                Specification-Version: 1.0
                Provided Capabilities
                Capability "osgi.service" with attributes objectClass=[com.adobe.aem.webvitals.impl.WebVitalsConfig]
                Capability "osgi.service" with attributes objectClass=[org.apache.sling.rewriter.TransformerFactory]
                Declarative Service Components
                Component #3686 com.adobe.aem.webvitals.impl.WebVitalsConfig, state active
                Component #3687 com.adobe.aem.webvitals.impl.WebVitalsTransformerFactory, state active
                Required Capabilities
                Capability "osgi.extender" with directives filter=(&(osgi.extender=osgi.component)(version>=1.4.0)(!(version>=2.0.0))) (provided by org.apache.felix.scr (34))
                Capability "osgi.ee" with directives filter=(&(osgi.ee=JavaSE)(version=1.8)) (provided by org.apache.felix.framework (0))
                Used Services
                Service #649 of type(s) [org.apache.logging.log4j.spi.Provider]
                Service #6,314 of type(s) [com.adobe.aem.webvitals.impl.WebVitalsConfig]

# Version tests

* [x] AEM 6.0.0  Failed, needs JDK7
* [x] AEM 6.1.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.2.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.3.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.4.0  Ok with JDK8 wont start with JDK11.
* [ ] AEM 6.5.0  TBD
* [x] AEM-CS SDK Works.

