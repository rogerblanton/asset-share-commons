package com.adobe.aem.demo.utils.impl;

import com.day.cq.commons.jcr.JcrUtil;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class WorkflowModelCopier {
    public static Resource copyToVarWorkflows(Resource sourceResource) throws RepositoryException, PersistenceException {
        ResourceResolver resourceResolver = sourceResource.getResourceResolver();

        Node node = JcrUtil.createPath("/var/workflow/models/demo/utils", "sling:Folder", resourceResolver.adaptTo(Session.class));

        Resource currentResource = resourceResolver.getResource("/var/workflow/models/demo/utils/" + sourceResource.getName());
        if (currentResource != null) {
            resourceResolver.delete(currentResource);
        }

        return resourceResolver.copy(sourceResource.getPath(), node.getPath());
    }
}
