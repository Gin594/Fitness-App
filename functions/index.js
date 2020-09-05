const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


exports.sendTeamNotifications = functions.firestore
   .document('planned_events/{topicId}')
   .onUpdate((change, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     //const document = change.exists ? change.after.data() : null;
     const document = change.after.data();
     if (document) {
       var message = {
         notification: {
           title: document.title + ' has changed the status of a team walk',
           body: document.title
         },
         topic: context.params.topicId
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully pushed notification:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error pushing notification:', error);
           return error;
         });
     }

     console.log('Nothing was created ... null');
     return "document was null or emtpy";
   });


exports.notifyOfTeamMemberStatusChange = functions.firestore
    .document('planned_events/proposed/routes/{routeID}').onDelete((change,
    context) => {

        const topic = 'test-topics';
         const document = change.data();
         if (document) {
           var message = {
             notification: {
               title: document.title + ' Has been canceled',
               body: document.title
             },
             topic: topic
           };
           return admin.messaging().send(message)
             .then((response) => {
               // Response is a message ID string.
               console.log('Successfully pushed notification:', response);
               return response;
             })
             .catch((error) => {
               console.log('Error pushing notification:', error);
               return error;
             });
         }



         console.log('Nothing was created ... null');
         return "document was null or emtpy";



});


exports.notifyTeamNewRouteWasProposed = functions.firestore
    .document('planned_events/proposed/routes/{routeName}').onCreate((snap, context) =>{

        const topic = 'new-team-walk';
        const document = snap.data();
        if (document){

            var message = {
                notification: {
                    title: document.creator + 'invites you on ' + document.title,
                    body: 'Go to the proposed walks page'
                },
                topic: topic
            };
            return admin.messaging().send(message).then((response) => {
                // Response is a message ID string.
                console.log('Successfully pushed notification:', response);
                return response;
            }).catch((error) => {
                 console.log('Error pushing notification:', error);
                 return error;
            });
        }

        console.log('Nothing was created ... null');
        return "document was null or emtpy";

});


exports.proposedWalkMemberStatusChange = functions.firestore
    .document('planned_events/proposed/routes/{routeName}').onUpdate((snap,
    context) =>{

        const topic = 'team-walk-status-change';
        const document = snap.after.data();

        const invited = context.params.invitedMembers;
        console.log('data from invited ', document.invitedMembers);

        if (document){

            var message = {
                notification: {
                    title: 'Team Member Accepted Walk',
                    body: 'data should have the invited members'
                },
                data: document.invitedMembers,

                topic: topic
            };
            return admin.messaging().send(message).then((response) => {
                           // Response is a message ID string.
                           console.log('Successfully pushed notification:', response);
                           return response;
                         })
                         .catch((error) => {
                           console.log('Error pushing notification:', error);
                           return error;
                         });
        }

        console.log('Nothing was created ... null');
        return "document was null or emtpy";

});

