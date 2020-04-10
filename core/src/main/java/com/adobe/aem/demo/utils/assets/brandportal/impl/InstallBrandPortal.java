package com.adobe.aem.demo.utils.assets.brandportal.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.*;
import com.adobe.granite.crypto.CryptoSupport;
import com.adobe.granite.keystore.KeyStoreNotInitialisedException;
import com.adobe.granite.keystore.KeyStoreService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Arrays;
import java.util.Map;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/brand-portal",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
@SuppressWarnings("squid:S2068")
public class InstallBrandPortal extends AbstractExecutable implements Executable {
    public static final String[] SERVICE_USER_IDS = { "brandportal-imsconfig-service",  "bp-imsconfig-service"};


    private static final String PATH_CERTS = "/apps/demo-utils/resources/brand-portal/";
    private static Logger log = LoggerFactory.getLogger(InstallBrandPortal.class);
    @Reference
    private transient KeyStoreService keyStoreService;

    @Reference
    private transient CryptoSupport cryptoSupport;

    @Reference
    private transient Packaging packaging;

    private final String[] ALLOWED_REGIONS = { "na1", "na2", "emea1", "emea2", "apac" };

    @Reference(target = "(sling.servlet.resourceTypes=cq/adobeims-configuration/components/admin/datasources/imsconfigurations/update)")
    private transient Servlet createImsConfigurationServlet;

    @Reference(target = "(sling.servlet.resourceTypes=cq/adobeims-configuration/components/admin/datasources/imsconfigurations/delete)")
    private transient Servlet deleteImsConfigurationsServlet;

    @Reference(target = "(sling.servlet.paths=/apps/brandportal/config)")
    private transient Servlet createCloudServiceServlet;

    //@Reference(target = "(sling.servlet.resourceTypes=cq/bp/components/admin/configurations/delete)")
    //private transient Servlet deleteCloudServiceServlet;

    @Override
    public String getName() {
        return "brand-portal";
    }

