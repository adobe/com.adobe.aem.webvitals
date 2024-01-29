# WebVitals Support for Adobe Experience Manager 6.x

This bundle implements a Apache Sling Rewriter Transformer that provides support for best practice web development as detailed on Google Core WebVitals documentation. This bundle can be installed into AEM 6.1 to AEM 6.5. It must not be used in AEM as a Cloud Service.

The bundle applies optimisations to html emitted by an AEM instance and adds RUM to enable a customer to both optimise performance and monitor the impact. 

The aim is to ensure Google Core WebVitals scores are acceptable and aligned with current Adobe thinking and best practice,  as far as that is possible without customers rewriting their code bases in AEM.

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


# Version tests

* [x] AEM 6.0.0  Failed, needs JDK7
* [x] AEM 6.1.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.2.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.3.0  Ok with JDK8 wont start with JDK11.
* [x] AEM 6.4.0  Ok with JDK8 wont start with JDK11.
* [ ] AEM 6.5.0  TBD
* [x] AEM-CS SDK Works.

