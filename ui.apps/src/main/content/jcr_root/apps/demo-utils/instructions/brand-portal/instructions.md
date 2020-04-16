
<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/26354/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

For an overview video click [here](https://helpx.adobe.com/experience-manager/kt/eseminars/gems/aem-brand-portal.html).

## Shared Brand Portal accounts

<table>
<thead>
<tr>
<th>Env</th>
<th>Brand Portal URL</th>
<th>Brand Portal Admin (Adobe ID Credentials)</th>
<th>Automatic AEM set up</th>
</tr>
</thead>
<tbody>
<tr>
<td>NA1</td>
<td><a href="https://bpdemona1.brand-portal.adobe.com">https://bpdemona1.brand-portal.adobe.com</a></td>
<td>demouser@adobe.com / Brandportal#4</td>
<td><a href="/apps/demo-utils/instructions/brand-portal.install.html?region=na1" class="button">Set up</a></td>
</tr>
<tr>
<td>NA2</td>
<td><a href="https://bpdemona2.brand-portal.adobe.com">https://bpdemona2.brand-portal.adobe.com</a></td>
<td>demouser@adobe.com / Brandportal#4</td>
<td><a href="/apps/demo-utils/instructions/brand-portal.install.html?region=na2" class="button">Set up</a></td>
</tr>
<tr>
<td>EMEA1</td>
<td><a href="https://bpdemoemea1.brand-portal.adobe.com">https://bpdemoemea1.brand-portal.adobe.com</a></td>
<td>demouser@adobe.com / Brandportal#4</td>
<td><a href="/apps/demo-utils/instructions/brand-portal.install.html?region=emea1" class="button">Set up</a></td>
</tr>
<tr>
<td>EMEA2</td>
<td><a href="https://bpdemoemea2.brand-portal.adobe.com">https://bpdemoemea2.brand-portal.adobe.com</a></td>
<td>demouser@adobe.com / Brandportal#4</td>
<td><a href="/apps/demo-utils/instructions/brand-portal.install.html?region=emea2" class="button">Set up</a></td>
</tr>
<tr>
<td>APAC</td>
<td><a href="https://bpdemoapac.brand-portal.adobe.com">https://bpdemoapac.brand-portal.adobe.com</a></td>
<td>demouser@adobe.com/ Brandportal#4</td>
<td><a href="/apps/demo-utils/instructions/brand-portal.install.html?region=apac" class="button">Set up</a></td>
</tr>
</tbody>
</table>


## Asset sourcing (uploading assets to Brand Portal and sync'ing them to AEM Author)

Brand Portal supports [Asset Sourcing](https://docs.adobe.com/content/help/en/experience-manager-brand-portal/using/asset-sourcing-in-brand-portal/brand-portal-asset-sourcing.html) however with regards to demo'ing there is a caveat:

+ When an asset is uploaded to Brand Portal, the first AEM Author instance to poll that Brand Portal tenant for assets-to-sync, will sync the assets and clear the sync queue.

This means if two demo'ers set up Asset Sourcing on two different AEM Authors for the same Brand Portal tenant, it is unknown which AEM Author will sync back assets uploaded to Brand Portal. 

Because of this, the guidance for demo'ing asset sourcing is to use hosted AEM Author's maintained by Adobe Engineering that are paired with the respective Brand Portal instances.


<table>
<thead>
<tr>
<th>Env</th>
<th>Brand Portal URL</th>
<th>Brand Portal Admin (Adobe ID) credentials</th>
<th>Hosted AEM Author (requires VPN)</th>
<th>AEM Author Admin credentials</th>
</tr>
</thead>
<tbody>
<tr>
<td>NA1</td>
<td><a href="https://bpdemona1.brand-portal.adobe.com">https://bpdemona1.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
<td><a href="http://bpdemona1.corp.adobe.com:4502" x-cq-linkchecker="skip" target="_blank"></a>http://bpdemona1.corp.adobe.com:4502</td>
<td>bpdemo / bpdemo</td>
</tr>
<tr>
<td>NA2</td>
<td><a href="https://bpdemona2.brand-portal.adobe.com">https://bpdemona2.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
<td><a href="http://bpdemona2.corp.adobe.com:4502" x-cq-linkchecker="skip" target="_blank"></a>http://bpdemona2.corp.adobe.com:4502</td>
<td>bpdemo / bpdemo</td>
</tr>
<tr>
<td>EMEA1</td>
<td><a href="https://bpdemoemea1.brand-portal.adobe.com">https://bpdemoemea1.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
<td><a href="http://bpdemoemea1.corp.adobe.com:4502" x-cq-linkchecker="skip" target="_blank"></a>http://bpdemoemea1.corp.adobe.com:4502</td>
<td>bpdemo / bpdemo</td>
</tr>
<tr>
<td>EMEA2</td>
<td><a href="https://bpdemoemea2.brand-portal.adobe.com">https://bpdemoemea2.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
<td><a href="http://bpdemoemea2.corp.adobe.com:4502" x-cq-linkchecker="skip" target="_blank"></a>http://bpdemoemea2.corp.adobe.com:4502</td>
<td>bpdemo / bpdemo</td>
</tr>
<tr>
<td>APAC</td>
<td><a href="https://bpdemoapac.brand-portal.adobe.com">https://bpdemoapac.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
<td><a href="http://bpdemoapac.corp.adobe.com:4502" x-cq-linkchecker="skip" target="_blank"></a>http://bpdemoapac.corp.adobe.com:4502</td>
<td>bpdemo / bpdemo</td>
</tr>
</tbody>
</table>

## Manual set up

The Demo Shared Brand Portal instances can be set up manually to integrate with AEM following the instructions on Adobe Documentation.

The divergence from Adobe Docs is that Demo Utils provides pre-configured Keystore to install ito AEM that have been setup in Adobe Console to allow AEM to talk to Brand Portal. 

1. Download the following keystore to install to AEM, based on the Shared Brand Portal instance you want to connect to. 

1. Navigate to __AEM > Tools > Security > Users__
1. Locate and edit the Brand Portal service user
    * AEM as a Cloud Service: `bp-imsconfig-service`
    * AEM 6.5: `brandportal-imsconfig-service`
1. Click on the __Keystore__ tab
1. __Create Keystore__ if needed
    * Use password: `demo` 
1. Open the __Add Private key from Keystore file__ section
1. Enter the keystore entry based on which Shared Brand Portal instance you want to connect to.

| Brand Portal Env to connect to | New Alias | Keystore file | Keystore File Password | Private Key Alias | Private Key Password | 
|--------------------------------|-----------|---------------|------------------------|-------------------|----------------------|
| NA1   | demo-utils_brand-portal_na1 | [brand-portal-bpdemona1-keystore.p12](/apps/demo-utils/resources/brand-portal/bpdemona1/brand-portal-bpdemona1-keystore.p12) | demo | demo-utils_brand-portal_na1  | demo | 
| NA2   | demo-utils_brand-portal_na2 | [brand-portal-bpdemona2-keystore.p12](/apps/demo-utils/resources/brand-portal/bpdemona2/brand-portal-bpdemona2-keystore.p12) | demo | demo-utils_brand-portal_na2  | demo | 
| EMEA1 | demo-utils_brand-portal_emea1 | [brand-portal-bpdemoemea1-keystore.p12](/apps/demo-utils/resources/brand-portal/bpdemoemea1/brand-portal-bpdemoemea1-keystore.p12) | demo | demo-utils_brand-portal_emea1 | demo | 
| EMEA1 | demo-utils_brand-portal_emea2 | [brand-portal-bpdemoemea2-keystore.p12](/apps/demo-utils/resources/brand-portal/bpdemoemea2/brand-portal-bpdemoemea2-keystore.p12) | demo | demo-utils_brand-portal_emea2 | demo | 
| APAC | demo-utils_brand-portal_apac | [brand-portal-bpdemoapac-keystore.p12](/apps/demo-utils/resources/brand-portal/bpdemoapac/brand-portal-bpdemoapac-keystore.p12) | demo | demo-utils_brand-portal_apac | demo | 

1. Tap the __Submit__ button
1. Tap __Save and Close__ in the upper-right
1. Follow the manual set up steps outlined in [Adobe documentation](https://docs.adobe.com/content/help/en/experience-manager-brand-portal/using/publish/configure-aem-assets-with-brand-portal.html), and select the `demo-utils_brand-portal_xxxxx` key in the Adobe IMS Technical Account Creation Wizard's > Certificate drop-down, and then skip the Adobe I/O console set up.  

## Demos

* [Brand Portal Demo](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-assets-brand-portal.html)

## Other materials

* [Credentials for Shared Demo Brand Portal servers](https://wiki.corp.adobe.com/display/AdobeDAM/Brand+Portal+demo+instances)

* Videos
    * [AEM Assets Brand Portal Overview Video](https://helpx.adobe.com/experience-manager/kt/eseminars/gems/aem-brand-portal.html)
    * [Set up Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-technical-video-setup.html)
    * [Using Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-feature-video-use.html)
    * [Developing for Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-technical-video-develop.html)
* Adobe Docs
    * [Overview of AEM Assets Brand Portal](https://helpx.adobe.com/experience-manager/brand-portal/using/brand-portal.html)
    * [What's new in AEM Assets Brand Portal](https://helpx.adobe.com/experience-manager/brand-portal/using/whats-new.html)
    * [Understanding Brand Portal in AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-article-understand.html)
    * [AEM as a Cloud Service Brand Portal Set up](https://docs.adobe.com/content/help/en/experience-manager-cloud-service/assets/brand-portal/configure-aem-assets-with-brand-portal.html#configure-the-cloud-service)
