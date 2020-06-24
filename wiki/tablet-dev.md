# Debugging on tablet

### Enable developer options and USB debugging

 - To enable developer options, tap the Build Number option 7 times. 
   You can find this option in one of the following locations, depending on your Android version:

    |||
    | --- |---|
    | Android 8 | Settings > System > About Phone > Build Number |
    | Android >= 9 | Settings > About Phone > Build Number |

 - Enable USB debugging:

    |||
    | --- |---|
    | Android 8 | Settings > System > Developer Options > USB debugging |
    | Android >= 9 | Settings > System > Advanced > Developer Options > USB debugging |

### Forward app port:

To be able to open `localhost:3000` (according to port in [run](run.md) and [build](build.md) sections) on tablet port 3000 should be forwarded:

 - Open devices page: [chrome://inspect/#devices](chrome://inspect/#devices).

 - Click button "Port forwarding" and add line `3000 | localhost:3000`. Then click "Done".

### Open App page

 - On [devices page](chrome://inspect/#devices) in section "Remote Target" find name of your mobile device.
 
 - In this block enter URL into text field and click "Open". Entered page url should be opened on tablet.
 
 - Then click "inspect" to open DevTools of the page .

---

### External links

- [Configure on-device developer options](https://developer.android.com/studio/debug/dev-options)
- [Get Started with Remote Debugging Android Devices](https://developers.google.com/web/tools/chrome-devtools/remote-debugging)
- [Access Local Servers](https://developers.google.com/web/tools/chrome-devtools/remote-debugging/local-server)
