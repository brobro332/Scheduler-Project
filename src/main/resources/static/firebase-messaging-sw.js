// firebase-messaging-sw.js
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-messaging-compat.js');

// firebase-messaging-sw.js
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.4.0/firebase-messaging-compat.js');

fetch('/api/var')
    .then(function(response) {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(function(data) {
        var firebaseApiKey = data.apiKey;
        var firebaseAuthDomain = data.authDomain;
        var firebaseProjectId = data.projectId;
        var firebaseStorageBucket = data.storageBucket;
        var firebaseMessagingSenderId = data.messagingSenderId;
        var firebaseAppId = data.appId;

        firebase.initializeApp({
            apiKey: firebaseApiKey,
            authDomain: firebaseAuthDomain,
            projectId: firebaseProjectId,
            storageBucket: firebaseStorageBucket,
            messagingSenderId: firebaseMessagingSenderId,
            appId: firebaseAppId
        });

        const messaging = firebase.messaging();
        // Firebase Messaging을 여기서 설정할 수 있습니다.
    })
    .catch(function(error) {
        console.error('환경 변수를 가져오는 중 오류 발생: ', error);
    });