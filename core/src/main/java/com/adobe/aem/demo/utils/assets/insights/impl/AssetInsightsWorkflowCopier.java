package com.adobe.aem.demo.utils.assets.insights.impl;

import com.adobe.aem.demo.utils.impl.WorkflowModelCopier;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.Collections;
import java.util.Map;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AssetInsightsWorkflowCopier {
    private static final Logger log = LoggerFactory.getLogger(AssetInsightsWorkflowCopier.class);
    private static final String SERVICE_ACCOUNT_IDENTIFIER = "aem-demo-utils";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Activate
    protected void activate() {
        final Map<String, Object> authInfo = Collections.singletonMap(
                ResourceResolverFactory.SUBSERVICE,
                SERVICE_ACCOUNT_IDENTIFIER);

        // Get the auto-closing Service resource resolver
        try (ResourceResolver serviceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo)) {
            if (serviceResolver != null) {
                // Do some work w your service resource resolver
                Resource source = serviceResolver.getResource("/apps/demo-utils/resources/asset-insights/generate-asset-insights");
                WorkflowModelCopier.copyToVarWorkflows("Generate Asset Insights", source);
                serviceResolver.commit();
            } else {
                log.error("Could find the service user to copy Demo Utils - Assets Insights Workflow Model to the runtime location");
            }
        } catch (LoginException | RepositoryException | PersistenceException e) {
            log.error("Could not copy Demo Utils - Assets Insights Workflow Model to the runtime location", e);
        }
    }
}
