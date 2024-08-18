// firebase.js
import admin from 'firebase-admin';
import { createRequire } from 'module';
const require = createRequire(import.meta.url);
const serviceAccount = require('../firebase-admin.json');

if (!admin.apps.length) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://hancafe-3b24f-default-rtdb.firebaseio.com",
    storageBucket: "hancafe-3b24f.appspot.com"
  });
}

const bucket = admin.storage().bucket();

export { bucket };
