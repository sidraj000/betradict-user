import * as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
admin.initializeApp()
export const onMessageCreate=functions.database.ref('/match/cricket/{id}').onCreate((event)=>{

    const payLoad = {
        notification:{
            title: 'Message received',
            body: 'You received a new message',
            sound: "default"
        }
    };

   return admin.messaging().sendToTopic('betradict',payLoad)

})