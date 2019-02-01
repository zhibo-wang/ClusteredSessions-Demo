
var item = {
    fpJxId: '',
    apiFileId: '',
    zbList: [
        {
            x: 175,
            y: 30,
            width: 140.00,
            height: 400.00,
            type: 'dragTest',
            fpOcrId: '1'
        },
        {
            x: 390,
            y: 30,
            width: 200.00,
            height: 400.00,
            type: 'dragTest',
            fpOcrId: '1'
        }
    ],
    mxList: []
}

$('#dragWrap').data('ocrFp', item);

var img = document.getElementById("sourceImg");
img.onload = function() {
    $('#selectImage').show();
}

var imgApi = null;
$("#selectImage").on('click', function () {
    imgApi = $(this).dragBox();
})

$('#switchImage').on('change', function () {
    console.log(window.URL.createObjectURL(this.files[0]))
    $('#sourceImg').attr('src', window.URL.createObjectURL(this.files[0]))
})

function createCanvasObj(data) {
    let url = $('img').attr('src');
    let myImage = new Image();
    myImage.src = url;
    for(let i = 0; i < data.length; i ++) {
        let node = data[i];
        node.width = node.x2 - node.x1;
        node.height = node.y2 - node.y1;
        let $canvas = $('<canvas ></canvas>'), c = $canvas[0];
        $canvas.attr("width", node.width);
        $canvas.attr("height", node.height);
        var ctx = c.getContext("2d");
        ctx.drawImage(img, node.x1, node.y1, node.width,node.height, 0, 0, node.width, node.height);
        // $("#page-content").append($canvas);
        postData(c.toDataURL())
    }
}

$('#recogniteImage').on('click', function () {
    $('#page-content').empty();
    var api = $('#selectImage').data('dragBox');
    var data = api.calCutImgCoordinate(api.calImgRadio());
    createCanvasObj(data)
    getImgBase64();
})

function getImgBase64() {
    let canvas = document.getElementsByTagName('canvas');
    for (let index = 0; index < canvas.length; index++) {
        const element = canvas[index];
        postData(element.toDataURL())
    }
}

function postData(imgBase64) {
    imgBase64 = imgBase64.substring(imgBase64.indexOf(',')+1);
    $.ajax({
        type: 'POST',
        url: '/sendImageToBaidu.do',
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            base64Img: imgBase64
        },
        success: function (result) {
            var data = JSON.parse(result);
            renderResult('#page-content', data["words_result"])
        }
    })
}

/**
 * 渲染识别内容
 * */
function renderResult(wrapId, data) {
    let $wrap = $(wrapId), $ul = $('<ul></ul>'), html = '';
    for (let i = 0; i < data.length; i++) {
        let wordObj = data[i];
        html += '<li>';
        html +=  '<span>' + (i + 1) + '、' + '</span>';
        html +=  wordObj["words"];
        html += '</li>';
    };
    $ul.html(html);
    $wrap.append($ul)
}