    @Override
    public void execute(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws Exception {
        doGet(request, response);
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        if (createCloudServiceServlet == null) {
            throw new ServletException("Brand Portal create cloud service cannot be found.");
        }

        if (createImsConfigurationServlet == null) {
            throw new ServletException("Brand Portal create IMS ConfigurationServlet cannot be found.");
        }

        if (deleteImsConfigurationsServlet == null) {
            throw new ServletException("Brand Portal delete IMS ConfigurationServlet cannot be found");
        }

        try {
            //removeImsTechnicalAccountConfigurations(request, response);
            CleanerUtil.removeChildren(request.getResourceResolver(), "/conf/global/settings/cloudconfigs/mediaportal");

            try {
                createKeyStore(request.getResourceResolver(), getRegion(request));
            } catch (Exception e) {
                log.error("Could not create keystore for Brand Portal integration", e);
                throw new ServletException(e);
            }


            String accessTokenConfigId =  createImsConfiguration(request, response);

            createCloudService(request, response, getRegion(request), accessTokenConfigId);

            /*
            permissionCloudService(request, response, accessTokenConfigId);
            */
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not create cloud service config for Brand Portal", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    public void execute(ResourceResolver resourceResolver, ValueMap params) throws Exception {

    }

    /* ************************************************************************************************************
       IMS CONFIGURATION
    * ************************************************************************************************************/

    private String createImsConfiguration(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        request.getResourceResolver().refresh();

        String region = getRegion(request);

        final Map<String, String[]> requestParams = getParameters(region);

        SlingHttpServletRequest wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createImsConfigurationServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200 && StringUtils.isNotBlank(wrappedResponse.getString())) {
            throw new ServletException("Could not set up Brand Portal cloud service");
        }

        log.info("Successfully set up IMS configuration for Brand Portal for [ {} ]", wrappedResponse.getString());

        return wrappedResponse.getString();
    }

    private String getRegion(SlingHttpServletRequest request) {
        String region = request.getParameter("region");
        if (!ArrayUtils.contains(ALLOWED_REGIONS, region)) {
            region = "na2";
        }
        return region;
    }

    private  Map<String, String[]> getParameters(String region) throws ServletException {

        // NA2 is  default
        String iss = "2069A62C56EAD08F7F000101@AdobeOrg";
        String sub = "A484429F5E862D7A0A495EAA@techacct.adobe.com";
        String aud = "https://ims-na1.adobelogin.com/c/7ad48cc321ce43bdb8b0489497d55872";
        String alias = "demo-utils_brand-portal_na2";
        String apiKey = "7ad48cc321ce43bdb8b0489497d55872";
        String clientSecret = "7d2fc93c-d952-408d-ae7b-8a01c652c761";

        if ("na1".equalsIgnoreCase(region)) {
            iss = "29CFA55456EACF547F000101@AdobeOrg";
            sub = "14BF79FE5E862D3E0A495FB3@techacct.adobe.com";
            aud = "https://ims-na1.adobelogin.com/c/de14a41f305a41dfb4d5e4acf3feee44";
            alias = "demo-utils_brand-portal_na1";
            apiKey = "de14a41f305a41dfb4d5e4acf3feee44";
            clientSecret = "549c4da3-82e2-4586-980b-3962ddaf2bdc";
        } else if ("emea1".equalsIgnoreCase(region)) {
            iss = "191AFA8056EACDBB7F000101@AdobeOrg";
            sub = "ACCE75145E862E0B0A495E70@techacct.adobe.com";
            aud = "https://ims-na1.adobelogin.com/c/6789645c6aa447de955a278106787a28";
            alias = "demo-utils_brand-portal_emea1";
            apiKey = "6789645c6aa447de955a278106787a28";
            clientSecret = "aed748b2-0556-41df-8959-b52dc53184ac";
        } else if ("emea2".equalsIgnoreCase(region)) {
            iss = "1EA4181256EACE797F000101@AdobeOrg";
            sub = "854639175E862E2D0A495C40@techacct.adobe.com";
            aud = "https://ims-na1.adobelogin.com/c/79258273baf0468fb25f21726e212d5b";
            alias = "demo-utils_brand-portal_emea2";
            apiKey = "79258273baf0468fb25f21726e212d5b";
            clientSecret = "7e4a8dc4-6602-4bcc-9bed-39ef06c061a1";
        } else if ("apac".equalsIgnoreCase(region)) {
            iss = "FB856A9C56FE216D7F000101@AdobeOrg";
            sub = "D28B6BD95E862DBF0A495F94@techacct.adobe.com";
            aud = "https://ims-na1.adobelogin.com/c/c08d15d841524278a0fa287a245caa52";
            alias = "demo-utils_brand-portal_apac";
            apiKey = "c08d15d841524278a0fa287a245caa52";
            clientSecret = "9b972ad4-792e-44ed-bd5e-a90f0c927d26";
        }

        String finalIss = iss;
        String finalSub = sub;
        String finalAud = aud;
        final String jwtClaims = new JSONObject() {{
            try {
                put("exp", "1939182446");
                put("iss", finalIss);
                put("sub", finalSub);
                put("https://ims-na1.adobelogin.com/s/ent_brand_portal_sdk", true);
                put("aud", finalAud);
            } catch (JSONException e) {
                throw new ServletException("Could not create IMS Payload object for Brand Portal " + region + " setup", e);
            }
        }}.toString();

        return  ImmutableMap.<String, String[]>builder().
                put("cloudServiceName", new String[]{"Adobe Brand Portal"}).
                put("keypairAliases", new String[]{alias}).
                put("keypairAlias", new String[]{alias}).
                put("newKeypairAlias", new String[]{alias}).
                put("title", new String[]{"Brand Portal ( " + region.toUpperCase() + " )"}).
                put("authServerUrl", new String[]{"https://ims-na1.adobelogin.com"}).
                put("apiKey", new String[]{apiKey}).
                put("clientSecret", new String[]{clientSecret}).
                put("jwtClaims", new String[]{jwtClaims}).
                build();
    }


    /* ************************************************************************************************************
        CREATE CLOUD SERVICE
    * ************************************************************************************************************/

    public void createCloudService(SlingHttpServletRequest request, SlingHttpServletResponse response, String region, String accessTokenConfigId) throws Exception {
        request.getResourceResolver().refresh();

        final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                put("item", new String[]{""}).
                put("name", new String[]{"Brand Portal (" + region.toUpperCase() + ")"}).
                put("tenantURL", new String[]{ "https://bpdemon" + region + ".brand-portal.adobe.com" }).
                put("pubFolder", new String[]{ "" }).
                put("oauthScopes", new String[]{ "dam-read dam-write dam-sync cc-share" }).
                put("imsConfig", new String[]{accessTokenConfigId}).
                put("sling:resourceType", new String[]{"dam/components/mediaportal/config"}).
                put("pubFolder@Delete", new String[]{""}).
                build();

        SlingHttpServletRequestWrapper wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createCloudServiceServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200) {
            throw new ServletException("Could not setup Brand Portal cloud service: " + wrappedResponse.getString());
        }

        // Sleep to let the OSGi service bind and create the node under /conf/global/settings/brand-portal
        Thread.sleep(1000);
    }

    /* ************************************************************************************************************
        PERMISSION CLOUD SERVICE
    * ************************************************************************************************************/

