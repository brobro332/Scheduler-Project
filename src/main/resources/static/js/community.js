let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
                $("#btn-update").on("click", ()=>{
                    this.update();
                });
                $("#btn-deletePost").on("click", ()=>{
                    this.deletePost();
                });
            },

            save: function() {

                var title = $("#title").val();
                var content = $("textarea[name = content]").val();

                let data = {
                        title: $("#title").val(),
                        content: $("textarea[name = content]").val(),
                };

                if(title == '') {

                    alert("제목을 입력해주세요.");
                    return false;
                }

                if(content == '') {

                    alert("내용을 입력해주세요.");
                    return false;
                }

                $.ajax({
                    type: "POST",
                    url: "/api/community/write",
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function(resp) {
                    if(resp.statusCode == 400 || resp.statusCode == 500){
                        alert(resp.message);
                        } else {
                        alert(resp.message);
                        location.href = "/community/view";
                    }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            },

            update: function() {

                    var title = $("#title").val();
                    var content = $("textarea[name = content]").val();
                    var post_id = $('#post_id').val();

                    let data = {
                            title: $("#title").val(),
                            content: $("textarea[name = content]").val(),
                    };

                    if(title == '') {

                        alert("제목을 입력해주세요.");
                        return false;
                    }

                    if(content == '') {

                        alert("내용을 입력해주세요.");
                        return false;
                    }

                    $.ajax({
                        type: "PUT",
                        url: "/api/community/update/" + post_id,
                        data: JSON.stringify(data),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json"
                    }).done(function(resp) {
                        if(resp.statusCode == 400 || resp.statusCode == 500){
                            alert(resp.message);
                            } else {
                            alert(resp.message);
                            location.href = "/community/view";
                        }
                    }).fail(function(error) {
                        alert(JSON.stringify(error));
                    });
                },

                deletePost: function() {

                    var post_id = $('#post_id').val();

                    $.ajax({
                        type: "DELETE",
                        url: "/api/community/delete/" + post_id,
                        contentType: "application/json; charset=utf-8",
                        dataType: "json"
                    }).done(function(resp) {
                        if(resp.statusCode == 400 || resp.statusCode == 500){
                            alert(resp.message);
                            } else {
                            alert(resp.message);
                            location.href = "/community/view";
                        }
                    }).fail(function(error) {
                        alert(JSON.stringify(error));
                    });
                }
}

index.init();