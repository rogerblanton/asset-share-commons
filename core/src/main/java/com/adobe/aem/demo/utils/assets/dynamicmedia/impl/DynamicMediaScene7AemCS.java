package com.adobe.aem.demo.utils.assets.dynamicmedia.impl;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.cloudservices.CloudServiceCreator;
import com.adobe.aem.demo.utils.cloudservices.impl.AbstractCloudServiceCreator;
import com.adobe.aem.demo.utils.impl.CleanerUtil;
import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.principal.PrincipalManager;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.commons.jackrabbit.authorization.AccessControlUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.Queue;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.servlethelpers.MockRequestPathInfo;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = {Servlet.class, Executable.class},
        enabled = true,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/dynamic-media",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
@SuppressWarnings("squid:S2068")
public class DynamicMediaScene7AemCS extends AbstractCloudServiceCreator implements CloudServiceCreator, Executable {
    public static final String PAGE_PATH = "/conf/global/settings/cloudconfigs";
    public static final String PAGE_NAME = "dmscene7";
    public static final String TEMPLATE = "/libs/dam/templates/dmscene7";
    public static final String TITLE = "Dynamic Media (DMS7)";
    public static final String CONF_GLOBAL_DM = "/conf/global/settings/dam/dm";
    public static final String CONF_GLOBAL_IS_SETTINGS = CONF_GLOBAL_DM + "/imageserver";
    private static final String DEF_ICC_PATH = "/etc/dam/imageserver/profiles";
    private static final String DYNAMIC_MEDIA_REPLICATION = "dynamic-media-replication";
    private static Logger log = LoggerFactory.getLogger(DynamicMediaScene7.class);

    @Reference(target = "(sling.servlet.selectors=sampleassets)")
    private transient Servlet sampleAssetsServlet;

    @Reference
    private transient CryptoSupport cryptoSupport;

    @Reference
    private transient JobManager jobManager;

    private static void addToAdministrators(Session session) throws RepositoryException {
        if (!(session instanceof JackrabbitSession)) {
            throw new RepositoryException("The repository does not support dynamic_media_replication user");
        }
        JackrabbitSession jackrabbitSession = (JackrabbitSession) session;

        UserManager userManager = jackrabbitSession.getUserManager();
        Authorizable me = userManager.getAuthorizable(session.getUserID());
        Authorizable administrators = userManager.getAuthorizable("administrators");

        if (administrators != null && administrators.isGroup()) {
            ((Group) administrators).addMember(me);
        } else {
            log.warn("Could not find [ administrators ] user group to add user [ {} ]", session.getUserID());
        }

        session.save();
    }

    public static void addACLPermissions(Session session) throws RepositoryException {
        // 1. Get ACL controls
        AccessControlManager accessControlManager = session.getAccessControlManager();
        // acl user
        Principal dMRPrincipal = getDynamicMediaReplicationPrincipal(session);
        // 2. read permissions
        Privilege[] read = new Privilege[]{accessControlManager.privilegeFromName(Privilege.JCR_READ)};
        // dynamic-media-replication needs permissions on these folders
        // 3. dam/dm
        addDynamicMediaReplication(session, dMRPrincipal, accessControlManager, read, CONF_GLOBAL_DM);
        // 4. IS settings
        addDynamicMediaReplication(session, dMRPrincipal, accessControlManager, read, CONF_GLOBAL_IS_SETTINGS);
        // 5. /etc/dam/imageserver/profiles
        addDynamicMediaReplication(session, dMRPrincipal, accessControlManager, read, DEF_ICC_PATH);
    }

    private static void addDynamicMediaReplication(Session session, Principal dMRPrincipal, AccessControlManager accessControlManager, Privilege[] read, String path) throws RepositoryException {
        // 1. check if it exists
        if (session.nodeExists(path) == false) {
            return; // do nothing
        }
        JackrabbitAccessControlList jackrabbitAccessControlList = AccessControlUtils.getAccessControlList(session, path);
        for (AccessControlEntry entry : jackrabbitAccessControlList.getAccessControlEntries()) {
            if (dMRPrincipal.equals(entry.getPrincipal())) {
                return; // do nothing
            }
        }
        // 2. add permissions
        jackrabbitAccessControlList.addEntry(dMRPrincipal, read, true);
        // 3. save the permissions to code
        accessControlManager.setPolicy(path, jackrabbitAccessControlList);
        log.info("Configured acl permissions for dynamic-media-replication user at " + path);
    }

    /**
     * Get principal user from session
     */
    private static Principal getDynamicMediaReplicationPrincipal(Session session) throws RepositoryException {
        if (!(session instanceof JackrabbitSession)) {
            throw new RepositoryException("The repository does not support dynamic_media_replication user");
        }
        JackrabbitSession jackrabbitSession = (JackrabbitSession) session;
        PrincipalManager principalManager = jackrabbitSession.getPrincipalManager();
        return principalManager.getPrincipal(DYNAMIC_MEDIA_REPLICATION);
    }

    @Override
    public String getName() {
        return "dynamic-media-scene7";
    }

