function addField(fieldObj) {
    var name = $("#field-name").val();
    var id = $("#field-id").val();
    if(name.length<1){
        layer.msg("字段名称不能为空");
        return;
    }
    var id = $("#field-id").val();
    var reg = /^[0-9a-zA-Z]+$/;
    if(!reg.test(id)){
        layer.msg("唯一标识只能是英文字母和数字");
        return;
    }
    var isnessary = $("#field-isnessary").prop("checked");

    //标识不能重复
    var repeated = 0;
    $("#item-list .form-group .field-name .field-id").each(function () {
        if($(this).text() == id){
            repeated++;
        }
    });
    var type = $("#field-type option:selected").val();
    var op = $("#op-type").val();
    var html = "";
    if(op == "edit"){
        if(repeated>1){
            layer.msg("唯一标识在当前表单中不能重复");
            return;
        }
        fieldObj.find(".field-name .name").text(name);
        fieldObj.find(".field-name .field-id").text(id);
        if(isnessary){
            if(fieldObj.find(".field-name .field-isnessary").html() == undefined){
                fieldObj.find(".field-name .field-id").after($("#isnessary").html());
            }
        }else{
            if(fieldObj.find(".field-name .field-isnessary").html() != undefined){
                fieldObj.find(".field-name .field-isnessary").remove();
            }
        }
    }else {
        if(repeated>0){
            layer.msg("唯一标识在当前表单中不能重复");
            return;
        }
        switch (type){
            case "radio":
                html = $("#radio-item").html();
                break;
            case "checkbox":
                html = $("#checkbox-item").html();
                break;
            default:
                html = $("#text-item").html();
                break;
        }
        html = html.replace(/_NAME/g,name);
        html = html.replace(/_ID/g,id);
        $("#item-list").append(html);
        var thisObj = $("#item-list .form-group").last();
        if(isnessary){
            thisObj.find(".field-name .field-id").after($("#isnessary").html());
        }
    }
}

function addRadio(radioList) {
    var content = $("#radio-content").val();
    if(content.length <1){
        layer.msg("选项值不能为空");
        return;
    }
    var html = $("#radio").html().replace(/_CHOICE/g, content);
    if(radioList.find("p") != undefined){
        radioList.find("p").remove();
    }
    radioList.append(html);
}

function addCheckbox(checkboxList) {
    var content = $("#checkbox-content").val();
    if(content.length <1){
        layer.msg("选项值不能为空");
        return;
    }
    var html = $("#checkbox").html().replace(/_CHOICE/g, content);
    if(checkboxList.find("p") != undefined){
        checkboxList.find("p").remove();
    }
    checkboxList.append(html);
}