let index = {
		    init: function() {
            $("#btn-back").on("click", ()=>{
                   window.history.back();
               });
            window.addEventListener("load", ()=>{
                   this.searchInfo();
               });
            },

            searchInfo: function() {

                $.ajax({
                     type: "GET",
                     url: "/scheduler/view",
                     dataType: "json"
                }).done(function(resp) {
                if(resp.statusCode == 400 || resp.statusCode == 500){
                    alert("회원정보 조회에 실패하였습니다.");
                }
                }).fail(function(error) {
                    alert(JSON.stringify(error));
                });
            }
}

index.init();