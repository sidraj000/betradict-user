import * as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
admin.initializeApp()
export const onMessageCreate=functions.database.ref('/match/cricket/{id}').onCreate((snapshot,context)=>{

    const matchdata=snapshot.val()
    const payLoad = {
        notification:{
            title: matchdata.teamA+'vs'+matchdata.teamB,
            body: 'new match added',
            sound: "default"
        }
    };

   return admin.messaging().sendToTopic('betradict',payLoad)
})
