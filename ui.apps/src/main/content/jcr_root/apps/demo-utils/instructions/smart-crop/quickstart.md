
## Smart Crop demo

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
