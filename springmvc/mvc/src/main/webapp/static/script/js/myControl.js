
var item = {
    fpJxId: '',
    apiFileId: '',
    zbList: [
        {
            x: 100,
            y: 800,
            width: 400.00,
            height: 2000.00,
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

$("#selectImage").on('click', function () {
    $(this).dragBox()
})

$('#cutImage').on('click', function () {
    let data = [];
    let $drsMoveHandle = $('.drsMoveHandle ');
    let $img = $("#sourceImg");
    $drsMoveHandle.each(function () {
        let node = {};
        let $topLeftNode = $(this).find('.dragresize-tl');
        let $botomRightNode = $(this).find('.dragresize-br');
        node.leftOne = $topLeftNode.offset().left;
        node.leftTwo = $botomRightNode.offset().left;
        node.topOne = $topLeftNode.offset().top;
        node.topTwo = $botomRightNode.offset().top;
        node.startX = node.leftOne - $img.offset().left;
        node.startY = node.topOne - $img.offset().top;
        node.width = node.leftTwo - node.leftOne;
        node.height = node.topTwo - node.topOne;
        data.push(node)
    })
    createCanvasObj(data);

})

function createCanvasObj(data) {
    console.log(data)
    let url = $('img').attr('src');
    let myImage = new Image();
    myImage.src = url;
    // myImage.crossOrigin = "anonymous";
    for(let i = 0; i < data.length; i ++) {
        let node = data[i];
        let $canvas = $('<canvas ></canvas>'), c = $canvas[0];
        $canvas.attr("width", node.width);
        $canvas.attr("height", node.height);
        var ctx = c.getContext("2d");
        // ctx.drawImage(img, 0, 0, , img.getAttribute('height'));
        ctx.drawImage(img, node.startX, node.startY, node.width,node.height, 0, 0, node.width, node.height);
        $("#page-content").append($canvas);
    }
}

$('#recogniteImage').on('click', function () {
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
        url: 'http://localhost:8880/sendImageToBaidu.do',
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        data: {
            base64Img: imgBase64
        },
        success: function (result) {
            $("#page-content").append(result);
        }
    })
}