package com.mindzglobal.www.mindz.sms;

/**
 * Created by swarajpal on 19-04-2016.
 */
public interface SmsListener {

        public void messageReceived(String messageText);
        public void differentMob(String messageText);
}
