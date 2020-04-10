package com.adobe.aem.demo.utils.models.impl;

import com.adobe.aem.demo.utils.models.MarkdownWrapper;
import com.adobe.granite.license.ProductInfoProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.featureflags.Features;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.osgi.framework.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Model(adaptables = {SlingHttpServletRequest.class}, adapters = {MarkdownWrapper.class}, resourceType = {
        MarkdownWrapperImpl.RESOURCE_TYPE}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MarkdownWrapperImpl implements MarkdownWrapper {
    private static final Logger log = LoggerFactory.getLogger(MarkdownWrapperImpl.class);

    protected static final String RESOURCE_TYPE = "demo-utils/components/base";

    //private static final Pattern INCLUDE = Pattern.compile("<!--\\s?INCLUDE:\\s?([\\.a-zA-Z0-9_-]+)\\s?-->", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);


    // This is the first Major/Minor GA Version of AEM as a Cloud Service
    public static final Version ORIGINAL_CLOUD_SERVICE_VERSION = new Version(2019, 12,   0);


    private static final String CLOUD_SERVICE_TAG = "<!-- CLOUD-SERVICE_INSTRUCTIONS -->";
    private static final String QUICKSTART_TAG = "<!-- QUICKSTART_INSTRUCTIONS -->";
    private static final String SIX_FIVE_TAG = "<!-- 65_INSTRUCTIONS -->";

    private Resource resource;
    private String forceInstructionType = null;

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private Features features;

    @OSGiService
    private ProductInfoProvider productInfoProvider;

    @PostConstruct
    private void init() {
        resource = request.getResource();
        forceInstructionType = StringUtils.strip(request.getParameter("i"));

        if (!ArrayUtils.contains(new String[]{"cs", "qs", "65"}, forceInstructionType)) {
            forceInstructionType = null;
        }
    }


    public String getCloudServiceInstructions() {
        String fileName = request.getResource().getValueMap().get("cloudService", String.class);
        String instructions =  getInstructions(fileName + ".md");

        if ("cs".equals(forceInstructionType)) {
            return instructions;
        } else if (forceInstructionType != null) {
            return "";
        } else if (!is65() && isCloudService()) {
            return instructions;
        }

        return "";
    }

    public String getCloudServiceQuickstartInstructions() {
        String fileName = request.getResource().getValueMap().get("cloudServiceQuickstart", String.class);
        String instructions =  getInstructions(fileName + ".md");

        if ("qs".equals(forceInstructionType)) {
            return instructions;
        } else if (forceInstructionType != null) {
            return "";
        } else if (!is65() && !isCloudService()) {
            return instructions;
        }

        return "";
    }

    public String get65Instructions() {
        String fileName = request.getResource().getValueMap().get("aem65", String.class);
        String instructions =  getInstructions(fileName + ".md");

        if ("65".equals(forceInstructionType)) {
            return instructions;
        } else if (forceInstructionType != null) {
            return "";
        } else if (is65() && !isCloudService()) {
            return instructions;
        }

        return "";
    }

    public String getInstructions() {
        String instructions = getInstructions("instructions.md");

        instructions = StringUtils.replace(instructions, CLOUD_SERVICE_TAG, getCloudServiceInstructions());
        instructions = StringUtils.replace(instructions, QUICKSTART_TAG, getCloudServiceQuickstartInstructions());
        instructions = StringUtils.replace(instructions, SIX_FIVE_TAG, get65Instructions());

        return instructions;
    }

    protected String getInstructions(final String filename) {
        InputStream is = getMarkdownFile(filename);
        try {
            return processMarkdownFile(is);
        } catch (IOException e) {
            log.debug("Could not process markdown file", e);
        }
        return null;
    }

    protected String processMarkdownFile(final InputStream inputStream) throws IOException {
        if (inputStream != null) {

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                final ArrayList<Extension> extensions = new ArrayList<>();
                extensions.add(TablesExtension.create());
                final Parser parser = Parser.builder().extensions(extensions).build();
                final HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

                final Node document = parser.parseReader(reader);
                final String htmlString = renderer.render(document);

                return htmlString;

            } catch (IOException e) {
                log.error("Issue parsing markdown file", e);
            } finally {
                inputStream.close();
            }
        }

        // No instructions available
        return null;
    }

    private InputStream getMarkdownFile(final String filename) {
        //log.debug("Resource Path " + resource.getPath() + "/" + filename + "/jcr:content");

        final Resource dataResource = resource.getChild(filename + "/jcr:content");

        if (dataResource != null) {
            return dataResource.adaptTo(InputStream.class);
        }

        return null;
    }

    private boolean isCloudService() {
        if (is65()) {
            return false;
        } else if (StringUtils.equalsIgnoreCase(request.getParameter("m"), "cs")) {
            return true;
        } else  if (StringUtils.equalsIgnoreCase(request.getParameter("m"), "q")) {
            return false;
        } else {
            return features.isEnabled("com.adobe.dam.asset.nui.feature.flag");
        }
    }

    private boolean is65() {
        if (productInfoProvider.getProductInfo().getVersion().compareTo(ORIGINAL_CLOUD_SERVICE_VERSION) > 0) {
            return false;
        } else {
            return true;
        }
    }
}