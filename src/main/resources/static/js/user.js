let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
                $("#btn-update").on("click", ()=>{
                    this.update();
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
                var password = $("#password").val();
                var checkedPassword = $("#checkedPassword").val();

                if (password != checkedPassword) {
                alert("패스워드가 일치하지 않습니다.");
                $('#valid_checkedPassword').text("패스워드가 일치하지 않습니다.");
                $('#valid_checkedPassword').css('color', 'red');
                return false;
                }

                let data = {
                        password: $("#password").val(),
                        checkedPassword: $("#checkedPassword").val(),
                        name: $("#name").val(),
                        phone: $("#phone").val()
                };

                $.ajax({
                    type: "PUT",
                    url: "/api/user/update",
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
            }
}

index.init();