$(function () {
    if($(".select-item").prop("checked") == true){
        $(".select-all").prop("checked",true);
    }
    $("input[class='select-all']").click(function(){ //全选框勾选
        if($(this).prop("checked")==true){
            $("input[class='select-item']").prop("checked",true);
        }else{
            $("input[class='select-item']").prop("checked",false);
        }
    });

    $("input[class='select-item']").click(function(){
        if($(this).prop("checked")==true){
            var allChecked = true; //是否勾上全选按钮
            $("input[class='select-item']").each(function(){
                if($(this).prop("checked")==false){
                    allChecked = false;
                }
            });
            if(allChecked){
                $("input[class='select-all']").prop("checked",true);
            }else{
                $("input[class='select-all']").prop("checked",false);
            }
        }else{
            $("input[class='select-all']").prop("checked",false); //全选按钮不能勾选
        }
    });
})