// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/3.9.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/3.9.0/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
// var config = {
//   apiKey: "AIzaSyBeW9Ip7oU9U640m7x-2XncWNtVwXBiV9s",
//   authDomain: "tubes-3-wbd-ojek-online.firebaseapp.com",
//   databaseURL: "https://tubes-3-wbd-ojek-online.firebaseio.com",
//   projectId: "tubes-3-wbd-ojek-online",
//   storageBucket: "tubes-3-wbd-ojek-online.appspot.com",
//   messagingSenderId: "183053726880"
// };
firebase.initializeApp({
  messagingSenderId: "183053726880"
});

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
//const messaging = firebase.messaging();

firebase.messaging().setBackgroundMessageHandler(function(payload) {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);
  // Customize notification here
  const notificationTitle = 'Background Message Title';
  const notificationOptions = {
    body: 'Background Message body.',
    icon: '/firebase-logo.png'
  };

  return self.registration.showNotification(notificationTitle,
      notificationOptions);
});