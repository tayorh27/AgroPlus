package com.ap.agroplus.Notification;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by sanniAdewale on 21/04/2017.
 */

public class NotificationExtenderExample extends NotificationExtenderService{
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        return false;
    }
}