    public void permissionCloudService(SlingHttpServletRequest request, SlingHttpServletResponse response, String accessTokenConfigId) throws Exception {
        request.getResourceResolver().refresh();

        // TODO
        final String pid = ""; // findConfigId(request.getResourceResolver(), accessTokenConfigId);

        final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                put("name", new String[]{"Brand Portal"}).
                put("imsConfig", new String[]{accessTokenConfigId}).
                //put("environment", new String[]{ "PROD" }).
                        put("locale", new String[]{"en_US"}).
                        put("configurationId", new String[]{pid}).
                        put("user", new String[]{"/home/groups/d/dam-users", "/home/groups/a/administrators"}).
                        build();

        SlingHttpServletRequestWrapper wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createCloudServiceServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200) {
            throw new ServletException("Could not setup Adobe Stock cloud service");
        }

        //log.debug("Updated permissions on Adobe Stock Cloud Service [ {} ]", pid);
    }

    /* ************************************************************************************************************
        UPDATE KEYSTORE
    * ************************************************************************************************************/

    private void createKeyStore(ResourceResolver resourceResolver, String region) throws Exception {

        String aliasPrefix = "demo-utils_brand-portal_";
        String password = "demo";
        String keystorePassword = "demo";

        String actualServiceUserId = null;

        for (String serviceUserId : SERVICE_USER_IDS) {
            try {
                if (!keyStoreService.keyStoreExists(resourceResolver, serviceUserId)) {
                    keyStoreService.createKeyStore(resourceResolver, serviceUserId, keystorePassword.toCharArray());
                    log.info("Created keystore for [ {} ]", serviceUserId);
                } else {
                    try {
                        keyStoreService.changeKeyStorePassword(resourceResolver, serviceUserId, keystorePassword.toCharArray(), keystorePassword.toCharArray());
                    } catch (SecurityException e) {
                        CleanerUtil.remove(resourceResolver, getUserPath(resourceResolver, serviceUserId));
                        keyStoreService.createKeyStore(resourceResolver, serviceUserId, keystorePassword.toCharArray());
                    }
                }

                actualServiceUserId = serviceUserId;
                break;
            } catch (Exception e) {
                log.debug("Could not find a user for userId [ {} ]", serviceUserId, e);
            }
        }

        log.debug("Valid Service User Id: [ {} ]", actualServiceUserId);

        String finalActualServiceUserId = actualServiceUserId;
        Arrays.stream(ALLOWED_REGIONS).forEach(regionName -> {

            log.debug("Processing region [ {} ]", regionName);

            try {
                final String alias = aliasPrefix + regionName;
                // brand-portal-bpdemopapac-keystore.p12
                final String certName = StringUtils.lowerCase("brand-portal-bpdemo" + regionName + "-keystore.p12");
                final String certPath = PATH_CERTS  + "bpdemo" + regionName + "/" + certName;
                log.debug("Cert path at [ {} == null ~> {}]", certPath, resourceResolver.getResource(certPath) == null);
                final InputStream ksStream = resourceResolver.getResource(certPath).adaptTo(InputStream.class);
                final String userId = getUserId(resourceResolver, finalActualServiceUserId);

                //keyStoreService.getKeyStore(resourceResolver, finalActualServiceUserId);
                final KeyStore inputKeyStore = KeyStore.getInstance("pkcs12");
                inputKeyStore.load(ksStream, password.toCharArray());
                final KeyStore.Entry entry = inputKeyStore.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
                final KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) entry;

                removeKeyStoreEntry(resourceResolver, alias, userId);

                keyStoreService.addKeyStoreKeyEntry(resourceResolver, userId, alias, pkEntry.getPrivateKey(), pkEntry.getCertificateChain());

                log.info("Updated keystore for [ {} ] for region [ {} ]", finalActualServiceUserId, regionName);
            } catch(Exception e) {
                log.error("Unable to update keystore to region [ {} ]", regionName, e);
            }
        });
    }

    private void removeKeyStoreEntry(ResourceResolver resourceResolver, String alias, String userId) throws IOException {
        KeyStore store = null;
        try {
            store = keyStoreService.getKeyStore(resourceResolver, userId);
        } catch (KeyStoreNotInitialisedException e) {
            log.error("Unable to perform remove alias operation because the store was not initialised.");
        }
        if (store != null) {
            try {
                if (store.containsAlias(alias)) {
                    store.deleteEntry(alias);
                }
            } catch (KeyStoreException e) {
                throw new IOException(e);
            }
        }
    }

    private String getUserId(ResourceResolver resourceResolver, String userId) throws RepositoryException {
        return ((JackrabbitSession) resourceResolver.adaptTo(Session.class)).getUserManager().getAuthorizable(userId).getID();
    }

    private String getUserPath(ResourceResolver resourceResolver, String userId) throws RepositoryException {
        return ((JackrabbitSession) resourceResolver.adaptTo(Session.class)).getUserManager().getAuthorizable(userId).getPath();
    }

}