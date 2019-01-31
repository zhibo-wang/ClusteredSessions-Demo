;
(function ($) {
	/**
	 * 将发票识别功能做成插件，可以是任意jQuery对象调用这个插件
	 * @author yezhouan
	 * @param  {[type]} coordinate 对应的坐标对象，请注意和interpreter方法里面的对象结构是否对应，不对应请修改interpreter方法
	 * @return {[type]}            返回jQuery对象
	 */
	var DragBox = (function () {
		function dragBox(elem) {
			var that = this;
			this.elem = elem; //jqeury 元素对象
			var _data = this.elem.parent('.part').data('ocrFp');
			this.coordObj = _data.zbList; //传进来的坐标值的对象
			this.tgImg = this.elem.siblings('canvas'); //目标发票图片jquery对象
			this.scaleLeval = 0;
			this.rotateLeval = 0; //用于判断角度的
			if (!_data.ocrAngle) {
				this.angle = 0;
			} else {
				this.angle = -1 * ftsps.number.m2F(_data.ocrAngle).toFixed(2);
			}
			// this.tgImg.on('load', function(event) {
			// 	//加载完成去掉背景
			// 	$(this).length > 0 && $(this).parents('.nodata').removeClass('nodata');

			// 	//初始化图片宽高，已解决切换过快时导致宽高不正确
			// 	$(this).css('width','100%');

			// 	that.init();
			// });
			that.init();
		}
		dragBox.prototype = {
			//初始化
			init: function () {

				console.log('初始图片宽度：' + this.tgImg.width());

				//设置图片容器宽高和位置
				this._rotateFix(true);
				// 利用dragresize插件生成可拖拉的对象
				this.dragresize();
				//创建拖拉元素
				this.create(this.interpreter());
				//角度纠正旋转
				this._angleFix();
			},
			//dragresize插件的使用
			dragresize: function () {
				var _ratio = this.calRatio();
				var dragresize = new DragResize('dragresize', {
					minWidth: 40 / _ratio,
					minHeight: 40 / _ratio,
					minLeft: 8,
					minTop: 8,
					maxLeft: this.elem.width() - 8,
					maxTop: this.elem.height() - 8
				});
				//插件判断是否为可拖拉的元素方法
				dragresize.isElement = function (elm) {
					if (elm.className && elm.className.indexOf('ftsp-dragresize') > -1) return true;
				};
				//插件判断元素是否可拖拉的方法
				dragresize.isHandle = function (elm) {
					if (elm.className && elm.className.indexOf('drsMoveHandle') > -1) return true;
				};
				dragresize.apply(this.elem.get(0));
			},
			/**
			 * 将后台传进来的坐标对象转换为该方法使用的标准的坐标，当后台传过来的json结构改变时就修改此方法
			 */
			interpreter: function () {
				var arr = [],
					coordinate = this.coordObj,
					ratio = this.calRatio();
				if (!coordinate) return arr;
				for (var i = 0; i < coordinate.length; i++) {
					var val = coordinate[i],
						coord = {};
					if (!this._match(val.type)) continue;
					coord.x1 = val.x / ratio;
					coord.y1 = val.y / ratio;
					coord.x2 = (parseFloat(val.x) + parseFloat(val.width)) / ratio;
					coord.y2 = (parseFloat(val.y) + parseFloat(val.height)) / ratio;
					coord.type = val.type;
					coord.id = val.id;
					arr.push(coord);
				}

				return arr;
			},
			//清空拖拉元素的容器
			clear: function () {
				this.elem.html('');
			},
			/**
			 * 创建可拖拉的元素节点
			 * @author yezhouan
			 * @param  {[type]} arr   传入的坐标数组，结构为{[x1,x2,y1,y2,type]}
			 * @param  {[type]} ratio 图片缩放比例
			 */
			create: function (arr, ratio) {
				var _ratio = ratio || 1;
				this.clear();
				for (var i = 0; i <= arr.length; i++) {
					if (!arr[i]) break;
					var val = arr[i];
					var $html = $('<div></div>').addClass('ftsp-dragresize').addClass('drsMoveHandle').css({
						left: (val.x1) / _ratio,
						top: (val.y1) / _ratio,
						width: (val.x2 - val.x1) / _ratio,
						height: (val.y2 - val.y1) / _ratio
					}).text(this._match(val.type)).
						data('item-id', val.id); //纠正框显示的文字
					this.elem.append($html);
				}
			},
			_getImgActualSize: function () {
				var _src = this.elem.siblings('img').attr('src');
				var img = new Image(); //利用新建图像获取实际宽高
				img.src = _src;
				return {
					height: img.height,
					width: img.width
				};
			},
			//计算并返回当前对象发票图片的缩放比例
			calRatio: function () {
				var ratio = (this._getImgActualSize().width / this.tgImg.width()).toFixed(2); //获取目标图片的实际宽度并算出缩放比例
				return ratio;
			},
			//放大功能，同时放大可拖拉容器
			enlarger: function () {
				if (this.scaleLeval > 1) {
					return;
				}
				var _tgImg = this.tgImg,
					_ele = this.elem;

				_tgImg.width(_tgImg.width() * 2);
				_ele.width(_tgImg.width())
					.height(_tgImg.height());

				this.init();

				this.scaleLeval++;
			},
			//缩小功能
			smaller: function () {
				if (this.scaleLeval < 0) {
					return;
				}
				var _tgImg = this.tgImg,
					_ele = this.elem;

				_tgImg.width(_tgImg.width() / 2);
				_ele.width(_tgImg.width())
					.height(_tgImg.height());

				this.init();

				this.scaleLeval--;
			},
			//计算当前拖拉框坐标，可以传入缩放比例获取真实坐标
			calCoordinate: function (ratio) {
				var current = [],
					_ratio = ratio || 1;
				this.elem.find('.ftsp-dragresize').each(function (index, el) {
					var coord = {};
					coord.x1 = parseFloat($(this).css('left')) * _ratio;
					coord.y1 = parseFloat($(this).css('top')) * _ratio;
					coord.x2 = (parseFloat($(this).css('left')) + parseFloat($(this).width())) * _ratio;
					coord.y2 = (parseFloat($(this).css('top')) + parseFloat($(this).height())) * _ratio;
					coord.id = $(this).data('item-id');
					current.push(coord);
				});
				return current;
			},
			//转换为后台所需要的数据结构
			zbList: function () {
				var current = this.calCoordinate(this.calRatio()),
					coord = this.coordObj;
				for (var i = 0; i < coord.length; i++) {
					for (var j = 0; j < current.length; j++) {
						if (!current[j].id) continue;
						if (coord[i].id === current[j].id) {
							coord[i].x = current[j].x1;
							coord[i].y = current[j].y1;
							coord[i].width = current[j].x2 - current[j].x1;
							coord[i].height = current[j].y2 - current[j].y1;
						}
					}
				}
				return coord;
			},
			//纠正框提示文字匹配
			_match: function (type) {
				var o = {
					fpdm: '1', //发票代码
					fphm: '2', //发票号码
					kprq: '3', //开票日期
					//mmq: '4', //密码区
					xfmc: '4-1', //销方名称
					xfnsrsbh: '4-2', //销方纳税人识别号
					hwhyslwfwmc: '5-1', //货物或劳务名称
					//ggxh: '6-2',//规格型号
					//dw: '6-3',//单位
					mxnum: '5-2', //数量
					//mxdj: '6-5',//单价
					mxje: '5-3', //明细金额
					mxsl: '5-4', //明细税率
					mxse: '5-5', //明细税额
					je: '6', //金额合计
					se: '7', //税额合计
					jshj: '8', //价税合计
					//gfnsrsbh: '',//纳税人识别号
					dragTest: '切片'//购方名称
				};
				return o[type] || '';
			},
			/**
			 * 修复图片旋转的时候有部分超出容器无法显示的问题
			 * @author yezhouan
			 * @return {[type]} [description]
			 */
			_rotateFix: function (isFirstLoad) {
				var _rad = this.angle * (Math.PI / 180);
				var _w = Math.abs(this.tgImg.width() * Math.cos(_rad)) + Math.abs(this.tgImg.height() * Math.cos(Math.PI * 0.5 - _rad)); //旋转后图片占的位宽
				var parent_w = this.tgImg.width(); //父元素容器的宽度
				var dif_r = parent_w / _w; //相差比例
				var of_t = (Math.abs(this.tgImg.height() * Math.cos(_rad)) + Math.abs(this.tgImg.width() * Math.cos(Math.PI * 0.5 - _rad)) - this.tgImg.height()) * 0.5; //旋转后图片占的位高
				var of_l = (_w - this.tgImg.width()) * 0.5;
				this.elem.parent('.part').css({
					paddingBottom: of_t * 2,
					paddingRight: of_l * 2
				});
				if (isFirstLoad) {
					this.tgImg.width(this.tgImg.width() * dif_r - 3);
				}
				//赋予拖拉框容器宽高
				this.elem.width(this.tgImg.width())
					.height(this.tgImg.height());
				if (navigator.userAgent.indexOf("MSIE 8.0") > 0) {

				} else {
					this.tgImg.css({
						top: of_t,
						left: of_l
					});
				}
				this.elem.css({
					top: of_t,
					left: of_l
				});
			},
			/**
			 * 当图片有角度的时候进行图片的旋转
			 * @author yezhouan
			 * @return {[type]} [description]
			 */
			_angleFix: function () {
				if (navigator.userAgent.indexOf("MSIE 8.0") > 0) {
					var rad = this.angle * (Math.PI / 180);
					var m11 = Math.cos(-rad),
						m12 = -1 * Math.sin(-rad),
						m21 = Math.sin(-rad),
						m22 = m11;
					this.tgImg[0].style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11=" + m11 + ",M12=" + m12 + ",M21=" + m21 + ",M22=" + m22 + ",SizingMethod='auto expand')";

				} else {
					this.tgImg.css('transform', 'rotate(' + -1 * this.angle + 'deg)');
				}

			}

		};
		return dragBox;
	})();
	$.fn.dragBox = function (coordinate) {
		//单例模式
		return this.each(function () {
			var me = $(this),
				instance = $(this).data('dragBox');
			if (!instance) {
				$(this).data('dragBox', new DragBox(me));
			}
		});
	};
})(jQuery);