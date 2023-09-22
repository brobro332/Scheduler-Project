// firebase-messaging-sw.js
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-messaging-compat.js');

  firebase.initializeApp({
    apiKey: "AIzaSyDv-8VaMQnhg8WkybcvgtzxBqTzatGh1t0",
    authDomain: "scheduler-project-fad66.firebaseapp.com",
    projectId: "scheduler-project-fad66",
    storageBucket: "scheduler-project-fad66.appspot.com",
    messagingSenderId: "973664071169",
    appId: "1:973664071169:web:5e515d753f9f3ec8565a00"
  });

  const messaging = firebase.messaging();