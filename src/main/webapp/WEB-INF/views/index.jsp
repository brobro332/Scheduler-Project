<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="layout/user/header.jsp"%>

<div class="container" style="height: 300px; min-height: 360px;">
<div style="position: relative;">
<div>
    <div style="display: inline-block; position: absolute;">
        <div style="width:470px; height:300px; position: relative; left: 7%;">
        <p style="color: gray;"><small>Welcome to SAPP<small></p>
        <h1>계획은 <kbd id="kbd">"꼼꼼하게"</kbd><br/>
        당신의 프로젝트 플래너,<br/>
        저희가 도와드릴게요!</h1>
        <br/>
        <button onclick="location.href='/scheduler/selectPRJPlanners'" class="btn btn-lg" style="background-color: #956be8; color: white;"><b>시작하기</b></button>
        </div>
    </div>
</div>

<div style="display: inline-block; position: relative; left: 65%;">
<img src="/image/banner-right-spap.png" style="width:300px;">
</div>
</div>
</div>
<div style="display: inline-block; position: relative; left: 18%; width: 70%;">

    <br/><br/><br/>
    <div>
        <div style="display: inline-block;">
            <h3>📑 프로젝트 플래너</h3>
            <h5>업무 일지를 작성해 프로젝트를 계획하고 포트폴리오를 만들어보세요</h5>

            <br/><br/><br/>

            <h3>🕛 스케줄러</h3>
            <h5>프로젝트 마감일이 다가올 경우 브라우저 알림을 받을 수 있어요</h5>

            <br/><br/><br/>

            <h3>👩‍👧‍👦 커뮤니티</h3>
            <h5>커뮤니티에서 정보를 공유하고 소통해보세요</h5>
        </div>
    </div>

    <br/><br/><br/>
</div>

<script src="https://www.gstatic.com/firebasejs/10.4.0/firebase-app-compat.js"></script>
<script src="https://www.gstatic.com/firebasejs/10.4.0/firebase-messaging-compat.js"></script>
<script type="module">

    $.ajax({
        url: '/api/var',
        method: 'GET',
        success: function (data) {
            var firebaseApiKey = data.apiKey;
            var firebaseAuthDomain = data.authDomain;
            var firebaseProjectId = data.projectId;
            var firebaseStorageBucket = data.storageBucket;
            var firebaseMessagingSenderId = data.messagingSenderId;
            var firebaseAppId = data.appId;

            var firebaseConfig = {
              apiKey: firebaseApiKey,
              authDomain: firebaseAuthDomain,
              projectId: firebaseProjectId,
              storageBucket: firebaseStorageBucket,
              messagingSenderId: firebaseMessagingSenderId,
              appId: firebaseAppId
            };

            // Initialize Firebase
            firebase.initializeApp(firebaseConfig);

            const messaging = firebase.messaging();

              if ('serviceWorker' in navigator) {
                // 서비스 워커를 등록합니다.
                navigator.serviceWorker.register('/firebase-messaging-sw.js')
                  .then(function(registration) {
                    console.log('서비스 워커 등록 성공:', registration);
                  })
                  .catch(function(error) {
                    console.error('서비스 워커 등록 실패:', error);
                  });
              }

              $(document).ready(function () {
                function requestNotificationPermission() {
                  var notificationStatus = Notification.permission;

                  if (notificationStatus === 'default') {
                    Notification.requestPermission().then(function (permission) {

                    });
                  }
                }

                requestNotificationPermission();

                $.ajax({
                  method: 'GET',
                  url: '/api/user/loginStatus',
                  dataType: 'json',
                  success: function (data) {
                    if (data.statusCode === 200) {
                      var notificationStatus = Notification.permission;

                      if (notificationStatus === 'granted') {
                        messaging.getToken()
                          .then(function (currentToken) {
                            if (currentToken) {

                              $.ajax({
                                url: '/api/user/targetToken',
                                type: 'PUT',
                                contentType: 'application/json',
                                data: JSON.stringify({ targetToken: currentToken }),
                                success: function () {
                                  console.log('FCM 토큰이 서버로 전송되었습니다.' + currentToken);
                                },
                                error: function (error) {
                                  console.error('FCM 토큰 전송 실패:', error);
                                }
                              });
                            } else {
                              console.log("푸시 알림 토큰이 없습니다.");
                            }
                          })
                          .catch(function (err) {
                            console.log("푸시 알림 토큰을 가져오는 동안 오류 발생:", err);
                          });
                      } else {
                            $.ajax({
                                url: '/api/user/targetToken',
                                type: 'PUT',
                                contentType: 'application/json',
                                data: JSON.stringify({ targetToken: null }),
                                success: function () {
                                  console.log('FCM 토큰을 NULL 값으로 수정합니다.');
                                },
                                error: function (error) {
                                  console.error('FCM 토큰 전송 실패:', error);
                                }
                              });
                      }
                    }
                  },
                  error: function (error) {
                    alert(error);
                  }
                });
              });
        },
        error: function (error) {
            console.error('환경 변수를 가져오는 중 오류 발생: ', error);
        }
    });
</script>

<%@ include file="layout/user/footer.jsp"%>