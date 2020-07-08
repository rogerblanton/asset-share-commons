
<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/21718/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

> Watch a quick video on the Adobe Asset Link integration with InDesign CC.
> Note this integration currently requires a pre-release version of InDesign CC 2020.

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/28988/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

---

### Changes from Adobe Asset Link 1.x to 2.x

* `assetlink-settings.json` is no longer needed to specify the AEM Author instance. You can now specify AEM Authors instances in the Asset Link 2.x Extension panel directly, i.e. `http://localhost:4502`
* You can setup multiple AEM Author connections in Asset Link, and toggle between them, in the CC App.
* To demo, you still need to use the Extension Manager and `.zxp` file, however real customers deploy the Adobe Asset Link extension by configuring it to be included in the CC App their employees will download (via adminconsole.adobe.com)

---
 
## One-time local machine (CC App) set up
 
The installation of the Adobe Extension only needs to be performed once per the life of the machine.

<ol>
    <li>Download and install the Extension Manager application from <a href="https://install.anastasiy.com"   x-cq-linkchecker="skip" target="_blank" >https://install.anastasiy.com</a>.
    <li>Download the Adobe Asset Link extension (<a href="https://link.enablementadobe.com/asset-link-zxp"  x-cq-linkchecker="skip" target="_blank">com.adobe.assetlink.zxp</a>) to your computer.
    <li>Open the Extension Manager application, click Install and select <code>com.adobe.assetlink.zxp</code>. Install the extension.
</ol>

<!-- CLOUD-SERVICE_INSTRUCTIONS -->

<!-- QUICKSTART_INSTRUCTIONS -->

<!-- 65_INSTRUCTIONS -->

## FPO placement in InDesign CC

FPO placement in InDesign CC required the pre-released InDesign CC 2020 release.

* <a x-cq-linkchecker="skip" href="http://link.enablementadobe.com/asset-link-indesign-cc-2020">InDesign CC 2020</a>

The __Create FPO Rendition__ toggle in the __Thumbnail Process__ step must enabled.renditions

Note that The __AEM Demo Utils > Configure Adobe Asset Link button__ above automatically enables this for you.

![DAM Update Asset - Thumbnail Process - FPO](./adobe-asset-link/images/fpo-renditions.png)

To use FPO asset renditions in InDesign CC via Adobe Asset Link, you must re-process the assets with the __DAM Update Asset__ workflow in AEM.

* AEM > Assets > Files > Select folders or assets to re-process > Create > Workflow > DAM Update Asset workflow

## Using the integration

1. Open Adobe Photoshop, Illustrator or InDesign.
2. Go to Window > Extensions
3. Click on Adobe Asset Link
4. The Adobe Asset Link panel with AEM content available, start searching or browsing.

## Troubleshooting

### Adobe XD and Asset Link

Adobe XD supports Adobe Asset Link. To install, open XD, navigate to Plug-ins and search for "Adobe Asset Link".

Note that Adobe XD (for macOS) requires the used to HTTPS to connect to AEM Author.

## Demos

* [Adobe Asset Link Demo](https://internal.adobedemo.com/content/demo-hub/en/demos/external/europa-creative-cloudinappexperience.html)
* [Adobe Asset Link Demo Video (#2)](https://www.adobe.com/marketing/experience-manager-assets/adobe-asset-link.html)

## Other materials

* [Adobe Asset Link Docs](https://www.adobe.com/creativecloud/business/enterprise/adobe-asset-link.html)
* [Adobe Pre-release Site](https://www.adobeprerelease.com/beta/12CD68B7-238C-47F0-A211-C86DCFB57145)
* [INTERNAL Adobe Only - FAQ](https://adobe.ly/2C5Dj5C)
* [INTERNAL Adobe Only - Adobe Asset Link with Dynamic Media](https://wiki.corp.adobe.com/display/~gklebus/Set+up+Europa+and+Dynamic+Media+on+AEM+6.4+L21) 
* [Adobe.com Marketing page](https://www.adobe.com/creativecloud/business/enterprise/adobe-asset-link.html)
