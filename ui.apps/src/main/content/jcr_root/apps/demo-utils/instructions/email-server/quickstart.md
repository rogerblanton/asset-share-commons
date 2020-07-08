## Local Set up

When running AEM as a Cloud Service locally via the AEM SDK's Quickstart Jar, Demo Utils installs a Email server configuration that works, because the AEM as a Could Service infrastructure is not in play to block the calls from AEM to the Email Server.

This package installs a SMTP E-mail Server configuration using the e-mail account **aem@enablementadobe.com**.

This SMTP server uses SendGrid and works on or off the Adobe VPN.

Please only use this SMTP Servers credentials for Demo Utils as it is limited to 25,000 e-mails a month.

Remember, AEM cannot send e-mail from the Cloud infrastructure at this time, this ONLY works when running AEM locally using the Quickstart Jar.

## Manual set up

In the event you need to manually set up the AEM Mail service, this can be done via the OSGi configuration using the demo configuration.

1. Navigate to [AEM > Tools > Operations > Web Console] (http://localhost:4502/system/console/configMgr/com.day.cq.mailer.DefaultMailService)
2. Edit __Day CQ Mail Service__ and fill out the following settings:
+ SMTP server host name: `smtp.sendgrid.net`
+ SMTP server port: `465`
+ SMTP user: `apikey`
+ SMTP password: `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`
+ From address: `aem@enablementadobe.com`
+ SMTP use SSL: `true`
+ SMTP use StartTLS: `false`
+ Debug email: `false`
3. Tap __Save__ 
4. Now go have AEM send some e-mails!