    private Map<String, String> getParams() throws CryptoException {
        final Map<String, String> params = new HashMap<>();

        // This is no longer recommended since S7 assets are id based.
        //final String uniquePath = "_aem-demo-utils/" + getSharedName() + "/";

        params.put("cq:cloudservicename", "dmscene7");
        params.put("sling:resourceType", "dam/components/scene7/dmscene7page");

        params.put("email", "dynamicmedia-na1@adobe.com");
        params.put("companyHandle", "c|230999");
        params.put("companyname", "DynamicmediaNA1");
        params.put("jcr:title", "Dynamic Media");

        params.put("password", "nC:6UA:o8");
        params.put("password@Encrypted", "true");
        params.put("previewServer", "https://preview1.assetsadobe.com");
        params.put("publishEnabled", "selective");
        params.put("publishServer", "http://s7d1.scene7.com");
        params.put("region", "northamerica-enterprise");
        params.put("rootPath", "DynamicmediaNA1/");
        params.put("s7RootPath", "DynamicmediaNA1/");
        params.put("syncEnabled", "on");
        params.put("targetPath", "/content/dam/");
        params.put("selective-sync", "off");
        params.put("userHandle", "u|149206|dynamicmedia-na1@adobe.com");

        return params;
    }

    /* Copied from DMS7 Code -- Not exported */

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            response.setContentType("text/html; charset=utf-8'");

            execute(request, response);
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not setup Dynamic Media Scene7", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final Map config = getParams();

        addToAdministrators(resourceResolver.adaptTo(Session.class));

        removeExisting(resourceResolver);

        JcrUtils.getOrCreateByPath("/conf/global/settings/cloudconfigs",
            JcrResourceConstants.NT_SLING_FOLDER,
            resourceResolver.adaptTo(Session.class));

        execute(resourceResolver,
                cryptoSupport,
                PAGE_PATH,
                PAGE_NAME,
                TEMPLATE,
                TITLE,
                config);

                
        resourceResolver.copy(
                "/apps/demo-utils/resources/dynamic-media-scene7/mimeTypes",
                PAGE_PATH + "/" + PAGE_NAME + "/" + JcrConstants.JCR_CONTENT);
        
                try {
                    resourceResolver.commit();

                    log.info("AEM Demo Utils - DMS7 - triggerSetupJob");
                     invokeOotbSampleAssetsServlet(resourceResolver, "triggerSetupJob");
                } catch (Exception e) {
                    log.error("Could not setup Demo Utils due to:", e);
                    throw new Exception("AEM Demo Utils - Could not setup DM");
                }
    }

    private void invokeOotbSampleAssetsServlet(ResourceResolver resourceResolver, String operationName) throws ServletException, IOException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.error("Could not sleep... oh well!", e);
        }

        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(resourceResolver);
        MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

        response.setCharacterEncoding("UTF-8");
        request.setMethod(HttpConstants.METHOD_POST);
        ((MockRequestPathInfo) request.getRequestPathInfo()).setSelectorString("sampleassets." + operationName);
        ((MockRequestPathInfo) request.getRequestPathInfo()).setExtension("json");

        try {
            sampleAssetsServlet.service(request, response);

            //log.info("AEM Demo Utils DMS7 progress status: " + response.getOutputAsString());

            if (response.getStatus() == 200) {
                log.info("AEM Demo Utils triggered off Dynamic Media set up.");
            } else if (response.getStatus() == 500) {
                throw new ServletException("Unable to have OOTB sampleassets servlet process " + operationName + " request");
            }
        } catch (Throwable e) {
            if (e instanceof ServletException) {
                throw e;
            } else {
                log.error("Mock response had problem with response buffer... ignore it.");
            }
        }
    }

    private void killJobs(ResourceResolver resourceResolver) {
        Queue s7queue = jobManager.getQueue("Scene7 Synchronization");
        if (s7queue != null) {
            s7queue.removeAll();
        }

        Queue transientWfQueue = jobManager.getQueue("Granite Transient Workflow Queue");
        if (transientWfQueue != null) {
            transientWfQueue.removeAll();
        }
    }

    public void setAccessControl(Session session, Node presetNode) {
        String userId = DYNAMIC_MEDIA_REPLICATION;
        try {
            UserManager userManager = ((JackrabbitSession) session).getUserManager();
            if (null != userManager) {
                Authorizable auth = userManager.getAuthorizable(userId);
                if (null != auth) {
                    Principal p = auth.getPrincipal();
                    AccessControlUtils.addAccessControlEntry(session, presetNode.getPath(), p,
                            new String[]{Privilege.JCR_READ}, true);
                }
            }
        } catch (RepositoryException e) {
            log.error("Error setting ACL, Preset may not publish.", e);
        }
    }

    private void removeExisting(ResourceResolver resourceResolver) throws PersistenceException {
        CleanerUtil.remove(resourceResolver, PAGE_PATH + "/" + PAGE_NAME);
        CleanerUtil.remove(resourceResolver, CONF_GLOBAL_DM);
        CleanerUtil.remove(resourceResolver, "/conf/global/settings/cloudconfigs/dmscene7");
        //CleanerUtil.remove(resourceResolver, "/content/dam/_CSS");
        //CleanerUtil.remove(resourceResolver, "/content/dam/_DMSAMPLE");

        killJobs(resourceResolver);
    }
}


    /*
    // Do not use since S7 uses filename and NOT path as unique ID

    private String getSharedName() {
        String computerName = null;
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME")) {
            computerName = StringUtils.stripToNull(env.get("COMPUTERNAME"));
        } else if (env.containsKey("HOSTNAME")) {
            computerName = StringUtils.stripToNull(env.get("HOSTNAME"));
        } else {
            Runtime rt = Runtime.getRuntime();
            try {
                Process pr = rt.exec("hostname");
                StringWriter writer = new StringWriter();
                IOUtils.copy(pr.getInputStream(), writer, "utf-8");
                computerName = StringUtils.stripToNull(writer.toString());
                pr.destroy();
            } catch (IOException e) {

            }

            if (StringUtils.stripToNull(computerName) == null) {
                computerName = String.valueOf(new java.util.Date().getTime());
            }
        }

        return computerName;
    }
    */

