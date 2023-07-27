let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
                $("#btn-update").on("click", ()=>{
                    this.update();
                });
                window.addEventListener("load", ()=>{
                    this.profileImg();
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

            update: function() {

                var formData = new FormData();
                formData.append("uploadImg", $("input[name='uploadImg']")[0].files[0]);

                var data = {
                    info: {
                        password: $("#password").val(),
                        checkedPassword: $("#checkedPassword").val(),
                        name: $("#name").val(),
                        phone: $("#phone").val()
                    }
                }

                formData.append(
                    "update",
                    new Blob([JSON.stringify(data.info)], { type: "application/json" })
                );

                var password = $("#password").val();
                var checkedPassword = $("#checkedPassword").val();

                if (password != checkedPassword) {
                alert("패스워드가 일치하지 않습니다.");
                $('#valid_checkedPassword').text("패스워드가 일치하지 않습니다.");
                $('#valid_checkedPassword').css('color', 'red');
                return false;
                }

                $.ajax({
                    type: "PUT",
                    url: "/api/user/update",
                    processData : false,
                    contentType : false,
                    data : formData,
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);

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

            profileImg: function() {
                $.ajax({
                    type: "GET",
                    url: "/user/info"
                }).done(function(resp) {
                    $("#navbar-interceptor").load(location.href+" #navbar-interceptor");
                });
            },

            checkEmail: function() {

                let $emailconfirm = $("#emailconfirm").val();
                let $emailconfirmTxt = $("#emailconfirmTxt").val();

                $.ajax({
                      type : "POST",
                      url : "/api/user/mailConfirm",
                      data : {
                         "email" : $("#email").val()
                      },
                      success : function(data){
                         alert("해당 이메일로 인증번호 발송이 완료되었습니다. \n 확인부탁드립니다.")
                         console.log("data : "+data);
                         chkEmailConfirm(data, $emailconfirm, $emailconfirmTxt);
                      }
                   });

                    function chkEmailConfirm(data, $emailconfirm, $emailconfirmTxt){
                        $emailconfirm.on("keyup", function(){
                            if (data != $emailconfirm.val()) { //
                                emconfirmchk = false;
                                $emailconfirmTxt.html("<span id='emconfirmchk'>인증번호가 잘못되었습니다</span>")
                                $("#emconfirmchk").css({
                                    "color" : "#FA3E3E",
                                    "font-weight" : "bold",
                                    "font-size" : "10px"

                                })
                                //console.log("중복아이디");
                            } else { // 아니면 중복아님
                                emconfirmchk = true;
                                $emailconfirmTxt.html("<span id='emconfirmchk'>인증번호 확인 완료</span>")

                                $("#emconfirmchk").css({
                                    "color" : "#0D6EFD",
                                    "font-weight" : "bold",
                                    "font-size" : "10px"

                                })
                            }
                        })
                    }
                }
}

index.init();