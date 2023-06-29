let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
            },

            save: function() {
                var password = $("#password").val();
                var checkedPassword = $("#checkedPassword").val();

                if (password != checkedPassword) {
                alert("패스워드가 일치하지 않습니다.");
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
                        alert("회원가입에 실패하였습니다.");

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
                        alert("회원가입이 완료되었습니다.");
                        location.href = "/";
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            }
}

index.init();