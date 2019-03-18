$(function () {
    //替换tempalte内容
    $("div[frame-id]").each(function () {
        var type = $(this).data("type");
        switch (type) {
            case "column":
                $(this).prepend(createBtn("C"));
                break;
            case "tab":
                $(this).prepend(createBtn("T"));
                break;
        }
    });
    //获取template 内容，填充
    getTemplate();

    //按钮悬浮效果
    /*
    $('.frame-buttons').affix({
        offset: {
            top: 395//滚动中距离页面顶端的位置

        }
    });
    */
    //绑定所有的可拖拽块
    updateStatus();

    $(".frame-block").draggable({
        revers: "invalid",
        helper: "clone",
        connectToSortable: "div[region-id]",
        start: function (event, ui) {
            var block = ui.helper;
            block.removeClass("btn btn-default");
            var max = maxFrameId();
            switch (block.data("type")) {
                case "column":
                    block.attr("frame-id", ++max).removeClass("btn btn-default btn-block frame-block").html(createColumn());
                    break;
                case "tab":
                    block.attr("frame-id", ++max).removeClass("btn btn-default btn-block frame-block").html(createTab());
                    break;
            }
        },
        stop: function (event, ui) {
            var block = ui.helper;
            switch (block.data("type")) {
                case "column":
                    block.removeAttr("style").addClass("row part").removeClass("ui-draggable ui-draggable-handle");

                    break;
                case "tab":
                    block.removeAttr("style").addClass("part").removeClass("ui-draggable ui-draggable-handle");
                    break;
            }
            updateStatus();

            var frameid = block.attr("frame-id");
            var params = block.attr("params-list");
            if (params == "") {
                showParams(block.data("type"), frameid, "");
            } else {
                $("#Params").val(params);
                addParams(block.data("type"), frameid);
            }

        }
    });
    //删除布局
    $("#init").on("click", ".frame-delete", function () {
        var block = $(this);
        var id = block.parent().attr("frame-id");
        layer.confirm("确认删除该布局及其下的其它布局吗？",
            { btn: ["确认", "取消"] },
            function () {

                block.parent().remove();
                layer.closeAll();
            }, function () { })
    });

    //修改布局
    $("#init").on("click", ".frame-edit", function () {
        var id = $(this).parent().attr("frame-id");
        var block = $(this).parent();

        layer.confirm("修改布局会删除该布局下的其它布局",
            { btn: ["继续", "取消"] },
            function () {
                var type = block.data("type");
                var params = block.attr("params-list");
                layer.closeAll();
                showParams(type, id, params);
            }, function () { })

    });

    $("#btnRm").click(function () {
        rmAll();
    })
});

//初始化
function updateStatus() {
    $("div[region-id]").sortable({
        dropOnEmpty: true,
        revert: true,
        connectWith: 'div[region-id]'
    });
}

function createBtn(title) {
    str = '<span class="frame-title">'+title+'</span><a class="frame-edit"><i class="fa fa-edit "></i></a><a class="frame-delete"><i class="fa fa-remove "></i></a>';

    return str;
}

function createColumn() {
    return createBtn("C");
}

function createTab() {
    str = createBtn("T");
    str += '<ul class="nav nav-tabs" role="tablist">';
    str += '</ul>';
    str += '<div class="tab-content" role="contenlist">';
    str += '</div>';

    return str;
}

//弹出添加参数弹框
function showParams(type, frameid, value) {
    switch (type) {
        case "column":
            $("#paramsTitle").html("分栏参数");
            $("#paramsIntro").html("相加等于12，逗号分隔。");
            $("#Params").attr("placeholder", "3,6,3");

            $("#paramsType").val("column");
            break;
        case "tab":
            $("#paramsTitle").html("TAB参数");
            $("#paramsIntro").html("多个栏目名称，逗号分隔。");
            $("#Params").attr("placeholder", "栏目1,栏目2");

            $("#paramsType").val("tab");
            break;
    }
    $("#Params").val(value);
    $("#paramsSubmit").attr("onclick", "addParams('" + type + "'," + frameid + ")");
    layer.open({
        type: 1,
        title: '添加参数',
        shadeClose: false,
        shade: 0.3,
        area: ['500px', '300px'],
        content: $('#showParams')
    });
}

//为布局添加参数
function addParams(type, frameid) {
    param = $("#Params").val();
    if (!param) {
        $("#Params").focus();
        return false;
    }
    var ps = param.split(',');
    var max = maxRegionId();
    var obj = $("div[frame-id=" + frameid + "]");

    switch (type) {
        case "column":
            var count = 0;
            var str = createBtn("C");
            for (var i = 0; i < ps.length; i++) {
                if (ps[i] == "") {
                    layer.msg("参数格式错误");
                    return false;
                }
                count += parseInt(ps[i]);
            }
            if (count != 12) {
                layer.msg("参数之和不为12");
                return false;
            }
            for (var i = 0; i < ps.length; i++) {
                str += '<div class="col-md-' + ps[i] + '"><div region-id="' + ++max + '"></div></div>';
            }
            obj.html(str);
            break;
        case "tab":
            var str1 = "";
            var str2 = "";
            for (var i = 0; i < ps.length; i++) {
                max++;
                str1 += '<li role="presentation" class="' + (i == 0 ? 'active' : '') + '"><a href="#tab-' + max + '" aria-controls="tab-' + max + '" role="tab" data-toggle="tab">' + ps[i] + '</a></li>';
                str2 += '<div role="tabpanel" class="tab-pane ' + ((i == 0 ? 'active' : '')) + '" id="tab-' + max + '" region-id="' + max + '"></div>';
            }
            obj.children('ul[role="tablist"]').html(str1);
            obj.children('div[role="contenlist"]').html(str2);
            break;
    }

    obj.attr("params-list", param); //记录参数，方便修改

    updateStatus();

    layer.closeAll();
}

//获取当前页最大的regionid
function maxRegionId() {
    var max = 0;
    $('*[region-id]').each(function (i, e) {
        var current = eval($(this).attr('region-id'));
        if (current > max) max = current;
    });

    return max;
}

function maxFrameId() {
    var max = 0;
    $('*[frame-id]').each(function (i, e) {
        var current = eval($(this).attr('frame-id'));
        if (current > max) max = current;
    });

    return max;
}

function rmAll() {
    if (!confirm('清除不可恢复！是否清除掉所有布局？')) return;

    $('#init').html('');
    $("#frameList").html("");
}

function save() {
    var html = $('#init').html();
    strC = new RegExp(createBtn("C"), "g");
    strT = new RegExp(createBtn("T"), "g");
    re =
        html = html.replace(strC, "");
    html = html.replace(strT, "");
    $('#Template').val(html);
}

function getTemplate() {
    $("#init").html($("#TemplateCopy").html());
}