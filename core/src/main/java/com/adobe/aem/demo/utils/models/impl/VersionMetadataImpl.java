package com.adobe.aem.demo.utils.models.impl;

import com.adobe.aem.demo.utils.impl.RequireAem;
import com.adobe.aem.demo.utils.models.VersionMetadata;
import com.adobe.granite.license.ProductInfoProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class}, adapters = {VersionMetadata.class}, resourceType = {
        VersionMetadataImpl.RESOURCE_TYPE}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VersionMetadataImpl implements VersionMetadata {

    protected static final String RESOURCE_TYPE = "demo-utils/components/base";
    private static final Logger log = LoggerFactory.getLogger(VersionMetadataImpl.class);

    @OSGiService
    RequireAem requireAem;

    @OSGiService
    ProductInfoProvider productInfoProvider;

    @Override
    public String getUtilsVersion() {
        Bundle demoUtilBundle = FrameworkUtil.getBundle(VersionMetadata.class);
        if (demoUtilBundle != null) {
            return demoUtilBundle.getVersion().getMajor() 
            + "."
            + demoUtilBundle.getVersion().getMinor()
            + "."
            + demoUtilBundle.getVersion().getMicro();
        }
        return "ERROR";
    }

    @Override
    public String getAemType() {
        if (requireAem.isCloudService()) {
            return "cloud-service";
        } else if (requireAem.isAemSdk()) {
            return "aem-sdk";
        } else if (requireAem.is65()) {
            return "6-5";
        }

        return "unknown";
    }

    @Override
    public String getFullVersion() {
        return productInfoProvider.getProductInfo().getVersion().toString();
    }

    @Override
    public String getMajorVersion() {
        return String.valueOf(productInfoProvider.getProductInfo().getVersion().getMajor());
    }

    @Override
    public String getMinorVersion() {
        return String.valueOf(productInfoProvider.getProductInfo().getVersion().getMinor());
    }

    @Override
    public String getMicroVersion() {
        return String.valueOf(productInfoProvider.getProductInfo().getVersion().getMicro());
    }

    @Override
    public String getQualifierVersion() {
        return productInfoProvider.getProductInfo().getVersion().getQualifier();
    }

    @Override
    public String getYearMonthDayVersion() {
        return StringUtils.substringBefore(getQualifierVersion(), "T");
    }
}