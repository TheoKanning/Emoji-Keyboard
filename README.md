# 😀⌨ #
Finally, an Android keyboard that converts pictures into emojis.

Available on the [Play Store](https://play.google.com/store/apps/details?id=com.theokanning.emojikeyboard)

## How 😀⌨ works ##
😀⌨ works by posting photos to the Google Cloud Vision Api, labelling them, and then converting the
text labels into emojis using emoji-java. Since not all labels can be converted directly to emojis, 
this project also includes a map to convert specific labels. Whenever a label isn't recognized, an 
event is sent to Firebase Analytics so it can be added later. 

## How to use 😀⌨ ##
Follow these steps to enable 😀⌨ on your phone
1. Open Settings
2. Select System
3. Select Languages and Input
4. Select Virtual Keyboards
5. Select Manage Keyboards
6. Press the toggle to activate 😀⌨
Now 😀⌨ is enabled!

In order to use 😀⌨ to type, start typing anywhere. A keyboard icon will appear in the bottom right of the screen. Press it to see keyboard options and select 😀⌨.

Samsung Devices:
1. Enable 😀⌨ by going to Settings as outlined in the steps above.
2. To make 😀⌨ your current keyboard, start typing anywhere. Swipe down from the top of the phone to see your notifications, and there should be a notification allowing you to switch keyboards.

## Technologies Used ##
* [Google Clout Vision Api](https://cloud.google.com/vision/)
* [emoji-java](https://github.com/vdurmont/emoji-java)
* [CameraKit-Android](https://github.com/wonderkiln/CameraKit-Android)
* [Firebase Analytics](https://firebase.google.com/products/analytics/)