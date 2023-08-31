let index = {
		    init: function() {
                $("#btn-save").on("click", ()=>{
                    this.save();
                });
            },

            save: function() {
                let data = {
                        title: $("#title").val(),
                        content: $("textarea[name = content]").val(),
                };

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
            }
}

index.init();