<div class="unsupported">
Sending e-mail from AEM as a Cloud Service requires your customer submitting an Adobe Support request to open up the port for their environment.
</div>

Because each AEM as a Cloud Service environment must be specially requested via Adobe Support to allow e-mail traffic to be sent, this is not "automatically enabled" via Demo Utils.
 
To enable e-mail support using Demo Utils

1. Deploy Demo Utils to your AEM as a Cloud Service environment
1. Log an Adobe Support ticket requesting that the e-mail ports be opened for yoru specific Environment.
    + This request may take several days to complete.
    + Note that if the environment is deleted, the request must be re-submitted for newly re-created environments.
1. Once Adobe Support has processed your request, the Demo Utils email service configuration will be able to communicate out and send emails from AEM author.