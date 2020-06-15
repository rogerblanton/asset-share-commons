package com.adobe.aem.demo.utils.impl;

import com.adobe.granite.license.ProductInfoProvider;
import org.apache.sling.featureflags.Features;
import org.osgi.framework.Version;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class RequireAem {
    public static final Version ORIGINAL_CLOUD_SERVICE_VERSION = new Version(2019, 12,   0);

    @Reference
    private Features features;

    @Reference
    private ProductInfoProvider productInfoProvider;

    public boolean isCloudService() {
        return !is65() && features.isEnabled("com.adobe.dam.asset.nui.feature.flag");
    }

    public boolean isAemSdk() {
        return !is65() && !features.isEnabled("com.adobe.dam.asset.nui.feature.flag");
    }

    public boolean is65() {
        if (productInfoProvider.getProductInfo().getVersion().compareTo(ORIGINAL_CLOUD_SERVICE_VERSION) > 0) {
            return false;
        } else {
            return true;
        }
    }
}
