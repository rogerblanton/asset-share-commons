
## Smart Crop Image demo

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/21519/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

1. Navigate to the Image Profile Page: **Tools** > **Assets** > **<a href="/mnt/overlay/dam/gui/content/processingprofilepage/imageprocessingprofiles.html/conf/global/settings/dam/adminui-extension/imageprofile" target="_blank">Image Profiles</a>**
2. Click on the **Create** button and create a new image profile
   1. Select the cropping options as - Smart Crop
   2. Configure your options for Responsive Image Crop
   3. Save your changes

    ![Smart Crop Image profile](./smart-crop/images/smart-crop-image-profile.png)
    
3. Select the newly created Image Profile and assign it to a folder in the **DAM**
 
    ![Image profile assign](./smart-crop/images/apply-processing-folder.png)
    
4. From the AEM Start menu navigate to **Assets** > **Files** > and find the the folder selected in the previous step and you should be able to notice the processing profile applied:

    ![smart crop](./smart-crop/images/surfing-folder-smart-crop.png)
    
5. Select the folder and in the menu click **Create** > **Workflow** > **Dam Update Asset Workflow**

    ![update asset workflow](./smart-crop/images/update-asset-workflow.png)
    
6. Navigate into the folder and you should be able to select and asset and then select the smart crop button to see the crops:

    ![smart crop button](./smart-crop/images/smart-crop-button.png)
    
7. Any new assets uploaded to this folder will be automatically Smart Cropped.

## Smart Crop Video demo

[Download the demo snowboarder video](https://link.enablementadobe.com/smart-crop__snowboarder) to demonstrate how Smart Crop Video tracks (and crops) to the moving focal point.

1. Ensure that you have completed the steps to [Set up Dynamic Media](/apps/demo-utils/instructions/dynamic-media.html) **before** proceeding. (Sorry if you came here first!)
2. Navigate to the Video Profiles Page: **Tools** > **Assets** > **<a href="/mnt/overlay/dam/gui/content/s7dam/videoprofiles/videoprofiles.html" target="_blank">Video Profiles</a>**
3. Select the OOTB **Adaptive Encoding Profile** and tap **Copy** from the top action bar.
4. Give the new profile a meaningful name such as: `Video Smart Crop`
5. Select the newly profile created video profile and tap **Edit** in the top action bar
6. Under the **Smart Crop Ratio** section...
    ![Smart Crop Video profile](./smart-crop/images/video-profile__smart-crop.png)
7. Tap the **Add new** button
   1. Name: `Mobile - Vertical`
   2. Crop ratio: `9x16`
8. Tap the **Add new** button again
   1. Name: `Mobile - Horizontal`
   2. Crop ratio: `16x9`
9. Tap **Save**   

10. Select the newly created Video Profile and assign it to an **Assets Folder** that contains video to smart crop.
11. From the AEM Start menu navigate to **Assets** > **Files** > and find the folder selected in the previous step and you should be able to notice the processing profile applied:
12. Select the folder (or specific assets that are in the smart cropped folder) and in the top action bar, click **Reprocess Assets**. Note this will ONLY process assets in the immediate folder and NOT in sub-folders.
13. Wait for the assets to complete processing (the Processing badge should disappear from the asset cards)
14. Tap into a processed asset, open the left side bar, and select **Viewers**
15. Select the **SmartCropVideo** from the viewers list
16 Resize the video preview window to be narrow, causing the video to move render its vertical `9x16` crop. Watch the video's focal point move around as the vide plays.
17. Any new video assets uploaded to this folder will be automatically Smart Cropped.

### Troubleshooting Smart Crop Video

If in the asset's Viewers view, the SmartCropVideo viewer is listed as "SmartCropVideo (unprocessed)" then:

1. Navigate to Tools > Assets > Viewers
2. Locate the SmartCropVideo viewer, select it, and re-publish it
3. Navigate back to the asset via Assets > Files
4. Select the asset, and tap Reprocess Asset in the top action bar
5. Wait for the asset to full process, and check again

