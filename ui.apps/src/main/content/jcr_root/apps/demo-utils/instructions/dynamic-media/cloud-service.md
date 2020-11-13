
## Automatic Local Set up

Automatic set up for Dynamic Media has been REMOVED indefinitely. Please use the Manual Set up instructions below instead.

<!--
Click the button below to automatically set up the Dynamic Media Cloud Service.

**AFTER CLICKING, BE PATIENT... you will be re-directed to a success or failure page indicating if it's set up successfully.**

<a href="/apps/demo-utils/instructions/dynamic-media.install.html?id=na" class="button">Configure for North America</a>
-->
### Manually Set up Dynamic Media Cloud Service

1. Navigate to **AEM > Tools > Cloud Services**.
2. Click on **Dynamic Media Configuration** card.
3. Navigate into the **global** folder and tap **Create** in the top left.
4. Create a new Dynamic Media cloud configuration
    * Title: **Dynamic Media**
    * Email: **dynamicmedia-na@adobe.com**
    * Password: **$Dynamicna1**
    * Region: **North America - Enterprise**
    * Press `Connect to Dynamic Media`
    * Company: **DynamicMediaNA**
    * Company Root Folder Path: **DynamicMediaNA/**
        * *Do **not** change this value.*
    * Publish Assets: **Selective**
    * Secure Preview Server: **https://preview1.assetsadobe.com**
        * *Do **not** change this value.*

    ![Cloud Config](./dynamic-media/images/cloud-config.png)

#### Troubleshooting permissions

If you are unable to create Presets or Viewers, it is likely you do not have the property permissions on your AEM as a Cloud Service Author.
To resolve this, add yourself to the `administrators` user group in AEM.

1. Log in to AEM as a Cloud Service AEM Author instance using your Adobe ID
    + Note that you MUST be in the AEM Administrators Product Profile for your AEM Author, otherwise you will not be able to self-administer permissions
1. Tools > Security > Groups
1. Locate the `administrators` group and tap on its title
1. Tap the Members tab
1. Add your user to the as a member of the `administrators` group
1. Tap Save and Close, and try again


#### Special instructions

* **Publish Assets: Upon Activation**
    * During manual set up, if `Publish Assets: Upon Activation` is selected, you **must** wait 15 mins for the supporting assets to be published to and processed by Scene 7.
    * After waiting 15 mins, you must Publish any Image and Viewer presets at:
        * <a href="/mnt/overlay/dam/gui/content/s7dam/viewerpresets/viewerpresets.html" target="_blank">AEM > Tools > Assets > Viewer Presets</a>
        * <a href="/mnt/overlay/dam/gui/content/s7dam/imagepresets/imagepresets.html" target="_blank">AEM > Tools > Assets > Image Presets</a>
* Allow all Presets to publish before using. When `Publish Assets` is set to `Immediately`, they will automatically queue up to publish.