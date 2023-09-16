if (!window.userLoaded) {

    window.userLoaded = true;

let emailCertifyChk = false;

let index = {
    init: function() {
        $("#btn-save").on("click", ()=>{
            this.save();
        });
        $("#btn-updatePassword").on("click", ()=>{
            this.updatePassword();
        });
        $("#btn-updateInfo").on("click", ()=>{
            this.updateInfo();
        });
        window.addEventListener("load", ()=>{
            this.selectProfileImg();
        });
        $("#btn-deleteProfileImg").on("click", ()=>{
            this.deleteProfileImg();
        });
        $("#btn-uploadProfileImg").on("click", ()=>{
            this.uploadProfileImg();
        });
        $("#checkEmail").on("click", ()=>{
            this.checkEmail();
        });
    },

    save: function() {
        var password = $("#password").val();
        var checkedPassword = $("#checkedPassword").val();

        if (password != checkedPassword) {
        alert("패스워드가 일치하지 않습니다.");
        $('#valid_checkedPassword').text("패스워드가 일치하지 않습니다.");
        $('#valid_checkedPassword').css('color', 'red');
        return false;
        }

        if (emailCertifyChk == false) {
        return false
        }

        let data = {
                email: $("#email").val(),
                password: $("#password").val(),
                checkedPassword: $("#checkedPassword").val(),
                name: $("#name").val(),
                phone: $("#phone").val()
        };

        $.ajax({
            type: "POST",
            url: "/signUp",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);

                if(resp.data.hasOwnProperty('valid_email')){
                    $('#valid_email').text(resp.data.valid_email);
                    $('#valid_email').css('color', 'red');
                } else $('#valid_email').text('');

                if(resp.data.hasOwnProperty('valid_password')){
                    $('#valid_password').text(resp.data.valid_password);
                    $('#valid_password').css('color', 'red');
                } else $('#valid_password').text('');

                if(resp.data.hasOwnProperty('valid_name')){
                    $('#valid_name').text(resp.data.valid_name);
                    $('#valid_name').css('color', 'red');
                } else $('#valid_name').text('');

                if(resp.data.hasOwnProperty('valid_phone')){
                    $('#valid_phone').text(resp.data.valid_phone);
                    $('#valid_phone').css('color', 'red');
                } else $('#valid_phone').text('');

                } else {
                alert(resp.message);
                location.href = "/";
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },

    updatePassword: function() {

        var data = {
                prevPassword: $('#prevPassword').val(),
                password: $("#password").val(),
                checkedPassword: $("#checkedPassword").val(),
        }

        var password = $("#password").val();
        var checkedPassword = $("#checkedPassword").val();

        if (password != checkedPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        $('#valid_checkedPassword').text("비밀번호가 일치하지 않습니다.");
        $('#valid_checkedPassword').css('color', 'red');
        return false;
        }

        $.ajax({
            type: "PUT",
            url: "/api/user/password",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);

                if(resp.data.hasOwnProperty('valid_password')){
                    $('#valid_password').text(resp.data.valid_password);
                    $('#valid_password').css('color', 'red');
                } else $('#valid_password').text('');

                } else {
                alert(resp.message);
                location.href = "/";
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },

    updateInfo: function() {

        var data = {
                name: $("#name").val(),
                phone: $("#phone").val()
        }

        $.ajax({
            type: "PUT",
            url: "/api/user/info",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function(resp) {
            if(resp.statusCode == 400 || resp.statusCode == 500){
                alert(resp.message);

                if(resp.data.hasOwnProperty('valid_name')){
                    $('#valid_name').text(resp.data.valid_name);
                    $('#valid_name').css('color', 'red');
                } else $('#valid_name').text('');

                if(resp.data.hasOwnProperty('valid_phone')){
                    $('#valid_phone').text(resp.data.valid_phone);
                    $('#valid_phone').css('color', 'red');
                } else $('#valid_phone').text('');

                } else {
                alert(resp.message);
                location.href = "/";
            }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },

    selectProfileImg: function() {
        $.ajax({
            type: "GET",
            url: "/user/info"
        }).done(function(resp) {
            $("#navbar-interceptor").load(location.href+" #navbar-interceptor");
        });
    },

    deleteProfileImg: function() {

     $.ajax({
             type: "DELETE",
             url: "/api/user/info/profileImg",
             dataType: "json"
        }).done(function(resp) {
        if(resp.statusCode == 400 || resp.statusCode == 500){
            alert(resp.message);
        } else {
            alert(resp.message);
            location.href="/user/info";
        }
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
     },

     uploadProfileImg: function() {

             var formData = new FormData();
             formData.append("uploadImg", $("input[name='uploadImg']")[0].files[0]);

         $.ajax({
                 type: "PUT",
                 url: "/api/user/info/profileImg",
                 processData : false,
                 contentType : false,
                 data : formData
             }).done(function(resp) {
                 if(resp.statusCode == 400 || resp.statusCode == 500){
                     alert(resp.message);
                     } else {
                     alert(resp.message);
                     location.href = "/user/info";
                 }
             }).fail(function(error) {
                 alert(JSON.stringify(error));
             });
     },

    checkEmail: function() {

        alert("해당 이메일로 인증번호가 발송되었습니다.")
        let emailCertify = $("#emailCertify").val();

        $.ajax({
              type : "POST",
              url : "/api/user/emailCode",
              data : {
                 "email" : $("#email").val()
              },
              success : function(data){
                 console.log("data : "+data);
                 chkEmailCertify(data, emailCertify);
              }
           });

            function chkEmailCertify(data, emailCertify){
                $('#emailCertify').on("keyup", function() {
                if (data != $('#emailCertify').val()) {
                    emailCertifyChk = false;
                    $('#emailCertifyTxt').text("인증번호가 일치하지 않습니다.");
                    $('#emailCertifyTxt').css('color', 'red');
                } else {
                    emailCertifyChk = true;
                    $('#emailCertifyTxt').text('인증번호 확인 완료');
                    $('#emailCertifyTxt').css('color', 'red');
                }
            })
        }
    }
}

index.init();
}