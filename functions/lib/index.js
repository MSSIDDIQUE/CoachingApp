// imports firebase-functions module
const functions = require('firebase-functions');
// imports firebase-admin module
const admin = require('firebase-admin');
admin.initializeApp();
/* Listens for new messages added to /messages/:pushId and sends a notification to subscribed users */
exports.pushNotification = functions.database.ref('/messages/{pushId}').onWrite((change, context) => {
    console.log('Push notification event triggered');
    /* Grab the current value of what was written to the Realtime Database */
    const valueObject = change.after.val();
    /* Create a notification and data payload. They contain the notification information, and message to be sent respectively */
    const payload = {
        notification: {
            "title": 'App Name',
            "body": "New message",
            "sound": "default"
        },
        data: {
            title: valueObject.title,
            messagge: valueObject.messagge
        }
    };
    /* Create an options object that contains the time to live for the notification and the priority. */
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };
    return admin.messaging().sendToTopic("notifications", payload, options).then(function (response) {
        console.log('Notification sent successfully:', response);
    })
        .catch(function (error) {
        console.log('Notification sent failed:', error);
    });
});
//# sourceMappingURL=index.js.map