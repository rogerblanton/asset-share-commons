package com.adobe.aem.demo.utils.impl;

import com.day.cq.commons.jcr.JcrUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

public class WorkflowModelCopier {
    public static Resource copyToVarWorkflows(final String title, final Resource sourceResource) throws RepositoryException, PersistenceException {
        ResourceResolver resourceResolver = sourceResource.getResourceResolver();

        Node node = JcrUtil.createPath("/var/workflow/models/demo/utils", "sling:Folder", resourceResolver.adaptTo(Session.class));

        Resource currentResource = resourceResolver.getResource("/var/workflow/models/demo/utils/" + sourceResource.getName());
        if (currentResource != null) {
            resourceResolver.delete(currentResource);
        }


        Resource destResource = resourceResolver.create(resourceResolver.getResource("/var/workflow/models/demo/utils"), sourceResource.getName(),
                ImmutableMap.<String, Object>builder().
                        put("jcr:primaryType", "cq:WorkflowModel").
                        put("title", title).
                        put("description", "Demo utils workflow model for " + title).
                        put("sling:resourceType", "cq/workflow/components/model")
                        .build());



        resourceResolver.copy(sourceResource.getPath() + "/metaData", destResource.getPath());
        resourceResolver.copy(sourceResource.getPath() + "/nodes", destResource.getPath());
        resourceResolver.copy(sourceResource.getPath() + "/transitions", destResource.getPath());

        return destResource;

    }
}
